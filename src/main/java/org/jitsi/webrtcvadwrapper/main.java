package org.jitsi.webrtcvadwrapper;

import org.jitsi.webrtcvadwrapper.WebRTCVad;
import org.jitsi.webrtcvadwrapper.audio.ByteSignedPcmAudioSegment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import javax.sound.sampled.*;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

 class Example {
    private static final int FRAME_SIZE = 320;

    public static void main(String[] args) {
        String audioDirectoryPath = "/Users/abhinav.jain/Downloads/jitsi-webrtc-vad-wrapper/src/main/java/org/jitsi/webrtcvadwrapper/26-audio";

        File audioDirectory = new File(audioDirectoryPath);
        File[] audioFiles = audioDirectory.listFiles();

        if (audioFiles != null) {
            for (File file : audioFiles) {
                if (file.isFile() && file.getName().endsWith(".wav")) {
                    try {
                        processAudioFile(file);
                    } catch (IOException | UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void processAudioFile(File file) throws IOException, UnsupportedAudioFileException {

        System.out.println("Processing file: " + file.getName());

        byte[] audioBytes = readFileToByteArray(file);
        ByteSignedPcmAudioSegment audioSegment = new ByteSignedPcmAudioSegment(audioBytes);
        int[] linear16Audio = audioSegment.to16bitPCM();

        WebRTCVad vad = new WebRTCVad(16000, 1);

        long totalSpeechDuration = 0;
        long totalSilenceDuration = 0;

        for (int i = 0; i + FRAME_SIZE <= linear16Audio.length; i += FRAME_SIZE) {
            int[] frame = new int[FRAME_SIZE];
            System.arraycopy(linear16Audio, i, frame, 0, FRAME_SIZE);

            boolean isSpeechSegment = vad.isSpeech(frame);
            if (file.getName().equals("speech-18.wav")) {
                System.out.print(isSpeechSegment+" ");
                if (isSpeechSegment)
                    totalSpeechDuration += 20;
                else
                    totalSilenceDuration += 20;
            }
        }

        if (file.getName().equals("speech-18.wav")) {
            System.out.println();
            System.out.println("Total speech duration=" + totalSpeechDuration + "ms");
            System.out.println("Total silence duration=" + totalSilenceDuration + "ms");
        }
    }

    private static byte[] readFileToByteArray(File file) throws IOException {
        try (InputStream is = new FileInputStream(file);
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            IOUtils.copy(is, os);
            return os.toByteArray();
        }
    }
}


