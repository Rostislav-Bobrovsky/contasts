package com.itechart.contacts.model;

/**
 * Created by Rostislav on 22-Feb-15.)
 */
public enum Sex {
    MALE("Male"),
    FEMALE("Female");

    private String displayName;

    Sex(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

//    public static List<Sex> getUnselectedValues(Sex sex) {
//        List<Sex> sexes = Arrays.asList(values());
//        Iterator<Sex> values = sexes.iterator();
//        while(values.hasNext()) {
//            Sex value = values.next();
//            if(value.equals(sex)) {
//                values.remove();
//            }
//        }
//        return sexes;
//    }

    public static Sex convertToSex(String sex) {
        if ("f".equals(sex) || "Female".equals(sex)) {
            return Sex.FEMALE;
        } else if ("m".equals(sex) || "Male".equals(sex)) {
            return Sex.MALE;
        }
        return null;
    }

    public static String convertToString(Sex sex) {
        switch (sex) {
            case MALE:
                return "m";
            case FEMALE:
                return "f";
            default:
                return null;
        }
    }
}
