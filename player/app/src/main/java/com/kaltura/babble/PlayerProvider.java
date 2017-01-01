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
import com.kaltura.playkit.backend.base.OnMediaLoadCompletion;
import com.kaltura.playkit.backend.mock.MockMediaProvider;
import com.kaltura.playkit.connect.ResultElement;

/**
 * Created by itanbarpeled on 01/01/2017.
 */

public class PlayerProvider {


    private static final String LOCAL_STANDALONE_JSON_FILE_NAME = "standalonePlayer.json";


    private Player mMainPlayer;
    private Player mSecondPlayer;
    private int mCount;


    public interface OnPlayerReadyListener {
        void onPlayerReady(Player basePlayer, Player secondPlayer);
    }

    public PlayerProvider() {
        mCount = 0;
        mMainPlayer = null;
        mSecondPlayer = null;
    }




    public void getPlayer(String mainMediaId, Activity context, OnPlayerReadyListener listener) {

        setMockMediaProvider(mainMediaId, context, listener, true);
        setMockMediaProvider(mainMediaId, context, listener, false);

    }



    private void setMockMediaProvider(String mediaId, Activity context, OnPlayerReadyListener listener, boolean mainPlayer) {


        MediaEntryProvider mockMediaProvider = new MockMediaProvider(LOCAL_STANDALONE_JSON_FILE_NAME, context, mediaId);


        loadMediaProvider(mockMediaProvider, listener, context, mainPlayer);


    }



    private void loadMediaProvider(MediaEntryProvider mediaEntryProvider,
                                   final OnPlayerReadyListener listener, final Activity context, final boolean mainPlayer) {

        mediaEntryProvider.load(new OnMediaLoadCompletion() {

            @Override
            public void onComplete(final ResultElement<PKMediaEntry> response) {

                context.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if (response.isSuccess()) {
                            onMediaLoaded(response.getResponse(), listener, context, mainPlayer);

                        } else {

                            String error = "failed to fetch media data: " + (response.getError() != null ? response.getError().getMessage() : "");
                            Log.e(MainActivity.TAG, error);
                            listener.onPlayerReady(null, null);

                        }
                    }
                });
            }
        });
    }



    private void onMediaLoaded(PKMediaEntry mediaEntry, final OnPlayerReadyListener listener, Activity context, final boolean mainPlayer) {

        PlayerConfig playerConfig = new PlayerConfig();

        playerConfig.media.setMediaEntry(mediaEntry);
        playerConfig.media.setStartPosition(MainActivity.START_POSITION);

        Log.v(MainActivity.TAG, "onMediaLoaded startPosition " + playerConfig.media.getStartPosition());

        final Player player = PlayKitManager.loadPlayer(playerConfig, context);
        player.prepare(playerConfig.media);


        player.addEventListener(new PKEvent.Listener() {

            @Override
            public void onEvent(PKEvent event) {

                PlayerEvent.TracksAvailable tracksAvailable = (PlayerEvent.TracksAvailable) event;
                PKTracks tracks = tracksAvailable.getPKTracks();

                for (AudioTrack audioTrack : tracks.getAudioTracks()) {

                    Log.v(MainActivity.TAG, "PlayerProvider. TRACK. mainPlayer = " + mainPlayer + ". label = " + audioTrack.getLabel()
                            + ". language = " + audioTrack.getLanguage() + " uniqueId = " + audioTrack.getUniqueId());
                }


                if (mainPlayer) {
                    setMainPlayer(player, listener);
                } else {
                    setSeccondPlayer(player, listener);
                }

            }

        }, PlayerEvent.Type.TRACKS_AVAILABLE);


    }



    private void setMainPlayer(Player player, OnPlayerReadyListener listener) {

        player.changeTrack("Audio:1,1,0");

        returnResult(player, listener, true);
    }




    private void setSeccondPlayer(Player player, OnPlayerReadyListener listener) {

        player.changeTrack("Audio:1,0,0");

        returnResult(player, listener, false);
    }





    private void returnResult(Player player, OnPlayerReadyListener listener, boolean mainPlayer) {

        mCount++;

        if (mainPlayer) {
            mMainPlayer = player;
        } else {
            mSecondPlayer = player;
        }

        if (mCount == 2) {
            listener.onPlayerReady(mMainPlayer, mSecondPlayer);
        }

    }





}
