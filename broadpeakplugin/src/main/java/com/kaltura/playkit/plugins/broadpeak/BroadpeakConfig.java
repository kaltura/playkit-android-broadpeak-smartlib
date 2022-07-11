package com.kaltura.playkit.plugins.broadpeak;

import java.util.Map;

/**
 * Created by alex_lytvynenko on 04.11.2020.
 */
public class BroadpeakConfig {

    private String analyticsAddress;
    private String nanoCDNHost;
    private String broadpeakDomainNames;
    private String uuid;
    private String deviceType;
    private String userAgent;
    private Integer nanoCDNResolvingRetryDelay; // In miliseconds
    private Boolean nanoCDNHttpsEnabled;

    private String adCustomReference;
    private Map<String, String> adParameters;
    private Map<String, String> customParameters;
    private Map<Integer, Object> options;

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

    public String getDeviceType() {
        return deviceType;
    }

    public BroadpeakConfig setDeviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public BroadpeakConfig setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public Integer getNanoCDNResolvingRetryDelay() {
        return nanoCDNResolvingRetryDelay;
    }

    /**
     * Set the delay to resolve the nanoCDN
     * Set the value in milliseconds, the minimum value is 1000.
     */
    public BroadpeakConfig setNanoCDNResolvingRetryDelay(Integer nanoCDNResolvingRetryDelay) {
        this.nanoCDNResolvingRetryDelay = nanoCDNResolvingRetryDelay;
        return this;
    }

    public Boolean getNanoCDNHttpsEnabled() {
        return nanoCDNHttpsEnabled;
    }

    public BroadpeakConfig setNanoCDNHttpsEnabled(Boolean nanoCDNHttpsEnabled) {
        this.nanoCDNHttpsEnabled = nanoCDNHttpsEnabled;
        return this;
    }

    public String getAdCustomReference() {
        return adCustomReference;
    }

    public BroadpeakConfig setAdCustomReference(String adCustomReference) {
        this.adCustomReference = adCustomReference;
        return this;
    }

    public Map<String, String> getAdParameters() {
        return adParameters;
    }

    public BroadpeakConfig setAdParameters(Map<String, String> adParameters) {
        this.adParameters = adParameters;
        return this;
    }

    public Map<String, String> getCustomParameters() {
        return customParameters;
    }

    public BroadpeakConfig setCustomParameters(Map<String, String> customParameters) {
        this.customParameters = customParameters;
        return this;
    }

    public Map<Integer, Object> getOptions() {
        return options;
    }

    public BroadpeakConfig setOptions(Map<Integer, Object> options) {
        this.options = options;
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
        if (getUUID() != null ? !getUUID().equals(that.getUUID()) : that.getUUID() != null)
            return false;
        if (getDeviceType() != null ? !getDeviceType().equals(that.getDeviceType()) : that.getDeviceType() != null)
            return false;
        if (getUserAgent() != null ? !getUserAgent().equals(that.getUserAgent()) : that.getUserAgent() != null)
            return false;
        if (getNanoCDNResolvingRetryDelay() != null ? !getNanoCDNResolvingRetryDelay().equals(that.getNanoCDNResolvingRetryDelay()) : that.getNanoCDNResolvingRetryDelay() != null)
            return false;
        if (getNanoCDNHttpsEnabled() != null ? !getNanoCDNHttpsEnabled().equals(that.getNanoCDNHttpsEnabled()) : that.getNanoCDNHttpsEnabled() != null)
            return false;
        if (getAdCustomReference() != null ? !getAdCustomReference().equals(that.getAdCustomReference()) : that.getAdCustomReference() != null)
            return false;
        if (getAdParameters() != null ? !getAdParameters().equals(that.getAdParameters()) : that.getAdParameters() != null)
            return false;
        if (getCustomParameters() != null ? !getCustomParameters().equals(that.getCustomParameters()) : that.getCustomParameters() != null)
            return false;
        return getOptions() != null ? getOptions().equals(that.getOptions()) : that.getOptions() == null;
    }

    @Override
    public int hashCode() {
        int result = getAnalyticsAddress() != null ? getAnalyticsAddress().hashCode() : 0;
        result = 31 * result + (getNanoCDNHost() != null ? getNanoCDNHost().hashCode() : 0);
        result = 31 * result + (getBroadpeakDomainNames() != null ? getBroadpeakDomainNames().hashCode() : 0);
        result = 31 * result + (getUUID()  != null ? getUUID() .hashCode() : 0);
        result = 31 * result + (getDeviceType() != null ? getDeviceType().hashCode() : 0);
        result = 31 * result + (getUserAgent() != null ? getUserAgent().hashCode() : 0);
        result = 31 * result + (getNanoCDNResolvingRetryDelay() != null ? getNanoCDNResolvingRetryDelay().hashCode() : 0);
        result = 31 * result + (getNanoCDNHttpsEnabled() != null ? getNanoCDNHttpsEnabled().hashCode() : 0);
        result = 31 * result + (getAdCustomReference() != null ? getAdCustomReference().hashCode() : 0);
        result = 31 * result + (getAdParameters() != null ? getAdParameters().hashCode() : 0);
        result = 31 * result + (getCustomParameters() != null ? getCustomParameters().hashCode() : 0);
        result = 31 * result + (getOptions() != null ? getOptions().hashCode() : 0);
        return result;
    }

}
