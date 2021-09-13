package com.minkiapps.hostest.util;

import ohos.app.Context;
import ohos.telephony.RadioInfoManager;
import ohos.telephony.SignalInformation;
import ohos.wifi.WifiDevice;

import java.util.List;

public class OfflineUtil {

    public static boolean checkIfHasInternetConnectivity(final Context context) {
        final WifiDevice wifiDevice = WifiDevice.getInstance(context);
        if(wifiDevice.isWifiActive() && wifiDevice.isConnected()) {
            LogUtils.d("OfflineUtil", "WIFI is connected.");
        }

        final RadioInfoManager radioInfoManager = RadioInfoManager.getInstance(context);

        final List<SignalInformation> signalInfoList = radioInfoManager.getSignalInfoList(radioInfoManager.getPrimarySlotId());
        for (final SignalInformation signalInfo : signalInfoList) {
            LogUtils.d("OfflineUtil", "Found NetworkType: " + signalInfo.getNetworkType());
        }

        return true;
    }
}
