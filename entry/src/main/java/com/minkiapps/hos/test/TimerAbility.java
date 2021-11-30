package com.minkiapps.hos.test;

import com.minkiapps.hos.test.util.DateUtils;
import com.minkiapps.hos.test.util.LogUtils;
import com.minkiapps.hos.test.util.ResUtil;
import com.minkiapps.hostest.ResourceTable;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.event.intentagent.IntentAgent;
import ohos.event.intentagent.IntentAgentConstant;
import ohos.event.intentagent.IntentAgentHelper;
import ohos.event.intentagent.IntentAgentInfo;
import ohos.event.notification.NotificationHelper;
import ohos.event.notification.NotificationRequest;
import ohos.rpc.RemoteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.minkiapps.hos.test.MyApplication.ONGOING_CARD_SLOT_ID;

public class TimerAbility extends Ability {
    private static final String TAG = TimerAbility.class.getName();

    private static final long UPDATE_PERIOD = 1000L;

    private static final int NOTIFICATION_ID = 1001;
    private static final String ONGOING_TAG = "Ongoing_Overview";

    final Timer timer = new Timer();

    @Override
    public void onStart(Intent intent) {
        startTimer();
        super.onStart(intent);
    }

    private void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                createNotification(DateUtils.getCurrentDate("HH:mm:ss"));
            }
        }, 0, UPDATE_PERIOD);
    }

    public void createNotification(String text){
        NotificationRequest request = new NotificationRequest(this, NOTIFICATION_ID);
        request.setSlotId(ONGOING_CARD_SLOT_ID);
        request.setLittleIcon(ResUtil.getPixelMap(this, ResourceTable.Media_icon));
        NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent();
        content.setText(text);
        NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(content);
        request.setContent(notificationContent);
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(getBundleName())
                .withAbilityName(HiWearMainAbility.class.getName())
                .build();
        intent.setOperation(operation);
        List<Intent> intentList = new ArrayList<>();
        intentList.add(intent);
        IntentAgentInfo paramsInfo = new IntentAgentInfo(200,
                IntentAgentConstant.OperationType.START_ABILITY, IntentAgentConstant.Flags.UPDATE_PRESENT_FLAG, intentList, null);
        IntentAgent intentAgent = IntentAgentHelper.getIntentAgent(this, paramsInfo);
        request.setIntentAgent(intentAgent);
        try {
            NotificationHelper.publishNotification(ONGOING_TAG, request);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        keepBackgroundRunning(NOTIFICATION_ID, request);
    }

    private void removeNotification() {
        try { NotificationHelper.cancelNotification(ONGOING_TAG, NOTIFICATION_ID);
        } catch (RemoteException exception) {
            LogUtils.e(TAG, "A remote exception occurred when publish ongoing card notification.");
        }
    }

    @Override
    public void onBackground() {
        LogUtils.i(TAG, "TimerAbility::onBackground");
        super.onBackground();
    }

    @Override
    public void onStop() {
        LogUtils.i(TAG, "TimerAbility::onStop");
        super.onStop();
        timer.cancel();
        cancelBackgroundRunning();
        removeNotification();
    }
}