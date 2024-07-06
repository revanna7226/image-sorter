package com.revs.sorter.service;

import com.revs.sorter.model.DirectoryInfo;
import com.revs.sorter.model.DriveName;
import com.revs.sorter.model.ImageMovementInfo;
import org.springframework.stereotype.Service;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImageDirService {

    public static final Set<String> DEFAULT_DIRS =
            new HashSet<>(Arrays.asList("Images", "Temp"));

    public static final String IMAGE_SORTER_BASE_DIR = "ImageSorter";

    public void createDefaultDirectories(DriveName driveName) {
        DEFAULT_DIRS.stream().map(dirName -> DirectoryInfo.builder().driveName(driveName.getDriveName()).dirName(dirName).build())
                .forEach(this::createDirectory);
    }

    public List<DriveName> getAvailableDriveNames() {
        List<DriveName> driveNames = new ArrayList<>();
        File[] roots = File.listRoots();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        for(File drive: roots) {
            DriveName driveName = DriveName.builder()
                    .driveLetter(drive.getAbsolutePath().substring(0,1))
                    .driveName(fsv.getSystemDisplayName(drive))
                    .build();
            driveNames.add(driveName);
        }
        return driveNames;
    }

    public void createDirectory(DirectoryInfo dirInfo) {
        String driveName = dirInfo.getDriveName();
        String dirName = dirInfo.getDirName();
        File imageSorterDir = new File( driveName + ":\\" + IMAGE_SORTER_BASE_DIR + "\\" + dirName);
        boolean isExists = false;
        if (!imageSorterDir.exists()) {
            imageSorterDir.mkdirs();
        }
    }

    public Set<String> getAllDirectories(String driveName) {
        String dirPath = driveName + ":\\" + IMAGE_SORTER_BASE_DIR;
        return Stream.of(new File(dirPath).listFiles())
                .filter(file -> file.isDirectory() && !file.getName().equals("Images"))
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    public String moveImageToDirectory(ImageMovementInfo imageMovementInfo) {
        String driveName = imageMovementInfo.getDriveName();
        try {
            Files.move(Paths.get(driveName + ":\\" + IMAGE_SORTER_BASE_DIR + "\\Images\\" + imageMovementInfo.getImageName()),
                    Paths.get(driveName + ":\\" + IMAGE_SORTER_BASE_DIR + "\\" + imageMovementInfo.getDirName() + "\\" + imageMovementInfo.getImageName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
