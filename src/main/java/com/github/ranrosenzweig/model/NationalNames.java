package com.github.ranrosenzweig.model;

public class NationalNames {

    Integer id;
    String name;
    Integer year;
    String gender;
    Integer count;

    //Convert String array to NationalNames object
    public NationalNames(String stateNamesStr) {

        //Split the string
        String[] attributes = stateNamesStr
                .split(",");

        //Assign values
        this.id = Integer.valueOf(attributes[0]);
        this.name = attributes[1];
        this.year = Integer.valueOf(attributes[2]);
        this.gender = attributes[3];
        if (attributes.length == 5){
            // National Name
            this.count = Integer.valueOf(attributes[4]);
        }else {
            // State Name
            this.count = Integer.valueOf(attributes[5]);
        }

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}