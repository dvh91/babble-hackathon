package com.kaltura.babble;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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


    //public static final long MEDIA_START_POSITION = 18;
    //private static final long BABBLE_START_TIME = 24740;
    //private static final long BABBLE_END_TIME = 28900;

    public static final long MEDIA_START_POSITION = 30;
    private static final long BABBLE_START_TIME = 49070;
    private static final long BABBLE_END_TIME = 49500;
    private static final long BABBLE_APPEARANCE_INTERVAL = 3000;
    private static final long BABBLE_EXIT_INTERVAL = 9000;
    private static final long PLAYER_INVALID_POSITION = -1;
    private static final long LONG_EQUALS = 15;

    private static final String BABBLE_ORIGIN_PHRASE = "Thanks!";
    private static final String BABBLE_SECONDARY_PHRASE = "Merci!";


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
    private ImageView mBabbleOriginController;
    private ImageView mBabbleSecondaryController;
    private ImageView mBabbleControllerButton;
    private ImageView mBabbleProtection;
    private ImageView mBabbleRipple;
    private ImageView mBabbleMask;
    private TextView mOriginalControllerBabbleTriangle;
    private TextView mSecondControllerBabbleTriangle;
    private TextView mControllerBabbleTriangle;
    private AnimatorSet mBabbleRippleAnimator;


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
        mBabbleOriginController = (ImageView) findViewById(R.id.babble_original_controller);
        mBabbleSecondaryController = (ImageView) findViewById(R.id.babble_secondary_controller);
        mBabbleControllerButton = (ImageView) findViewById(R.id.babble_controller_button);
        mPlayerContainer = (FrameLayout) findViewById(R.id.player_container);
        mPlayerView = (LinearLayout) findViewById(R.id.player_view);
        mPlayerControlsView = (PlayerControlsView) findViewById(R.id.player_controls_view);
        mBabbleProtection = (ImageView) findViewById(R.id.babble_protection);
        mBabbleRipple = (ImageView) findViewById(R.id.babble_ripple);
        mBabbleMask = (ImageView) findViewById(R.id.babble_mask);
        mOriginalControllerBabbleTriangle = (TextView) findViewById(R.id.babble_original_triangle);
        mSecondControllerBabbleTriangle = (TextView) findViewById(R.id.babble_secondary_triangle);
        mControllerBabbleTriangle = (TextView) findViewById(R.id.babble_controller_triangle);

        mPlayerControlsController = new PlayerControlsController(mPlayerControlsView);
        mPlayerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mInvokeBabbleListener) {
                    mPlayerControlsController.handleContainerClick();
                }

            }
        });

        mBabbleControllerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                handleBabbleControllerClick();
            }

        });



        mBabbleOriginController.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                handleBabbleClick(mBabbleOriginController, MAIN_BABBLE);
            }
        });


        mBabbleSecondaryController.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                handleBabbleClick(mBabbleSecondaryController, SECOND_BABBLE);
            }
        });



        setBabbleRippleAnimation();

        initPLayers();
        setUpdateProgressTask(true);

    }


    private void handleBabbleClick(final ImageView imageView, BabbleState babblePlayingState) {

        buttonClickAnimation(imageView);
        rectangleAnimation(babblePlayingState);

        playBabble(babblePlayingState);

    }


    private void buttonClickAnimation(final View view) {

        float scalingFactor = 1.25f;
        view.setScaleX(scalingFactor);
        view.setScaleY(scalingFactor);

        view.postDelayed(new Runnable() {

            @Override
            public void run() {

                float scalingFactor = 1.0f;
                view.setScaleX(scalingFactor);
                view.setScaleY(scalingFactor);

            }

        }, 400);
    }



    private void initPLayers() {


        PlayerProvider playerProvider = new PlayerProvider();
        playerProvider.getPlayer("hakona_matata", MainActivity.this, new PlayerProvider.OnPlayerReadyListener() {


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


                mVideoPlayer.addEventListener(new PKEvent.Listener() {

                    @Override
                    public void onEvent(PKEvent event) {

                        mVideoPlayer.play();
                        mBaseAudioPlayer.play();

                    }

                }, PlayerEvent.Type.CAN_PLAY);



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


    private void setBabbleRippleAnimation() {

        ObjectAnimator animX = ObjectAnimator.ofFloat(mBabbleRipple, "scaleX", 1f, 1.5f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(mBabbleRipple, "scaleY", 1f, 1.5f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mBabbleRipple, "alpha", 1f, 0f);

        setRepeat(animX);
        setRepeat(animY);
        setRepeat(alpha);

        mBabbleRippleAnimator = new AnimatorSet();

        mBabbleRippleAnimator.playTogether(animX, animY, alpha);
        mBabbleRippleAnimator.setDuration(700);

    }


    private void babbleFlingAnimation() {

        AnimatorSet flingSet = new AnimatorSet();


        AnimatorSet upSet = new AnimatorSet();
        ObjectAnimator animUp = ObjectAnimator.ofFloat(mBabbleMask, "translationY", 100f, 0f);
        ObjectAnimator avatarTransparent = ObjectAnimator.ofFloat(mBabbleControllerButton, "alpha", 0f, 0f);
        upSet.playTogether(animUp, avatarTransparent);
        upSet.setDuration(600);


        AnimatorSet scale = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mBabbleMask, "scaleX", 1f, 3f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mBabbleMask, "scaleY", 1f, 3f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mBabbleMask, "alpha", 1f, 0f);

        ObjectAnimator avatarX = ObjectAnimator.ofFloat(mBabbleControllerButton, "scaleX", 0.3f, 1f);
        ObjectAnimator avatarY = ObjectAnimator.ofFloat(mBabbleControllerButton, "scaleY", 0.3f, 1f);
        ObjectAnimator avatarAlpha = ObjectAnimator.ofFloat(mBabbleControllerButton, "alpha", 0f, 1f);

        scale.playTogether(scaleX, scaleY, alpha, avatarX, avatarY, avatarAlpha);
        scale.setDuration(500);

        flingSet.play(upSet).before(scale);

        mBabbleMask.setVisibility(View.VISIBLE);
        setBabbleController(true, false);

        flingSet.start();


        flingSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                mBabbleMask.setVisibility(View.INVISIBLE);

                babbleRippleAnimation(true);

            }

        });

    }

    private void babbleRippleAnimation(boolean toShow) {

        if (toShow) {

            mBabbleRippleAnimator.start();
            mBabbleRipple.setVisibility(View.VISIBLE);

        } else  {

            mBabbleRipple.setVisibility(View.INVISIBLE);
            mBabbleRippleAnimator.cancel();

        }

    }


    private void setRepeat(ObjectAnimator animator) {
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
    }


    private void rectangleAnimation(BabbleState babbleState) {

        final TextView view;
        final String phrase;


        switch (babbleState) {

            case MAIN_BABBLE:
                mSecondControllerBabbleTriangle.setVisibility(View.INVISIBLE);
                view = mOriginalControllerBabbleTriangle;
                phrase = BABBLE_ORIGIN_PHRASE;
                break;

            case SECOND_BABBLE:
                mOriginalControllerBabbleTriangle.setVisibility(View.INVISIBLE);
                view = mSecondControllerBabbleTriangle;
                phrase = BABBLE_SECONDARY_PHRASE;
                break;

            default: // main babble controller
                view = mControllerBabbleTriangle;
                phrase = BABBLE_SECONDARY_PHRASE;
                break;

        }


        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        anim.setDuration(150);
        view.setText("");
        view.setVisibility(View.VISIBLE);
        anim.start();

        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                view.setText(phrase);

                view.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        view.setText("");
                        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
                        anim.setDuration(150);
                        anim.start();

                    }

                }, 2000);

            }

        });

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

        boolean endBabble = Math.abs(BABBLE_END_TIME - position) < LONG_EQUALS;

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
            mBabbleProtection.setVisibility(View.INVISIBLE);
            mBabbleOriginController.setVisibility(View.INVISIBLE);
            mBabbleSecondaryController.setVisibility(View.INVISIBLE);
            mOriginalControllerBabbleTriangle.setVisibility(View.INVISIBLE);
            mSecondControllerBabbleTriangle.setVisibility(View.INVISIBLE);


        } else { // if playing - we pause

            mVideoPlayer.pause();
            mBaseAudioPlayer.pause();
            mSecondAudioPlayer.pause();

            mPausedPosition = mBaseAudioPlayer.getCurrentPosition();

            mInvokeBabbleListener = true;

            setBabbles();
            babbleRippleAnimation(false);

            setBabbleController(true, true);
            mBabbleProtection.setVisibility(View.VISIBLE);
            mBabbleOriginController.setVisibility(View.VISIBLE);
            mBabbleSecondaryController.setVisibility(View.VISIBLE);
            mControllerBabbleTriangle.setVisibility(View.INVISIBLE);

        }

        mIsPaused = !mIsPaused;

    }


    private void setBabbleController(boolean toShow, boolean play) {

        if (play) {
            mBabbleControllerButton.setImageResource(R.drawable.play_button);
        } else {
            mBabbleControllerButton.setImageResource(R.drawable.pumbaa);
        }

        mBabbleControllerButton.setVisibility(toShow ? View.VISIBLE : View.INVISIBLE);
    }



    private void switchAudio(long position) {

        boolean enterBabble = Math.abs(BABBLE_START_TIME - BABBLE_APPEARANCE_INTERVAL - position) < LONG_EQUALS;
        boolean startSwitch = Math.abs(BABBLE_START_TIME - position) < LONG_EQUALS;
        boolean endSwitch = Math.abs(BABBLE_END_TIME - position) < LONG_EQUALS;
        boolean exitBabble = Math.abs(BABBLE_END_TIME + BABBLE_EXIT_INTERVAL - position) < LONG_EQUALS;


        if (enterBabble) {
            babbleFlingAnimation();
        }


        if (startSwitch) {

            rectangleAnimation(NONE); // NONE is main babble controller

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
            babbleRippleAnimation(false);
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
