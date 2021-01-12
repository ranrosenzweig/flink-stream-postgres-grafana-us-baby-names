package com.github.ranrosenzweig.model;

public class StateNames extends NationalNames{

    String state;

    //Convert String array to StateNames object
    public StateNames(String stateNamesStr) {
        super(stateNamesStr);
        //Split the string
        String[] attributes = stateNamesStr
                .split(",");
        //Extract state from stateNamesStr, other attributes taken from super
        this.state = attributes[4];
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}