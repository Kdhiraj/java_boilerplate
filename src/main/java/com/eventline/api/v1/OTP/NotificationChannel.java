

package com.eventline.api.v1.OTP;

import lombok.Getter;

@Getter
public enum NotificationChannel {
    SMS(1),
    EMAIL(2);

    private final int value;

    NotificationChannel(int value) {
        this.value = value;
    }

}




