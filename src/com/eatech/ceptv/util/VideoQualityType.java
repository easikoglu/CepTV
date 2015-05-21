package com.eatech.ceptv.util;


public enum VideoQualityType {

	AUTO(0, "AUTO - Otomatik Kalite", null), 
	HD(1,  "HD - S�per Kalite", 664000), 
	HQ(2, "HQ - Y�ksek Kalite", 464000), 
	SD(3, "SD - Standart Kalite", 264000);

	private int id;
	private String displayName;
	private Integer bandwidth;

   VideoQualityType(int id, String displayName, Integer bandwidth) {
		this.id = id;
		this.displayName = displayName;
		this.bandwidth = bandwidth;
	}
	
	public static VideoQualityType getById(int id) {		
		for(VideoQualityType contentType : VideoQualityType.values()) {
			if( contentType.getId() == id) {
				return contentType;
			}
		}
		return AUTO;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
	
	
	public Integer getBandwidth() {
		return bandwidth;
	}

}
