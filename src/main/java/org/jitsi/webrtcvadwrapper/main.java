package org.jitsi.webrtcvadwrapper;

import java.util.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;

class main {

    public static void main(String[] args) {

        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // Extract numerical part from keys
                int num1 = Integer.parseInt(o1.substring(o1.indexOf('-') + 1, o1.indexOf('.')));
                int num2 = Integer.parseInt(o2.substring(o2.indexOf('-') + 1, o2.indexOf('.')));
                return Integer.compare(num1, num2);
            }
        };

        String audioDirectoryPath = "/Users/abhinav.jain/Downloads/jitsi-webrtc-vad-wrapper/src/main/java/org/jitsi/webrtcvadwrapper/5-hours-output";

        File audioDirectory = new File(audioDirectoryPath);
        File[] audioFiles = audioDirectory.listFiles();

        int iterations = 1;
        Map<String, Double> treeMap = new TreeMap<>(comparator);

        if (audioFiles != null) {
        for (int i = 0; i < iterations; i++) {
                for (File file : audioFiles) {
                    if (file.isFile() && file.getName().endsWith(".wav") ) {
                        try {
                           //AudioProcessor.processAudioFileWebRTC(file,treeMap);
                            AudioProcessor.processAudioFileVAD4j(file,treeMap);
                        } catch (IOException | UnsupportedAudioFileException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            for (Map.Entry<String, Double> entry : treeMap.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue()/iterations + "ns");
            }
        }
    }
}
