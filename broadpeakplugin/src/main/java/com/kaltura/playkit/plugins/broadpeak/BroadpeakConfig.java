package com.kaltura.playkit.plugins.broadpeak;

/**
 * Created by alex_lytvynenko on 04.11.2020.
 */
public class BroadpeakConfig {

    private String analyticsAddress;
    private String nanoCDNHost;
    private String broadpeakDomainNames;

    public String getAnalyticsAddress() {
        return analyticsAddress;
    }

    public BroadpeakConfig setAnalyticsAddress(String analyticsAddress) {
        this.analyticsAddress = analyticsAddress;
        return this;
    }

    public String getNanoCDNHost() {
        return nanoCDNHost;
    }

    public BroadpeakConfig setNanoCDNHost(String nanoCDNHost) {
        this.nanoCDNHost = nanoCDNHost;
        return this;
    }

    public String getBroadpeakDomainNames() {
        return broadpeakDomainNames;
    }

    public BroadpeakConfig setBroadpeakDomainNames(String broadpeakDomainNames) {
        this.broadpeakDomainNames = broadpeakDomainNames;
        return this;
    }
}
