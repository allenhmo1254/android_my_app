package com.example.jingzhongjie.tabbarviewtest.foundation;

import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jingzhongjie on 17/2/9.
 */

public class ActivityManager {
    private static final String TAG = "ActivityManager";

    public static final String ACTIVITY_TYPE_NATIVE  = "native";
    public static final String ACTIVITY_TYPE_WEBVIEW = "webview";
    public static final String ACTIVITY_TYPE_WEBAPP  = "webapp";

    /** 页面的 action 参数 Key*/
    public static final String INTENT_PARAM_KEY_ACTION = "acAction";
    /** 标面参数 key */
    public static final String INTENT_PARAM_KEY_TITLE= "acTitle";
    /**业务requestCode */
    public static final String INTENT_PARAM_KEY_REQUEST_CODE = "acRequestCode";
    /** URL */
    public static final String INTENT_PARAM_KEY_URL = "acURL";
    /** Activity Key */
    public static final String INTENT_PARAM_KEY_ACTIVITYKEY = "acActivityKey";

    /**要打开的Activity 没有找到，这个错误没有参数*/
    public static final int ERROR_ACTIVITY_NOT_FOUND       = 1;
    /**Pop 操作时的 number 参数不正确，这个错误没有参数*/
    public static final int ERROR_POP_NUMBER_ERROR         = 2;
    /**当前Activity 栈为空不能进能行操作*/
    public static final int ERROR_ACTIVITY_STACK_IS_EMPTY  = 3;
    /**Config key 未找到*/
    public static final int ERROR_CONFIG_KEY_NOT_FOUND     = 4;
    /**Pop 操作时的 目标 Activity 未找到*/
    public static final int ERROR_POP_TARGET_ACTIVITY_NOT_FOUND = 5;

    //activities
    private final LinkedList<Activity> mActivities = new LinkedList<Activity>();

    //pop 操作时如果弹出的不只是顶层 Activity，则该参数保存的是最后一个要弹出的 Activity
    private Activity mLastPopTarget;

    private static ActivityManager activityManagerInstance = new ActivityManager();

    /**
     * 返回 ActivityManager 实例，如果没有调用过 setActivityManager 方法设置过实例，则返回一个默认实例。
     * 你可以用 setActivityManager 设置一个自定义的 ActivityManager 实例，不过需要注意，如果在应用运行期间
     * 重新设置 ActivityManager 实例则会破坏导航栈，因此需要在应用初始化阶段完成设置
     * @return ActivityManager 实例
     */
    public static ActivityManager getActivityManager (){
        return activityManagerInstance;
    }

    /**
     * 设置自定义 ActivityManager 实列，需要注意，如果在应用运行期间重新设置 ActivityManager 实例则会破坏
     * 导航栈，因此需要在应用初始化阶段完成设置
     * @param am ActivityManager 实列或其子类实例，该参数不能为 null。
     */
    public static void setActivityManager(ActivityManager am){
        if (am == null){
            return;
        }

        activityManagerInstance = am;
    }

    protected ActivityManager() {
        /**禁步外部直接实例化*/
    }

    /**
     * 返回当前导航栈中的所有的 Activity
     * @return Activity 列表
     */
    public LinkedList<Activity> getActivities() {
        return (LinkedList<Activity>)mActivities.clone();
    }

    /**
     * 使用 key 方法启动一个 Activity，缺省使用最顶层的 Activity 开启新 Activity。
     *
     * @param key       目标 Activity 对应的 key，key 定义在一个配置文件中。
     *
     * @return 如果启动成功返回 true，如果启动失败则返回 false。
     */
    public boolean start(String key){
        return start(key, new Intent());
    }

    /**
     * 使用 key 方法启动一个 Activity，缺省使用最顶层的 Activity 开启新 Activity。
     *
     * @param key       目标 Activity 对应的 key，key 定义在一个配置文件中。
     * @param intent    Intent 对象
     * @return 如果启动成功返回 true，如果启动失败则返回 false。
     */
    public boolean start(String key, Intent intent){
        return startForResult(key, intent, -1);
    }

    /**
     * 使用 key 方法启动一个 Activity，缺省使用最顶层的 Activity 开启新 Activity。
     *
     * @param key       目标 Activity 对应的 key，key 定义在一个配置文件中。
     * @param intent    Intent 对象。
     * @param requestCode 请求码
     *
     * @return 如果启动成功返回 true，如果启动失败则返回 false。
     */
    public boolean startForResult(String key, Intent intent,int requestCode){
        return startForResult(getTopActivity(), key, intent, requestCode);
    }

    /**
     * 使用 key 方法启动一个 Activity，缺省使用最顶层的 Activity 开启新 Activity。
     *
     * @param current   当前的 Activity
     * @param key       目标 Activity 对应的 key，key 定义在一个配置文件中。
     * @param intent    Intent 对象。
     * @param requestCode 请求码
     *
     * @return 如果启动成功返回 true，如果启动失败则返回 false。
     */
    public boolean startForResult(Activity current,String key, Intent intent,int requestCode){
        intent = makeIntentByKey(current,key, intent);

        if (intent == null){
            return false;
        }

        if(!onPreStartActivity(current, key, intent, requestCode)){
            return false;
        }

        return startActivityForResult(current, intent, requestCode);
    }

    /**
     * 使用类名方式 启动一个 Activity，缺省使用最顶层的 Activity 开启新 Activity。
     *
     * @param action     目标 Activity 的类名，
     * @return 如果启动成功返回 true，如果启动失败则返回 false。
     *
     * @see #startWithActionForResult(Activity, String, Intent, int)
     */
    public boolean startWithAction(String action){
        return startWithActionForResult(action, null, -1);
    }

    /**
     * 使用类名方式 启动一个 Activity，缺省使用最顶层的 Activity 开启新 Activity。
     *
     * @param action     目标 Activity 的类名
     * @param intent     Intent 对象。
     *
     * @return 如果启动成功返回 true，如果启动失败则返回 false。
     *
     * @see #startWithActionForResult(Activity, String, Intent, int)
     */
    public boolean startWithAction(String action, Intent intent){
        return startWithActionForResult(action, intent, -1);
    }

    /**
     * 使用类名方式 启动一个 Activity，缺省使用最顶层的 Activity 开启新 Activity。
     *
     * @param action     目标 Activity 的类名
     * @param intent     Intent 对象。
     *
     * @return 如果启动成功返回 true，如果启动失败则返回 false。
     *
     * @see #startWithActionForResult(Activity, String, Intent, int)
     */
    public boolean startWithActionForResult(String action, Intent intent,int requestCode){
        return startWithActionForResult(getTopActivity(), action, intent, requestCode);
    }

    /**
     * 使用类名方式 启动一个 Activity，缺省使用最顶层的 Activity 开启新 Activity。
     *
     * @param action     目标 Activity 的类名，注意：这个类名不是类全名，具体情况如下：<br>
     *                   1. 目标Activity 的类名必须以  ***Activity 为类名
     *                   2. 如果目标 Activity 在应用包目录下，则 action 是相于应用包目录的相对包路径名，例如：
     *                      应用包目录为 com.xxx.xxx，目标 Activity 类名为 com.xxx.xxx.aaa.***Activity，则
     *                      action 值应为 “.aaa.***”，注意前面要有“.”，最后不包括 Activity。
     *                   3. 如果目标 Activity 没有在应用包目录下，则 action 是包路全径名，例如：应用包目录为 com.xxx.xxx，
     *                      目标 Activity 类名为 com.yyy.yyy.aaa.***Activity，则 action 值应为 “com.yyy.yyy.aaa.***”，
     *                      注意前面要有“.”，最后不包括 Activity。
     *
     * @param intent     Intent 对象。
     *
     * @return 如果启动成功返回 true，如果启动失败则返回 false。
     */
    public boolean startWithActionForResult(Activity current,String action, Intent intent, int requestCode){
        intent = (intent != null ? intent : new Intent());
        setPackgeAndClassByAction(current,action,intent);

        if(!onPreStartActivity(current, action, intent, requestCode)){
            return false;
        }

        return startActivityForResult(current, intent, requestCode);
    }

    /**
     * 关闭当前最顶层的 Activity，返回状态码为 RESULT_CANCELED
     *
     * @return 如果调用成功返回 true
     */
    public boolean finish(){
        return finish(Activity.RESULT_CANCELED, null);
    }

    /**
     * 关闭当前最顶层的 Activity
     * @param resultCode  返回码
     * @param intent      Intent 对象
     * @return            如果调用成功返回 true。
     */
    public boolean finish(int resultCode,Intent intent){
        return pop(1, resultCode, intent);
    }

    /**
     * 弹出导航栈中的Activity，直到 key 指定的 Activity 为止（key 所指定的 Activity 为最终要显示的 Activity）。
     * @param key           Activity 的 Key
     * @param resultCode    返回码
     * @param intent        Intent 对象
     * @return              如果调用成功返回 true。
     */
    public boolean pop(String key,int resultCode,Intent intent){
        Activity activity = getActivityByKey(key);

        if (activity == null){
            return false;
        }

        Activity topActivity = getTopActivity();
        if (topActivity == null){
            onError(ERROR_ACTIVITY_STACK_IS_EMPTY,null);
            return false;
        }

        return pop(activity, resultCode, intent);
    }

    /**
     * 弹出导航栈中的Activity，直到 action 指定的 Activity 为止（action 所指定的 Activity 为最终要显示的 Activity）。
     * @param action        目标 Activity 的类名。 {@link #startWithActionForResult(Activity, String, Intent, int)}
     * @param resultCode    返回码
     * @param intent        Intent 对象
     * @return              如果调用成功返回 true。
     */
    public boolean popWithAction(String action,int resultCode,Intent intent){
        Activity willShowActivity = getActivityByAction(action);
        return pop(willShowActivity, resultCode, intent);
    }

    /**
     * 从导航栈中弹出指定数量的 Activity，如果 number 参数小于0 或大于当前栈中的 Activity 则调用失败。
     * @param number        要弹出的　Activity 数量
     * @return              如果调用成功返回 true。
     */
    public boolean pop(int number){
        return pop(number, -1, null);
    }

    /**
     * 从导航栈中弹出指定数量的 Activity，如果 number 参数小于 1 或大于当前栈中的 Activity 数量则调用失败。
     * @param number        要弹出的　Activity 数量
     * @param resultCode    返回码
     * @param intent        Intent 对象
     * @return              如果调用成功返回 true。
     */
    public boolean pop(int number,int resultCode,Intent intent){
        int size = mActivities.size();
        if (number > size || number < 1){
            //number 值不正确
            Log.e(TAG, "pop 视图的数量不正确，不能小于0或大于当前视图数量");
            onError(ERROR_POP_NUMBER_ERROR,null);
            return false;
        }

        //计算最一个要弹出的Activity 的索引
        int lastTarget = size - number;

        return popActivity(mActivities.get(lastTarget),resultCode,intent);
    }

    /**
     * 从当前的导航栈中弹出视图
     * @param willShowTarget  要弹出的到的目标视图（弹出完成后将要显示的那个视图）。
     * @param resultCode      结果代码
     * @param intent          Intent 对象
     */
    public boolean pop(Activity willShowTarget,int resultCode,Intent intent){
        if (willShowTarget == null){
            //willShowTarget 未找到
            onError(ERROR_POP_TARGET_ACTIVITY_NOT_FOUND,null);
            return false;
        }

        int index = mActivities.indexOf(willShowTarget);

        if (index < 0 || index >= mActivities.size() - 1){
            //队列中没有此视图或索值不正确
            Log.e(TAG, "pop 操作失败，目标视图没有找到。");
            onError(ERROR_ACTIVITY_NOT_FOUND,null);
            return false;
        }

        Activity lastPopActivity = mActivities.get(index + 1);
        return popActivity(lastPopActivity, resultCode, intent);
    }

    /**
     * 获取栈顶的 Activitiy，即当前显示的 Activity。如果当前栈为空，则返回 null。
     * @return Activity
     */
    public Activity getTopActivity(){
        return mActivities.peekLast();
    }

    /**
     * 获取栈底的 Activitiy，即当根 Activity。如果当前栈为空，则返回 null。
     * @return Activity
     */
    public Activity getBottomActivity(){
        return mActivities.peekFirst();
    }

    /**
     * 判断当前应用是否在背景运行
     * @return true 表示背景运行。
     */
    public static boolean isBackgroundRunning(){
        Application app = Foundation.shareInstance().currentApplication();
        if (app == null){
            return false;
        }

        android.app.ActivityManager activityManager = (android.app.ActivityManager) app.getSystemService(Context.ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
        for (android.app.ActivityManager.RunningAppProcessInfo process:processes){
            if (process.processName.equals(app.getPackageName())){
                if (process.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND ||
                        process.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE){
                    return true;
                }
                else{
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * 处理消息
     *
     * @param code 当前消息的 code
     * @param data 当前消息的 data
     * @return     如果 return false 则不继续执行 launch
     */
    protected void onError(int code, Object data){

    }

    /**
     * 启动 Activity 之前, 生成 Intent 之后调用
     *
     * @param current       当前 Activity
     * @param key           目标 Activity 对应的 key，如果不是用key 方式打开的则该参数为 null
     * @param intent        Intent
     * @param requestCode   RequestCode
     * @return              如果 return false 则不会打开目标 Activity
     */
    protected boolean onPreStartActivity(Activity current, String key, Intent intent, int requestCode){
        return true;
    }

    /**
     * pop 系列方法实行弹出操作前触发此事件
     *
     * @param current   当前 Activity
     * @param target    目标 Activity，最终要显示的下层 Activity。
     * @param resultCode 请求码
     * @param intent    bundle
     * @return          如果 return false 则不会 pop 目标 Activity
     */
    protected boolean onPrePopActivity(Activity current,Activity target, int resultCode,Intent intent){
        return true;
    }

    /**
     * 根据 type 类型获取类名，子类可以重写这个方法以定制自已的 type 或 类名。
     * 当 type 为 native 时 action 参数为Activity类名。 {@link #startWithActionForResult(Activity, String, Intent, int)}
     * 当 type 为其它值时 action 可为空值。
     *
     * @param context   当前应用上下文，从这个上下文中获取获包名。
     * @param type      Activity 类型
     * @param action    action Activity 的类名。
     * @return 返回类全名，如果类型不正确或不能返回类名则返回 null
     */
    protected String getClassNameByType(Context context,String type,String action){
        String packageName = context.getApplicationContext().getPackageName();

        if (ACTIVITY_TYPE_NATIVE.equals(type)){
            if (action.startsWith(".")){
                return packageName + action + "Activity";
            }
            else{
                return action + "Activity";
            }
        }
        else if(ACTIVITY_TYPE_WEBAPP.equals(type)){
            return null;
        }
        else if(ACTIVITY_TYPE_WEBVIEW.equals(action)){
            return null;
        }
        else{
            return null;
        }
    }

    /**
     * 根据 config 配置设置 intent，子类可以重写这个方法定制自已的处理罗辑
     * @param config  配置数据
     * @param intent  将要启动的 intent
     */
    protected void setIntentByConfig(JSONObject config, Intent intent){
        String type = config.optString("type","WebApp");

        if (ACTIVITY_TYPE_NATIVE.equals(type)){
            //处理 native 类型的一些事情
        }
        else if(ACTIVITY_TYPE_WEBAPP.equals(type)){
            //处理 WebApp 类型的一些事情
        }
        else if(ACTIVITY_TYPE_WEBVIEW.equals(type)){
            //处理 WebView 类型的一些事情
        }
    }

    /**
     * 打开一个 Activity
     * @param current   当前 Activity
     * @param intent    Intent 实例
     * @param requestCode  请求码
     * @return 打开成功返回 true
     */
    private boolean startActivityForResult(Activity current, Intent intent,int requestCode) {
        try {
            current.startActivityForResult(intent, requestCode);
        }
        catch (ActivityNotFoundException e){
            //Activty 没有找到，报此异常。
            e.printStackTrace();

            onError(ERROR_ACTIVITY_NOT_FOUND,null);
            return false;
        }

        return true;
    }

    /**
     * 从当前的导航栈中弹出视图
     * @param lastPopTarget 最后一个要弹出的 Activity
     * @param resultCode    结果代码
     * @param intent        Intent 对象
     */
    private boolean popActivity(Activity lastPopTarget,int resultCode,Intent intent){
        Activity topActivity = getTopActivity();
        if (topActivity != null){
            topActivity.setResult(resultCode, intent);
        }
        else{
            onError(ERROR_ACTIVITY_STACK_IS_EMPTY, null);
            return false;
        }

        int index = mActivities.indexOf(lastPopTarget);
        Activity willShowActivity = null;
        if (index > 0){
            willShowActivity = mActivities.get(index - 1);
        }

        if (!onPrePopActivity(topActivity,willShowActivity,resultCode,intent)){
            return false;
        }

        if (!topActivity.equals(lastPopTarget)){
            this.mLastPopTarget = lastPopTarget;
        }
        else{
            this.mLastPopTarget = null;
        }

        topActivity.finish();

        return true;
    }

    private Intent makeIntentByKey(Context context,String key,Intent intent){
        JSONObject config = getConfigByKey(key);

        if (config == null){
            onError(ERROR_CONFIG_KEY_NOT_FOUND,null);
            Log.e("ActivityLauncher", String.format("key '%s' not found", key));
            return null;
        }

        Intent newIntent = intent != null ? new Intent(intent) : new Intent();
        String type = config.optString("type","WebApp");
        String action = config.optString("action",null);
        String title = config.optString("title","");
        String url = config.optString("url","");
        String packageName = context.getApplicationContext().getPackageName();
        String className = getClassNameByType(context,type,action);

        if (className == null){
            return newIntent;
        }

        newIntent.putExtra(INTENT_PARAM_KEY_ACTION,action);
        newIntent.putExtra(INTENT_PARAM_KEY_TITLE,title);
        newIntent.putExtra(INTENT_PARAM_KEY_ACTIVITYKEY,key);
        newIntent.putExtra(INTENT_PARAM_KEY_URL,url);

        newIntent.setClassName(packageName,className);

        setIntentByConfig(config,newIntent);

        return newIntent;
    }

    /**
     * 将 action 指定的 Activity 的类名设备到 Intent 中。
     * @param context  Activity 上下文
     * @param action action Activity 的类名。{@link #startWithActionForResult(Activity, String, Intent, int)}
     * @param intent Intent 对象
     */
    private void setPackgeAndClassByAction(Context context,String action,Intent intent){
        if (null == action || "".equals(action) || null == intent){
            return;
        }

        String packageName = context.getApplicationContext().getPackageName();
        String className = getClassNameByType(context,ACTIVITY_TYPE_NATIVE,action);

        intent.setClassName(packageName,className);
    }

    /**
     * 根据 action 从当前栈中找到对应 activity，如果没有找到就返回 null。
     * @param action  action Activity的类名
     * @return Activity 实例，如果未找到则返回 null。
     *
     * @see #getClassNameByType(Context context, String type, String action)
     */
    private Activity getActivityByAction(String action){
        Activity topActivity = getTopActivity();
        if (topActivity == null){
            return null;
        }

        String className = getClassNameByType(topActivity,ACTIVITY_TYPE_NATIVE,action);

        for (Activity activity : mActivities){
            if(activity.getClass().getName().equals(className)){
                return activity;
            }
        }

        return null;
    }

    /**
     * 根据 Key 从当前栈中找到对应 activity，如果没有找到就返回 null。
     * @param key  Activity 的 Key
     * @return Activity 实例，如果未找到则返回 null。
     *
     * @see #getClassNameByType(Context context, String type, String action)
     */
    private Activity getActivityByKey(String key){
        JSONObject config = getConfigByKey(key);

        if (config == null || key == null){
            onError(ERROR_CONFIG_KEY_NOT_FOUND,null);
            return null;
        }

        Activity topActivity = getTopActivity();
        if (topActivity == null){
            return null;
        }

        String className = getClassNameByType(topActivity,ACTIVITY_TYPE_NATIVE,config.optString("action"));

        for (Activity activity : mActivities){
            if(activity.getClass().getName().equals(className) &&
                    activity.getIntent() != null &&
                    key.equals(activity.getIntent().getStringExtra(INTENT_PARAM_KEY_ACTIVITYKEY))){
                return activity;
            }
        }

        return null;
    }

    private JSONObject getConfigByKey(String key){
        JSONObject config = Foundation.shareInstance().getActivityConfig();

        if (config == null){
            return null;
        }

        JSONObject data = config.optJSONObject("config");

        if (data == null){
            return null;
        }

        return data.optJSONObject(key);
    }

    //------------------- Activity Lifecycle --------------
    /**
     * Activity 生命周期处理，该方法在 BaseToolbarActivity 类中调用，如果你的 Activity 不是继承自 BaseToolbarActivity，
     * 则需要在Activity 周期事件中调用该方法。
     * @param activity Activity 实例
     * @param savedInstanceState bundle 对象。
     */
    public void activityOnCreate(Activity activity, Bundle savedInstanceState) {
        //Activity 创建了，将它添加到 activities 列表中。
        mActivities.add(activity);
    }

    /**
     * Activity 生命周期处理
     * @param activity Activity 实例
     * @see #activityOnCreate(Activity, Bundle)
     */
    public void activityOnStart(Activity activity) {

    }

    /**
     * Activity 生命周期处理
     * @param activity Activity 实例
     * @see #activityOnCreate(Activity, Bundle)
     */
    public void activityOnResume(Activity activity) {
        //当栈顶 Activity Finish 并且还没消毁时，会调用下层 Activity 的 onResume 事件，
        //在这里判断下层的 Activity 是否是最后一个要弹出的 Activity 如果不是则继续 finish 操作，
        //如果是弹出此Activity 后置空 lastPopTarget。
        if (this.mLastPopTarget != null){
            if (activity.equals(this.mLastPopTarget)){
                this.mLastPopTarget = null;
            }
            activity.finish();
        }

        //将当前显示的 Activity 放在栈顶
        if (mActivities.remove(activity)){
            mActivities.add(activity);
        }
    }

    /**
     * Activity 生命周期处理
     * @param activity Activity 实例
     * @see #activityOnCreate(Activity, Bundle)
     */
    public void activityOnPause(Activity activity) {

    }

    /**
     * Activity 生命周期处理
     * @param activity Activity 实例
     * @see #activityOnCreate(Activity, Bundle)
     */
    public void activityOnStop(Activity activity) {

    }

    /**
     * Activity 生命周期处理
     * @param activity Activity 实例
     * @see #activityOnCreate(Activity, Bundle)
     */
    public void activityOnDestroy(Activity activity) {
        //Activity 被消毁（finish 或 旋转了屏幕），将它从activities 列表中移除
        mActivities.remove(activity);
    }

    /**
     * Activity 生命周期处理
     * @param activity Activity 实例
     * @param outState 用于保存状态变参数的 Bundle
     * @see #activityOnCreate(Activity, Bundle)
     */
    public void activityOnSaveInstanceState(Activity activity,Bundle outState){

    }
}
