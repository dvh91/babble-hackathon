package com.kaltura.babble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import com.kaltura.babble.player.PlayerControlsController;
import com.kaltura.babble.player.PlayerControlsView;
import com.kaltura.playkit.PKEvent;
import com.kaltura.playkit.Player;
import com.kaltura.playkit.PlayerEvent;

import static com.kaltura.babble.MainActivity.BabbleState.MAIN_BABBLE;
import static com.kaltura.babble.MainActivity.BabbleState.NONE;
import static com.kaltura.babble.MainActivity.BabbleState.SECOND_BABBLE;

public class MainActivity extends AppCompatActivity  {


    public static final String TAG = "BABBLE_LOG";


    public static final long MEDIA_START_POSITION = 18;
    private static final long BABBLE_START_TIME = 24740;
    private static final long BABBLE_END_TIME = 28900;
    private static final long BABBLE_APPEARANCE_INTERVAL = 3000;
    private static final long PLAYER_INVALID_POSITION = -1;


    private PlayerControlsController mPlayerControlsController;
    private boolean mInvokeBabbleListener;
    private BabbleState mBabblePlayingState;
    PlayerControlsView mPlayerControlsView;
    private LinearLayout mPlayerView;
    private Timer mTimer;
    private boolean mIsPaused;
    private long mPausedPosition;

    private Player mVideoPlayer;
    private Player mBaseAudioPlayer;
    private Player mSecondAudioPlayer;
    private FrameLayout mPlayerContainer;

    private UpdateProgressTask mUpdateProgressTask;
    private ImageView mBabbleControllerBackground;
    private ImageView mBabbleOriginController;
    private ImageView mBabbleSecondaryController;
    private ImageView mBabbleControllerButton;


    enum BabbleState {
        NONE,
        MAIN_BABBLE,
        SECOND_BABBLE
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIsPaused = false;
        mPausedPosition = 0;
        mInvokeBabbleListener = false;
        mBabblePlayingState = NONE;

        mPlayerView = (LinearLayout) findViewById(R.id.player_view);
        mBabbleControllerBackground = (ImageView) findViewById(R.id.babble_controller_background);
        mBabbleOriginController = (ImageView) findViewById(R.id.babble_original_controller);
        mBabbleSecondaryController = (ImageView) findViewById(R.id.babble_secondary_controller);
        mBabbleControllerButton = (ImageView) findViewById(R.id.babble_controller_button);
        mPlayerContainer = (FrameLayout) findViewById(R.id.player_container);
        mPlayerView = (LinearLayout) findViewById(R.id.player_view);
        mPlayerControlsView = (PlayerControlsView) findViewById(R.id.player_controls_view);

        mPlayerControlsController = new PlayerControlsController(mPlayerControlsView);
        mPlayerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayerControlsController.handleContainerClick();
            }
        });

        mBabbleControllerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                handleBabbleControllerClick();
            }

        });


        mBabbleControllerBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBabbleControllerClick();
            }
        });


        mBabbleOriginController.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                playBabble(MAIN_BABBLE);
            }
        });


        mBabbleSecondaryController.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                playBabble(SECOND_BABBLE);
            }
        });



        initPLayers();
        setUpdateProgressTask(true);

    }


    private void initPLayers() {


        PlayerProvider playerProvider = new PlayerProvider();
        playerProvider.getPlayer("wait_to_be_king", MainActivity.this, new PlayerProvider.OnPlayerReadyListener() {


            @Override
            public void onPlayerReady(Player videoPlayer, Player baseAudioPlayer, Player secondAudioPlayer) {

                if (videoPlayer == null || baseAudioPlayer == null || secondAudioPlayer == null) {
                    Log.v(MainActivity.TAG, "error");
                    return;
                }


                mVideoPlayer = videoPlayer;
                mBaseAudioPlayer = baseAudioPlayer;
                mSecondAudioPlayer = secondAudioPlayer;

                mPlayerView.removeAllViews();
                mPlayerView.addView(mVideoPlayer.getView());


                mVideoPlayer.play();
                mBaseAudioPlayer.play();
                mSecondAudioPlayer.seekTo(BABBLE_START_TIME);


                mPlayerControlsController.setPlayer(mVideoPlayer, mBaseAudioPlayer);
                mPlayerControlsView.setVisibility(View.VISIBLE);

            }


        });

    }


    @Override
    public void onResume() {

        super.onResume();

        if (mPlayerControlsController != null) {
            mPlayerControlsController.onApplicationResumed();
        }

    }


    @Override
    public void onPause() {

        super.onPause();

        if (mPlayerControlsController != null) {
            mPlayerControlsController.onApplicationPaused();
        }

    }


    private void playBabble(BabbleState babblePlayingState) {


        switch (babblePlayingState) {

            case MAIN_BABBLE:
                mBaseAudioPlayer.play();
                break;

            case SECOND_BABBLE:
                mSecondAudioPlayer.play();
                break;
        }


        mBabblePlayingState = babblePlayingState;
        toggleBabblesClickable(false);

    }


    private void babbleListener(long position) {

        boolean endBabble = Math.abs(BABBLE_END_TIME - position) < 10;

        if (endBabble) {

            switch (mBabblePlayingState) {

                case MAIN_BABBLE:
                    mBaseAudioPlayer.pause();
                    mBaseAudioPlayer.seekTo(BABBLE_START_TIME);
                    break;

                case SECOND_BABBLE:
                    mSecondAudioPlayer.pause();
                    mSecondAudioPlayer.seekTo(BABBLE_START_TIME);
                    break;
            }

            toggleBabblesClickable(true);
        }
    }


    private void toggleBabblesClickable(boolean isClickable) {
        mBabbleOriginController.setClickable(isClickable);
        mBabbleSecondaryController.setClickable(isClickable);
        mBabbleControllerButton.setClickable(isClickable);
        mBabbleControllerBackground.setClickable(isClickable);
    }



    private void setBabbles() {

        mBaseAudioPlayer.setVolume(1);

        mBaseAudioPlayer.seekTo(BABBLE_START_TIME);
        mSecondAudioPlayer.seekTo(BABBLE_START_TIME);

        mBaseAudioPlayer.pause();
        mSecondAudioPlayer.pause();

    }


    private void handleBabbleControllerClick() {

        if (mIsPaused) { // if pause - we continue to play

            mBaseAudioPlayer.pause();
            mBaseAudioPlayer.seekTo(mPausedPosition);
            mBaseAudioPlayer.addEventListener(new PKEvent.Listener() {

                @Override
                public void onEvent(PKEvent event) {

                    mBaseAudioPlayer.setVolume(1);
                    mVideoPlayer.play();
                    mBaseAudioPlayer.play();

                    mInvokeBabbleListener = false;

                    //mSecondAudioPlayer.seekTo(NEXT_SWTICH);

                }

            }, PlayerEvent.Type.SEEKED);


            setBabbleController(false, false);
            mBabbleOriginController.setVisibility(View.INVISIBLE);
            mBabbleSecondaryController.setVisibility(View.INVISIBLE);


        } else { // if playing - we pause

            mVideoPlayer.pause();
            mBaseAudioPlayer.pause();
            mSecondAudioPlayer.pause();

            mPausedPosition = mBaseAudioPlayer.getCurrentPosition();

            mInvokeBabbleListener = true;
            setBabbles();

            setBabbleController(true, true);
            mBabbleOriginController.setVisibility(View.VISIBLE);
            mBabbleSecondaryController.setVisibility(View.VISIBLE);

        }

        mIsPaused = !mIsPaused;

    }


    private void setBabbleController(boolean toShow, boolean play) {

        if (play) {

            if (toShow) {
                mBabbleControllerBackground.setVisibility(View.VISIBLE);
                mBabbleControllerButton.setVisibility(View.INVISIBLE);

            } else {
                mBabbleControllerBackground.setVisibility(View.INVISIBLE);
                mBabbleControllerButton.setVisibility(View.INVISIBLE);
            }


        } else {

            mBabbleControllerButton.setImageResource(R.drawable.pumbaa);
            mBabbleControllerBackground.setVisibility(toShow ? View.VISIBLE : View.INVISIBLE);
            mBabbleControllerButton.setVisibility(toShow ? View.VISIBLE : View.INVISIBLE);

        }

    }



    private void switchAudio(long position) {

        boolean enterBabble = Math.abs(BABBLE_START_TIME - BABBLE_APPEARANCE_INTERVAL - position) < 10;
        boolean startSwitch = Math.abs(BABBLE_START_TIME - position) < 10;
        boolean endSwitch = Math.abs(BABBLE_END_TIME - position) < 10;
        boolean exitBabble = Math.abs(BABBLE_END_TIME + BABBLE_APPEARANCE_INTERVAL - position) < 10;


        if (enterBabble) {
            setBabbleController(true, false);
        }


        if (startSwitch) {

            mBaseAudioPlayer.setVolume(0);
            mSecondAudioPlayer.play();

            Log.v(MainActivity.TAG, "position " + position + " startSwitch = " + startSwitch + " endSwitch = " + endSwitch);
        }


        if (endSwitch) {

            mSecondAudioPlayer.pause();
            mBaseAudioPlayer.setVolume(1);

            Log.v(MainActivity.TAG, "position " + position + " startSwitch = " + startSwitch + " endSwitch = " + endSwitch);
        }


        if (exitBabble) {
            setBabbleController(false, false);
        }

    }




    private void setUpdateProgressTask(boolean startTracking) {

        if (startTracking) {


            if (mTimer == null) { // init only once
                mTimer = new Timer();
                mUpdateProgressTask = new UpdateProgressTask();
                mTimer.schedule(mUpdateProgressTask, 0, 5);
            }


        } else { // pause timer

            if (mTimer != null) {
                mTimer.cancel();
                mTimer.purge();
                mTimer = null;
                mUpdateProgressTask = null;
            }
        }
    }



    private class UpdateProgressTask extends TimerTask {

        @Override
        public void run() {

            if (mPlayerContainer != null) {

                mPlayerContainer.post(new Runnable() {

                    @Override
                    public void run() {

                        long position;

                        if (mVideoPlayer != null && mBaseAudioPlayer != null && mSecondAudioPlayer != null) {

                            if (mInvokeBabbleListener) {

                                switch (mBabblePlayingState) {

                                    case MAIN_BABBLE:
                                        position = mBaseAudioPlayer.getCurrentPosition();
                                        break;

                                    case SECOND_BABBLE:
                                        position = mSecondAudioPlayer.getCurrentPosition();
                                        break;

                                    default:
                                        position = PLAYER_INVALID_POSITION;
                                        break;
                                }

                                if (position != PLAYER_INVALID_POSITION) {
                                    babbleListener(position);
                                }


                            } else {

                                position = mVideoPlayer.getCurrentPosition();
                                switchAudio(position);

                            }
                        }



                    }
                });
            }
        }
    }



}
