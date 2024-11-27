package com.dungeonmaster.menu;

import android.app.Activity;
import android.content.Intent;

import com.dungeonmaster.LoadingScreen;
import com.error.NoConnection;
import com.error.NoLoginInfo;
import com.error.UserUnauthorized;
import com.serverconnection.Reconnect;
import com.serverconnection.Server;
import com.serverconnection.model.User;

import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.dungeonmaster.R;
import com.threed.jpct.Logger;

public class DataSynchronizer implements Runnable {

    private Activity loadingScreen;

    public DataSynchronizer(LoadingScreen loadingScreen) {
        this.loadingScreen = loadingScreen;
    }

    @Override
    public void run() {
        Intent nextScreen = new Intent(loadingScreen, MainMenu.class);
        int firstAnim = R.anim.slide_in_right;
        int secondAnim = R.anim.slide_out_left;

        try {
            new Server(loadingScreen);
        } catch (UserUnauthorized | NoLoginInfo unauthorized) {
            nextScreen = new Intent(loadingScreen, SignIn.class);
            firstAnim = R.anim.slide_in_left;
            secondAnim = R.anim.slide_out_right;
        } catch (NoConnection | IOException e) {
            nextScreen = new Intent(loadingScreen, MainMenu.class);
            firstAnim = R.anim.slide_in_right;
            secondAnim = R.anim.slide_out_left;
            Thread subThread = new Thread(new Reconnect(loadingScreen));
            subThread.start();
        }

        loadingScreen.startActivity(nextScreen);
        loadingScreen.overridePendingTransition(firstAnim, secondAnim);
    }
}
