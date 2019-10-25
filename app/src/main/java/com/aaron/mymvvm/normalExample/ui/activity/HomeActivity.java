package com.aaron.mymvvm.normalExample.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.aaron.baselibrary.BaseActivity;
import com.aaron.mvvmlibrary.adapter.BaseFragmentAdapter;
import com.aaron.mymvvm.R;
import com.aaron.mymvvm.normalExample.helper.DoubleClickHelper;
import com.aaron.mymvvm.normalExample.ui.fragment.TestFragmentA;
import com.aaron.mymvvm.normalExample.ui.fragment.TestFragmentB;
import com.aaron.mymvvm.normalExample.ui.fragment.TestFragmentC;
import com.aaron.mymvvm.normalExample.ui.fragment.TestFragmentD;
import com.aaron.utilslibrary.utils.ToastUtils;
import com.aaron.utilslibrary.utils.android.ActivityUtils;
import com.aaron.widgetlibrary.layout.NoScrollViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * desc   : 主页界面
 */
public final class HomeActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    ViewPager mViewPager;
    BottomNavigationView mBottomNavigationView;

    /**
     * ViewPager 适配器
     */
    private BaseFragmentAdapter<Fragment> mPagerAdapter;

    /**
     * 获取布局 ID
     *
     * @return 布局layout的id
     */
    @Override
    protected int f_getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void f_initView(Bundle savedInstanceState) {
        mViewPager = (NoScrollViewPager) findViewById(R.id.vp_home_pager);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bv_home_navigation);
        // 不使用图标默认变色
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mViewPager.addOnPageChangeListener(this);

        mPagerAdapter = new BaseFragmentAdapter<>(this);
        mPagerAdapter.addFragment(TestFragmentA.newInstance());
        mPagerAdapter.addFragment(TestFragmentB.newInstance());
        mPagerAdapter.addFragment(TestFragmentC.newInstance());
        mPagerAdapter.addFragment(TestFragmentD.newInstance());

        mViewPager.setAdapter(mPagerAdapter);

        // 限制页面数量
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mBottomNavigationView.setSelectedItemId(R.id.menu_home);
                break;
            case 1:
                mBottomNavigationView.setSelectedItemId(R.id.home_found);
                break;
            case 2:
                mBottomNavigationView.setSelectedItemId(R.id.home_message);
                break;
            case 3:
                mBottomNavigationView.setSelectedItemId(R.id.home_me);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                mViewPager.setCurrentItem(0);
                return true;
            case R.id.home_found:
                mViewPager.setCurrentItem(1);
                return true;
            case R.id.home_message:
                mViewPager.setCurrentItem(2);
                return true;
            case R.id.home_me:
                mViewPager.setCurrentItem(3);
                return true;
            default:
                break;
        }
        return false;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // 回调当前 Fragment 的 onKeyDown 方法
//        if (mPagerAdapter.getCurrentFragment().onKeyDown(keyCode, event)) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onBackPressed() {
        if (DoubleClickHelper.isOnDoubleClick()) {
            //移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack(false);
            f_postDelayed(new Runnable() {

                @Override
                public void run() {
                    // 进行内存优化，销毁掉所有的界面
                    ActivityUtils.removeAllActivity();
                    // 销毁进程（请注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
                    // System.exit(0);
                }
            }, 300);
        } else {
            ToastUtils.showShort(R.string.home_exit_hint);
        }
    }

    @Override
    protected void onDestroy() {
        mViewPager.removeOnPageChangeListener(this);
        mViewPager.setAdapter(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(null);
        super.onDestroy();
    }
}