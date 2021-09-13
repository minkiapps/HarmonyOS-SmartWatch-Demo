package com.minkiapps.hos.test.util;

import com.minkiapps.hostest.BuildConfig;
import ohos.app.Context;
import ohos.bundle.BundleInfo;
import ohos.rpc.RemoteException;
import ohos.system.DeviceInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;

public class SystemInfo {

    private static final String TAG = SystemInfo.class.getSimpleName();

    private final String packageName = BuildConfig.PACKAGE_NAME;
    private String versionName = "UNKNOWN";
    private int versionCode = -1;
    private final String modelName = DeviceInfo.getModel();
    private final String osName = "harmonyOS";
    private String osVersion = "UNKNOWN";
    private int apiVersion = -1;
    private final String deviceLocale = Locale.getDefault().toString();

    public SystemInfo(final Context context) {
        try {
            final BundleInfo bundleInfo = context.getBundleManager().getBundleInfo(context.getBundleName(), 0);
            versionName = bundleInfo.getVersionName();
            versionCode = bundleInfo.getVersionCode();
        } catch (RemoteException e) {
            LogUtils.e(TAG, "Failed to retrieve bundleInfo " + e.getMessage(), e);
        }

        initOSParams();
    }

    private void initOSParams() {
        try {
            Process p = Runtime.getRuntime().exec("getprop");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while (reader.read() != -1) {
                final String line = reader.readLine();
                if(line.contains("build.os.apiversion")) {
                    apiVersion = Integer.parseInt(parseStringBetweenSquareBrackets(line));
                } else if(line.contains("build.os.version")) {
                    osVersion = parseStringBetweenSquareBrackets(line);
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "Failed to read system properties" + e.getMessage(), e);
        }
    }

    private String parseStringBetweenSquareBrackets(final String line) {
        final int r = line.lastIndexOf("]");
        final int l = line.lastIndexOf("[");
        if(l != -1 && r != -1) {
            return line.substring(l + 1,r);
        } else {
            return "";
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getModelName() {
        return modelName;
    }

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    public String getDeviceLocale() {
        return deviceLocale;
    }

    @Override
    public String toString() {
        return "SystemInfo{" +
                "packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", modelName='" + modelName + '\'' +
                ", osName='" + osName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", apiVersion=" + apiVersion +
                ", deviceLocale='" + deviceLocale + '\'' +
                '}';
    }
}
