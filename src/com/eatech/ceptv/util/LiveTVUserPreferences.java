package com.eatech.ceptv.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;

import java.util.Map;
import java.util.TreeMap;

/*
* @au
* **/
public class LiveTVUserPreferences {
    private final static String PREF = "PREF";
    private final static String TIME_STAMP = "TimeStamp";

    private final static String TIMEOUT = "Timeout";
    private final static String LONG_TIMEOUT = "LongTimeout";
    private final static String VISITOR_COUNT = "VisitorCount";
    private final static String SESSION_ID = "SessionId";
    private final static String VIDEO_PLAZA_ID = "VideoPlazaID";

    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    public LiveTVUserPreferences(Context context) {
        this.appSharedPrefs = context.getSharedPreferences(PREF,
                Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public SharedPreferences getSharedPreference() {
        return appSharedPrefs;
    }

    public void saveTimeout(final int timeout) {
        prefsEditor.putInt(TIMEOUT, timeout * 1000);
        prefsEditor.commit();
    }

    public int getTimeout() {
        return appSharedPrefs.getInt(TIMEOUT, 4000);
    }

    public void saveSessionId(final String sessionId) {
        prefsEditor.putString(SESSION_ID, sessionId);
        prefsEditor.commit();
    }

    public String getSessionId() {
        return appSharedPrefs.getString(SESSION_ID, "");
    }
    
    public String getVideoPlazaId(Context context) {
    	String videoPlazaId = appSharedPrefs.getString(VIDEO_PLAZA_ID, "");
    	
    	if ( videoPlazaId == "" ) {
    		videoPlazaId = Secure.getString( context.getContentResolver(),
                    Secure.ANDROID_ID); ;
    		
    		prefsEditor.putString(VIDEO_PLAZA_ID, videoPlazaId );
            prefsEditor.commit();
    	}
    	
    	return videoPlazaId;
    }

    public void saveVisitorCount(final int count) {
        prefsEditor.putInt(VISITOR_COUNT, count);
        prefsEditor.commit();
    }

    public int getVisitorCount() {
        return appSharedPrefs.getInt(VISITOR_COUNT, 0);
    }

    public void saveLongTimeout(final int longTimeout) {
        prefsEditor.putInt(LONG_TIMEOUT, longTimeout * 1000);
        prefsEditor.commit();
    }

    public int getLongTimeout() {
        return appSharedPrefs.getInt(TIMEOUT, 20000);
    }

    public void saveTimeStamp(final String itemId, final int timeStamp) {
        final String key = TIME_STAMP + "-" + itemId;
        prefsEditor.putInt(key, timeStamp);
        prefsEditor.commit();
    }

    public void saveTimeStamp(final String itemId, final long timeStamp) {
        final String key = TIME_STAMP + "-" + itemId;
        prefsEditor.putLong(key, timeStamp);
        prefsEditor.commit();
    }

    public int getTimeStamp(final String itemId) {
        final String key = TIME_STAMP + "-" + itemId;

        try {
            return appSharedPrefs.getInt(key, 0);
        } catch (final ClassCastException exception) {
            return (int) appSharedPrefs.getLong(key, 0);
        }
    }

    public Map<String, String> getTimeStamps() {
        final Map<String, String> timeStamps = new TreeMap<String, String>();

        final Map<String, ?> keys = appSharedPrefs.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            final String key = entry.getKey();
            if (key.contains(TIME_STAMP)) {
                timeStamps.put(key, entry.getValue().toString());
            }
        }

        return timeStamps;
    }

    public void removeTimeStamp(final String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }

    public long getLongTimeStamp(final String itemId) {
        final String key = TIME_STAMP + "-" + itemId;

        return appSharedPrefs.getLong(key, 0);
    }

    // ==== SETTINGS ====
    public void toggleSetting(LiveTvConstants.Setting setting) {
        setPreference(setting.name(), !getSetting(setting));
    }

    public void setSetting(LiveTvConstants.Setting setting, boolean value) {
        prefsEditor.putBoolean(setting.name(), value);
        prefsEditor.commit();
    }

    public boolean getSetting(LiveTvConstants.Setting setting, boolean def) {
        return appSharedPrefs.getBoolean(setting.name(), def);
    }

    public boolean getSetting(LiveTvConstants.Setting setting) {
        return getSetting(setting, true);
    }

    public void setPreference(String key, Object value) {
        if (value instanceof Boolean) {
            prefsEditor.putBoolean(key, (Boolean) value);
        } else if (value instanceof String) {
            prefsEditor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            prefsEditor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            prefsEditor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            prefsEditor.putFloat(key, (Float) value);
        }

        prefsEditor.commit();
    }
}
