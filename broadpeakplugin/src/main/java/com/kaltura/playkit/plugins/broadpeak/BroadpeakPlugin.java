package com.kaltura.playkit.plugins.broadpeak;

import android.content.Context;

import com.kaltura.playkit.BuildConfig;
import com.kaltura.playkit.MessageBus;
import com.kaltura.playkit.PKError;
import com.kaltura.playkit.PKLog;
import com.kaltura.playkit.PKMediaConfig;
import com.kaltura.playkit.PKMediaEntry;
import com.kaltura.playkit.PKMediaSource;
import com.kaltura.playkit.PKPlugin;
import com.kaltura.playkit.Player;
import com.kaltura.playkit.PlayerEvent;
import com.kaltura.tvplayer.PKMediaEntryInterceptor;

import tv.broadpeak.smartlib.SmartLib;
import tv.broadpeak.smartlib.session.streaming.StreamingSession;
import tv.broadpeak.smartlib.session.streaming.StreamingSessionResult;

/**
 * Created by alex_lytvynenko on 04.11.2020.
 */
public class BroadpeakPlugin extends PKPlugin implements PKMediaEntryInterceptor {
    private static final PKLog log = PKLog.get("BroadpeakPlugin");

    private final String SMARTLIB_PRE_STARTUP_TIME_KEY = "pre_startup_time";
    private MessageBus messageBus;
    private StreamingSession session;
    private Player player;
    private long requestStartTime;
    private PKMediaEntry mediaEntry;

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
        if (config == null) {
            log.e("Broadpeak config is missing");
            return;
        }

        BroadpeakConfig bpConfig = (BroadpeakConfig) config;
        SmartLib.getInstance().init(context,
                bpConfig.getAnalyticsAddress(),
                bpConfig.getNanoCDNHost(),
                bpConfig.getBroadpeakDomainNames()
        );
        this.player = player;
        this.messageBus = messageBus;

        requestStartTime = System.currentTimeMillis();

        messageBus.addListener(this, PlayerEvent.error, event -> {
            log.d("BroadpeakPlugin PlayerEvent ERROR");
            if (event.error.severity == PKError.Severity.Fatal) {
                // Stop the session in case of Playback Error
                stopStreamingSession(null);
            }
        });
    }

    @Override
    protected void onUpdateMedia(PKMediaConfig mediaConfig) {
        log.d("Start onUpdateMedia");
    }

    @Override
    protected void onUpdateConfig(Object config) {
        log.d("Start onUpdateConfig");
    }

    @Override
    protected void onApplicationPaused() {
        log.d("Start onApplicationPaused");
    }

    @Override
    protected void onApplicationResumed() {
        log.d("Start onApplicationResumed");
    }

    @Override
    protected void onDestroy() {
        // Stop the session
        stopStreamingSession(null);

        mediaEntry = null;

        if (player != null) {
            player.destroy();
            player = null;
        }

        // Release SmartLib
        SmartLib.getInstance().release();
    }

    private void stopStreamingSession(Integer errorCode) {
        if (session != null) {
            if (errorCode != null) {
                session.stopStreamingSession(errorCode);
            } else {
                session.stopStreamingSession();
            }
            session = null;
        }
    }

    @Override
    public void apply(PKMediaEntry mediaEntry, PKMediaEntryInterceptor.Listener listener) {
        int errorCode = -1;
        String errorMessage = "Unknown";

        // Set the pre-startup time
        SmartLib.getInstance().setCustomParameter(SMARTLIB_PRE_STARTUP_TIME_KEY,
                (System.currentTimeMillis() - requestStartTime) + "");

        if (mediaEntry != null && mediaEntry.getSources() != null &&
                !mediaEntry.getSources().isEmpty() && mediaEntry.getSources().get(0) != null) {

            // Stop the session for fresh media entry
            if (this.mediaEntry != null) {
                stopStreamingSession(null);
            }

            this.mediaEntry = mediaEntry;

            PKMediaSource source = mediaEntry.getSources().get(0);
            // Start the session and get the final stream URL
            session = SmartLib.getInstance().createStreamingSession();
            session.attachPlayer(player);
            StreamingSessionResult result = session.getURL(source.getUrl());
            if (result != null && !result.isError()) {
                // Replace the URL
                source.setUrl(result.getURL());
            } else {
                // Stop the session in case of error
                if (result != null) {
                    errorCode = result.getErrorCode();
                    errorMessage = result.getErrorMessage();
                    stopStreamingSession(errorCode);
                } else {
                    stopStreamingSession(null);
                }
                // send event to MessageBus
                messageBus.post(new BroadpeakEvent.ErrorEvent(
                        BroadpeakEvent.Type.ERROR,
                        errorCode,
                        errorMessage)
                );
            }
        } else {
            stopStreamingSession(null);
            errorMessage = "Invalid media entry";
            messageBus.post(new BroadpeakEvent.ErrorEvent(
                    BroadpeakEvent.Type.ERROR,
                    errorCode,
                    errorMessage));
        }

        listener.onComplete();
    }
}