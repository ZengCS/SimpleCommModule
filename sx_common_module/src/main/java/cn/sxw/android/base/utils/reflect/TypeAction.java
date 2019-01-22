package cn.sxw.android.base.utils.reflect;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.lang.reflect.Field;

public interface TypeAction {
	void putToSharePreference(Editor editor, String key, Object object);
	void getFromPreference(SharedPreferences sharedPreferences, String key, Object dst, Field field);
}
