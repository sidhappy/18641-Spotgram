package lgm.cmu.spotagram.utils;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


/**
 * ���ڷ������������ļ��Ķ�д
 * 
 * @author ¬־Ӿ
 *
 */
public class ParameterUtils {
	public static final String KEY_SIL_THRESHOLD = "silenceThreshold";
	public static final String KEY_SPEAKER_NAME = "speakerName";
	public static final String KEY_SV_TASK_STATUS = "svTaskStatus";
	public static final String KEY_SV_ON = "speakerVeriOn";

	public static final String PREFERENCE_NAME = "setting";
	
	private static SharedPreferences preferences;
	
	public static void initPreference(Context context) {
		preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
	}
	
	public static String getStringValue(String key) {
		if(preferences == null) 
			return null;
		String value = preferences.getString(key, "");
		return value;
	}
	
	public static boolean getBooleanValue(String key) {
		if(preferences == null) 
			return false;
		return preferences.getBoolean(key, false);
	}
	
	public static int getIntValue(String key) {
		if(preferences == null) 
			return 0;
		return preferences.getInt(key, 0);
	}
	
	public static void setStringValue(String key,String info) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, info);
		editor.commit();
	}

	public static void setBooleanValue(String key,Boolean info) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, info);
		editor.commit();
	}
	
	public static void setIntValue(String key, int info) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, info);
		editor.commit();
	}
	
}
