package com.kaltura.playkit.plugins.broadpeak;

/**
 * Created by alex_lytvynenko on 04.11.2020.
 */
public class BroadpeakConfig {

    private String analyticsAddress;
    private String nanoCDNHost;
    private String broadpeakDomainNames;
    private String broadpea111kDomainNames;

    public String getAnalyticsAddress() {
        return analyticsAddress;
    }

    public void setAnalyticsAddress(String analyticsAddress) {
        this.analyticsAddress = analyticsAddress;
    }

    public String getNanoCDNHost() {
        return nanoCDNHost;
    }

    public void setNanoCDNHost(String nanoCDNHost) {
        this.nanoCDNHost = nanoCDNHost;
    }

    public String getBroadpeakDomainNames() {
        return broadpeakDomainNames;
    }

    public void setBroadpeakDomainNames(String broadpeakDomainNames) {
        this.broadpeakDomainNames = broadpeakDomainNames;
    }
}
