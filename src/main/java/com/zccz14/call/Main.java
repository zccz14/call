package com.zccz14.call;

import javax.sound.sampled.AudioFormat;

/**
 * Created by zccz14 on 17-5-1.
 */
public class Main {
    public static final int defaultPort = 9527;
    public static final int defaultBufferSize = 1024;
    public static final AudioFormat defaultAudioFormat = new AudioFormat(8000, 16, 2, true, true);

    public static void main(String[] args) {
        Server server = new Server(defaultPort);
    }
}
