package com.kaltura.playkit.plugins.broadpeak;

import android.content.Context;
import android.os.Handler;

import com.kaltura.playkit.BuildConfig;
import com.kaltura.playkit.MessageBus;
import com.kaltura.playkit.PKLog;
import com.kaltura.playkit.PKMediaConfig;
import com.kaltura.playkit.PKMediaEntry;
import com.kaltura.playkit.PKMediaEntryInterceptor;
import com.kaltura.playkit.PKMediaSource;
import com.kaltura.playkit.PKPlugin;
import com.kaltura.playkit.Player;

/**
 * Created by alex_lytvynenko on 04.11.2020.
 */
public class BroadpeakPlugin extends PKPlugin implements PKMediaEntryInterceptor {
    private static final PKLog log = PKLog.get("BroadpeakPlugin");

    private Player player;

    public static final Factory factory = new Factory() {
        @Override
        public String getName() {
            return "broadpeak";
        }

        @Override
        public PKPlugin newInstance() {
            return new BroadpeakPlugin();
        }

        @Override
        public String getVersion() {
            return BuildConfig.VERSION_NAME;
        }

        @Override
        public void warmUp(Context context) {
        }
    };


    @Override
    protected void onLoad(Player player, Object config, MessageBus messageBus, Context context) {
        this.player = player;
        this.player.addMediaEntryInterceptor(this);
    }

    @Override
    protected void onUpdateMedia(PKMediaConfig mediaConfig) {
    }

    @Override
    protected void onUpdateConfig(Object config) {
    }

    @Override
    protected void onApplicationPaused() {
    }

    @Override
    protected void onApplicationResumed() {
    }

    @Override
    protected void onDestroy() {
        player.addMediaEntryInterceptor(this);
    }

    @Override
    public void apply(PKMediaEntry mediaEntry, OnMediaInterceptorListener listener) {
        for (PKMediaSource source : mediaEntry.getSources()) {
            source.setUrl("http://example.com");
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        listener.onApplyMediaCompleted(null);
    }
}