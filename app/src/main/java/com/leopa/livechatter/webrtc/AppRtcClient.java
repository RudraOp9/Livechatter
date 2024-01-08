package com.leopa.livechatter.webrtc;

import android.content.Context;
import android.util.Log;

import com.leopa.livechatter.utils_constants.FireBase;

import org.webrtc.AudioTrack;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;

import java.util.ArrayList;
import java.util.List;

public class AppRtcClient {

    PeerConnectionFactory factory;
    PeerConnection peerConnection;
    MediaConstraints constraints;
    List<PeerConnection.IceServer> configuration =  new ArrayList<>();
    FireBase fireBase;

    public AppRtcClient(Context context, PeerConnection.Observer customPeerObserver) {
        fireBase = new FireBase();
        initPeerFactory(context);
        factory = createPeerFactory();
        configuration.add(PeerConnection.IceServer.builder("stun:stun.relay.metered.ca:80").createIceServer());
        configuration.add(PeerConnection.IceServer.builder("turn:global.relay.metered.ca:80").setUsername("0bbc71c182bef810ceaa8882").setPassword("jL04B0xT3hEcvuYT").createIceServer());
        configuration.add(PeerConnection.IceServer.builder("turn:global.relay.metered.ca:80?transport=tcp").setUsername("0bbc71c182bef810ceaa8882").setPassword("jL04B0xT3hEcvuYT").createIceServer());
        configuration.add(PeerConnection.IceServer.builder("turn:global.relay.metered.ca:443").setUsername("0bbc71c182bef810ceaa8882").setPassword("jL04B0xT3hEcvuYT").createIceServer());
        configuration.add(PeerConnection.IceServer.builder("turn:global.relay.metered.ca:443?transport=tcp").setUsername("0bbc71c182bef810ceaa8882").setPassword("jL04B0xT3hEcvuYT").createIceServer());

        if (peerConnection != null){
        }else {
            do {
                peerConnection = createCustomPeerConnection(customPeerObserver);
                Log.d("TAG ", "peer connection status :" + peerConnection.connectionState());
            } while (peerConnection.connectionState().toString().equals("NEW"));
        }

    }
    public AppRtcClient() {
    }

    private PeerConnectionFactory createPeerFactory() {
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        options.disableNetworkMonitor = false;
        options.disableEncryption = false;
        Log.d("TAG : position", "created createPeerFactory()");
        return PeerConnectionFactory.builder()
                .setOptions(options)
                .createPeerConnectionFactory();
    }

    private void initPeerFactory(Context context) {
        PeerConnectionFactory.InitializationOptions options3 = PeerConnectionFactory
                .InitializationOptions
                .builder(context)
                .setEnableInternalTracer(true)
                .createInitializationOptions();
        PeerConnectionFactory.initialize(options3);
        Log.d("TAG : position", "initPeerFactory()");
    }

    private PeerConnection createCustomPeerConnection(PeerConnection.Observer customPeerObserver) {

        Log.d("TAG : position","in create custom peer connection");
        return factory.createPeerConnection(configuration, customPeerObserver);
    }

    public void createOffer() {
        constraints = new MediaConstraints();
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        if (peerConnection != null) {
            Log.d("TAG : ", "peerconnection is not null");

            try {
                peerConnection.createOffer(new SdpObserver() {
                    @Override
                    public void onCreateSuccess(SessionDescription sessionDescription) {
                        super.onCreateSuccess(sessionDescription);
                        peerConnection.setLocalDescription(new SdpObserver() {
                            @Override
                            public void onSetSuccess() {
                                super.onSetSuccess();
                                sendSessionDescription(sessionDescription.description);
                                Log.d("TAG : position", "create offer : in set success: " + sessionDescription.description);
                                //its time to transfer this sdp to other peer
                            }

                        }, sessionDescription);
                    }
                }, constraints);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d("TAG : ", "peerconnection is  null");
        }
    }

    public void createAnswer() {
        constraints = new MediaConstraints();
        constraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        try {
            peerConnection.createAnswer(new SdpObserver() {
                @Override
                public void onCreateSuccess(SessionDescription sessionDescription) {
                    super.onCreateSuccess(sessionDescription);
                    peerConnection.setLocalDescription(new SdpObserver() {
                        @Override
                        public void onSetSuccess() {
                            super.onSetSuccess();
                            sendSessionDescription(sessionDescription.description);
                            Log.d("TAG : position", "create Answer : in set success: " + sessionDescription.description);
                        }
                    }, sessionDescription);
                }
            }, constraints);
        } catch (Exception ignored) {
        }
    }

    public void addIceCandidata(IceCandidate iceCandidate) {
        peerConnection.addIceCandidate(iceCandidate);
    }

    public void sendIceCandidate(IceCandidate iceCandidate) {
        addIceCandidata(iceCandidate);
        fireBase.sendIce(iceCandidate);

    }

    public void sessionReceived(SessionDescription sessionDescription) {
        peerConnection.setRemoteDescription(new SdpObserver(), sessionDescription);
    }

    public void sendSessionDescription(String sessionDescription) {
        fireBase.sendSSD(sessionDescription);
    }

    public void initAudio() {
        String localTrackId = "local_track";
        String localStreamId = "local_stream";
        AudioTrack localAudioTrack = factory.createAudioTrack(localTrackId + "_audio", factory.createAudioSource(new MediaConstraints()));
        MediaStream localStream = factory.createLocalMediaStream(localStreamId);
        localStream.addTrack(localAudioTrack);
        peerConnection.addStream(localStream);
    }
}
