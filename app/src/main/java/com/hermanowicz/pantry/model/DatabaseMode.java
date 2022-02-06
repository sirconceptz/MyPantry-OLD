package com.hermanowicz.pantry.model;

public class DatabaseMode {

    private Mode databaseMode = Mode.LOCAL;

    public enum Mode { LOCAL, ONLINE }

    public Mode getDatabaseMode() {
        return databaseMode;
    }

    public void setDatabaseMode(Mode dbMode) {
        this.databaseMode = dbMode;
    }
}