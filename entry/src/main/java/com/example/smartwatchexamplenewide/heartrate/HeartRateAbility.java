package com.example.smartwatchexamplenewide.heartrate;

import com.example.smartwatchexamplenewide.ResourceTable;
import com.example.smartwatchexamplenewide.util.LogUtils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;
import ohos.sensor.agent.CategoryBodyAgent;
import ohos.sensor.bean.CategoryBody;
import ohos.sensor.data.CategoryBodyData;
import ohos.sensor.listener.ICategoryBodyDataCallback;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class HeartRateAbility extends Ability {

    private final static long INTERVAL = TimeUnit.SECONDS.toNanos(1);
    private final static String TAG = HeartRateAbility.class.getSimpleName();

    private final CategoryBodyAgent categoryBodyAgent = new CategoryBodyAgent();
    private CategoryBody categoryBody;
    private ICategoryBodyDataCallback heartRateCallBack;

    private Text heartRateData;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);

        setSwipeToDismiss(true);
        setUIContent(ResourceTable.Layout_ability_heart_rate);

        heartRateData = (Text)findComponentById(ResourceTable.Id_text_heart_rate_data);

        categoryBody = categoryBodyAgent.getSingleSensor(CategoryBody.SENSOR_TYPE_HEART_RATE);

        // Create a sensor callback object.
        heartRateCallBack = new ICategoryBodyDataCallback() {
            @Override
            public void onSensorDataModified(CategoryBodyData categoryBodyData) {
                final String valueString = Arrays.toString(categoryBodyData.getValues());
                LogUtils.d(TAG, "On new sensor data: " + valueString);
                getUITaskDispatcher().asyncDispatch(() -> heartRateData.setText("Heart Rate: " + valueString));
            }

            @Override
            public void onAccuracyDataModified(CategoryBody categoryBody, int i) {
                LogUtils.d(TAG, "Heart Rate Accuracy Data modified, value: " + i);
            }

            @Override
            public void onCommandCompleted(CategoryBody categoryBody) {
                LogUtils.d(TAG, "Heart Rate onCommand ended, category name: " + categoryBody.getName());
            }
        };

        categoryBodyAgent.setSensorDataCallback(heartRateCallBack, categoryBody, INTERVAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        categoryBodyAgent.releaseSensorDataCallback(heartRateCallBack, categoryBody);
    }
}
