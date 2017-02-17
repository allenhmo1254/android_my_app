package com.example.jingzhongjie.tabbarviewtest.foundation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jingzhongjie on 17/2/15.
 */

public class BaseToolbarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);

        ActivityManager.getActivityManager().activityOnCreate(this, saveInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ActivityManager.getActivityManager().activityOnStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ActivityManager.getActivityManager().activityOnResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager.getActivityManager().activityOnPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        ActivityManager.getActivityManager().activityOnStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityManager.getActivityManager().activityOnDestroy(this);
    }
}
