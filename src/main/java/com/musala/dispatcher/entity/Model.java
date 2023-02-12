package com.musala.dispatcher.entity;

public enum Model {
    Lightweight("L"), Middleweight("M"),
    Cruiserweight("C"), Heavyweight("H");

    private String code;

    private Model(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
