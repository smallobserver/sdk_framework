package com.tinyblack.framework.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tinyblack.framework.R;


/**
 * @author : zlf
 * date    : 2019/4/17
 * github  : https://github.com/mamumu
 * blog    : https://www.jianshu.com/u/281e9668a5a6
 */
public class Loading extends Dialog {
    public static int LOADING_LAYOUT = R.layout.sdk_view_loading;
    /**
     * 延迟show及dismiss，防止dialog一闪而过，或者show->dismiss->show过程间隔太短出现“闪屏”
     */
    private static final int MIN_SHOW_TIME = 200; // ms
    private static final int MIN_DELAY = 400; // ms
    private Handler mHandler = new Handler(Looper.getMainLooper());
    long mStartTime = -1; // 开始显示时的时间

    boolean mPostedHide = false;

    boolean mPostedShow = false;

    boolean mDismissed = false;

    private final Runnable mDelayedHide = new Runnable() {

        @Override
        public void run() {
            mPostedHide = false;
            mStartTime = -1;
            dismiss();
        }
    };


    private final Runnable mDelayedShow = new Runnable() {

        @Override
        public void run() {
            mPostedShow = false;
            if (!mDismissed) {
                mStartTime = System.currentTimeMillis();
                try {
                    show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public Loading(Context context) {
        super(context);
    }

    public Loading(Context context, int themeResId) {
        super(context, themeResId);
    }


    private boolean isLoadingMessageShow;
    private TextView messageView;
    private String defaultLoadingText = null;

    public void setLoadingMessage(String message) {
        if (messageView == null) {
            messageView = findViewById(R.id.textMsg);
        }
        if (defaultLoadingText == null) {
            String content = messageView.getText().toString();
            if (TextUtils.isEmpty(content)) {
                defaultLoadingText = "加载中...";
            } else {
                defaultLoadingText = content;
            }
        }
        if (message != null) {
            messageView.setText(message);
        } else {
            messageView.setText(defaultLoadingText);
        }
    }

    public void setLoadingMessageShow(boolean isLoadingMessageShow) {
        if (messageView == null) {
            messageView = findViewById(R.id.textMsg);
        }
        if (isLoadingMessageShow) {
            messageView.setVisibility(View.VISIBLE);
        } else {
            messageView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeCallbacks();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks();
    }

    private void removeCallbacks() {
        mHandler.removeCallbacks(mDelayedHide);
        mHandler.removeCallbacks(mDelayedShow);
    }

    public synchronized void dismissDialog() {
        mDismissed = true;
        mHandler.removeCallbacks(mDelayedShow);
        mPostedShow = false;
        long diff = System.currentTimeMillis() - mStartTime;
        if (diff >= MIN_SHOW_TIME || mStartTime == -1) {
            // ContentLoadingProgressBar的显示时间已经超过了500ms或者还没有显示
            dismiss();
        } else {
            // ContentLoadingProgressBar的显示时间不足500ms
            if (!mPostedHide) {
                mHandler.postDelayed(mDelayedHide, MIN_SHOW_TIME - diff);
                mPostedHide = true;
            }
        }
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */

    public synchronized void showDialog() {
        // Reset the start time.
        mStartTime = -1;
        mDismissed = false;
        mHandler.removeCallbacks(mDelayedHide);
        mPostedHide = false;
        if (!mPostedShow) {
            mHandler.postDelayed(mDelayedShow, MIN_DELAY);
            mPostedShow = true;
        }
    }

    public static class Builder {

        private Context context;
        private String message;
        private boolean isShowMessage = true;
        private boolean isCancelable = false;
        private boolean isCancelOutside = false;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置提示信息
         *
         * @param message
         * @return
         */

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置是否显示提示信息
         *
         * @param isShowMessage
         * @return
         */
        public Builder setShowMessage(boolean isShowMessage) {
            this.isShowMessage = isShowMessage;
            return this;
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */

        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        /**
         * 设置是否可以点击外部取消
         *
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }

        public Loading create() {
            Loading loading = null;
            if (controller == null) {
                loading = new Loading(context, R.style.SDKFrameworkDialogStyle);
                loading.setContentView(LOADING_LAYOUT);
                loading.setCancelable(isCancelable);
                loading.setCanceledOnTouchOutside(isCancelOutside);
            } else {
                loading = controller.customDialog(context);
            }
            loading.setLoadingMessageShow(isShowMessage);
            loading.setLoadingMessage(message);
            //实现loading的透明度
//            WindowManager.LayoutParams lp=Loading.getWindow().getAttributes();
//            lp.alpha = 0.6f;
//            Loading.getWindow().setAttributes(lp);
            return loading;
        }
    }

    private static CustomDialogController controller;

    public static void setCustomDialogController(CustomDialogController controller) {
        Loading.controller = controller;
    }

    public interface CustomDialogController {
        /**
         * 自定位 loading 样式
         *
         * @param context
         * @return
         */
        Loading customDialog(Context context);
    }
}
