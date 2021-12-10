package com.minkiapps.hos.test;

import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.crash.AGConnectCrash;
import com.huawei.agconnect.crash.BuildConfig;
import com.minkiapps.hos.test.net.ApiService;
import com.minkiapps.hos.test.util.LogUtils;
import ohos.aafwk.ability.AbilityPackage;
import ohos.event.notification.NotificationHelper;
import ohos.event.notification.NotificationSlot;
import ohos.rpc.RemoteException;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

import java.util.concurrent.TimeUnit;

public class MyApplication extends AbilityPackage {

    public static final String TAG = MyApplication.class.getSimpleName();
    private static ApiService apiService = null;

    public static final String ONGOING_NOTIFICATION_LABEL = "minkiapps_card";
    public static final String ONGOING_CARD_SLOT_ID = "666";

    @Override
    public void onInitialize() {
        super.onInitialize();

        AGConnectInstance.initialize(this);

        final boolean enableCrash = true; //add logic to enable or not, e.g: !BuildConfig.DEBUG
        AGConnectCrash.getInstance().enableCrashCollection(enableCrash);

        LogUtils.d(TAG, "Init SmartWatch Example App");
        final NotificationSlot slot = new NotificationSlot(ONGOING_CARD_SLOT_ID, ONGOING_NOTIFICATION_LABEL, NotificationSlot.LEVEL_MIN);
        slot.setDescription("minkiapps_card");
        try {
            NotificationHelper.addNotificationSlot(slot);
        } catch (RemoteException ex) {
            LogUtils.e(TAG, "Add ongoing card slot exception");
        }
    }

    public synchronized static ApiService getApiService() {
        if(apiService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60L, TimeUnit.SECONDS)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.chucknorris.io/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create()).client(client)
                    .build();
            apiService = retrofit.create(ApiService.class);
        }

        return apiService;
    }
}
