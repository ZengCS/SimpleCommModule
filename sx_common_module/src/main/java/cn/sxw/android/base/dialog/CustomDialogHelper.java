package cn.sxw.android.base.dialog;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.utils.AutoUtils;

import cn.sxw.android.R;

/**
 * Created by ZengCS on 2017/7/31.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class CustomDialogHelper {

    public static void releaseDialog(AlertDialog alertDialog) {
        dismissDialog(alertDialog);
        alertDialog = null;
    }

    public static void dismissDialog(AlertDialog alertDialog) {
        try {
            if (alertDialog != null && alertDialog.isShowing())
                alertDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否允许显示Dialog
     */
    public static boolean canShowDialog(@NonNull Context context) {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            return !activity.isFinishing();
        }
        return false;
    }

    /**
     * 创建一个LoadingDialog
     *
     * @param context
     * @param msg
     * @return
     */
    public static AlertDialog showLoadingDialog(Context context, String msg) {
        View customView = View.inflate(context, R.layout.dialog_custom_loading, null);
        if (!TextUtils.isEmpty(msg)) {
            TextView tvTips = customView.findViewById(R.id.tv_tip);
            tvTips.setText(msg);
        }
        return showCustomDialog(context, customView, false);
    }


    /**
     * 创建一个自定义内容的Dialog
     *
     * @param context
     * @param customView
     * @return
     */
    public static AlertDialog showCustomDialog(Context context, View customView, boolean cancelAble) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogWithDim);
        builder.setView(customView).setCancelable(cancelAble);
        return builder.show();
    }

    public static AlertDialog showCustomConfirmDialog(Context context, String msg, NativeDialogCallback callback) {
        try {
            DialogParam dialogParam = new DialogParam(msg);
            return showCustomConfirmDialog(context, dialogParam, callback);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建一个自定义的确认对话框
     *
     * @param context     上下文
     * @param dialogParam 参数
     * @param callback    回调
     * @return AlertDialog对象
     */
    public static AlertDialog showCustomConfirmDialog(Context context, DialogParam dialogParam, final NativeDialogCallback callback) {
        View customView = View.inflate(context, R.layout.dialog_custom_confirm, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogWithDim);
        builder.setView(customView).setCancelable(true);

        if (dialogParam != null) {
            builder.setView(customView).setCancelable(dialogParam.isCancelAble());
            // 设置标题
            TextView tvTitle = customView.findViewById(R.id.id_tv_dialog_title);
            tvTitle.setText(dialogParam.getTitle());
            // 设置内容
            TextView tvContent = customView.findViewById(R.id.id_tv_dialog_content);
            if (dialogParam.getAdvMessage() != null) {
                tvContent.setText(dialogParam.getAdvMessage());
            } else {
                tvContent.setText(dialogParam.getMessage());
            }
            // 设置内容对齐方式
            if (!dialogParam.isCenterContent()) tvContent.setGravity(Gravity.LEFT);
            // 设置取消按钮
            TextView btnCancel = customView.findViewById(R.id.id_btn_dialog_cancel);
            btnCancel.setText(dialogParam.getNegativeBtnText());
            // 设置确认按钮
            TextView btnConfirm = customView.findViewById(R.id.id_btn_dialog_confirm);
            btnConfirm.setText(dialogParam.getPositiveBtnText());
            // 设置右上角x的显示状态
            customView.findViewById(R.id.id_iv_dialog_close).setVisibility(dialogParam.isShowCloseIcon() ? View.VISIBLE : View.INVISIBLE);
        }

        final AlertDialog dialog = builder.show();

        customView.findViewById(R.id.id_iv_dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null)
                    callback.onCancel();
            }
        });
        customView.findViewById(R.id.id_btn_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null)
                    callback.onCancel();
            }
        });
        customView.findViewById(R.id.id_btn_dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null)
                    callback.onConfirm();
            }
        });
        return dialog;
    }

    /**
     * 创建一个自定义的确认对话框
     *
     * @param context     上下文
     * @param innerView   自定义内容
     * @param dialogParam 参数
     * @param callback    回调
     * @return AlertDialog对象
     */
    public static AlertDialog showCustomConfirmDialog(Context context, View innerView, @NonNull DialogParam dialogParam, final NativeDialogCallback callback) {
        View customView = View.inflate(context, R.layout.dialog_custom_confirm_adv, null);
        if (dialogParam.getDialogSize() == DialogParam.SIZE_LARGE) {
            customView = View.inflate(context, R.layout.dialog_custom_confirm_adv_large, null);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogWithDim);
        builder.setView(customView);
        builder.setCancelable(dialogParam.isCancelAble());

        if (innerView != null) {
            // 设置标题
            TextView tvTitle = customView.findViewById(R.id.id_tv_dialog_title);
            tvTitle.setText(dialogParam.getTitle());
            // 设置取消按钮
            TextView btnCancel = customView.findViewById(R.id.id_btn_dialog_cancel);
            btnCancel.setText(dialogParam.getNegativeBtnText());
            // 设置确认按钮
            TextView btnConfirm = customView.findViewById(R.id.id_btn_dialog_confirm);
            btnConfirm.setText(dialogParam.getPositiveBtnText());
            // 设置按钮可见性
            btnCancel.setBackgroundResource(dialogParam.isHideBtns() ? R.drawable.bg_dialog_btn_white_bottom : R.drawable.bg_dialog_btn_white_left);
            btnConfirm.setVisibility(dialogParam.isHideBtns() ? View.GONE : View.VISIBLE);
            View line = customView.findViewById(R.id.id_dialog_btn_split_line);
            if (line != null)
                line.setVisibility(dialogParam.isHideBtns() ? View.GONE : View.VISIBLE);

            // 添加自定义View
            LinearLayout linearLayout = customView.findViewById(R.id.id_container_dialog_custom_view);
            linearLayout.addView(innerView);
            // 设置右上角x的显示状态
            customView.findViewById(R.id.id_iv_dialog_close).setVisibility(dialogParam.isShowCloseIcon() ? View.VISIBLE : View.INVISIBLE);
        }

        final AlertDialog dialog = builder.show();

        customView.findViewById(R.id.id_iv_dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null)
                    callback.onCancel();
            }
        });
        customView.findViewById(R.id.id_btn_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null)
                    callback.onCancel();
            }
        });
        customView.findViewById(R.id.id_btn_dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null)
                    callback.onConfirm();
            }
        });
        return dialog;
    }

    /**
     * 创建一个自定义的消息提示框
     *
     * @param context
     * @param msg
     * @return
     */
    public static AlertDialog showCustomMessageDialog(Context context, String msg) {
        DialogParam dialogParam = new DialogParam(msg);
        dialogParam.setPositiveBtnText(context.getString(R.string.txt_i_know));
        return showCustomMessageDialog(context, dialogParam, null);
    }

    /**
     * 创建一个自定义的消息提示框
     *
     * @param context
     * @param dialogParam
     * @return
     */
    public static AlertDialog showCustomMessageDialog(Context context, DialogParam dialogParam) {
        return showCustomMessageDialog(context, dialogParam, null);
    }

    /**
     * 创建一个自定义的消息提示框
     *
     * @param context
     * @param dialogParam
     * @param callback
     * @return
     */
    public static AlertDialog showCustomMessageDialog(Context context, DialogParam dialogParam, final NativeDialogCallback callback) {
        View customView = View.inflate(context, R.layout.dialog_custom_message, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogWithDim);
        builder.setView(customView).setCancelable(dialogParam.isCancelAble());

        if (dialogParam != null) {
            // 设置标题
            TextView tvTitle = customView.findViewById(R.id.id_tv_dialog_title);
            tvTitle.setText(dialogParam.getTitle());

            ScrollView scrollView = customView.findViewById(R.id.id_scrollview_content);
            // 设置内容
            TextView tvContent = customView.findViewById(R.id.id_tv_dialog_content);
            if (dialogParam.getAdvMessage() != null) {
                tvContent.setText(dialogParam.getAdvMessage());
            } else {
                tvContent.setText(dialogParam.getMessage());
            }
            ViewGroup.LayoutParams params = scrollView.getLayoutParams();
            int maxHeight = AutoUtils.getPercentHeightSize(300);
            if(params != null && params.width > 0){
                int contentH = getContentHeight(tvContent,params.width,tvContent.getText().toString());
                if(contentH > maxHeight){
                    params.height = maxHeight;
                }else {
                    params.height = AutoFrameLayout.LayoutParams.WRAP_CONTENT;
                }
            }else {
                if(params == null){
                    params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                params.height = maxHeight;
            }
            scrollView.setLayoutParams(params);

            // 设置确认按钮
            TextView btnConfirm = customView.findViewById(R.id.id_btn_dialog_confirm);
            btnConfirm.setText(dialogParam.getPositiveBtnText());
            // 设置右上角x的显示状态
            customView.findViewById(R.id.id_iv_dialog_close).setVisibility(dialogParam.isShowCloseIcon() ? View.VISIBLE : View.INVISIBLE);
        }

        final AlertDialog dialog = builder.show();

        customView.findViewById(R.id.id_iv_dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null)
                    callback.onCancel();
            }
        });
        customView.findViewById(R.id.id_btn_dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null)
                    callback.onConfirm();
            }
        });
        return dialog;
    }

    /**
     * 获取文字高度
     * @param textView
     * @param width
     * @param message
     * @return
     */
    private static int getContentHeight(@NonNull TextView textView,int width,String message){
        if(TextUtils.isEmpty(message)){
            return 0;
        }
        Paint paint = new Paint();
        paint.setTextSize(textView.getTextSize());
        int lines = 0;
        String[] splits = message.split("\n");
        for (String text : splits){
            float len = paint.measureText(text);

            lines += len / width + 1;
        }
        return (int) (lines * (textView.getLineHeight() + textView.getLineSpacingExtra()));
    }

    public static class DialogParam {
        public static final int SIZE_NORMAL = 0;// 正常
        public static final int SIZE_LARGE = 1;// 加大型

        private String title = "温馨提示";
        private String message = "您确定要这么做吗？";
        private String positiveBtnText = "确定";
        private String negativeBtnText = "取消";
        private CharSequence advMessage;
        private int dialogSize = SIZE_NORMAL;
        private boolean showCloseIcon = false;
        private boolean centerContent = true;// 默认居中
        private boolean cancelAble = false;// 默认居中
        private boolean hideBtns = false;// 隐藏按钮组

        public DialogParam() {
        }

        public DialogParam(String message) {
            this.message = message;
        }

        public DialogParam(CharSequence advMessage) {
            this.advMessage = advMessage;
        }

        public DialogParam(String title, String message) {
            this.title = title;
            this.message = message;
        }

        public DialogParam(String title, String message, String positiveBtnText, String negativeBtnText) {
            this.title = title;
            this.message = message;
            this.positiveBtnText = positiveBtnText;
            this.negativeBtnText = negativeBtnText;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPositiveBtnText() {
            return positiveBtnText;
        }

        public void setPositiveBtnText(String positiveBtnText) {
            this.positiveBtnText = positiveBtnText;
        }

        public String getNegativeBtnText() {
            return negativeBtnText;
        }

        public void setNegativeBtnText(String negativeBtnText) {
            this.negativeBtnText = negativeBtnText;
        }

        public boolean isShowCloseIcon() {
            return showCloseIcon;
        }

        public void setShowCloseIcon(boolean showCloseIcon) {
            this.showCloseIcon = showCloseIcon;
        }

        public CharSequence getAdvMessage() {
            return advMessage;
        }

        public void setAdvMessage(CharSequence advMessage) {
            this.advMessage = advMessage;
        }

        public boolean isCenterContent() {
            return centerContent;
        }

        public void setCenterContent(boolean centerContent) {
            this.centerContent = centerContent;
        }

        public boolean isCancelAble() {
            return cancelAble;
        }

        public void setCancelAble(boolean cancelAble) {
            this.cancelAble = cancelAble;
        }

        public int getDialogSize() {
            return dialogSize;
        }

        public void setDialogSize(int dialogSize) {
            this.dialogSize = dialogSize;
        }

        public boolean isHideBtns() {
            return hideBtns;
        }

        public void setHideBtns(boolean hideBtns) {
            this.hideBtns = hideBtns;
        }
    }

    public interface NativeDialogCallback {
        void onConfirm();

        void onCancel();
    }
}
