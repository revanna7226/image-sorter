package com.revs.sorter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class DriveName {
    private String driveName;

    public DriveName(String driveName) {
        this.driveName = driveName;
    }
}
