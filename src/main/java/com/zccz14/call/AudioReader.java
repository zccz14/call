package com.zccz14.call;

import javax.sound.sampled.*;

/**
 * Created by zccz14 on 17-5-1.
 */
public class AudioReader {

    private TargetDataLine line;

    public AudioReader(AudioFormat format) {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format, line.getBufferSize());
            line.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AudioFormat format = new AudioFormat(8000, 16, 2, true, true);
        AudioReader reader = new AudioReader(format);
        AudioWriter writer = new AudioWriter(format);
        byte[] bytes = new byte[reader.line.getBufferSize()];
        reader.line.start();
        while (reader.line.isActive()) {
            reader.line.read(bytes, 0, bytes.length);
            System.out.println(bytes.length);
            writer.play(bytes);
        }
    }

    public byte[] read() {
        byte[] bytes = new byte[line.getBufferSize()];
        line.read(bytes, 0, bytes.length);
        return bytes;
    }

    public void start() {
        line.start();
    }

    public void stop() {
        line.stop();
    }

    public int getLineBufferSize() {
        return line.getBufferSize();
    }
}
