package com.jiguang.ddd;

import javax.sound.sampled.*;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * @Auther: 23091
 * @Date: 2023/6/13 20:31
 * @Description:
 */
public class ss {
    public static void main(String[] args) {



        final float SAMPLE_RATE = 44100;
         final int SAMPLE_SIZE = 16;
    final int CHANNELS = 1;
   final boolean SIGNED = true;
        final boolean BIG_ENDIAN = false;
       final int FRAME_SIZE = 2; // 每个采样点占用的字节数
         final int BUFFER_SIZE = 4096;
      final String OUTPUT_FILE = "output.mp3";





      AudioFormat targetFormat;
         TargetDataLine targetLine;
      FileOutputStream outputStream;
       byte[] buffer;
        targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, SAMPLE_RATE, SAMPLE_SIZE, CHANNELS, (SAMPLE_SIZE / 8) * CHANNELS, SAMPLE_RATE, BIG_ENDIAN);


        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : mixerInfos) {
            System.out.println("Mixer: " + mixerInfo.getName());
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] targetLineInfos = mixer.getTargetLineInfo();



            // 遍历所有目标线路
            for (Line.Info targetLineInfo : targetLineInfos) {
                System.out.println("\t" + targetLineInfo);
                try {
                    Line line = mixer.getLine(targetLineInfo);
                    line.isControlSupported(targetFormat);


                    // 只检查 TargetDataLine 和 SourceDataLine
                    if (line instanceof TargetDataLine || line instanceof SourceDataLine) {
                        if (!line.isOpen()) {
                            line.open();
                        }
                        AudioFormat formats = ((DataLine) line).getFormat();
                        System.out.println("\tFormats: " + Arrays.toString(new AudioFormat[]{formats}));
                        line.close();
                    }
                } catch (Exception e) {
                    System.err.println("Unable to get line: " + e.getCause());
                    e.printStackTrace();
                }
            }
        }

    }
}
