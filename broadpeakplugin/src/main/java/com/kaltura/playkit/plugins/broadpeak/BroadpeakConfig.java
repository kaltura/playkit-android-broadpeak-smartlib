package com.kaltura.playkit.plugins.broadpeak;

/**
 * Created by alex_lytvynenko on 04.11.2020.
 */
public class BroadpeakConfig {

    private String analyticsAddress;
    private String nanoCDNHost;
    private String broadpeakDomainNames;
    private String uuid;

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

    public String getUUID() {
        return uuid;
    }

    public BroadpeakConfig setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BroadpeakConfig that = (BroadpeakConfig) o;

        if (getAnalyticsAddress() != null ? !getAnalyticsAddress().equals(that.getAnalyticsAddress()) : that.getAnalyticsAddress() != null)
            return false;
        if (getNanoCDNHost() != null ? !getNanoCDNHost().equals(that.getNanoCDNHost()) : that.getNanoCDNHost() != null)
            return false;
        if (getBroadpeakDomainNames() != null ? !getBroadpeakDomainNames().equals(that.getBroadpeakDomainNames()) : that.getBroadpeakDomainNames() != null)
            return false;
        return getUUID() != null ? getUUID().equals(that.getUUID()) : that.getUUID() == null;
    }

    @Override
    public int hashCode() {
        int result = getAnalyticsAddress() != null ? getAnalyticsAddress().hashCode() : 0;
        result = 31 * result + (getNanoCDNHost() != null ? getNanoCDNHost().hashCode() : 0);
        result = 31 * result + (getBroadpeakDomainNames() != null ? getBroadpeakDomainNames().hashCode() : 0);
        result = 31 * result + (getUUID() != null ? getUUID().hashCode() : 0);
        return result;
    }
}
