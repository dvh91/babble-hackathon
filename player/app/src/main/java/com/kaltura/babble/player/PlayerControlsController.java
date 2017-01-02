package com.kaltura.babble.player;

import android.os.Handler;

import com.kaltura.babble.R;
import com.kaltura.playkit.PKEvent;
import com.kaltura.playkit.PKLog;
import com.kaltura.playkit.Player;
import com.kaltura.playkit.PlayerEvent;
import com.kaltura.playkit.plugins.ads.AdEvent;
import com.kaltura.playkit.plugins.ads.AdInfo;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.kaltura.playkit.PlayerEvent.Type.CAN_PLAY;
import static com.kaltura.playkit.PlayerEvent.Type.ENDED;
import static com.kaltura.playkit.PlayerEvent.Type.PAUSE;
import static com.kaltura.playkit.PlayerEvent.Type.PLAYING;
import static com.kaltura.playkit.PlayerEvent.Type.SEEKING;
import static com.kaltura.playkit.PlayerEvent.Type.TRACKS_AVAILABLE;

/**
 * Created by itanbarpeled on 26/11/2016.
 */

public class PlayerControlsController implements PlayerControlsControllerInterface, PlayerControlsView.PlayerControlsEvents {

    private static final PKLog log = PKLog.get("PlayerControls");

    private static final int UPDATE_TIME_INTERVAL = 300;
    private static final int PROGRESS_BAR_MAX = 100;
    private static final int REMOVE_CONTROLS_TIMEOUT = 5000;

    private static final int FIFTEEN_MIN = 15 * 60 * 1000;
    private static final int FIFTEEN_SEC = 15 * 1000;

    private Player mVideoPlayer;
    private Player mBaseAudioPlayer;
    private Enum mPlayerState;
    private PlayerControlsView mPlayerControlsView;


    private Timer mTimer;
    private UpdateProgressTask mUpdateProgressTask;
    private Formatter formatter;
    private StringBuilder formatBuilder;

    private boolean mIsDragging;
    private boolean isAdDisplayed;
    private List<Long> adCuePoints;
    private AdInfo adInfo;
    private boolean allAdsCompeted;
    private boolean isAutoPlay;


    public PlayerControlsController(PlayerControlsView controlsView) {

        mPlayerControlsView = controlsView;

        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        mIsDragging = false;

        mPlayerControlsView.setControlsClickListener(this);
        setControlsView(false);

    }



    private void setUpdateProgressTask(boolean startTracking) {

        if (startTracking) {

            if (mVideoPlayer == null || mBaseAudioPlayer == null) { // don't start timer before player is ready
                return;
            }

            if (mTimer == null) { // init only once
                mTimer = new Timer();
                mUpdateProgressTask = new UpdateProgressTask();
                mTimer.schedule(mUpdateProgressTask, 0, UPDATE_TIME_INTERVAL);
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



    @Override
    public void setPlayer(Player videoPlayer, Player baseAudioPlayer) {

        mVideoPlayer = videoPlayer;
        mBaseAudioPlayer = baseAudioPlayer;

        mPlayerState = null; // init player state
        setPlayerListeners();
    }





    @Override
    public void handleContainerClick() {

        if (mPlayerState == null) {
            return;
        }

        boolean isControlsVisible = mPlayerControlsView.getControlsVisibility();
        log.v("handleContainerClick mPlayerState = " + mPlayerState + " isControlsVisible = " + isControlsVisible);
        // toggle visibility

        mPlayerControlsView.setControlsVisibility(!isControlsVisible);


        mPlayerControlsView.setSeekBarVisibility(true);


        if (isControlsVisible) {

            mPlayerControlsView.setPlayPauseVisibility(false, false);

        } else {

            if (mVideoPlayer.isPlaying()) {
                mPlayerControlsView.setPlayPauseVisibility(true, false);
            } else {
                mPlayerControlsView.setPlayPauseVisibility(true, true);
            }
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPlayerState == PLAYING || mPlayerState == AdEvent.Type.STARTED || mPlayerState == AdEvent.Type.RESUMED) {
                    mPlayerControlsView.setControlsVisibility(false);
                    mPlayerControlsView.setPlayPauseVisibility(false, false);
                }
            }
        }, REMOVE_CONTROLS_TIMEOUT);
    }

    private boolean isLiveAndNoDVR() {
        return false;
    }

    private boolean isLiveAndDVR() {
        return false;
    }

    @Override
    public void handleScreenOrientationChange(boolean setFullScreen) {

        mPlayerControlsView.setBackButtonVisibility(setFullScreen);
        mPlayerControlsView.setScreenSizeButtonVisibility(!setFullScreen);

    }

    public boolean isAutoPlay() {
        return isAutoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
    }

    private void updateProgress() {

        long duration = 0;
        long position = 0;
        long bufferedPosition = 0;

        if (mVideoPlayer != null) {
            duration = (mVideoPlayer.getDuration() > 0) ? mVideoPlayer.getDuration() : duration;
            position = mVideoPlayer.getCurrentPosition();
            bufferedPosition = mVideoPlayer.getBufferedPosition();
        }


        if (!mIsDragging) {
            setTimeIndicator(position, duration);
        }

        if (!mIsDragging) {
            mPlayerControlsView.setSeekBarProgress(progressBarValue(duration, position));
        }

        mPlayerControlsView.setSeekBarSecondaryProgress(progressBarValue(duration, bufferedPosition));

    }

    private void setTimeIndicator(long position, long duration) {

        String timeSeparator;
        StringBuilder timeIndicator = new StringBuilder();
        timeSeparator =  mPlayerControlsView.getContext().getString(R.string.time_indicator_separator);

        timeIndicator.append(stringForTime(position));
        timeIndicator.append(timeSeparator);
        timeIndicator.append(stringForTime(duration));

        mPlayerControlsView.setTimeIndicator(timeIndicator.toString());

    }


    private int progressBarValue(long duration, long position) {

        if (duration > 0) {
            return (int) ((position * PROGRESS_BAR_MAX) / duration);
        } else {
            return 0;
        }

    }


    private String stringForTime(long timeMs) {

        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        return hours > 0 ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
                : formatter.format("%02d:%02d", minutes, seconds).toString();
    }


    private long positionValue(long progress) {
        long positionValue = 0;

        if (mVideoPlayer != null) {
            long duration = mVideoPlayer.getDuration();
            positionValue = (duration * progress) / PROGRESS_BAR_MAX;
        }

        return positionValue;
    }

    @Override
    public void onApplicationResumed() {
        setUpdateProgressTask(true);
    }

    private boolean isPlaybackEndedState() {
        return mPlayerState == ENDED || (allAdsCompeted && isPostrollAvailableInAdCuePoint());
    }

    @Override
    public void onApplicationPaused() {
        setUpdateProgressTask(false);
        if (isAdDisplayed) {
            hideControls();
        }
    }


    public void destroyPlayer() {

        setControlsView(false);
        setUpdateProgressTask(false);



    }


    private void togglePlayPauseReplay() {

        log.v("togglePlayPause mPlayerState = " + mPlayerState);

        if (isPlaybackEndedState()) {

            hideControls();
            mVideoPlayer.seekTo(0);
            mBaseAudioPlayer.seekTo(0);
            mVideoPlayer.play();
            mBaseAudioPlayer.play();
            cleanAdData();

        } else if (mPlayerState == PLAYING || mPlayerState == AdEvent.Type.STARTED || mPlayerState == AdEvent.Type.RESUMED) {

            setControlsView(true);
            mVideoPlayer.pause();
            mBaseAudioPlayer.pause();

        } else if (mPlayerState == CAN_PLAY || mPlayerState == PAUSE || mPlayerState == AdEvent.Type.PAUSED) {

            setControlsView(false);
            mVideoPlayer.play();
            mBaseAudioPlayer.play();

        }
    }

    private void cleanAdData() {
        allAdsCompeted = false;
        adCuePoints = null;
        adInfo = null;
    }

    private void setControlsView(boolean showPlayer) {
        log.v("setControlsView showPlayer = " + showPlayer);
        if (showPlayer) {

            mPlayerControlsView.setPlayPauseVisibility(true, true);
            mPlayerControlsView.setProgressBarVisibility(false);

        } else {

            mPlayerControlsView.setControlsVisibility(false);
            mPlayerControlsView.setPlayPauseVisibility(false, false);
        }

    }

    private void setPlayerListeners() {

        mVideoPlayer.addEventListener(new PKEvent.Listener() {

                                     @Override
                                     public void onEvent(PKEvent event) {
                                         log.v("addEventListener " + event.eventType() + " mPlayerState = " + mPlayerState);

                                         Enum receivedEventType = event.eventType();

                                         if (receivedEventType == CAN_PLAY) {
                                             setUpdateProgressTask(true);
                                         } else if (receivedEventType == TRACKS_AVAILABLE) {
                                         } else if (receivedEventType == PLAYING) {
                                             if (mVideoPlayer.getCurrentPosition() >= mVideoPlayer.getDuration()) {
                                                 showControlsWithPlay();
                                             } else {
                                                 setControlsView(false);
                                             }
                                         } else if (receivedEventType == ENDED) {
                                             if (!isPostrollAvailableInAdCuePoint()) {
                                                 showControlsWithReplay();
                                             }
                                         } else if (receivedEventType == AdEvent.Type.CUEPOINTS_CHANGED) {
                                             adCuePoints =  ((AdEvent.AdCuePointsUpdateEvent)event).cuePoints;

                                         } else if (receivedEventType == AdEvent.Type.ALL_ADS_COMPLETED) {
                                             isAdDisplayed = false;
                                             allAdsCompeted = true;
                                             if (mVideoPlayer.getCurrentPosition() >= mVideoPlayer.getDuration()) {
                                                 if (isPostrollAvailableInAdCuePoint()) {
                                                     showControlsWithReplay();
                                                 }
                                             }
                                         } else if (receivedEventType == AdEvent.Type.CONTENT_PAUSE_REQUESTED) {
                                             mPlayerControlsView.setProgressBarVisibility(true);
                                             setControlsView(false);
                                         } else if (receivedEventType == AdEvent.Type.STARTED) {
                                             adInfo = ((AdEvent.AdStartedEvent)event).adInfo;

                                             isAdDisplayed = true;
                                             allAdsCompeted = false;
                                             mPlayerControlsView.setProgressBarVisibility(false);
                                             mPlayerControlsView.setSeekBarMode(false);
                                             setControlsView(false);
                                         } else if (receivedEventType == AdEvent.Type.TAPPED) {
                                             handleContainerClick();
                                         } else if (receivedEventType == AdEvent.Type.COMPLETED) {
                                             mPlayerControlsView.setSeekBarMode(true);
                                             isAdDisplayed = false;
                                             if (adInfo != null) {
                                                 if (adInfo.getAdPodPosition() ==  adInfo.getAdPodCount()) {
                                                     adInfo = null;
                                                 }
                                             }
                                         }  else if (receivedEventType == AdEvent.Type.SKIPPED) {
                                             mPlayerControlsView.setSeekBarMode(true);
                                         }

                                         if (event instanceof PlayerEvent || receivedEventType == AdEvent.Type.PAUSED || receivedEventType == AdEvent.Type.RESUMED ||
                                                 receivedEventType == AdEvent.Type.STARTED || (receivedEventType == AdEvent.Type.ALL_ADS_COMPLETED && isPostrollAvailableInAdCuePoint())) {
                                             mPlayerState = event.eventType();
                                         }
                                     }
                                 }, PlayerEvent.Type.PLAY, PlayerEvent.Type.PAUSE, CAN_PLAY, PlayerEvent.Type.SEEKING, PlayerEvent.Type.SEEKED, PlayerEvent.Type.PLAYING,  PlayerEvent.Type.ENDED, PlayerEvent.Type.TRACKS_AVAILABLE,

                AdEvent.Type.LOADED, AdEvent.Type.SKIPPED, AdEvent.Type.TAPPED, AdEvent.Type.CONTENT_PAUSE_REQUESTED, AdEvent.Type.CONTENT_RESUME_REQUESTED, AdEvent.Type.STARTED, AdEvent.Type.PAUSED, AdEvent.Type.RESUMED,
                AdEvent.Type.COMPLETED, AdEvent.Type.ALL_ADS_COMPLETED,AdEvent.Type.CUEPOINTS_CHANGED);


        mVideoPlayer.addStateChangeListener(new PKEvent.Listener() {
            @Override
            public void onEvent(PKEvent event) {

                PlayerEvent.StateChanged stateChanged = (PlayerEvent.StateChanged) event;
                log.v("addStateChangeListener " + event.eventType() + " = " + stateChanged.newState);
                switch (stateChanged.newState){
                    case IDLE:
                        log.d("StateChange Idle");
                        //TEMP untill no IDLE fired when not needed
                        mPlayerControlsView.setProgressBarVisibility(false);
                        break;
                    case LOADING:
                        log.d("StateChange Loading");
                        break;
                    case READY:
                        log.d("StateChange Ready");
                        mPlayerControlsView.setProgressBarVisibility(false);
                        break;
                    case BUFFERING:
                        log.e("StateChange Buffering");
                        mPlayerControlsView.setProgressBarVisibility(true);
                        break;
                }

            }
        });

    }


    @Override
    public void onControlsEvent(ControlsEvent controlsEvent) {

        ControlsEvent.ButtonClickEvent buttonClickEvent = controlsEvent.getButtonClickEvent();

        switch (buttonClickEvent) {

            case SELECT_TRACKS_DIALOG:
                //mPlayerControlsView.toggleControlsVisibility(false);
                break;

            case BACK_BUTTON:
                break;

            case FULL_SCREEN_SIZE:
                break;

            case PLAY_PAUSE:
                togglePlayPauseReplay();
                break;

            case DRAGG_STARTED:
            case DRAGGING:
            case DRAGG_ENDED:
                handleScrubBarDragging(buttonClickEvent, controlsEvent.getPosition());
                break;
        }
    }



    private void handleScrubBarDragging(ControlsEvent.ButtonClickEvent buttonClickEvent, long position) {

        switch (buttonClickEvent) {

            case DRAGG_STARTED:
                mIsDragging = true;
                break;

            case DRAGGING:
                setTimeIndicator(positionValue(position), mVideoPlayer.getDuration());
                break;

            case DRAGG_ENDED:
                mIsDragging = false;
                seek(positionValue(position));
                break;
        }
    }

    private void seek(long position) {
        if (mVideoPlayer != null && mBaseAudioPlayer != null) {
            mVideoPlayer.seekTo(position);
            mBaseAudioPlayer.seekTo(position);
        }
    }

    private void showMessage(int string) {


    }

    private void showControlsWithPlay() {
        log.v("showControlsWithPlay");
        mPlayerControlsView.setPlayPauseVisibility(true, true);
        mPlayerControlsView.setProgressBarVisibility(false);
        mPlayerControlsView.setControlsVisibility(true);
    }

    private void showControlsWithPause() {
        log.v("showControlsWithPause");
        mPlayerControlsView.setPlayPauseVisibility(true, true);
        mPlayerControlsView.setProgressBarVisibility(false);
        mPlayerControlsView.setControlsVisibility(true);

    }

    private void showControlsWithReplay() {
        log.v("showControlsWithPause");
        mPlayerControlsView.setPlayPauseVisibility(true, true, true);
        mPlayerControlsView.setProgressBarVisibility(false);
        mPlayerControlsView.setControlsVisibility(true);
    }

    void hideControls() {
        log.v("hideControls");
        mPlayerControlsView.setControlsVisibility(false);
        mPlayerControlsView.setPlayPauseVisibility(false, false);
    }

    private class UpdateProgressTask extends TimerTask {

        @Override
        public void run() {

            if (mPlayerControlsView != null) {

                mPlayerControlsView.post(new Runnable() {

                    @Override
                    public void run() {
                        updateProgress();
                    }
                });
            }
        }
    }


    private boolean isPostrollAvailableInAdCuePoint() {
        if (adCuePoints != null && adCuePoints.size() > 0) {
            if (adCuePoints.get(adCuePoints.size() -1) < 0) {
                return true;
            }
        }
        return false;
    }


}
