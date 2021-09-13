package com.minkiapps.hos.test.audio;

import com.minkiapps.hos.test.P2PAbilitySlice;
import com.minkiapps.hos.test.util.LogUtils;
import com.minkiapps.hostest.ResourceTable;
import com.huawei.watch.kit.hiwear.p2p.HiWearMessage;
import com.huawei.watch.kit.hiwear.p2p.SendCallback;
import com.huawei.watch.kit.hiwear.utils.FileUtil;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;

import java.io.*;

public class RecordAudioAbilitySlice extends P2PAbilitySlice implements AudioRecorder.RecorderListener {

    private static final String TAG = "RecordAudioSlice";

    private Button recordAudioButton;

    private AudioRecorder audioRecorder = null;
    private MyAudioPlayer audioPlayer = new MyAudioPlayer();

    private void sendAudioSampleToPhone(final File file) {
        final HiWearMessage.Builder builder = new HiWearMessage.Builder();
        builder.setPayload(file);
        HiWearMessage sendMessage = builder.build();

        //Create callback methods
        SendCallback sendCallback = new SendCallback() {
            @Override
            public void onSendResult(int resultCode) {
                LogUtils.d(TAG, "Send audio sample result: " + resultCode);
            }

            @Override
            public void onSendProgress(long progress) {
                LogUtils.d(TAG, "Send audio sample progress: " + progress);
            }
        };

        getP2PClient().send(sendMessage, sendCallback);
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_record_audio);

        recordAudioButton = (Button) findComponentById(ResourceTable.Id_button_record_audio);
        Button goBackButton = (Button) findComponentById(ResourceTable.Id_button_back);

        recordAudioButton.setClickedListener(component -> {
            if(audioRecorder == null) {
                recordAudioButton.setText("Is Recording...");
                audioRecorder = new AudioRecorder(this, this);
                audioRecorder.start();
            } else {
                audioRecorder.finishRecording();
                audioRecorder = null;
                recordAudioButton.setText("Record Audio");
            }
        });

        goBackButton.setClickedListener(component -> {
            terminate();
        });
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if(audioRecorder != null) {
            audioRecorder.shutDown();
            audioRecorder = null;
            recordAudioButton.setText("Record Audio");
        }

        audioPlayer.stopAndResetPlayer();
    }

    @Override
    public void onRecordingFinished(File file) {
        sendAudioSampleToPhone(file);
        audioPlayer.stopAndResetPlayer();
        audioPlayer.startPlayer();
        audioPlayer.writeBuffer(FileUtil.getFileContent(file));
    }

    @Override
    public void onRecordingError(Exception e) {

    }
}
