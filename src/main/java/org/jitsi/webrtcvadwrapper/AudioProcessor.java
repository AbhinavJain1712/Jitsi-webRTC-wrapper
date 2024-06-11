package org.jitsi.webrtcvadwrapper;

import org.jitsi.webrtcvadwrapper.audio.ByteSignedPcmAudioSegment;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.orctom.vad4j.VAD;
import com.orctom.vad4j.VadMode;
import com.orctom.vad4j.VadWindowSize;
import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import org.jitsi.webrtcvadwrapper.AudioSegmentation;

public class AudioProcessor {

    private static final int WEBRTC_FRAME_SIZE = 160;
    private static final int VAD4J_FRAME_SIZE = 320;


    public static void processAudioFileWebRTC(File file) throws IOException, UnsupportedAudioFileException {
        System.out.println("Using Jitsi-WebRTC Library:");

        byte[] audioBytes = AudioUtils.convertAudioFileToByteArray(file);
        ByteSignedPcmAudioSegment audioSegment = new ByteSignedPcmAudioSegment(audioBytes);
        int[] linear16Audio = audioSegment.to16bitPCM();

        WebRTCVad vad = new WebRTCVad(8000, 2);
        List<Double> start = new ArrayList<>();
        List<Double> end = new ArrayList<>();

        SoundCheck.checkSoundSegments(linear16Audio, WEBRTC_FRAME_SIZE, start, end, vad::isSpeech);

    }



    public static void processAudioFileVAD4j(File file) throws IOException, UnsupportedAudioFileException {
        System.out.println("Using Vad4j Library:");

        byte[] audioBytes = AudioUtils.convertAudioFileToByteArray(file);
        VAD vad = new VAD(VadWindowSize._20ms, VadMode.quality);

        List<Double> start = new ArrayList<>();
        List<Double> end = new ArrayList<>();

        SoundCheck.checkSoundSegments(audioBytes, VAD4J_FRAME_SIZE, start, end, vad::isSpeech);

    }
}

