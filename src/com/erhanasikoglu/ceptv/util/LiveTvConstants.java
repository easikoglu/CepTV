package com.erhanasikoglu.ceptv.util;

import java.util.HashMap;
import java.util.Map;

public class LiveTvConstants {

	public static final String SESSION_ID = "SESSION_ID";
	public static final String API_URL = "http://104.236.53.43:8080/api/";

	public static final String STATIC_URL = "http://104.236.53.43/";

	public static final String STATIC_LOGO_URL = "http://104.236.53.43/images/logos/";

	public static final String CLIENT_API_KEY = "cep_tv_client_id";

	public static final String APPLICATION_API_KEY = "cep_tv_app_id";




	public enum ErrorCode {
		WRONG_PASS("wrong_password"), NO_SESSION("not_logged_in");
		
		public String code;
		
		private ErrorCode(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	
	public enum Setting {
		USE3G(0), SOCIAL_SHARE(1), VIDEO_QUALITY(2);

		private final int id;

		private static final Map<Integer, Setting> lookup = new HashMap<Integer, Setting>();

		static {
			for (Setting setting : Setting.values()) {
				lookup.put(setting.getId(), setting);
			}
		}

		private Setting(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public static Setting get(int id) {
			return lookup.get(id);
		}
	}


}