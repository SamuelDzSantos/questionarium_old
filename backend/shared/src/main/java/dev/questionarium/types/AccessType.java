package dev.questionarium.types;

public enum AccessType {

    ACCESS("ACCESS"),
    REFRESH("REFRESH");

    final String accessType;

    AccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getAccessType() {
        return this.accessType;
    }
}