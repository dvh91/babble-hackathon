package com.kaltura.babble;

import android.app.Activity;
import android.util.Log;

import com.kaltura.playkit.AudioTrack;
import com.kaltura.playkit.MediaEntryProvider;
import com.kaltura.playkit.PKEvent;
import com.kaltura.playkit.PKMediaEntry;
import com.kaltura.playkit.PKTracks;
import com.kaltura.playkit.PlayKitManager;
import com.kaltura.playkit.Player;
import com.kaltura.playkit.PlayerConfig;
import com.kaltura.playkit.PlayerEvent;
import com.kaltura.playkit.PlayerState;
import com.kaltura.playkit.backend.base.OnMediaLoadCompletion;
import com.kaltura.playkit.backend.mock.MockMediaProvider;
import com.kaltura.playkit.connect.ResultElement;

import static com.kaltura.babble.PlayerProvider.PlayerType.BASE_AUDIO;
import static com.kaltura.babble.PlayerProvider.PlayerType.SECOND_AUDIO;
import static com.kaltura.babble.PlayerProvider.PlayerType.VIDEO_PLAYER;

/**
 * Created by itanbarpeled on 01/01/2017.
 */

public class PlayerProvider {


    private static final String LOCAL_STANDALONE_JSON_FILE_NAME = "standalonePlayer.json";


    private Player mVideoPlayer;
    private Player mBaseAudioPlayer;
    private Player mSecondAudioPlayer;
    private int mCount;

    enum PlayerType {

        VIDEO_PLAYER,
        BASE_AUDIO,
        SECOND_AUDIO

    }


    public interface OnPlayerReadyListener {
        void onPlayerReady(Player videoPlayer, Player baseAudioPlayer, Player secondAudioPlayer);
    }

    public PlayerProvider() {
        mCount = 0;
        mVideoPlayer = null;
        mBaseAudioPlayer = null;
        mSecondAudioPlayer = null;
    }




    public void getPlayer(String mainMediaId, Activity context, OnPlayerReadyListener listener) {

        setMockMediaProvider(mainMediaId, context, listener, VIDEO_PLAYER);
        setMockMediaProvider(mainMediaId, context, listener, BASE_AUDIO);
        setMockMediaProvider(mainMediaId, context, listener, SECOND_AUDIO);

    }



    private void setMockMediaProvider(String mediaId, Activity context, OnPlayerReadyListener listener, PlayerType playerType) {


        MediaEntryProvider mockMediaProvider = new MockMediaProvider(LOCAL_STANDALONE_JSON_FILE_NAME, context, mediaId);


        loadMediaProvider(mockMediaProvider, listener, context, playerType);


    }



    private void loadMediaProvider(MediaEntryProvider mediaEntryProvider,
                                   final OnPlayerReadyListener listener, final Activity context, final PlayerType playerType) {

        mediaEntryProvider.load(new OnMediaLoadCompletion() {

            @Override
            public void onComplete(final ResultElement<PKMediaEntry> response) {

                context.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if (response.isSuccess()) {
                            onMediaLoaded(response.getResponse(), listener, context, playerType);

                        } else {

                            String error = "failed to fetch media data: " + (response.getError() != null ? response.getError().getMessage() : "");
                            Log.e(MainActivity.TAG, error);
                            listener.onPlayerReady(null, null, null);

                        }
                    }
                });
            }
        });
    }



    private void onMediaLoaded(PKMediaEntry mediaEntry, final OnPlayerReadyListener listener, Activity context, final PlayerType playerType) {

        PlayerConfig playerConfig = new PlayerConfig();

        playerConfig.media.setMediaEntry(mediaEntry);
        playerConfig.media.setStartPosition(MainActivity.MEDIA_START_POSITION);

        Log.v(MainActivity.TAG, "onMediaLoaded startPosition " + playerConfig.media.getStartPosition());

        final Player player = PlayKitManager.loadPlayer(playerConfig, context);
        player.prepare(playerConfig.media);
        player.pause();


        player.addEventListener(new PKEvent.Listener() {

            @Override
            public void onEvent(PKEvent event) {

                /*
                PlayerEvent.TracksAvailable tracksAvailable = (PlayerEvent.TracksAvailable) event;
                PKTracks tracks = tracksAvailable.getPKTracks();

                for (AudioTrack audioTrack : tracks.getAudioTracks()) {

                    Log.v(MainActivity.TAG, "PlayerProvider. TRACK. mainPlayer = " + mainPlayer + ". label = " + audioTrack.getLabel()
                            + ". language = " + audioTrack.getLanguage() + " uniqueId = " + audioTrack.getUniqueId());
                }
                */


                switch(playerType) {

                    case VIDEO_PLAYER:
                        setVideoPlayer(player, listener);
                        break;

                    case BASE_AUDIO:
                        setBaseAudioPlayer(player, listener);
                        break;

                    case SECOND_AUDIO:
                        setSecondAudioPlayer(player, listener);
                        break;
                }


            }

        }, PlayerEvent.Type.TRACKS_AVAILABLE);


    }



    private void setVideoPlayer(Player player, OnPlayerReadyListener listener) {

        player.disableAudio(true);

        returnResult(player, listener, VIDEO_PLAYER);

    }




    private void setBaseAudioPlayer(Player player, OnPlayerReadyListener listener) {

        player.changeTrack("Audio:1,1,0");
        player.disableVideo(true);

        returnResult(player, listener, BASE_AUDIO);
    }




    private void setSecondAudioPlayer(Player player, OnPlayerReadyListener listener) {

        player.changeTrack("Audio:1,0,0");
        player.disableVideo(true);

        returnResult(player, listener, SECOND_AUDIO);
    }





    private void returnResult(Player player, OnPlayerReadyListener listener, PlayerType playerType) {

        mCount++;


        switch(playerType) {

            case VIDEO_PLAYER:
                mVideoPlayer = player;
                break;

            case BASE_AUDIO:
                mBaseAudioPlayer = player;
                break;

            case SECOND_AUDIO:
                mSecondAudioPlayer = player;
                break;
        }


        if (mCount == 3) {
            listener.onPlayerReady(mVideoPlayer, mBaseAudioPlayer, mSecondAudioPlayer);
        }

    }





}
