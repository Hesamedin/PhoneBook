package com.kamalan.phonebook;

import android.app.Application;

import com.flurry.android.FlurryAgent;

/**
 * Created by Hesam Kamalan on 5/4/15
 */
public class MyApplication extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        // init Flurry
        FlurryAgent.init(this, "SS3BQQDC3KSD9H6P9H4R");
    }
}
