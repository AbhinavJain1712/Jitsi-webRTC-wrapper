package org.jitsi.webrtcvadwrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jitsi.webrtcvadwrapper.audio.ByteSignedPcmAudioSegment;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.orctom.vad4j.VAD;
import com.orctom.vad4j.VadMode;
import com.orctom.vad4j.VadWindowSize;

import java.util.Map;

public class AudioProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AudioProcessor.class);
    private static final int WEBRTC_FRAME_SIZE = 160;
    private static final int VAD4J_FRAME_SIZE = 320;

    private AudioProcessor() {
        throw new UnsupportedOperationException("Utility class");
    }


    public static void processAudioFileWebRTC(File file,Map<String, Double> treeMap) throws IOException, UnsupportedAudioFileException {
        logger.info("Using Jitsi-WebRTC Library:");

        byte[] audioBytes = AudioUtils.convertAudioFileToByteArray(file);
        ByteSignedPcmAudioSegment audioSegment = new ByteSignedPcmAudioSegment(audioBytes);
        int[] linear16Audio = audioSegment.to16bitPCM();

        WebRTCVad vad = new WebRTCVad(8000, 0);
        List<Double> start = new ArrayList<>();
        List<Double> end = new ArrayList<>();

        double average = SoundCheck.checkSoundSegments(linear16Audio, WEBRTC_FRAME_SIZE, start, end, vad::isSpeech);

        for(int i=0;i<start.size();i++)
        {
            System.out.println(start.get(i) + "-" + end.get(i));
        }
        String key = file.getName();
        treeMap.put(key, treeMap.getOrDefault(key, 0.0) + average);

    }

    public static void processAudioFileVAD4j(File file,Map<String, Double> treeMap) throws IOException, UnsupportedAudioFileException {
        logger.info("Using Vad4j Library:");

        byte[] audioBytes = AudioUtils.convertAudioFileToByteArray(file);
        List<Double> start;
        List<Double> end;
        double average;
        try (VAD vad = new VAD(VadWindowSize._20ms, VadMode.quality)) {

            start = new ArrayList<>();
            end = new ArrayList<>();

            average = SoundCheck.checkSoundSegments(audioBytes, VAD4J_FRAME_SIZE, start, end, vad::isSpeech);
        }
        for(int i=0;i< start.size();i++)
        {
            System.out.println(start.get(i)+ "-" + end.get(i));
        }
        String key = file.getName();
        treeMap.put(key, treeMap.getOrDefault(key, 0.0) + average);
    }
}



