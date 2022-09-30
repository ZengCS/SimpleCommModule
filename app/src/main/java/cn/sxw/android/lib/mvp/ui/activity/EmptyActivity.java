package cn.sxw.android.lib.mvp.ui.activity;

import android.Manifest;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cn.sxw.android.base.adapter.CommonRecyclerAdapter;
import cn.sxw.android.base.app.ActivityStackManger;
import cn.sxw.android.base.bean.BlankBean;
import cn.sxw.android.base.di.component.AppComponent;
import cn.sxw.android.base.dialog.CustomDialogHelper;
import cn.sxw.android.base.imageloader.ImageLoader;
import cn.sxw.android.base.okhttp.HttpUrlEncode;
import cn.sxw.android.base.utils.JListKit;
import cn.sxw.android.base.utils.LogUtil;
import cn.sxw.android.lib.R;
import cn.sxw.android.lib.di.component.DaggerEmptyComponent;
import cn.sxw.android.lib.di.module.EmptyModule;
import cn.sxw.android.lib.mvp.base.BaseActivityAdv;
import cn.sxw.android.lib.mvp.presenter.EmptyPresenter;
import cn.sxw.android.lib.mvp.view.IEmptyView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@EActivity(R.layout.activity_empty)
@RuntimePermissions
public class EmptyActivity extends BaseActivityAdv<EmptyPresenter> implements IEmptyView {
    private ImageLoader mImageLoader;// 用于加载图片的管理类,默认使用glide
    private List<BlankBean> mItems = JListKit.newArrayList();
    private CommonRecyclerAdapter<BlankBean> mAdapter;

    // Views
    @ViewById(R.id.id_rv_empty)
    RecyclerView mRecyclerView;
    @ViewById(R.id.id_tv_response_info)
    TextView tvResponse;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerEmptyComponent
                .builder()
                .appComponent(appComponent)
                .emptyModule(new EmptyModule(this))
                .build()
                .inject(this);
        mImageLoader = mApplication.getAppComponent().imageLoader();
    }

    @Click({R.id.id_btn_get_bean,
            R.id.id_btn_get_boolean,
            R.id.id_btn_get_string,
            R.id.id_btn_get_integer,
            R.id.id_btn_failed,
            R.id.id_btn_error_json,
            R.id.id_btn_bad_gateway,
            R.id.id_btn_login,
            R.id.id_btn_login_t,
            R.id.id_btn_login_p,
            R.id.id_btn_token_expired,
            R.id.id_btn_not_found,
    })
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_token_expired:
                mPresenter.getStringByOkhttp("http://www.mocky.io/v2/5c3eeb293500002d003e9a63");
                break;
            case R.id.id_btn_login:
                onLogin(1);
                break;
            case R.id.id_btn_login_t:
                onLogin(2);
                break;
            case R.id.id_btn_login_p:
                onLogin(3);
                break;
            case R.id.id_btn_get_bean:
                mPresenter.getObjectByOkhttp("http://www.mocky.io/v2/5c35b8e63000009f0021b4a3");
                break;
            case R.id.id_btn_get_boolean:
                mPresenter.getStringByOkhttp("http://www.mocky.io/v2/5c3e9f6a3500005a003e98fd");
                break;
            case R.id.id_btn_get_string:
                mPresenter.getStringByOkhttp("http://www.mocky.io/v2/5c3ea04e350000860a3e98ff");
                break;
            case R.id.id_btn_get_integer:
                mPresenter.getStringByOkhttp("http://www.mocky.io/v2/5c3ea01d35000055003e98fe");
                break;
            case R.id.id_btn_failed:
                mPresenter.getObjectByOkhttp("http://www.mocky.io/v2/5c35c3b9300000780021b4e9");
                break;
            case R.id.id_btn_error_json:
                mPresenter.getObjectByOkhttp("http://www.mocky.io/v2/5c35c41a3000007f0021b4ec");
                break;
            case R.id.id_btn_not_found:// 自定义接口路径
                mPresenter.getObjectByOkhttp("/update/app/errorapi");
                break;
            case R.id.id_btn_bad_gateway:// 自定义接口路径
                // String urlEncode2 = HttpUrlEncode.encode("http", "api2.test.sxw.cn", "/update/app/list", null);
                // String urlEncode2 = HttpUrlEncode.encode("http://api2.test.sxw.cn/update/app/list", null);
                ConcurrentHashMap<String, String> hashMap = new ConcurrentHashMap<>();
                hashMap.put("wd", "Android");
                String urlEncode2 = HttpUrlEncode.encode("https://www.baidu.com/s", hashMap);
                mPresenter.getObjectByOkhttp(urlEncode2);
                break;
        }
    }

    @AfterViews
    void init() {
        // 添加下拉刷新能力
        super.addSwipeRefreshAbility();
        // 初始化列表
        initAdapter();
        // 初始化数据
        onRefresh();
    }

    private void initAdapter() {
        if (mAdapter == null) {
            mAdapter = new CommonRecyclerAdapter<BlankBean>(R.layout.item_blank, mItems) {
                @Override
                protected void convert(BaseViewHolder helper, BlankBean item) {
                    // 绑定数据
                    helper.setText(R.id.id_tv_blank_name, item.getName());
                    helper.setText(R.id.id_tv_blank_desc, item.getDesc());
                    // 加载网络图片
                    mImageLoader.displayImage(item.getPic(), 0, helper.getView(R.id.id_iv_blank_pic));
                    // Glide.with(mContext).load(item.getPic()).crossFade().into((ImageView) helper.getView(R.id.id_iv_blank_pic));
                    // 绑定子View点击事件
                    helper.getView(R.id.id_btn_inner).setOnClickListener(v -> {
                        // TODO 编写具体代码
                        int position = helper.getAdapterPosition();
                        showToast("我是元素内的按钮，position = " + position);
                    });
                    // 设置可见性 true for VISIBLE, false for GONE.
                    helper.setGone(R.id.id_view_new, item.isNewest());
                }
            };
            // mAdapter.addHeaderView();
            // mAdapter.addFooterView();
            // 点击事件
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                // TODO 编写具体代码
                showToast("Click Root position = " + position);
            });

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mAdapter);
            // 设置加载更多能力
            mAdapter.setEnableLoadMore(true);
            mAdapter.setOnLoadMoreListener(this::getDataFromNet, mRecyclerView);
            // 默认第一次加载会进入回调，如果不需要可以配置：
            mAdapter.disableLoadMoreIfNotFullPage();
        }
    }

    @Override
    protected void getDataFromNet() {
        LogUtil.d("currPage = " + currPage);
        mPresenter.getListByOkhttp("http://www.mocky.io/v2/5c35bf0d3000005f0021b4d8");
    }

    /**
     * 杀死自己
     */
    @Override
    public void killMyself() {
        ActivityStackManger.getInstance().removeActivity(this);
    }

    @Override
    public void onRequestSuccess(List<BlankBean> list) {
        hideLoading();
        mAdapter.setEnableLoadMore(true);
        if (list == null || list.size() == 0) {
            if (mItems == null || mItems.size() == 0)
                mAdapter.setEmptyView(notDataView);
            return;
        }
        showToast("数据加载成功~");

        if (currPage == 1)
            mItems.clear();
        mItems.addAll(list);
        // 模拟只加载4页数据
        hasMoreData = currPage < 4;

        if (currPage == 1) {
            mAdapter.setNewData(mItems);
        } else {
            if (hasMoreData) {
                mAdapter.loadMoreComplete();
            } else {
                mAdapter.loadMoreEnd();
            }
        }
        // 数据加载成功后，页数+1
        currPage++;
    }

    @Override
    public void onLoginResult(boolean success, String msg) {
        if (success) {
            showToast("登录成功。");
            // TODO 进入指定页面
            launchActivity(UserProfileActivity_.class);
        } else {
            showToast("登录失败，" + msg);
        }
    }

    @Override
    public void onFailed(String msg) {
        hideLoading();
        mAdapter.setEnableLoadMore(true);
        if (TextUtils.isEmpty(msg)) {
            msg = getString(R.string.txt_load_data_error);
        }
        showToast(msg);
        // 如果当前是第一页，加载失败后，显示errorView
        if (currPage == 1) {
            mAdapter.setEmptyView(errorView);
        } else {// 当前不是第一页,加载失败后,会在列表底部显示重试按钮
            mAdapter.loadMoreFail();
        }
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        if (mItems == null || mItems.size() == 0)
            mAdapter.setEmptyView(mLoadingView);
        currPage = 1;
        getDataFromNet();
    }

    @Override
    public TextView getTipsTextView() {
        return tvResponse;
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onLogin(int userType) {// 用户组类型(0=普通,1=学生,2=老师,3=家长,5=管理员,6=租户管理员)多个类型，请使用逗号分隔,如[2,5]
        switch (userType) {
            case 1:// 学生
                mPresenter.login("510101201703290037", "111111", userType);
                break;
            case 2:// 教师
                mPresenter.login("511101201703290014", "a123456", userType);
                break;
            case 3:// 家长
                // mPresenter.login("17000000003", "123456", userType);
                // mPresenter.login("18800078945", "123456", userType);
                mPresenter.login("15630000123", "123456", userType);
                break;
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        EmptyActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied() {
        showToast("请授权我们使用你的存储，否则无法登录。");
    }

    private AlertDialog mNeverAskDialog;

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onNeverAskAgain() {
        if (mNeverAskDialog == null) {
            String appName = getString(R.string.app_name);
            // 申请相机权限时被拒绝了，并且不再询问。
            mNeverAskDialog =
                    new AlertDialog.Builder(this)
                            .setMessage(getString(R.string.permission_tips_never_ask, appName, appName, "存储"))
                            .setPositiveButton("应用设置", (dialog, button) -> openAppDetailSettings())
                            .setNegativeButton("取消", (dialog, button) -> showToast("请授权我们使用你的存储，否则无法登录。"))
                            .show();
        } else if (!mNeverAskDialog.isShowing()) {
            mNeverAskDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomDialogHelper.releaseDialog(mNeverAskDialog);
    }
}
