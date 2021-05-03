package com.kaltura.playkit.plugins.broadpeak;

public enum BroadpeakError {
    Unknown(10000, "BroadpeakPlugin unknown error."),
    InvalidMediaEntry(10001, "Provided MediaEntry can not be modified. Check MediaEntry Sources."),
    InvalidSmartLibEntry(10002, "Invalid SmartLib entry"),
    InvalidStreamURL(10003, "SmartLib Stream URL is invalid.");

    public final int errorCode;
    public final String errorMessage;

    BroadpeakError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
