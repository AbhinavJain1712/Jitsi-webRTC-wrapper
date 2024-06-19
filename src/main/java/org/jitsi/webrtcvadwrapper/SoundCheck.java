package org.jitsi.webrtcvadwrapper;

import java.util.List;
import java.util.function.Function;
import java.lang.reflect.Array;

public class SoundCheck {

    public static <T> double checkSoundSegments(T audioSamples, int frameSize, List<Double> start, List<Double> end, Function<T, Boolean> isSpeechFunction, Class<?> componentType) {
        SoundState soundState = new SoundState();

        double averageDuration = 0.0;
        long frameCount = 0;

        int length = Array.getLength(audioSamples);

        for (int i = 0; i + frameSize <= length; i += frameSize) {
            T frame = (T)Array.newInstance(componentType, frameSize);

            System.arraycopy(audioSamples, i, frame, 0, frameSize);

            boolean isSpeechSegment = false;

            long startTime = System.nanoTime();
            isSpeechSegment = isSpeechFunction.apply(frame);
            long endTime = System.nanoTime();

            averageDuration += (endTime - startTime);
            frameCount++;

            soundState.update(isSpeechSegment, start, end);
        }

        //   System.out.println("Average Duration: "+averageDuration +"ns");
           soundState.finalizeLastSegment(start, end);
        return averageDuration / frameCount;
    }

    // Wrapper methods to call the generic method with specific types
    public static double checkSoundSegments(byte[] audioBytes, int frameSize, List<Double> start, List<Double> end, Function<byte[], Boolean> isSpeechFunction) {
        return checkSoundSegments(audioBytes, frameSize, start, end, isSpeechFunction, byte.class);
    }

    public static double checkSoundSegments(int[] audioSamples, int frameSize, List<Double> start, List<Double> end, Function<int[], Boolean> isSpeechFunction) {
        return checkSoundSegments(audioSamples, frameSize, start, end, isSpeechFunction, int.class);
    }
}
