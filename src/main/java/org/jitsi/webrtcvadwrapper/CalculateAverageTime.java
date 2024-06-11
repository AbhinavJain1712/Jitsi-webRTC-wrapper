package org.jitsi.webrtcvadwrapper;

import java.util.function.Function;

public class CalculateAverageTime {

     static long frameDuration=0;

    public static <T>long Iterations(T frame, boolean isSpeechSegment , Function<T,Boolean>isSpeechFunction) {
        for (int j = 0; j < 10000; j++) {
            long startTime = System.nanoTime();
            isSpeechSegment = isSpeechFunction.apply(frame);
            long endTime = System.nanoTime();
            frameDuration += (endTime - startTime);
        }
        frameDuration /= 10000;
           return frameDuration;
    }
}
