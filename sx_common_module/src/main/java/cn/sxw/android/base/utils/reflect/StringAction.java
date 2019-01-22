package cn.sxw.android.base.utils.reflect;

import java.lang.reflect.Field;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class StringAction implements TypeAction {

	@Override
	public void putToSharePreference(Editor editor, String key, Object object) {
		if (object != null) {
			editor.putString(key, (String) object);
		}
	}

	@Override
	public void getFromPreference(SharedPreferences sharedPreferences, String key, Object dst, Field field) {
		if (sharedPreferences.contains(key)) {
			field.setAccessible(true);
			try {
	            field.set(dst, sharedPreferences.getString(key, ""));
            } catch (IllegalAccessException e) {
	            e.printStackTrace();
            } catch (IllegalArgumentException e) {
	            e.printStackTrace();
            }
		}
	}

}
