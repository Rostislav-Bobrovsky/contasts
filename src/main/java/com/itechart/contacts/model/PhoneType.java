package com.itechart.contacts.model;

/**
 * Created by Rostislav on 25-Feb-15.)
 */
public enum PhoneType {
    HOME("Home"),
    MOBILE("Mobile");

    private String displayName;

    PhoneType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PhoneType convertToPhoneType(String phoneType) {
        if ("h".equals(phoneType) || "Home".equals(phoneType)) {
            return PhoneType.HOME;
        } else if ("m".equals(phoneType) || "Mobile".equals(phoneType)) {
            return PhoneType.MOBILE;
        }
        return null;
    }

    public static String convertToString(PhoneType phoneType) {
        switch (phoneType) {
            case HOME:
                return "h";
            case MOBILE:
                return "m";
            default:
                return null;
        }

    }
}
