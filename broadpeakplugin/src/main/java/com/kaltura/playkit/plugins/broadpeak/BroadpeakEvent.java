/*
 * ============================================================================
 * Copyright (C) 2017 Kaltura Inc.
 *
 * Licensed under the AGPLv3 license, unless a different license for a
 * particular library is specified in the applicable library path.
 *
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/agpl-3.0.html
 * ============================================================================
 */

package com.kaltura.playkit.plugins.broadpeak;

import com.kaltura.playkit.PKEvent;

/**
 * Created by alex.litvinenko on 30/11/2020.
 */
public class BroadpeakEvent implements PKEvent {

    public final BroadpeakEvent.Type type;
    public static final Class<ErrorEvent> error = ErrorEvent.class;

    public BroadpeakEvent(BroadpeakEvent.Type type) {
        this.type = type;
    }

    public enum Type {
        ERROR
    }

    public static class ErrorEvent extends BroadpeakEvent {

        public final int errorCode;
        public final String errorMessage;

        public ErrorEvent(Type errorType, int errorCode, String errorMessage) {
            super(errorType);
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }
    }

    @Override
    public Enum eventType() {
        return this.type;
    }
}
