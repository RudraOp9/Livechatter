package com.leopa.livechatter.webrtc;

import android.util.Log;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;

import java.util.Arrays;

public class CustomPeerObserver implements PeerConnection.Observer {



    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {
        Log.d("TAG : ","in onSignalingChange : "+ signalingState.toString());
    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        Log.d("TAG : ","in onIceConnectionChange : "+ iceConnectionState.toString());
    }

    @Override
    public void onIceConnectionReceivingChange(boolean b) {
        Log.d("TAG : ","in onIceConnectionReceivingChange : "+ b);
    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
        Log.d("TAG : ","in onIceGatheringChange : "+ iceGatheringState.toString());
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {

    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {

    }

    @Override
    public void onAddStream(MediaStream mediaStream) {

    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {
        Log.d("TAG : ","in onRemoveStream : "+ mediaStream.toString());
    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {
        Log.d("TAG : ","in onDataChannel : "+ dataChannel.toString());
    }

    @Override
    public void onRenegotiationNeeded() {
        Log.d("TAG : ","in onRenegotiationNeeded : ");
    }

    @Override
    public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
        Log.d("TAG : ","in onAddTrack : "+ rtpReceiver.toString() +" : "+ Arrays.toString(mediaStreams));
    }
}
