package org.jitsi.webrtcvadwrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;



public class AudioProcessingMain {
    private static final Logger logger = LoggerFactory.getLogger(AudioProcessingMain.class);
    public static void main(String[] args) {
        Comparator<String> comparator = createComparator();

        String audioDirectoryPath = "/Users/abhinav.jain/Downloads/jitsi-webrtc-vad-wrapper/src/main/java/org/jitsi/webrtcvadwrapper/audio_new";
        File audioDirectory = new File(audioDirectoryPath);
        File[] audioFiles = audioDirectory.listFiles();
        int iterations = 500;
        Map<String, Double> treeMapJitsi = new TreeMap<>(comparator);
        Map<String, Double> treeMapVad = new TreeMap<>(comparator);

        if (audioFiles != null) {
            processAudioFiles(audioFiles, iterations, treeMapJitsi, treeMapVad);
            System.out.println("Using Jitsi:");
            printResults(treeMapJitsi, iterations);
            System.out.println("Using Vad4j:");
            printResults(treeMapVad, iterations);

        }
    }
    private static Comparator<String> createComparator() {
        return (o1, o2) -> {
            int num1 = Integer.parseInt(o1.substring(o1.indexOf('-') + 1, o1.indexOf('.')));
            int num2 = Integer.parseInt(o2.substring(o2.indexOf('-') + 1, o2.indexOf('.')));
            return Integer.compare(num1, num2);
        };
    }
    private static void processAudioFiles(File[] audioFiles, int iterations, Map<String, Double> treeMapJitsi, Map<String, Double> treeMapVad) {
        for (int i = 0; i < iterations; i++) {
            for (File file : audioFiles) {
                if (file.isFile() && file.getName().endsWith(".wav")) {
                    processSingleFile(file, treeMapJitsi,treeMapVad);
                }
            }
        }
    }
    private static void processSingleFile(File file, Map<String, Double> treeMapJitsi, Map<String, Double> treeMapVad) {
        try {
            AudioProcessor.processAudioFileWebRTC(file, treeMapJitsi);
            AudioProcessor.processAudioFileVAD4j(file, treeMapVad);
        } catch (IOException | UnsupportedAudioFileException e) {
            logger.error("Error processing file {}: {}", file.getName(), e.getMessage());
        }
    }
    private static void printResults(Map<String, Double> treeMap, int iterations) {
        for (Map.Entry<String, Double> entry : treeMap.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue() / iterations + "µs");
        }
    }
}
