package com.example.smartwatchexamplenewide;

import com.example.smartwatchexamplenewide.audio.RecordAudioAbilitySlice;
import com.example.smartwatchexamplenewide.heartrate.HeartRateAbility;
import com.example.smartwatchexamplenewide.joke.JokeAbilitySlice;
import com.example.smartwatchexamplenewide.util.LogUtils;
import com.huawei.watch.kit.hiwear.p2p.*;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;

import java.nio.charset.StandardCharsets;

public class MainAbilitySlice extends P2PAbilitySlice {

    private final static String TAG = "MainSlice";

    //Views
    private Text tvLastReceivedMessage;
    private Image ivLastReceivedImage;

    private TaskDispatcher uiDispatcher;

    private final Receiver receiver = new Receiver() {
        @Override
        public void onReceiveMessage(HiWearMessage message) {
            final int type = message.getType();

            switch (type) {
                case HiWearMessage.MESSAGE_TYPE_DATA:
                    final String text = new String(message.getData());
                    LogUtils.d(TAG, "Received text: " + text);
                    uiDispatcher.syncDispatch(() ->{
                        tvLastReceivedMessage.setText(text);
                        tvLastReceivedMessage.startAutoScrolling();
                    });
                    break;
                case HiWearMessage.MESSAGE_TYPE_FILE:
                    LogUtils.d(TAG, "Received file.");
                    final PixelMap pixelMap = ImageSource.create(message.getFile(),
                            new ImageSource.SourceOptions())
                            .createPixelmap(new ImageSource.DecodingOptions());
                    uiDispatcher.syncDispatch(() -> {
                        ivLastReceivedImage.setPixelMap(pixelMap);
                    });
                    break;
            }
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        uiDispatcher = getUITaskDispatcher();
        tvLastReceivedMessage = (Text)findComponentById(ResourceTable.Id_text_received_message);
        ivLastReceivedImage = (Image) findComponentById(ResourceTable.Id_image_received_image);

        final Button button = (Button)findComponentById(ResourceTable.Id_button_send_message);
        button.setClickedListener(component -> {
            try {
                getP2PClient().ping(resultCode -> {
                    LogUtils.d(TAG, "ping result = " + resultCode);
                });
            } catch (Exception e) {
                LogUtils.d(TAG, e.getMessage());
            }
            sendMessageToMobile("Hello from SmartWatch!");
        });

        final Button recordButton = (Button)findComponentById(ResourceTable.Id_button_record_audio);
        recordButton.setClickedListener(component -> {
            present(new RecordAudioAbilitySlice(), new Intent());
        });

        final Button callRestAPIButton = (Button)findComponentById(ResourceTable.Id_button_get_joke);
        callRestAPIButton.setClickedListener(component -> {
            present(new JokeAbilitySlice(), new Intent());
        });

        final Button measureHeartRate = (Button) findComponentById(ResourceTable.Id_button_measure_heart_rate);
        measureHeartRate.setClickedListener(component -> {
            final Intent i = new Intent();
            final Operation operation = new Intent.OperationBuilder()
                    .withBundleName(getBundleName())
                    .withAbilityName(HeartRateAbility.class.getName())
                    .build();

            i.setOperation(operation);
            startAbility(i);
        });

        findComponentById(ResourceTable.Id_button_test_text_field).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(final Component component) {
                present(new TestTextFieldAbility(), new Intent());
            }
        });
    }

    private void sendMessageToMobile(String msg) {
        try {
            HiWearMessage.Builder builder = new HiWearMessage.Builder();
            builder.setPayload(msg.getBytes(StandardCharsets.UTF_8));
            HiWearMessage msgPayload = builder.build();
            SendCallback sendCallback = i -> {
                LogUtils.d(TAG, "Send result - " + i);
            };
            getP2PClient().send(msgPayload,sendCallback);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onActive() {
        super.onActive();
        getP2PClient().registerReceiver(receiver);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        getP2PClient().unregisterReceiver(receiver);
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    protected void onBackground() {
        super.onBackground();
    }
}
