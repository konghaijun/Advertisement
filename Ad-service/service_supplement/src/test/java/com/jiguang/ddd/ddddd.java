package com.jiguang.ddd;

import net.sourceforge.lame.lowlevel.LameEncoder;
import net.sourceforge.lame.mp3.Lame;
import net.sourceforge.lame.mp3.MPEGMode;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class MicrophoneRecorder {
  /*  private static final float SAMPLE_RATE = 16000;
    private static final int SAMPLE_SIZE = 8;
    private static final int CHANNELS = 1;
    private static final boolean SIGNED = true;
    private static final boolean BIG_ENDIAN = false;
    private static final int BUFFER_SIZE = 4096;
    private static final String OUTPUT_FILE = "output.mp3";
*/

/*
    private static final float SAMPLE_RATE = 44100;
    private static final int SAMPLE_SIZE = 16;
    private static final int CHANNELS = 1;
    private static final boolean SIGNED = true;
    private static final boolean BIG_ENDIAN = false;
    private static final int BUFFER_SIZE = 4096;
    private static final String OUTPUT_FILE = "output.mp3";*/

    private static final float SAMPLE_RATE = 44100;
    private static final int SAMPLE_SIZE = 16;
    private static final int CHANNELS = 1;
    private static final boolean SIGNED = true;
    private static final boolean BIG_ENDIAN = false;
    private static final int FRAME_SIZE = 2; // 每个采样点占用的字节数
    private static final int BUFFER_SIZE = 4096;
    private static final String OUTPUT_FILE = "output.mp3";





    private AudioFormat targetFormat;
    private TargetDataLine targetLine;
    private FileOutputStream outputStream;
    private byte[] buffer;

    public void start() {
        try {
            // 初始化录制配置
            targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, SAMPLE_RATE, SAMPLE_SIZE, CHANNELS, (SAMPLE_SIZE / 8) * CHANNELS, SAMPLE_RATE, BIG_ENDIAN);
            DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, targetFormat);
            if (!AudioSystem.isLineSupported(targetInfo)) {
                throw new RuntimeException("Microphone not supported");
            }


          //  line.isFormatSupported(format)
            // 获取麦克风权限
            Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
            Mixer mixer = AudioSystem.getMixer(mixerInfos[0]);
            targetLine = (TargetDataLine) mixer.getLine(targetInfo);
            targetLine.open(targetFormat);

            // 创建 MP3 编码器
            LameEncoder encoder = new LameEncoder(targetFormat, 128, MPEGMode.STEREO,Lame.QUALITY_HIGHEST,false);
          //  LameEncoder encoder = new LameEncoder(audioInputStream.getFormat(), 128, MPEGMode.STEREO, Lame.QUALITY_HIGHEST, false);

            // 创建输出文件流
            File outputFile = new File(OUTPUT_FILE);
            outputStream = new FileOutputStream(outputFile);

            // 开始录制
            targetLine.start();
            buffer = new byte[BUFFER_SIZE];

            System.out.println("Recording started");
            while (true) {
                int bytesRead = targetLine.read(buffer, 0, buffer.length);
                if (bytesRead == -1) {
                    break;
                }

                byte[] encodedBytes=new byte[encoder.getMP3BufferSize()];
               int a = encoder.encodeBuffer(buffer, 0, bytesRead,encodedBytes);
                outputStream.write(encodedBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 停止录制并关闭资源
            if (targetLine != null) {
                targetLine.stop();
                targetLine.close();
            }

            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        MicrophoneRecorder recorder = new MicrophoneRecorder();
        recorder.start();
    }
}
