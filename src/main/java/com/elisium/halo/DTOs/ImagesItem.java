package com.elisium.halo.DTOs;

public class ImagesItem{
	private String description;
	private String url;

	public String getDescription(){
		return description;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"ImagesItem{" + 
			"description = '" + description + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}
