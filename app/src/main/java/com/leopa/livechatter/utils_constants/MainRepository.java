package com.leopa.livechatter.utils_constants;

import android.content.Context;
import android.util.Log;


import com.google.gson.Gson;
import com.leopa.livechatter.webrtc.CustomPeerObserver;
import com.leopa.livechatter.webrtc.WebRTCClient;
import com.leopa.livechatter.utils_constants.ErrorCallBack;
import com.leopa.livechatter.utils_constants.SuccessCallBack;
import com.leopa.livechatter.utils_constants.NewEventCallBack;
import com.leopa.livechatter.model.DataModel;
import com.leopa.livechatter.model.DataModelType;

import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;

public class MainRepository implements WebRTCClient.Listener {

    public Listener listener;
    private final Gson gson = new Gson();
    private final FirebaseClient firebaseClient;

    private WebRTCClient webRTCClient;

    private String currentUsername;
    private String target;

    private SurfaceViewRenderer remoteView;

    private void updateCurrentUsername(String username){
        this.currentUsername = username;
    }

    private MainRepository(){
        this.firebaseClient = new FirebaseClient();
    }

    private static MainRepository instance;
    public static MainRepository getInstance(){
        if (instance == null){
            instance = new MainRepository();
        }
        return instance;
    }

    public void login(String username,String target, Context context, SuccessCallBack callBack){
        this.target = target;
        firebaseClient.login(username,()->{
            updateCurrentUsername(username);
            this.webRTCClient = new WebRTCClient(context,new CustomPeerObserver(){
                @Override
                public void onAddStream(MediaStream mediaStream) {
                    super.onAddStream(mediaStream);
                    try{
                        //adding media Stream for volume
                        mediaStream.videoTracks.get(0).addSink(remoteView);
                        Log.d("TAG : position", "in on add stream " + mediaStream.videoTracks.get(0)+" audio : " +mediaStream.audioTracks.get(0));

                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("TAG : position","in onAddStream exception : "+e.toString());
                    }
                }

                @Override
                public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
                    Log.d("TAG", "onConnectionChange: "+newState);
                    super.onConnectionChange(newState);
                    if (newState == PeerConnection.PeerConnectionState.CONNECTED && listener!=null){
                        listener.webrtcConnected();
                    }

                    if (newState == PeerConnection.PeerConnectionState.CLOSED ||
                            newState == PeerConnection.PeerConnectionState.DISCONNECTED ){
                        if (listener!=null){
                            listener.webrtcClosed();
                        }
                    }
                }

                @Override
                public void onIceCandidate(IceCandidate iceCandidate) {
                    super.onIceCandidate(iceCandidate);
                    webRTCClient.sendIceCandidate(iceCandidate,target);
                    Log.d("TAG : position","on Ice candidate must have send to webrtc : "+iceCandidate.toString());
                }
            },username);
            webRTCClient.listener = this;
            callBack.onSuccess();
        });
    }

    public void initLocalView(SurfaceViewRenderer view){
        webRTCClient.initLocalSurfaceView(view);
        Log.d("TAG : position","initLocalView");
    }

    public void initRemoteView(SurfaceViewRenderer view){
        webRTCClient.initRemoteSurfaceView(view);
        Log.d("TAG : position","initRemoteView");
        this.remoteView = view;
    }

    public void startCall(String target){
        webRTCClient.call(target);
        Log.d("TAG : position","StartCall");
    }

    public void switchCamera() {
        webRTCClient.switchCamera();
    }

    public void toggleAudio(Boolean shouldBeMuted){
        webRTCClient.toggleAudio(shouldBeMuted);
    }
    public void toggleVideo(Boolean shouldBeMuted){
        webRTCClient.toggleVideo(shouldBeMuted);
    }
    public void sendCallRequest(String target, ErrorCallBack errorCallBack){
        firebaseClient.sendMessageToOtherUser(
                new DataModel(target,currentUsername,null, DataModelType.StartCall),errorCallBack
        );
    }

    public void endCall(){
        webRTCClient.closeConnection();
    }

    public void subscribeForLatestEvent(NewEventCallBack callBack){
        firebaseClient.observeIncomingLatestEvent(model -> {
            switch (model.getType()){

                case Offer:
                    webRTCClient.onRemoteSessionReceived(new SessionDescription(
                            SessionDescription.Type.OFFER,model.getData()
                    ));
                    webRTCClient.answer(model.getSender());
                    Log.d("TAG : position","in case offer Session description received. Going to answer now.");
                    break;
                case Answer:
                    webRTCClient.onRemoteSessionReceived(new SessionDescription(
                            SessionDescription.Type.ANSWER,model.getData()
                    ));
                    Log.d("TAG : position","in case Answer Session description received. ");
                    break;
                case IceCandidate:
                    try{
                        IceCandidate candidate = gson.fromJson(model.getData(),IceCandidate.class);
                        webRTCClient.addIceCandidate(candidate);
                        Log.d("TAG : position","in case Icecandidate received. Going to add now.");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case StartCall:
                    callBack.onNewEventReceived(model);
                    break;
            }

        });
    }

    @Override
    public void onTransferDataToOtherPeer(DataModel model) {
        firebaseClient.sendMessageToOtherUser(model,()->{});
    }

    public interface Listener{
        void webrtcConnected();
        void webrtcClosed();
    }
}

