package cn.sxw.android.base.prefer;

/**
 * Created by Alex.Tang on 2017-03-20.
 */

public interface PreferencesHelper {

    /**
     * 存储对象在preference
     * @param object
     */
    void saveObject(Object object);

    /**
     *
     * @param cls
     * @param <T>
     * @return
     */
    <T> T initFromSharePreference(Class<T> cls);

    /**
     * 删除在preference的对象
     * @param object
     */

    void removeObject(Object object);

    /**
     * 存储long型在preference
     * @param l
     * @param tag
     */

    void saveLong(long l, String tag);
    /**
     * 获取在preference 的long型
     * @param tag
     */
     long getLong(String tag);

    /**
     *
     * @param str
     * @param tag
     */
    void saveStr(String str, String tag);

    /**
     *
     * @param tag
     * @return
     */
    String getStr(String tag);

    /**
     *
     * @param name
     * @param str
     * @param tag
     */
    void saveStr(String name, String str, String tag);

    /**
     *
     * @param name
     * @param tag
     * @return
     */
    String getStr(String name, String tag);

    /**
     *
     * @param bool
     * @param tag
     */
   void saveBoolean(boolean bool, String tag);

    /**
     *
     * @param tag
     * @return
     */
    boolean getBool(String tag);

    /**
     *
     * @param tag
     * @param defaultV
     * @return
     */
    boolean getBool(String tag, boolean defaultV);

    /**
     *
     * @param i
     * @param tag
     */
    void saveInt(int i, String tag);

    /**
     *
     * @param tag
     * @return
     */
    int getInt(String tag);

    /**
     *
     * @param i
     * @param tag
     */
    void saveFloat(float i, String tag);

    /**
     *
     * @param tag
     * @return
     */
    float getFloat(String tag);



}
