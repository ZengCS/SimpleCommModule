package cn.sxw.android.base.prefer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.lang.reflect.Field;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.sxw.android.base.di.scope.ApplicationContext;
import cn.sxw.android.base.di.scope.PreferenceInfo;
import cn.sxw.android.base.utils.reflect.ReflectHelper;
import cn.sxw.android.base.utils.reflect.TypeActionManager;


/**
 * Created by Alex.Tang on 2017-03-20.
 */
@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context,
                                @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    public void saveObject(Object object) {
        HashMap<String, Field> fields = ReflectHelper.getFields(object);
        Editor editor = mPrefs.edit();
        for (String key : fields.keySet()) {
            Field field = fields.get(key);
            try {
                TypeActionManager.getInstance().getTypeAction(field.getType())
                        .putToSharePreference(editor, key, field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        editor.commit();
    }

    public <T> T initFromSharePreference(Class<T> cls) {
        T obj = ReflectHelper.newInstance(cls);
        initFromSharePreference(obj);
        return obj;
    }

    public void initFromSharePreference(Object obj) {
        HashMap<String, Field> fields = ReflectHelper.getFields(obj);
        for (String key : fields.keySet()) {
            Field field = fields.get(key);
            TypeActionManager.getInstance().getTypeAction(field.getType()).getFromPreference(mPrefs, key, obj, field);
        }
    }

    public void removeObject(Object obj) {
        HashMap<String, Field> fields = ReflectHelper.getFields(obj);
        Editor editor = mPrefs.edit();
        for (String key : fields.keySet()) {
            editor.remove(key);
        }
        editor.commit();
    }

    public void saveLong(long l, String tag){

        Editor editor = mPrefs.edit();
        editor.putLong(tag, l);
        editor.commit();
    }

    public  long getLong(String tag){
        return mPrefs.getLong(tag, 0);
    }

    public  void saveStr(String str, String tag){
        Editor editor = mPrefs.edit();
        editor.putString(tag, str);
        editor.commit();
    }

    public  String getStr(String tag){
        return mPrefs.getString(tag, "");
    }

    public  void saveStr(String name, String str, String tag){
        Editor editor = mPrefs.edit();
        editor.putString(tag, str);
        editor.commit();
    }

    public  String getStr(String name, String tag){
        return mPrefs.getString(tag, "");
    }

    public  void saveBoolean(boolean bool, String tag){
        Editor editor = mPrefs.edit();
        editor.putBoolean(tag, bool);
        editor.commit();
    }

    public  boolean getBool(String tag ){
        return mPrefs.getBoolean(tag, true);
    }

    public  boolean getBool(String tag, boolean defaultV){
        return mPrefs.getBoolean(tag, defaultV);
    }

    public  void saveInt(int i, String tag){
        Editor editor = mPrefs.edit();
        editor.putInt(tag, i);
        editor.commit();
    }

    public  int getInt(String tag){
        return mPrefs.getInt(tag, 0);
    }

    public  void saveFloat(float i, String tag){
        Editor editor = mPrefs.edit();
        editor.putFloat(tag, i);
        editor.commit();
    }

    public  float getFloat(String tag){
        return mPrefs.getFloat(tag, 0);
    }

}
