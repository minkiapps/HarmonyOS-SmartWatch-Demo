package com.example.smartwatchexamplenewide.audio;

import com.example.smartwatchexamplenewide.util.LogUtils;
import ohos.app.Context;
import ohos.media.audio.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AudioRecorder extends Thread {

    public interface RecorderListener {
        void onRecordingFinished(final File file);

        void onRecordingError(final Exception e);
    }

    private static class RecorderState {
        static final int STATE_IDLE = 0;
        static final int STATE_RECORDING = 1;
        static final int STATE_FINISHED_RECORDING = 2;
        static final int STATE_RELEASED = 3;
    }

    private static final String TAG = "AudioRecorder";
    private static final int SAMPLING_RATE_IN_HZ = 44100;
    private static final int CHANNEL_CONFIG =
            AudioStreamInfo.getChannelCount(AudioStreamInfo.ChannelMask.CHANNEL_IN_STEREO);

    private static final int BUFFER_SIZE = AudioCapturer.getMinBufferSize(SAMPLING_RATE_IN_HZ,
            CHANNEL_CONFIG,
            AudioStreamInfo.EncodingFormat.ENCODING_PCM_16BIT.getValue()) * 2;

    private final Context context;
    private final RecorderListener recorderListener;

    private final AudioCapturer capture;

    private AtomicInteger state = new AtomicInteger(RecorderState.STATE_IDLE);

    public AudioRecorder(Context context, RecorderListener recorderListener) {
        this.context = context;
        this.recorderListener = recorderListener;

        final AudioStreamInfo audioStreamInfo = new AudioStreamInfo.Builder().encodingFormat(
                AudioStreamInfo.EncodingFormat.ENCODING_PCM_16BIT)
                .channelMask(AudioStreamInfo.ChannelMask.CHANNEL_IN_STEREO)
                .sampleRate(SAMPLING_RATE_IN_HZ)
                .build();
        final AudioCapturerInfo info = new AudioCapturerInfo.Builder().audioStreamInfo(audioStreamInfo).build();
        capture = new AudioCapturer(info);

        AudioCapturerCallback cb = new AudioCapturerCallback() {
            @Override
            public void onCapturerConfigChanged(List<AudioCapturerConfig> configs) {
                configs.forEach(config -> {
                    LogUtils.d(TAG, "On new AudioCapturer Config: " + config);
                });
            }
        };
        AudioManager audioManager = new AudioManager();
        audioManager.registerAudioCapturerCallback(cb);
    }

    @Override
    public synchronized void start() {
        super.start();
        state.set(RecorderState.STATE_RECORDING);
    }

    public synchronized void finishRecording() {
        state.set(RecorderState.STATE_FINISHED_RECORDING);
    }

    public synchronized void shutDown() {
        state.set(RecorderState.STATE_RELEASED);
    }

    @Override
    public void run() {
        try {
            final File file = new File(context.getCacheDir(), "Recording.pcm");
            file.deleteOnExit();
            file.createNewFile();
            byte[] buffer;
            final FileOutputStream outStream = new FileOutputStream(file);

            capture.start();
            while (state.get() == RecorderState.STATE_RECORDING) {
                buffer = new byte[BUFFER_SIZE];
                int result = capture.read(buffer, 0, BUFFER_SIZE);
                LogUtils.d(TAG, "Reading bytes: " + result);
                outStream.write(buffer);
            }
            outStream.close();
            capture.stop();

            if(state.get() == RecorderState.STATE_FINISHED_RECORDING) {
                recorderListener.onRecordingFinished(file);
            }
        } catch (final Exception e) {
            LogUtils.e(TAG, "Recording failed: " + e.getMessage());
            recorderListener.onRecordingError(e);
        }
    }
}
