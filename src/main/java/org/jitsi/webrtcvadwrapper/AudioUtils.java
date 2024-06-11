package org.jitsi.webrtcvadwrapper;

import org.apache.commons.io.IOUtils;
import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class AudioUtils {

    public static byte[] convertAudioFileToByteArray(File file) throws IOException, UnsupportedAudioFileException {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file)) {
            AudioFormat format = audioInputStream.getFormat();

            if ((int) format.getSampleRate() != 8000) {
                throw new UnsupportedAudioFileException("Unsupported sample rate: " + format.getSampleRate());
            }

            if (format.getChannels() == 2) {
                AudioFormat monoFormat = new AudioFormat(
                        format.getSampleRate(),
                        format.getSampleSizeInBits(),
                        1,
                        true,
                        format.isBigEndian()
                );
                try (AudioInputStream monoAudioInputStream = AudioSystem.getAudioInputStream(monoFormat, audioInputStream);
                     ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    IOUtils.copy(monoAudioInputStream, os);
                    return os.toByteArray();
                }
            } else {
                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    IOUtils.copy(audioInputStream, os);
                    return os.toByteArray();
                }
            }
        }
    }
}
