package pub.androidrubick.android.device;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;

import pub.androidrubick.android.app.ARContext;
import pub.androidrubick.android.utils.AndroidUtils;
import pub.androidrubick.base.io.Files;
import pub.androidrubick.base.text.Strings;
import pub.androidrubick.base.utils.ArraysCompat;
import pub.androidrubick.base.utils.Objects;
import pub.androidrubick.base.utils.StandardSystemProperty;

/**
 * 获取设备信息的工具类
 *
 * <p/>
 *
 * Created by Yin Yong on 2015/8/29 0029.
 *
 * @since 1.0
 */
public class DeviceInfos {

    private DeviceInfos() { /* no instance needed */ }

    private static float sDensity = 0.0f;
    private static float sScaledDensity = 0.0f;
    private static int sDisplayWidth = 0;
    private static int sDisplayHeight = 0;

    // >>>>>>>>>>>>>>>>>>>
    private static void getDisplay() {
        if (sDisplayWidth <= 0 || sDisplayHeight <= 0 || sDensity <= 0.0f) {
            WindowManager wm = (WindowManager) ARContext.app().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            sDisplayWidth = dm.widthPixels;
            sDisplayHeight = dm.heightPixels;
            sDensity = dm.density;
            sScaledDensity = dm.scaledDensity;
        }
    }

    /**
     * 获取屏幕宽度(单位：px)
     * @since 1.0
     */
    public static int getScreenWidth() {
        getDisplay();
        return sDisplayWidth;
    }

    /**
     * 获取屏幕高度(单位：px)
     * @since 1.0
     */
    public static int getScreenHeight() {
        getDisplay();
        return sDisplayHeight;
    }

    /**
     * 获取屏幕密度
     * @since 1.0
     */
    public static float getDensity() {
        getDisplay();
        return sDensity;
    }

    /**
     * 用于字体大小的密度
     * @since 1.0
     */
    public static float getScaledDensity() {
        getDisplay();
        return sScaledDensity;
    }

    /**
     * 获取客户端的分辨率（例如480x800）
     * @since 1.0
     */
    public static String getDeviceResolution() {
        return getDeviceResolution("x");
    }

    /**
     * 获取客户端的分辨率
     * @param linkMark 连接符，{@link #getDeviceResolution()} 使用的是“x”
     * @since 1.0
     */
    public static String getDeviceResolution(String linkMark) {
        int width = getScreenWidth();
        int height = getScreenHeight();
        return width + linkMark + height;
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // 获取系统、机器相关的信息
    /**
     * 获取sdk版本号
     * @since 1.0
     */
    public static int androidSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean isSDKOver(int targetSDK) {
        return androidSDKVersion() >= targetSDK;
    }

    /**
     * 获取android 系统发布版本，如“2.3.3”，“4.0.3”
     * @since 1.0
     */
    public static String androidOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取本机的发布版本
     * @return model of the device, or 'unknown'
     * @since 1.0
     */
    public static String buildId() {
        return Build.ID;
    }

    /**
     * 获取本机机型，如“MI 4W”（小米4）
     * @return model of the device, or 'unknown'
     * @since 1.0
     */
    public static String model() {
        String model = Build.MODEL;
        if (Strings.isEmpty(model)) {
            return Build.UNKNOWN;
        }
        return model;
    }

    /**
     * 获取本机品牌，如Xiaomi
     *
     * @return brand of the device, or 'unknown'
     * @since 1.0
     */
    public static String brand() {
        String brand = Build.BRAND;
        if (Strings.isEmpty(brand)) {
            return Build.UNKNOWN;
        }
        return brand;
    }

    /**
     * 获取本机制造商，如Xiaomi
     *
     * @return brand of the device, or 'unknown'
     * @since 1.0
     */
    public static String manufacturer() {
        String manufacturer = Build.MANUFACTURER;
        if (Strings.isEmpty(manufacturer)) {
            return Build.UNKNOWN;
        }
        return manufacturer;
    }


    private static String sDeviceUuid;
    /**
     * 设备唯一识别号
     * @since 1.0
     */
    public static String uuid() {
        if (null == sDeviceUuid) {
            DeviceUuidFactory factory = new DeviceUuidFactory(ARContext.app());
            sDeviceUuid = factory.getDeviceUuid();
        }
        return sDeviceUuid;
    }

    /**
     * 获得设备识别认证码
     * <p/>
     * 需要权限：android.permission.READ_PHONE_STATE
     * @return the unique device ID,
     * for example, the IMEI for GSM and the MEID or ESN for CDMA phones.
     * Return null if device ID is not available.
     * @since 1.0
     */
    public static String getIMEI() {
        AndroidUtils.ensurePermission(android.Manifest.permission.READ_PHONE_STATE);
        TelephonyManager tm = (TelephonyManager) ARContext.app()
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return null;
        }
        return tm.getDeviceId();
    }

    /**
     * 获取<code>User-Agent</code>
     * @since 1.0
     */
    public static String userAgent() {
        String agent = StandardSystemProperty.HTTP_AGENT.value();
        if (Strings.isEmpty(agent)) {
            String osType = "Android";
            String androidVersion = androidOsVersion();
            String device = model();
            String id = buildId();
            agent = String.format("Mozilla/5.0 (Linux; U; %s %s; %s Build/%s)",
                    osType, androidVersion, device, id);
            // Mozilla/5.0 (Linux; U; Android 4.3; en-us; HTC One - 4.3 - API 18 -
            // 1080x1920 Build/JLS36G)
        }
        return agent;
    }

    /**
     * 判断手机是否有外部存储
     * @since 1.0
     */
    public static boolean hasExternalStorage() {
        return Objects.equals(Environment.MEDIA_MOUNTED, Environment.getExternalStorageState());
    }

    /**
     * 如果没有模拟存储和外部存储设备，返回null；
     * <br/>
     * 如果API < 21，返回{@link android.content.Context#getExternalCacheDir()}；
     * <br/>
     * 如果API >= 21，返回{@link android.content.Context#getExternalCacheDirs()}；
     * <br/>
     * @since 1.0
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static File[] getExternalCacheDirs() {
        // get ext cache dir
        if (isSDKOver(Build.VERSION_CODES.KITKAT)) {
            File[] extCacheDirs = ARContext.app().getExternalCacheDirs();
            if (!ArraysCompat.isEmpty(extCacheDirs)) {
                return extCacheDirs;
            }
        }
        File extCacheDir = ARContext.app().getExternalCacheDir();
        if (Objects.isNull(extCacheDir) || Files.exists(extCacheDir)) {
            return null;
        }
        return ArraysCompat.by(extCacheDir);
    }

}