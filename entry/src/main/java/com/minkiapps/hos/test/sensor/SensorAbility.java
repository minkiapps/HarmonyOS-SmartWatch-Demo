package com.minkiapps.hos.test.sensor;

import com.minkiapps.hostest.ResourceTable;
import com.minkiapps.hos.test.util.LogUtils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;
import ohos.sensor.agent.CategoryBodyAgent;
import ohos.sensor.agent.CategoryMotionAgent;
import ohos.sensor.bean.CategoryBody;
import ohos.sensor.bean.CategoryMotion;
import ohos.sensor.data.CategoryBodyData;
import ohos.sensor.data.CategoryMotionData;
import ohos.sensor.listener.ICategoryBodyDataCallback;
import ohos.sensor.listener.ICategoryMotionDataCallback;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SensorAbility extends Ability {

    private final static long INTERVAL = TimeUnit.SECONDS.toNanos(1);
    private final static String TAG = SensorAbility.class.getSimpleName();

    private final CategoryBodyAgent categoryBodyAgent = new CategoryBodyAgent();
    private final CategoryMotionAgent categoryMotionAgent = new CategoryMotionAgent();
    private CategoryBody heartRateCategory;
    private CategoryMotion stepsCategory;

    private ICategoryBodyDataCallback heartRateCallBack;
    private ICategoryMotionDataCallback motionDataCallback;

    private Text heartRateData;
    private Text stepsData;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);

        setSwipeToDismiss(true);
        setUIContent(ResourceTable.Layout_ability_heart_rate);

        heartRateData = (Text)findComponentById(ResourceTable.Id_text_heart_rate_data);
        stepsData = (Text)findComponentById(ResourceTable.Id_text_steps_data);

        heartRateCategory = categoryBodyAgent.getSingleSensor(CategoryBody.SENSOR_TYPE_HEART_RATE);
        stepsCategory = categoryMotionAgent.getSingleSensor(CategoryMotion.SENSOR_TYPE_PEDOMETER);

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

        motionDataCallback = new ICategoryMotionDataCallback() {
            @Override
            public void onSensorDataModified(final CategoryMotionData categoryMotionData) {
                final float value = categoryMotionData.getValues()[0];
                LogUtils.d(TAG, "On new step data: " + value);
                getUITaskDispatcher().asyncDispatch(() -> stepsData.setText("Steps Data: " + value));
            }

            @Override
            public void onAccuracyDataModified(final CategoryMotion categoryMotion, final int i) {
                LogUtils.d(TAG, "Steps Data modified, value: " + i);
            }

            @Override
            public void onCommandCompleted(final CategoryMotion categoryMotion) {
                LogUtils.d(TAG, "Steps onCommand completed, category name: " + categoryMotion.getName());
            }
        };

        categoryBodyAgent.setSensorDataCallback(heartRateCallBack, heartRateCategory, INTERVAL);
        categoryMotionAgent.setSensorDataCallback(motionDataCallback, stepsCategory, INTERVAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        categoryBodyAgent.releaseSensorDataCallback(heartRateCallBack, heartRateCategory);
        categoryMotionAgent.releaseSensorDataCallback(motionDataCallback, stepsCategory);
    }
}
