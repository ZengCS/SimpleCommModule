package cn.sxw.android.base.utils.reflect;

import java.lang.reflect.Field;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class FloatAction implements TypeAction {

	@Override
	public void putToSharePreference(Editor editor, String key, Object object) {
		if (object != null) {
			editor.putFloat(key, (Float)object);
		}
	}

	@Override
	public void getFromPreference(SharedPreferences sharedPreferences, String key, Object dst, Field field)  {

		if (sharedPreferences.contains(key)) {
			field.setAccessible(true);
			try {
	            field.set(dst, sharedPreferences.getFloat(key, 0F));
            } catch (IllegalAccessException e) {
	            e.printStackTrace();
            } catch (IllegalArgumentException e) {
	            e.printStackTrace();
            }
		}
	}
}
