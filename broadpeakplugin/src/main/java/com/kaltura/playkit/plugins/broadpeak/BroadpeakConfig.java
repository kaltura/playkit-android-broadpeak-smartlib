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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BroadpeakConfig that = (BroadpeakConfig) o;

        if (analyticsAddress != null ? !analyticsAddress.equals(that.analyticsAddress) : that.analyticsAddress != null)
            return false;
        if (nanoCDNHost != null ? !nanoCDNHost.equals(that.nanoCDNHost) : that.nanoCDNHost != null)
            return false;
        return broadpeakDomainNames != null ? broadpeakDomainNames.equals(that.broadpeakDomainNames) : that.broadpeakDomainNames == null;
    }

    @Override
    public int hashCode() {
        int result = analyticsAddress != null ? analyticsAddress.hashCode() : 0;
        result = 31 * result + (nanoCDNHost != null ? nanoCDNHost.hashCode() : 0);
        result = 31 * result + (broadpeakDomainNames != null ? broadpeakDomainNames.hashCode() : 0);
        return result;
    }
}
