package com.revs.sorter.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriveName {
    private String driveName;

    public DriveName(String driveName) {
        this.driveName = driveName;
    }

    public DriveName(){

    }
}