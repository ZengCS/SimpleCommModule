package cn.sxw.android.base.utils.reflect;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.lang.reflect.Field;

public class BooleanAction implements TypeAction {

	@Override
	public void putToSharePreference(Editor editor, String key, Object object) {
		if (object != null) {
			editor.putBoolean(key, (Boolean)object);
		}
	}

	@Override
	public void getFromPreference(SharedPreferences sharedPreferences, String key, Object dst, Field field)  {

		if (sharedPreferences.contains(key)) {
			field.setAccessible(true);
			try {
	            field.set(dst, sharedPreferences.getBoolean(key, false));
            } catch (IllegalAccessException e) {
	            e.printStackTrace();
            } catch (IllegalArgumentException e) {
	            e.printStackTrace();
            }
		}
	}

}
