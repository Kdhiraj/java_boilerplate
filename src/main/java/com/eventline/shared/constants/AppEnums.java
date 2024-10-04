package com.eventline.shared.constants;

import lombok.Getter;

/**
 * This class contains application-wide constants such as user roles, account statuses,
 * verification types, and other configuration constants.
 */
public class AppEnums {

    // User Roles
    public enum Role {
        ADMIN,
        SUB_ADMIN,
        USER,
        GUEST
    }

    // User Permissions
    public enum Permission {
        VIEW,
        CREATE,
        UPDATE,
        DELETE
    }

    // Account Status
    @Getter
    public enum ListingStatus {
        INACTIVE("0"),
        ACTIVE("1"),
        DELETED("2");

        private final String value;

        ListingStatus(String value) {
            this.value = value;
        }

    }

    // Platforms
    @Getter
    public enum Platform {
        IOS("1"),
        ANDROID("2"),
        WEB("3");

        private final String value;

        Platform(String value) {
            this.value = value;
        }

    }

    // Channel Types
    @Getter
    public enum ChannelType {
        PHONE("PHONE"),
        EMAIL("EMAIL");

        private final String value;

        ChannelType(String value) {
            this.value = value;
        }

    }

    // Next Steps
    @Getter
    public enum NextStep {
        NONE("0"),
        VERIFY("1"),
        SETUP("2");

        private final String value;

        NextStep(String value) {
            this.value = value;
        }

    }

    // Environments
    @Getter
    public enum Environment {
        LOCAL("local"),
        DEV("development"),
        QA("qa"),
        PROD("production"),
        PRE_PROD("pre_prod"),
        STAGE("stage");

        private final String value;

        Environment(String value) {
            this.value = value;
        }

    }

    // Verification Types
    @Getter
    public enum VerifyType {
        MFA("mfa"),
        ONBOARD("OnBoard");

        private final String value;

        VerifyType(String value) {
            this.value = value;
        }

    }

    // Sort Order
    @Getter
    public enum SortOrder {
        ASC("ASC"),
        DESC("DESC");

        private final String value;

        SortOrder(String value) {
            this.value = value;
        }

    }

    // Sort Order Values
    @Getter
    public enum SortOrderValues {
        ASC(1),
        DESC(-1);

        private final int value;

        SortOrderValues(int value) {
            this.value = value;
        }

    }

    // Social Types
    @Getter
    public enum SocialType {
        GOOGLE("1"),
        APPLE("2"),
        FACEBOOK("3");

        private final String value;

        SocialType(String value) {
            this.value = value;
        }

    }

    // Login Types
    @Getter
    public enum LoginType {
        SOCIAL("1"),
        GUEST("2"),
        NORMAL("3");

        private final String value;

        LoginType(String value) {
            this.value = value;
        }

    }



    // Business Steps
    @Getter
    public enum BusinessSteps {
        FIRST("1"),
        SECOND("2"),
        THIRD("3"),
        FOURTH("4");

        private final String value;

        BusinessSteps(String value) {
            this.value = value;
        }

    }

    // TTL Constants
    public static final class TTL {
        public static final long REFRESH_TOKEN = 7 * 24 * 60 * 60; // 7 Days in seconds
        public static final long ACCESS_TOKEN = 60 * 60; // 1 Hour in seconds
        public static final long AUTH_TOKEN = 60 * 60; // 1 Hour in seconds
        public static final long MFA_TOKEN = 5 * 60; // 5 Minutes in seconds
        public static final long PWD_TOKEN = 3 * 60 * 60; // 3 Hours in seconds
        public static final long NEVER = 365 * 24 * 60 * 60; // 1 Year in seconds
        public static final long PWD_EXPIRATION = 90L * 24 * 60 * 60 * 1000; // 90 Days in milliseconds
    }
}
