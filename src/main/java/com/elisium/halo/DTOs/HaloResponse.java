package com.elisium.halo.DTOs;

import java.util.List;

public class HaloResponse{
	private List<DataItem> data;

	public List<DataItem> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"HaloResponse{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}