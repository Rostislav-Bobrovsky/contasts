package com.itechart.contacts.model;

/**
 * Created by Rostislav on 18-Feb-15.)
 */
public enum RelationshipStatus {
    NONE_SELECTED("-None selected-"),
    SINGLE("Single"),
    IN_A_RELATIONSHIP("In a relationship"),
    ENGAGED("Engaged"),
    MARRIED("Married"),
    IN_LOVE("In love"),
    IT_IS_COMPLICATED("It is complicated"),
    ACTIVELY_SEARCHING("Actively searching");

    private String displayName;

    RelationshipStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static RelationshipStatus convertToRelationshipStatus(String relationshipStatus) {
        if ("nosel".equals(relationshipStatus) || "-None selected-".equals(relationshipStatus)) {
            return RelationshipStatus.NONE_SELECTED;
        } else if ("sngl".equals(relationshipStatus) || "Single".equals(relationshipStatus)) {
            return RelationshipStatus.SINGLE;
        } else if ("rel".equals(relationshipStatus) || "In a relationship".equals(relationshipStatus)) {
            return RelationshipStatus.IN_A_RELATIONSHIP;
        } else if ("eng".equals(relationshipStatus) || "Engaged".equals(relationshipStatus)) {
            return RelationshipStatus.ENGAGED;
        } else if ("mar".equals(relationshipStatus) || "Married".equals(relationshipStatus)) {
            return RelationshipStatus.MARRIED;
        } else if ("lov".equals(relationshipStatus) || "In love".equals(relationshipStatus)) {
            return RelationshipStatus.IN_LOVE;
        } else if ("cmpl".equals(relationshipStatus) || "It is complicated".equals(relationshipStatus)) {
            return RelationshipStatus.IT_IS_COMPLICATED;
        } else if ("srch".equals(relationshipStatus) || "Actively searching".equals(relationshipStatus)) {
            return RelationshipStatus.ACTIVELY_SEARCHING;
        }
        return null;
    }

    public static String convertToString(RelationshipStatus relationshipStatus) {
        switch (relationshipStatus) {
            case NONE_SELECTED:
                return "nosel";
            case SINGLE:
                return "sngl";
            case IN_A_RELATIONSHIP:
                return "rel";
            case ENGAGED:
                return "eng";
            case MARRIED:
                return "mar";
            case IN_LOVE:
                return "lov";
            case IT_IS_COMPLICATED:
                return "cmpl";
            case ACTIVELY_SEARCHING:
                return "srch";
            default:
                return null;
        }
    }
}
