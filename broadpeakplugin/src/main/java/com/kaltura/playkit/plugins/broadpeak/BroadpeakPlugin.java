package com.kaltura.playkit.plugins.broadpeak;

import android.content.Context;
import android.text.TextUtils;

import com.kaltura.playkit.BuildConfig;
import com.kaltura.playkit.InterceptorEvent;
import com.kaltura.playkit.MessageBus;
import com.kaltura.playkit.PKError;
import com.kaltura.playkit.PKLog;
import com.kaltura.playkit.PKMediaConfig;
import com.kaltura.playkit.PKMediaEntry;
import com.kaltura.playkit.PKMediaFormat;
import com.kaltura.playkit.PKMediaSource;
import com.kaltura.playkit.PKPlugin;
import com.kaltura.playkit.Player;
import com.kaltura.playkit.PlayerEvent;
import com.kaltura.tvplayer.PKMediaEntryInterceptor;

import java.util.HashMap;
import java.util.Map;

import tv.broadpeak.smartlib.SmartLib;
import tv.broadpeak.smartlib.session.streaming.StreamingSession;
import tv.broadpeak.smartlib.session.streaming.StreamingSessionResult;

/**
 * Created by alex_lytvynenko on 04.11.2020.
 */
public class BroadpeakPlugin extends PKPlugin implements PKMediaEntryInterceptor {
    private static final PKLog log = PKLog.get("BroadpeakPlugin");

    private MessageBus messageBus;
    private final Map<String, StreamingSession> sessionsMap = new HashMap<>();
    private Player player;
    private BroadpeakConfig config;
    private Context context;

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
    protected void onLoad(final Player player, Object config, final MessageBus messageBus, Context context) {
        if (!(config instanceof BroadpeakConfig)) {
            log.e("Broadpeak config is missing");
            return;
        }

        if (messageBus == null) {
            log.e("Broadpeak messageBus == null");
            return;
        }

        BroadpeakConfig bpConfig = (BroadpeakConfig) config;
        this.config = bpConfig;

        SmartLib.getInstance().init(context,
                bpConfig.getAnalyticsAddress(),
                bpConfig.getNanoCDNHost(),
                bpConfig.getBroadpeakDomainNames());

        addGeneralConfig(bpConfig);
        
        this.player = player;
        this.messageBus = messageBus;
        this.context = context;

        this.messageBus.addListener(this, PlayerEvent.error, event -> {
            if (PKError.Severity.Fatal.equals(event.error.severity)) {
                log.e("PlayerEvent Fatal Error " + event.error.message);
                // Stop the session in case of Playback Error
                stopCurrentStreamingSession();
            }
        });
        
        this.messageBus.addListener(this, PlayerEvent.stopped, event -> {
            log.d("PlayerEvent stopped: calling stopStreamingSession");
            // Stop the session in case of Playback stop
            stopCurrentStreamingSession();
        });
    }

    private void addGeneralConfig(BroadpeakConfig bpConfig) {
        if (!TextUtils.isEmpty(bpConfig.getUUID())) {
            SmartLib.getInstance().setUUID(bpConfig.getUUID());
        }

        if (!TextUtils.isEmpty(bpConfig.getDeviceType())) {
            SmartLib.getInstance().setDeviceType(bpConfig.getDeviceType());
        }

        if (!TextUtils.isEmpty(bpConfig.getUserAgent())) {
            SmartLib.getInstance().setUserAgent(bpConfig.getUserAgent());
        }

        if (bpConfig.getNanoCDNResolvingRetryDelay() != null) {
            SmartLib.getInstance().setNanoCDNResolvingRetryDelay(bpConfig.getNanoCDNResolvingRetryDelay());
        }

        if (bpConfig.getNanoCDNHttpsEnabled() != null) {
            SmartLib.getInstance().setNanoCDNHttpsEnabled(bpConfig.getNanoCDNHttpsEnabled());
        }
    }

    @Override
    protected void onUpdateMedia(PKMediaConfig mediaConfig) {
        log.d("Start onUpdateMedia");
    }

    @Override
    protected void onUpdateConfig(Object config) {
        // Calling onUpdateConfig with new config by application
        // while not in changeMedia flow will break Broadpeak SmartLib Session

        // If Broadpeak config needs a change then do it only while changing the Media by application

        log.d("Start onUpdateConfig");
        if (!(config instanceof BroadpeakConfig)) {
            log.e("Broadpeak config is missing");
            return;
        }

        BroadpeakConfig bpConfig = (BroadpeakConfig) config;
        if (!this.config.equals(bpConfig)) {
            this.config = bpConfig;
            restartSmartLib(bpConfig);
        } else {
            log.d("Previous and latest configs are same");
        }
    }

    private void restartSmartLib(BroadpeakConfig bpConfig) {
        log.d("Releasing SmartLib and initializing with updated configs");
        stopCurrentStreamingSession();
        SmartLib.getInstance().release();
        if (bpConfig != null) {
            SmartLib.getInstance().init(context,
                    bpConfig.getAnalyticsAddress(),
                    bpConfig.getNanoCDNHost(),
                    bpConfig.getBroadpeakDomainNames());

            addGeneralConfig(bpConfig);
        }
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
        stopCurrentStreamingSession();
        // Release SmartLib
        SmartLib.getInstance().release();
        if (messageBus != null) {
            messageBus.removeListeners(this);
        }
    }

    private void stopCurrentStreamingSession() {
        log.d("stopCurrentStreamingSession");
        if (player != null && player.getMediaSource() != null) {
            stopStreamingSession(player.getMediaSource().getUrl());
        }
    }

    private void stopStreamingSession(String sessionKey) {
        log.d("stopStreamingSession called with sessionKey=[" + sessionKey + "]");
        if (sessionKey != null && sessionsMap.containsKey(sessionKey)) {
            StreamingSession session = sessionsMap.get(sessionKey);
            if (session != null) {
                session.stopStreamingSession();
            }
            sessionsMap.remove(sessionKey);
        }
    }

    @Override
    public void apply(PKMediaEntry mediaEntry, PKMediaEntryInterceptor.Listener listener) {

        int errorCode = BroadpeakError.Unknown.errorCode;
        String errorMessage = BroadpeakError.Unknown.errorMessage;

        if (mediaEntry != null && mediaEntry.getSources() != null &&
                !mediaEntry.getSources().isEmpty() && mediaEntry.getSources().get(0) != null &&
                !TextUtils.isEmpty(mediaEntry.getSources().get(0).getUrl())) {
            log.d("Apply Entry " + mediaEntry.getName() + " - " + mediaEntry.getId() + " url: " + mediaEntry.getSources().get(0).getUrl());

            // Stop the session for fresh media entry
            stopCurrentStreamingSession();

            PKMediaSource source = mediaEntry.getSources().get(0);
            if (PKMediaFormat.udp.equals(source.getMediaFormat())) {
                // Replace the URL
                log.d("Apply New Entry URL for UDP: " + mediaEntry.getName() + " - " + mediaEntry.getId() + " url: " + source.getUrl());
                source.setUrl(source.getUrl());
                listener.onComplete();
                return;
            }
            // Start the session and get the final stream URL
            StreamingSession session = SmartLib.getInstance().createStreamingSession();
            if (session == null) {
                sendBroadpeakErrorEvent(errorCode, errorMessage);
                return;
            }
            sessionsMap.put(source.getUrl(), session);

            addSessionConfig(session);
            session.attachPlayer(player, messageBus);

            StreamingSessionResult result = session.getURL(source.getUrl());
            if (result != null && !result.isError()) {
                sendSourceUrlSwitchedEvent(source, result);

                // Replace the URL
                log.d("Apply New Entry URL  " + mediaEntry.getName() + " - " + mediaEntry.getId() + " url: " + result.getURL());
                source.setUrl(result.getURL());
            } else {
                // Stop the session in case of error
                if (result != null) {
                    errorCode = result.getErrorCode();
                    errorMessage = result.getErrorMessage();
                }
                stopStreamingSession(source.getUrl());
                // send event to MessageBus
                sendBroadpeakErrorEvent(errorCode, errorMessage);
            }
        } else {
            stopCurrentStreamingSession();
            errorMessage = BroadpeakError.InvalidMediaEntry.errorMessage;
            errorCode = BroadpeakError.InvalidMediaEntry.errorCode;
            sendBroadpeakErrorEvent(errorCode, errorMessage);
        }

        listener.onComplete();
    }

    private void addSessionConfig(StreamingSession session) {
        if (!TextUtils.isEmpty(config.getAdCustomReference())) {
            session.setAdCustomReference(config.getAdCustomReference());
        }
        
        if (config.getAdParameters() != null) {
            for (Map.Entry<String, String> entry : config.getAdParameters().entrySet()) {
                if (entry != null && entry.getKey() != null) {
                    session.setAdParameter(entry.getKey(), entry.getValue());
                }
            }
        }

        if (config.getCustomParameters() != null) {
            for (Map.Entry<String, String> entry : config.getCustomParameters().entrySet()) {
                if (entry != null && entry.getKey() != null) {
                    session.setCustomParameter(entry.getKey(), entry.getValue());
                }
            }
        }

        if (config.getOptions() != null) {
            for (Map.Entry<Integer, Object> entry : config.getOptions().entrySet()) {
                if (entry != null && entry.getKey() != null) {
                    if (entry.getValue() instanceof Integer) {
                        session.setOption(entry.getKey(), (int) entry.getValue());
                    } else if (entry.getValue() instanceof Boolean) {
                        session.setOption(entry.getKey(), (boolean) entry.getValue());
                    } else if (entry.getValue() instanceof String) {
                        session.setOption(entry.getKey(), entry.getValue().toString());
                    }
                }
            }
        }
    }

    private void sendSourceUrlSwitchedEvent(PKMediaSource source, StreamingSessionResult result) {
        String originalUrl = source.getUrl();
        String updatedUrl = result.getURL();
        if (!TextUtils.isEmpty(originalUrl) && !TextUtils.isEmpty(updatedUrl) && !originalUrl.equals(updatedUrl)) {
            if (messageBus != null) {
                messageBus.post(new InterceptorEvent.SourceUrlSwitched(
                        InterceptorEvent.Type.SOURCE_URL_SWITCHED,
                        originalUrl,
                        updatedUrl));
            }
        }
    }

    private void sendBroadpeakErrorEvent(int errorCode, String errorMessage) {
        if (messageBus != null) {
            messageBus.post(new BroadpeakEvent.ErrorEvent(
                    BroadpeakEvent.Type.BROADPEAK_ERROR,
                    errorCode,
                    errorMessage));
        }
    }
}
