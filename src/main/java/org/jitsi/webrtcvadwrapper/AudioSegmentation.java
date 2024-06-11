package org.jitsi.webrtcvadwrapper;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.io.ByteArrayInputStream;

public class AudioSegmentation {

    public static void generateNewAudioFile(File inputFile, List<Double> startTimes, List<Double> endTimes, File outputFile) throws IOException, UnsupportedAudioFileException {
        // Read the original audio file
        AudioInputStream originalAudioStream = AudioSystem.getAudioInputStream(inputFile);
        AudioFormat originalFormat = originalAudioStream.getFormat();

        // Determine the number of channels and sample rate
        int channels = originalFormat.getChannels();
        float sampleRate = originalFormat.getSampleRate();

        System.out.println(originalAudioStream.getFrameLength());
        System.out.println(originalFormat.getFrameSize());

        // Create a byte array to store the entire audio data
        byte[] originalAudioData = new byte[(int) (originalAudioStream.getFrameLength() * originalFormat.getFrameSize())];
        originalAudioStream.read(originalAudioData);

        // Create a new byte array to store the concatenated audio segments
        int totalLength = 0;
        for (int i = 0; i < startTimes.size(); i++) {
            double startTime = startTimes.get(i);
            double endTime = endTimes.get(i);

            // Convert start and end times to sample indices
            int startSampleIndex = (int) (startTime * sampleRate * channels * 2); // * channels * 2 for 16-bit PCM
            int endSampleIndex = (int) (endTime * sampleRate * channels * 2);

            // Calculate the length of the audio segment
            int segmentLength = endSampleIndex - startSampleIndex;

            // Add the length of the segment to the total length
            totalLength += segmentLength;
        }
        System.out.println(totalLength);
        byte[] newAudioData = new byte[totalLength];
        int offset = 0;

        // Extract and concatenate audio segments
        for (int i = 0; i < startTimes.size(); i++) {
            double startTime = startTimes.get(i);
            double endTime = endTimes.get(i);

            // Convert start and end times to sample indices
            int startSampleIndex = (int) (startTime * sampleRate * channels * 2);
            int endSampleIndex = (int) (endTime * sampleRate * channels * 2);

            // Extract the audio segment
            System.arraycopy(originalAudioData, startSampleIndex, newAudioData, offset, endSampleIndex - startSampleIndex);

            // Update the offset for the next segment
            offset += endSampleIndex - startSampleIndex;
        }

        // Create an AudioInputStream from the new audio data
        AudioInputStream newAudioStream = new AudioInputStream(new ByteArrayInputStream(newAudioData), originalFormat, totalLength / originalFormat.getFrameSize());

        // Write the new audio data to a file
        AudioSystem.write(newAudioStream, AudioFileFormat.Type.WAVE, outputFile);

        // Close the streams
        originalAudioStream.close();
        newAudioStream.close();
    }

    public static void playAudioFile(File audioFile) throws IOException, UnsupportedAudioFileException {
        try {
            // Get an audio input stream from the file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioInputStream.getFormat();

            // Get a line to play the audio
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);
            audioLine.open(format);
            audioLine.start();

            // Play the audio
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                audioLine.write(buffer, 0, bytesRead);
            }

            // Cleanup
            audioLine.drain();
            audioLine.close();
            audioInputStream.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
