package com.example.jingzhongjie.tabbarviewtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.example.jingzhongjie.tabbarviewtest.app.Application;
import com.example.jingzhongjie.tabbarviewtest.app.ToolbarActivity;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends ToolbarActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @OnClick(R.id.btnPremiere)
    public void onBtnPremiereClick()
    {
        Logger.d("开播按钮");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Application.shareApplication();

        if (savedInstanceState != null) {
            FragmentManager fm = getSupportFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment fragment : fragments) {
                if (fragment != null){
                    ft.remove(fragment);
                }
            }
            ft.commit();
            fm.executePendingTransactions();
        }

        radioGroup.setOnCheckedChangeListener(this);
        radioGroup.check(R.id.radioHome);
    }

    private Fragment findFragmentByTag(int tag)
    {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(String.valueOf(tag));
        if (fragment != null)
        {
            return fragment;
        }

        if (tag == R.id.radioHome)
        {
            fragment = new HomeFragment();
        }
        else if (tag == R.id.radioMe)
        {
            fragment = new MyFragment();
        }
        fm.beginTransaction().add(R.id.fragmentContainer, fragment, String.valueOf(tag)).commit();
        fm.executePendingTransactions();

        return fragment;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Logger.d("checkedId = "+checkedId);
        FragmentManager fm = getSupportFragmentManager();
        Fragment home = findFragmentByTag(R.id.radioHome);
        Fragment my = findFragmentByTag(R.id.radioMe);
        if (checkedId == R.id.radioHome)
        {
            fm.beginTransaction().show(home).hide(my).commit();
        }
        else if (checkedId == R.id.radioMe)
        {
            fm.beginTransaction().show(my).hide(home).commit();
        }
        fm.executePendingTransactions();
    }
}
