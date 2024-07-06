package com.revs.sorter.controller;

import com.revs.sorter.model.DirectoryInfo;
import com.revs.sorter.model.DriveName;
import com.revs.sorter.model.ImageMovementInfo;
import com.revs.sorter.service.ImageDirService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Set;

@RestController
public class ImageDirController {

    @Autowired
    ImageDirService imageDirService;

//    @PostConstruct
//    public void initialTask() {
//        this.imageDirService.createDefaultDirectories();
//    }

    @GetMapping("/drives")
    public List<DriveName> getAllDrives() {
        return this.imageDirService.getAvailableDriveNames();
    }

    @GetMapping("/dirs/{driveName}")
    public Set<String> getAllDirs(@PathVariable String driveName) {
        return this.imageDirService.getAllDirectories(driveName);
    }

    @PostMapping("/dirs")
    public Set<String> createDir(@RequestBody DirectoryInfo directoryInfo) {
        this.imageDirService.createDirectory(directoryInfo);
        return this.imageDirService.getAllDirectories(directoryInfo.getDriveName());
    }

    @PostMapping("/move")
    public String moveImageToDir(@RequestBody ImageMovementInfo imageMovementInfo) {
        return this.imageDirService.moveImageToDirectory(imageMovementInfo);
    }

    @PostMapping("/upload")
    public String uploadMultipartFile(@RequestParam("files") MultipartFile[] files){ //} , Model modal) {
        System.out.println(files);
//        try {
//            // Declare empty list for collect the files data
//            // which will come from UI
//            List<FileModal> fileList = new ArrayList<FileModal>();
//            for (MultipartFile file : files) {
//                String fileContentType = file.getContentType();
//                String sourceFileContent = new String(file.getBytes(), StandardCharsets.UTF_8);
//                String fileName = file.getOriginalFilename();
//                FileModal fileModal = new FileModal(fileName, sourceFileContent, fileContentType);
//
//                // Adding file into fileList
//                fileList.add(fileModal);
//            }
//
//            // Saving all the list item into database
//            fileServiceImplementation.saveAllFilesList(fileList);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Send file list to View using modal class
//        // fileServiceImplementation.getAllFiles() used to
//        // fetch all file list from DB
//        modal.addAttribute("allFiles", fileServiceImplementation.getAllFiles());

        return "FileList";
    }

    @PostMapping("/createDefaultDirs")
    public String createDefaultDirs(@RequestBody DriveName driveName) {
        this.imageDirService.createDefaultDirectories(driveName);
        return "Created";
    }

}
