package com.kaltura.babble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import com.kaltura.playkit.Player;

import static com.kaltura.babble.MainActivity.BabbleState.MAIN_BABBLE;
import static com.kaltura.babble.MainActivity.BabbleState.NONE;
import static com.kaltura.babble.MainActivity.BabbleState.SECOND_BABBLE;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "BABBLE_LOG";


    public static final long MEDIA_START_POSITION = 18;
    private static final long BABBLE_START_TIME = 24740;
    private static final long BABBLE_END_TIME = 28900;
    private static final long BABBLE_APPEARANCE_INTERVAL = 3000;
    private static final long PLAYER_INVALID_POSITION = -1;



    private boolean mInvokeBabbleListener;
    private BabbleState mBabblePlayingState;
    private Timer mTimer;
    private boolean mIsPaused;
    private long mPausedPosition;

    private LinearLayout mPlayerContainer;
    private Player mBaseLanguagePlayer;
    private Player mSecondLanguagePlayer;

    private UpdateProgressTask mUpdateProgressTask;
    private ImageView mBabbleControllerBackground;
    private ImageView mBabbleOriginController;
    private ImageView mBabbleSecondaryController;
    private ImageView mBabbleControllerButton;
    private ImageView mAtmosphereImage;


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

        mPlayerContainer = (LinearLayout) findViewById(R.id.player_view);
        mBabbleControllerBackground = (ImageView) findViewById(R.id.babble_controller_background);
        mBabbleOriginController = (ImageView) findViewById(R.id.babble_original_controller);
        mBabbleSecondaryController = (ImageView) findViewById(R.id.babble_secondary_controller);
        mBabbleControllerButton = (ImageView) findViewById(R.id.babble_controller_button);
        mAtmosphereImage = (ImageView) findViewById(R.id.atmosphere_image);


        mBabbleControllerButton.setOnClickListener(new View.OnClickListener() {

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
            public void onPlayerReady(Player basePlayer, Player secondPlayer) {

                if (basePlayer == null || secondPlayer == null) {
                    Log.v(MainActivity.TAG, "error");
                    return;
                }


                mBaseLanguagePlayer = basePlayer;
                mSecondLanguagePlayer = secondPlayer;

                mPlayerContainer.removeAllViews();
                mPlayerContainer.addView(mBaseLanguagePlayer.getView());


                mBaseLanguagePlayer.play();
                mSecondLanguagePlayer.seekTo(BABBLE_START_TIME);


            }


        });

    }


    private void playBabble(BabbleState babblePlayingState) {


        switch (babblePlayingState) {

            case MAIN_BABBLE:
                mBaseLanguagePlayer.play();
                break;

            case SECOND_BABBLE:
                mSecondLanguagePlayer.play();
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
                    mBaseLanguagePlayer.pause();
                    mBaseLanguagePlayer.seekTo(BABBLE_START_TIME);
                    break;

                case SECOND_BABBLE:
                    mSecondLanguagePlayer.pause();
                    mSecondLanguagePlayer.seekTo(BABBLE_START_TIME);
                    break;
            }

            toggleBabblesClickable(true);
        }
    }


    private void toggleBabblesClickable(boolean isClickable) {
        mBabbleOriginController.setClickable(isClickable);
        mBabbleSecondaryController.setClickable(isClickable);
        mBabbleControllerButton.setClickable(isClickable);
    }



    private void setBabbles() {

        mBaseLanguagePlayer.setVolume(1);
        mBaseLanguagePlayer.seekTo(BABBLE_START_TIME);
        mSecondLanguagePlayer.seekTo(BABBLE_START_TIME);

        mBaseLanguagePlayer.pause();
        mSecondLanguagePlayer.pause();

    }


    private void handleBabbleControllerClick() {

        if (mIsPaused) { // if pause - we continue to play

            mBaseLanguagePlayer.seekTo(mPausedPosition);
            mBaseLanguagePlayer.setVolume(1);
            mBaseLanguagePlayer.play();
            //mSecondLanguagePlayer.seekTo(NEXT_SWTICH);

            mInvokeBabbleListener = false;

            setBabbleController(false, false);
            mBabbleOriginController.setVisibility(View.INVISIBLE);
            mBabbleSecondaryController.setVisibility(View.INVISIBLE);
            mAtmosphereImage.setVisibility(View.INVISIBLE);



        } else { // if playing - we pause

            mBaseLanguagePlayer.pause();
            mSecondLanguagePlayer.pause();
            mPausedPosition = mBaseLanguagePlayer.getCurrentPosition();

            mInvokeBabbleListener = true;
            setBabbles();

            setBabbleController(true, true);
            //mAtmosphereImage.setVisibility(View.VISIBLE);
            mBabbleOriginController.setVisibility(View.VISIBLE);
            mBabbleSecondaryController.setVisibility(View.VISIBLE);

        }

        mIsPaused = !mIsPaused;

    }


    private void setBabbleController(boolean toShow, boolean play) {

        if (play) {

            mBabbleControllerButton.setImageResource(R.drawable.play_ic);

        } else {

            mBabbleControllerButton.setImageResource(R.drawable.pumbaa);

        }


        mBabbleControllerBackground.setVisibility(toShow ? View.VISIBLE : View.INVISIBLE);
        mBabbleControllerButton.setVisibility(toShow ? View.VISIBLE : View.INVISIBLE);


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

            mBaseLanguagePlayer.setVolume(0);
            mSecondLanguagePlayer.play();

            Log.v(MainActivity.TAG, "position " + position + " startSwitch = " + startSwitch + " endSwitch = " + endSwitch);
        }


        if (endSwitch) {

            mSecondLanguagePlayer.pause();
            mBaseLanguagePlayer.setVolume(1);

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

                        if (mBaseLanguagePlayer != null && mSecondLanguagePlayer != null) {

                            if (mInvokeBabbleListener) {

                                switch (mBabblePlayingState) {

                                    case MAIN_BABBLE:
                                        position = mBaseLanguagePlayer.getCurrentPosition();
                                        break;

                                    case SECOND_BABBLE:
                                        position = mSecondLanguagePlayer.getCurrentPosition();
                                        break;

                                    default:
                                        position = PLAYER_INVALID_POSITION;
                                        break;
                                }

                                if (position != PLAYER_INVALID_POSITION) {
                                    babbleListener(position);
                                }


                            } else {

                                position = mBaseLanguagePlayer.getCurrentPosition();
                                switchAudio(position);

                            }
                        }

                    }
                });
            }
        }
    }



}
