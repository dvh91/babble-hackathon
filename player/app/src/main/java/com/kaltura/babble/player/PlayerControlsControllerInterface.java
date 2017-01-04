package com.kaltura.babble.player;

import com.kaltura.playkit.Player;
import com.kaltura.playkit.PlayerConfig;

/**
 * Created by itanbarpeled on 26/11/2016.
 */

public interface PlayerControlsControllerInterface {

    void handleScreenOrientationChange(boolean setFullScreen);

    void onApplicationResumed();

    void onApplicationPaused();

    void setPlayer(Player videoPlayer, Player baseAudioPlayer);

    void handleContainerClick();

}
