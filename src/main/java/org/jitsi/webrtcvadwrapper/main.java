package org.jitsi.webrtcvadwrapper;


import java.io.File;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;

class main {

    public static void main(String[] args) {
        String audioDirectoryPath = "/Users/abhinav.jain/Downloads/jitsi-webrtc-vad-wrapper/src/main/java/org/jitsi/webrtcvadwrapper/5-hours-output";

        File audioDirectory = new File(audioDirectoryPath);
        File[] audioFiles = audioDirectory.listFiles();

        if (audioFiles != null) {
            for (File file : audioFiles) {
                if (file.isFile() && file.getName().endsWith(".wav") && file.getName().equals("speech-102.wav")) {
                    try {
                        System.out.println("Processing file: " + file.getName());
                        AudioProcessor.processAudioFileWebRTC(file);
                        AudioProcessor.processAudioFileVAD4j(file);
                    } catch (IOException | UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
