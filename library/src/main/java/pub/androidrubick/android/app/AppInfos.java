package pub.androidrubick.android.app;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

import pub.androidrubick.base.collection.CollectionsCompat;
import pub.androidrubick.base.logging.ARLogger;
import pub.androidrubick.base.utils.MathCompat;
import pub.androidrubick.base.utils.Objects;

/**
 * 获取应用相关的信息工具类，如应用版本号，版本名称，meta信息
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/8/29 0029.
 *
 * @since 1.0
 */
public class AppInfos {

    private AppInfos() { /* no instance needed */ }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // 获取应用程序相关的信息

    /**
     * 返回应用的包名
     * @since 1.0
     */
    public static String packageName() {
        return ARContext.app().getPackageName();
    }

    /**
     * 返回当前程序版本号
     * @since 1.0
     */
    public static int versionCode(int defVersion) {
        int versionCode = defVersion;
        try {
            // ---get the package info---
            PackageManager pm = ARContext.app().getPackageManager();
            // 这里的context.getPackageName()可以换成你要查看的程序的包名
            PackageInfo pi = pm.getPackageInfo(ARContext.app().getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            ARLogger.error("versionCode", e);
        }
        return versionCode;
    }

    /**
     * 返回当前程序版本名
     * @since 1.0
     */
    public static String versionName(String defVersion) {
        String versionName = defVersion;
        try {
            // ---get the package info---
            PackageManager pm = ARContext.app().getPackageManager();
            // 这里的context.getPackageName()可以换成你要查看的程序的包名
            PackageInfo pi = pm.getPackageInfo(ARContext.app().getPackageName(), 0);
            versionName = pi.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return defVersion;
            }
        } catch (Exception e) {
            ARLogger.error("versionName", e);
        }
        return versionName;
    }

    /**
     * Additional meta-data associated with this app
     * @since 1.0
     */
    public static Bundle allMetaData() {
        try {
            Context context = ARContext.app();
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            // 这里的context.getPackageName()可以换成你要查看的程序的包名
            ApplicationInfo pi = pm.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return pi.metaData;
        } catch (Exception e) {
            ARLogger.error("allMetaData", e);
            return null;
        }
    }

    /**
     * Additional meta-data of key associated with this app
     * @since 1.0
     */
    @SuppressWarnings("unchecked")
    public static <T>T getMetaData(String key) {
        Bundle bundle = allMetaData();
        if (null != bundle) {
            return (T) bundle.get(key);
        }
        return null;
    }
    // end
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // 组件相关
    /**
     * 验证组件是否是本应用的Activity
     * @since 1.0
     */
    public static boolean isMyActivity(ComponentName componentName) {
        try {
            Context context = ARContext.app();
            PackageManager pm = context.getPackageManager();
            ActivityInfo activityInfo = pm.getActivityInfo(componentName, 0);
            return Objects.equals(activityInfo.packageName, context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 验证组件是否是本应用的Service
     * @since 1.0
     */
    public static boolean isMyService(ComponentName componentName) {
        try {
            Context context = ARContext.app();
            PackageManager pm = context.getPackageManager();
            ServiceInfo serviceInfo = pm.getServiceInfo(componentName, 0);
            return Objects.equals(serviceInfo.packageName, context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // end
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // 进程相关
    /**
     * 获取当前进程的进程名称
     * @since 1.0
     */
    public static String processName() {
        Context context = ARContext.app();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();
        if (CollectionsCompat.isEmpty(runningAppProcessInfos)) {
            return null;
        }
        final int myPid = android.os.Process.myPid();
        String processName = null;
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcessInfos) {
            if (info.pid == myPid) {
                processName = info.processName;
                break;
            }
        }
        return processName;
    }

    /**
     * 当前的进程名是否是<code>processName</code>
     * @since 1.0
     */
    public static boolean isProcess(String processName) {
        return Objects.equals(processName, processName());
    }

    /**
     * 当前的进程名是否是主应用进程（一般地，主应用进程名同包名）
     *
     * <p/>
     *
     * NOTE: “主应用”的进程名称为`应用包名`。
     *
     * @since 1.0
     */
    public static boolean isMainProcess() {
        return isProcess(packageName());
    }

    /**
     * 应用是否存在指定的进程
     * @since 1.0
     */
    public static boolean hasProcess(long pid) {
        Context context = ARContext.app();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();
        if (CollectionsCompat.isEmpty(runningAppProcessInfos)) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcessInfos) {
            if (info.pid == pid) {
                return true;
            }
        }
        return false;
    }
    /**
     * 应用是否存在指定的进程
     * @since 1.0
     */
    public static boolean hasProcess(String processName) {
        Context context = ARContext.app();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();
        if (CollectionsCompat.isEmpty(runningAppProcessInfos)) {
            return false;
        }
        final int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcessInfos) {
            if (info.pid == myPid) {
                if (Objects.equals(processName, info.processName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 主应用是否打开
     *
     * <p/>
     *
     * NOTE: “主应用”的进程名称为`应用包名`。
     * @since 1.0
     */
    public static boolean isMainProcessAlive() {
        return hasProcess(packageName());
    }
    // end
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // memory
    /**
     * 获取应用能够分配的最大内存
     * @since 1.0
     */
    public static long getMemoryClass() {
        long ret = -1;
        try {
            Context context = ARContext.app();
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ret = am.getMemoryClass(); // 单位为M
            ret *= (1024L * 1024L);
        } catch (Exception e) { }
        return MathCompat.ifLessThan(ret, 1L, Runtime.getRuntime().maxMemory());
    }

    /**
     * 获取一定比率（<code>ratio</code>）（0-1）的内存大小（总内存（{@link #getMemoryClass()}）* <code>ratio</code>）
     * @since 1.0
     */
    public static long memoryByRatio(float ratio) {
        ratio = MathCompat.limitByRange(ratio, 0f, 1f);
        return (long) (getMemoryClass() * ratio);
    }

    @SuppressLint("NewApi")
    public static void printMemeory() {
        ARLogger.debug("memory getMemoryClass = " + getMemoryClass());
        // 下面是系统的
        try {
            Context context = ARContext.app();
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(memoryInfo);
            ARLogger.debug("memory availMem = " + memoryInfo.availMem);
            ARLogger.debug("memory threshold = " + memoryInfo.threshold);
            ARLogger.debug("memory totalMem = " + memoryInfo.totalMem);
        } catch (Exception e) { }
        ARLogger.debug("memory runtime maxMemory = " + Runtime.getRuntime().maxMemory());
        ARLogger.debug("memory runtime totalMemory = " + Runtime.getRuntime().totalMemory());
        ARLogger.debug("memory runtime freeMemory = " + Runtime.getRuntime().freeMemory());
    }
    // end
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



    /**
     * 是否是处于屏幕顶端
     * @since 1.0
     */
    public static boolean isTopApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return isTopAppAfterAPI21();
        } else {
            return isTopAppBeforeAPI21();
        }
    }

    private static boolean isTopAppAfterAPI21() {
//        return XActivityController.isForeground() || isTopAppBeforeAPI21();
        return isTopAppBeforeAPI21();
    }

    private static boolean isTopAppBeforeAPI21() {
        try {
            ActivityManager am = (ActivityManager) ARContext.app()
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            if (null != tasks && !tasks.isEmpty()) {
                ActivityManager.RunningTaskInfo taskInfo = tasks.get(0);
                return isMyActivity(taskInfo.topActivity) || isMyActivity(taskInfo.baseActivity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}