package com.minkiapps.hos.test.audio;

import ohos.media.audio.AudioCapturer;
import ohos.media.audio.AudioRenderer;
import ohos.media.audio.AudioRendererInfo;
import ohos.media.audio.AudioStreamInfo;

public class MyAudioPlayer {

    private static final int SAMPLING_RATE_IN_HZ = 44100;

    private static final int CHANNEL_CONFIG =
            AudioStreamInfo.getChannelCount(AudioStreamInfo.ChannelMask.CHANNEL_IN_STEREO);

    private static final int BUFFER_SIZE = AudioCapturer.getMinBufferSize(SAMPLING_RATE_IN_HZ,
            CHANNEL_CONFIG,
            AudioSettings.ENCODING_FORMAT.getValue());

    private AudioRenderer mAudioRenderer;

    public MyAudioPlayer() {
        AudioStreamInfo audioStreamInfo = new AudioStreamInfo.Builder().sampleRate(SAMPLING_RATE_IN_HZ)
                .audioStreamFlag(AudioStreamInfo.AudioStreamFlag.AUDIO_STREAM_FLAG_MAY_DUCK)
                .encodingFormat(AudioSettings.ENCODING_FORMAT)
                .channelMask(AudioStreamInfo.ChannelMask.CHANNEL_OUT_STEREO)
                .streamUsage(AudioStreamInfo.StreamUsage.STREAM_USAGE_MEDIA)
                .build();

        AudioRendererInfo audioRendererInfo = new AudioRendererInfo.Builder().audioStreamInfo(audioStreamInfo)
                .audioStreamOutputFlag(AudioRendererInfo.AudioStreamOutputFlag.AUDIO_STREAM_OUTPUT_FLAG_DIRECT_PCM)
                .bufferSizeInBytes(BUFFER_SIZE)
                .isOffload(false)
                .build();
        mAudioRenderer = new AudioRenderer(audioRendererInfo, AudioRenderer.PlayMode.MODE_STREAM);
        mAudioRenderer.setSampleRate(SAMPLING_RATE_IN_HZ);
        final float volume = 10.0f;
        mAudioRenderer.setVolume(volume);
    }

    public void writeBuffer(byte[] buffer) {
        mAudioRenderer.write(buffer, 0, buffer.length);
    }

    public boolean startPlayer(){
        return mAudioRenderer.start();
    }

    public void stopAndResetPlayer() {
        mAudioRenderer.stop();
        mAudioRenderer.flush();
    }
}
