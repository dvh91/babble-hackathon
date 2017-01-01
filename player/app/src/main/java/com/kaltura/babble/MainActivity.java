package com.kaltura.babble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.kaltura.playkit.Player;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "BABBLE_LOG";


    public static final long START_POSITION = 18;
    private static final long START_TIME = 24740;
    private static final long END_TIME = 28900;



    private Timer mTimer;

    private LinearLayout mPlayerContainer;
    private Player mBaseLanguagePlayer;
    private Player mSecondLanguagePlayer;

    private UpdateProgressTask mUpdateProgressTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayerContainer = (LinearLayout) findViewById(R.id.player_view);

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
                mSecondLanguagePlayer.play();
                mSecondLanguagePlayer.setVolume(0);

            }


        });

    }



    private void playBabble(long position) {


        boolean first = Math.abs(START_TIME - position) < 10;
        boolean second = Math.abs(END_TIME - position) < 10;


        //Log.v(MainActivity.TAG, "position " + position);


        if (first) {
            mBaseLanguagePlayer.setVolume(0);
            mSecondLanguagePlayer.setVolume(1);

            Log.v(MainActivity.TAG, "position " + position + " first = " + first + " second = " + second);
        }


        if (second) {
            mBaseLanguagePlayer.setVolume(1);
            mSecondLanguagePlayer.setVolume(0);

            Log.v(MainActivity.TAG, "position " + position + " first = " + first + " second = " + second);
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

                        if (mBaseLanguagePlayer != null) {
                            long position = mBaseLanguagePlayer.getCurrentPosition();
                            playBabble(position);
                            //Log.v(MainActivity.TAG, "position " + position);
                        }

                    }
                });
            }
        }
    }



}
