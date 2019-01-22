package cn.sxw.android.base.ui;

import android.support.v4.app.FragmentActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity
public abstract class BaseFragmentActivity extends BaseActivity {
	protected BaseApplication mApplication;
	protected BackPressListener listener;

	public interface BackPressListener {
		public void onBackPressed();
	}

	@AfterViews
	protected void init() {
		toFirstFragment();
	}

	@Override
	public void onBackPressed() {
		if (listener != null) {
			listener.onBackPressed();
		} else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
			getSupportFragmentManager().popBackStack();
		} else {
			finish();
		}
	}

	public void setOnBackPressedListener(BackPressListener backPressListener) {
		listener = backPressListener;
	}

	public abstract void toFirstFragment();
}
