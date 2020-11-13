package com.kaltura.playkit.plugins.broadpeak;

import android.content.Context;

import com.kaltura.playkit.BuildConfig;
import com.kaltura.playkit.MessageBus;
import com.kaltura.playkit.OnMediaInterceptorListener;
import com.kaltura.playkit.PKError;
import com.kaltura.playkit.PKLog;
import com.kaltura.playkit.PKMediaConfig;
import com.kaltura.playkit.PKMediaEntry;
import com.kaltura.playkit.PKMediaEntryInterceptor;
import com.kaltura.playkit.PKMediaSource;
import com.kaltura.playkit.PKPlugin;
import com.kaltura.playkit.Player;
import com.kaltura.playkit.PlayerEvent;
import com.kaltura.playkit.player.PKPlayerErrorType;

import tv.broadpeak.smartlib.SmartLib;
import tv.broadpeak.smartlib.session.streaming.StreamingSession;
import tv.broadpeak.smartlib.session.streaming.StreamingSessionResult;

/**
 * Created by alex_lytvynenko on 04.11.2020.
 */
public class BroadpeakPlugin extends PKPlugin implements PKMediaEntryInterceptor {
    private static final PKLog log = PKLog.get("BroadpeakPlugin");

    private final String SMARTLIB_PRE_STARTUP_TIME_KEY = "pre_startup_time";
    private Player player;
    private MessageBus messageBus;
    private StreamingSession session;
    private long requestStartTime;

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
        BroadpeakConfig bpConfig = (BroadpeakConfig) config;
        SmartLib.getInstance().init(context,
                bpConfig.getAnalyticsAddress(),
                bpConfig.getNanoCDNHost(),
                bpConfig.getBroadpeakDomainNames()
        );
        session = SmartLib.getInstance().createStreamingSession();

        this.messageBus = messageBus;
        this.player = player;
        this.player.addMediaEntryInterceptor(this);

        session.attachPlayer(this.player);

        requestStartTime = System.currentTimeMillis();
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
        // Stop the session
        session.stopStreamingSession();

        player.removeMediaEntryInterceptor(this);
        player = null;

        // Release SmartLib
        SmartLib.getInstance().release();
    }

    @Override
    public void apply(PKMediaEntry mediaEntry, OnMediaInterceptorListener listener) {
        // Set the pre-startup time
        SmartLib.getInstance().setCustomParameter(SMARTLIB_PRE_STARTUP_TIME_KEY,
                (System.currentTimeMillis() - requestStartTime) + "");

        if (mediaEntry.getSources().size() > 0) {
            PKMediaSource source = mediaEntry.getSources().get(0);
            // Start the session and get the final stream URL
            StreamingSessionResult result = session.getURL(source.getUrl());
            if (!result.isError()) {
                // Replace the URL
                source.setUrl(result.getURL());
            } else {
                // send event to MessageBus
                messageBus.post(new PlayerEvent.Error(
                        new PKError(
                                PKPlayerErrorType.LOAD_ERROR,
                                result.getErrorMessage(),
                                new Throwable(result.getErrorMessage()))
                ));
            }
        }

        listener.onApplyMediaCompleted();
    }
}