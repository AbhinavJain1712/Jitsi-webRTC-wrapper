package org.jitsi.webrtcvadwrapper;

import java.util.List;

public class SoundState {

    private long time = 0;
    private boolean isSound = false;
    private long soundTime = 0;
    private long soundStart = 0;
    private long soundEnd = 0;
    private long silenceTime = 0;

    public void update(boolean isSpeechSegment, List<Double> start, List<Double> end) {

        time += 20;
        checkSound(isSpeechSegment);
        updateSpeechSegment(isSpeechSegment, start, end);
    }
    private void checkSound(boolean isSpeechSegment) {
        if (!isSound) {
            if (isSpeechSegment) {
                soundTime += 20;
                if (soundTime == 200) {
                    isSound = true;
                    soundTime = 0;
                }
            } else {
                soundTime = 0;
            }
        }
    }

    private void updateSpeechSegment(boolean isSpeechSegment, List<Double> start, List<Double> end) {
        if (isSound) {
            if (soundStart == 0) {
                soundStart = time - 200;
            }
            if (!isSpeechSegment) {
                silenceTime += 20;
            } else {
                silenceTime = 0;
            }
            if (silenceTime == 300) {
                isSound = false;
                soundEnd = time - 300;
                start.add(soundStart / 1000.0);
                end.add(soundEnd / 1000.0);
                soundStart = 0;
                soundEnd = 0;
            }
        }
    }

    public void finalizeLastSegment(List<Double> start, List<Double> end) {
        if (soundStart > 0) {
            soundEnd = time;
            start.add(soundStart / 1000.0);
            end.add(soundEnd / 1000.0);
        }
    }
}
