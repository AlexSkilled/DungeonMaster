package com.serverconnection;

import android.app.Activity;

public class Reconnect implements Runnable {

    private Activity activity;

    public Reconnect(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(15000);
                new Server(activity);
                break;
            } catch (Exception e){

            }
        }

    }
}
