package com.elisium.halo.ConnectionUtilities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elisium.halo.R;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    private Context context;
    private CardObject[] data;
    private LayoutInflater inflater;
    private MenuListener listener;

    public interface MenuListener {
        public void buttonPushed(String usersChoice);
    }

    public CardsAdapter(Context context, CardObject[] data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(data[position].title);
        holder.itemView.setOnClickListener(v -> {
            Log.d("HALO", "item click: " + data[position].title);
            listener.buttonPushed(data[position].title);
        });
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data.length;
    }


    //In the ViewHolder you declare all the components of your xml layout for you recyclerView items ( and the data is assigned in the onBindViewHolder, above)
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Title);
        }
    }

    public void addCustomEventListener(MenuListener listener) {
        this.listener = listener;
    }
}

