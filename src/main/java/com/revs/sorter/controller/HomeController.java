package com.revs.sorter.controller;

import com.revs.sorter.entity.DriveName;
import com.revs.sorter.entity.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeController {

    @GetMapping("/drives")
    public List<DriveName> getAvailableDirveNames() {
        List<DriveName> driveNames = new ArrayList<>();
        File[] roots = File.listRoots();
        for(File drive: roots) {
            driveNames.add(new DriveName(drive.getAbsolutePath().substring(0,1)));
        }
        return driveNames;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData> createDirectories() {
        return ResponseEntity.ok(new ResponseData());
    }

}
