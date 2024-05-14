package com.elisium.halo;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.elisium.halo.ConnectionUtilities.Halo;
import com.elisium.halo.DTOs.DataItem;
import com.elisium.halo.DTOs.HaloResponse;
import com.elisium.halo.DTOs.ImagesItem;
import com.elisium.halo.Fragments.HaloMain;
import com.elisium.halo.Utilities.DefaultSettings;
import com.elisium.mat.MainActivityDrawer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HaloActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_halo_main);
        Bundle bundle = getIntent().getExtras();
        sId = bundle.getString("sid");
        lat = bundle.getDouble("lat");
        lon = bundle.getDouble("lon");
        halo(sId, lat, lon);
    }

    private static final String TAG = HaloActivity.class.getSimpleName();
    public ArrayList<String> imgsList;
    private long downloadID;
    private String sId;
    private double lat;
    private double lon;
    private List<String> haloTopics = Collections.synchronizedList(new ArrayList<>()) ;

    /**
     * Get & download Halo cards
     * @return -1 error or 0 success
     */
    private synchronized int halo(String eId, double lat, double lon) {
        Halo halo = new Halo();
        HaloResponse haloCards;
        try {
            haloCards = halo.getHaloCards(
                    eId,
                    (float) lat,
                    (float) lon
            );
            if (haloCards != null)
                Log.d(
                        TAG,
                        haloCards.toString()
                );
            else {
                this.finish();
                return -2;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
        DefaultSettings.getInstance(this)
                .saveHaloCards(haloCards);
        try {
            List<DataItem> dataList;
            dataList = haloCards.getData();
            imgsList = new ArrayList<>();
            for (DataItem item : dataList) {
                List<ImagesItem> imagesList = item.getImages();
                File dir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        item.getTitle().isEmpty() ? "halo" : item.getTitle());
                if (!dir.exists()) {
                    dir.mkdir();
                }
                haloTopics.add(item.getTitle());
                for (ImagesItem image : imagesList) {
                    synchronized (this) {
                        String url = image.getUrl();
                        String fileName = url.substring(url.lastIndexOf('/') + 1);
                        fileName = fileName.substring(0, 1).toUpperCase()
                                + fileName.substring(1);

                        if (fileName.contains("?"))
                            fileName = fileName.substring(0, fileName.lastIndexOf('?'));

                        File file = new File(dir, fileName);
                        if (!file.exists()) {
                            DownloadManager.Request request = new DownloadManager
                                    .Request(Uri.parse(url))
                                    .setNotificationVisibility(
                                            DownloadManager.Request
                                                    .VISIBILITY_VISIBLE
                                    )// Visibility of the download Notification
                                    .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                                    .setTitle(fileName)// Title of the Download Notification
                                    .setDescription("Downloading")// Description of the Download Notification
                                    .setRequiresCharging(false)// Set if charging is required to begin the download
                                    .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                                    .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
                            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                            downloadID = downloadManager.enqueue(request);

                            boolean finishDownload = false;
                            int progress;
                            while (!finishDownload) {
                                Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadID));
                                if (cursor.moveToFirst()) {
                                    int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                                    switch (status) {
                                        case DownloadManager.STATUS_FAILED: {
                                            finishDownload = true;
                                            break;
                                        }
                                        case DownloadManager.STATUS_PAUSED:
                                            break;
                                        case DownloadManager.STATUS_PENDING:
                                            break;
                                        case DownloadManager.STATUS_RUNNING: {
                                            final long total = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                                            if (total >= 0) {
                                                final long downloaded = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                                progress = (int) ((downloaded * 100L) / total);
                                                // if you use downloadmanger in async task, here you can use like this to display progress.
                                                // Don't forget to do the division in long to get more digits rather than double.
                                                //  publishProgress((int) ((downloaded * 100L) / total));
                                            }
                                            break;
                                        }
                                        case DownloadManager.STATUS_SUCCESSFUL: {
                                            finishDownload = true;
                                            Toast.makeText(HaloActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                                            imgsList.add(file.getAbsolutePath());
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            imgsList.add(file.getAbsolutePath());
                        }
                    }
                }
            }

            DefaultSettings.getInstance(this)
                    .saveHaloTopics(haloTopics);

            ArrayList<String> tempArray = new ArrayList<>(haloTopics);

            if (!haloTopics.isEmpty()) {
                HaloMain haloMain = new HaloMain();
                Bundle arg = new Bundle();
                arg.putStringArrayList(
                        HaloMain.CARDS_LIST,
                        tempArray
                );
                haloMain.setArguments(arg);
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out,
                                android.R.anim.slide_out_right,
                                android.R.anim.slide_in_left
                        )
                        .add(R.id.halo_container,
                                haloMain,
                                "HaloMain"
                        )
                        .commit();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(HaloActivity.this, "Halo Download Completed", Toast.LENGTH_LONG).show();
            }
        }
    };
}
