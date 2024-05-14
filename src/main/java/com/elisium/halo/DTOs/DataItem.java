package com.elisium.halo.DTOs;

import java.util.List;

public class DataItem{
	private List<ImagesItem> images;
	private String description;
	private String id;
	private String title;

	public List<ImagesItem> getImages(){
		return images;
	}

	public String getDescription(){
		return description;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"images = '" + images + '\'' + 
			",description = '" + description + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}