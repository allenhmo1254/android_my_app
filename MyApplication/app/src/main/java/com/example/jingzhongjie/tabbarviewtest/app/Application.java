package com.example.jingzhongjie.tabbarviewtest.app;

import com.example.jingzhongjie.tabbarviewtest.BuildConfig;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Created by jingzhongjie on 17/2/14.
 */

public class Application extends android.app.Application
{
    private static Application application = new Application();

    public static Application shareApplication()
    {
        return application;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        initLogger();
    }

    private void initLogger()
    {
        LogLevel level = BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE;
        Logger.init("MyApp")
                .setLogLevel(level)
                .setMethodCount(2)
                .hideThreadInfo();
    }
}
