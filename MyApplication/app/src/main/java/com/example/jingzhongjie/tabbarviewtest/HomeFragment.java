package com.example.jingzhongjie.tabbarviewtest;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jingzhongjie.tabbarviewtest.home.HallFragment;
import com.example.jingzhongjie.tabbarviewtest.home.HotFragment;
import com.example.jingzhongjie.tabbarviewtest.home.RecommendFragment;
import com.example.jingzhongjie.tabbarviewtest.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {

    @BindView(R.id.pagerSlidingTabStrip)
    PagerSlidingTabStrip pagerSlidingTabStrip;
    @BindView(R.id.searchButton)
    ImageView searchButton;
    @BindView(R.id.listButton)
    ImageView listButton;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @OnClick(R.id.searchButton)
    public void onSearchButtonClick()
    {
//        ActivityManager.getActivityManager().startWithAction(".activity.main.List");
    }

    @OnClick(R.id.listButton)
    public void onListButtonClick()
    {
//        ActivityManager.getActivityManager().startWithAction(".activity.main.Search");
    }

    private int[] titles = {R.string.navigation_001, R.string.navigation_002, R.string.navigation_003};
    private List<Fragment> fragments = new ArrayList<>();
    private MyPagerFragmentAdapter adapter;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        unbinder = ButterKnife.bind(this, view);

        FragmentManager fm = getChildFragmentManager();
        if (savedInstanceState != null)
        {
            List<Fragment> fragments = fm.getFragments();
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment fragment : fragments)
            {
                if (fragment != null)
                {
                    ft.remove(fragment);
                }
            }
            ft.commit();
            fm.executePendingTransactions();
        }

        if (fragments.isEmpty())
        {
            fragments.add(new RecommendFragment());
            fragments.add(new HotFragment());
            fragments.add(new HallFragment());
        }

        if (adapter == null)
        {
            adapter = new MyPagerFragmentAdapter(fm);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager.setAdapter(adapter);
        pagerSlidingTabStrip.setViewPager(viewPager);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class MyPagerFragmentAdapter extends FragmentStatePagerAdapter
    {
        public MyPagerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(titles[position]);
        }

        @Override
        public Parcelable saveState() {
            //阻止 Fragment 的恢复和保存
            return null;
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            //阻止 Fragment 的恢复和保存
        }
    }
}
