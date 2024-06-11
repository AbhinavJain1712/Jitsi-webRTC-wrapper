package org.jitsi.webrtcvadwrapper;

import java.util.List;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SoundCheck {

    public static void checkSoundSegments(byte[] audioBytes, int frameSize, List<Double> start, List<Double> end, Function<byte[], Boolean> isSpeechFunction) {
        SoundState soundState = new SoundState();

        double averageDuration = 0.0;
        long ct = 0;

// Warm-up period
        int warmUpFrames = 100;
        for (int i = 0; i < warmUpFrames; i++) {
            byte[] frame = new byte[frameSize];
            System.arraycopy(audioBytes, i * frameSize, frame, 0, frameSize);
            isSpeechFunction.apply(frame);  // Just call the function without measuring time
        }

        for (int i = warmUpFrames * frameSize; i + frameSize <= audioBytes.length; i += frameSize) {
            byte[] frame = new byte[frameSize];
            System.arraycopy(audioBytes, i, frame, 0, frameSize);

            boolean isSpeechSegment = false;

            long frameDuration = 0;
            if (ct < 1) {
                for (int j = 0; j < 10000; j++) {
                    long startTime = System.nanoTime();
                    isSpeechSegment = isSpeechFunction.apply(frame);
                    long endTime = System.nanoTime();
                    frameDuration += (endTime - startTime);
                }
                frameDuration /= 10000;
            }

            averageDuration += frameDuration;
            ct++;

            soundState.update(isSpeechSegment, start, end);
        }

        System.out.println("Average Duration: "+averageDuration +"ns");
        soundState.finalizeLastSegment(start, end);

    }

    public static void checkSoundSegments(int[] audioSamples, int frameSize, List<Double> start, List<Double> end, Function<int[], Boolean> isSpeechFunction) {

        SoundState soundState = new SoundState();

        double averageDuration = 0.0;
        long ct = 0;

// Warm-up period
        int warmUpFrames = 100;
        for (int i = 0; i < warmUpFrames; i++) {
            int[] frame = new int[frameSize];
            System.arraycopy(audioSamples, i * frameSize, frame, 0, frameSize);
            isSpeechFunction.apply(frame);  // Just call the function without measuring time
        }

        for (int i = warmUpFrames * frameSize; i + frameSize <= audioSamples.length; i += frameSize) {
            int[] frame = new int[frameSize];
            System.arraycopy(audioSamples, i, frame, 0, frameSize);

            boolean isSpeechSegment = false;

            long frameDuration = 0;
            if (ct < 1) {
                for (int j = 0; j < 10000; j++) {
                    long startTime = System.nanoTime();
                    isSpeechSegment = isSpeechFunction.apply(frame);
                    long endTime = System.nanoTime();
                    frameDuration += (endTime - startTime);
                }
                frameDuration /= 10000;
            }

            averageDuration += frameDuration;
            ct++;

            soundState.update(isSpeechSegment, start, end);
        }

        System.out.println("Average Duration: "+averageDuration +"ns");
        soundState.finalizeLastSegment(start, end);
    }
}
