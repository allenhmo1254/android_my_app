package com.example.jingzhongjie.tabbarviewtest.foundation;

import android.app.Application;

import org.json.JSONObject;

/**
 * Created by jingzhongjie on 17/2/9.
 */

public class Foundation {
    private static Foundation singleton = new Foundation();
    private JSONObject mActivityConfig;
    private Application mApplication;

    /**
     * Foundation 单例实例获取，在使用 Foundation 层的任何类之前需要先初始化该类。
     * @return 单例实例
     */
    public static synchronized Foundation shareInstance(){
        return singleton;
    }

    /**
     * 设置 Activity 的配置数据，该配置数据是用于 ActivityManager， ActivityManager 可以通过 tag 来启动一个
     * Activity ,这样就可以降低两个Activity 之前的偶合度。
     * 如果不配置此数据则 ActivityManager 不能通过 tag 来打开 activity，只能使用类名称。
     *
     * @param config 配置数据
     * @return {@link Foundation}
     */
    public Foundation setActivityConfig(JSONObject config){
        mActivityConfig = config;
        return this;
    }

    /**
     * 获取 Activity 的配置数据，该配置数据是用于 ActivityManager， ActivityManager 可以通过 tag 来启动一个
     * Activity ,这样就可以降低两个Activity 之前的偶合度。
     * 如果不配置此数据则 ActivityManager 不能通过 tag 来打开 activity，只能使用类名称。
     *
     * @return 配置数据
     */
    public JSONObject getActivityConfig(){
        return mActivityConfig;
    }

    /**
     * 获取当前应用实例，该实例对象是在实始化阶段使用 setCurrentApplication 方法设置进去的。
     * @return 当前应用实例
     */
    public Application currentApplication(){
        return mApplication;
    }

    /**
     * 设置当前应用实例
     *
     * @param application 当前应用实例
     * @return {@link Foundation}
     */
    public Foundation setCurrentApplication(Application application){
        mApplication = application;
        return this;
    }
}
