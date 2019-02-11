package com.zcs.android.lib.sketch.mvp.view;

import android.graphics.Bitmap;

import com.zcs.android.lib.sketch.bean.PhotoRecord;
import com.zcs.android.lib.sketch.bean.SketchHistory;
import com.zcs.android.lib.sketch.utils.SketchMode;

import java.util.List;

/**
 * Created by ZengCS on 2017/10/11.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public interface ISketchView {
    void initViews();

    void showToast(String msg);

    void sketchUndo();

    void sketchRedo();

    boolean isSketchEmpty();

    void sketchClear();

    void sketchClearAutoResetUserPhoto();

    PhotoRecord getUserPhotoRecord();

    void resetUserPhoto(PhotoRecord userPhotoRecord);

    boolean sketchSaveJpg(String filePath);

    void sketchSaveThumb(String filePath);

    void sketchSetBg(Bitmap bitmap);

    void sketchSetBgByPath(String filePath);

    void addPicByPath(String filePath);

    void addPicByBitmap(Bitmap bitmap);

    void addPicByUser(String url, Bitmap bitmap);

    void setSketchMode(SketchMode sketchMode);

    void cleanSpotlight();

    void onColorChanged();

    boolean updateFontSize(int mode);

    List<SketchHistory> getHistoryList();

    void drawByHistory(List<SketchHistory> historyList);

    void onOptionChanged(int option, int position);

    void effectiveDragText();

    void savePenWidth();

    void setSketchToolbarVisible(int visible);

    void hideToolMenu();

    void NotSetPenDefault();

    void invalidateToolMenus(boolean showAll);
}
