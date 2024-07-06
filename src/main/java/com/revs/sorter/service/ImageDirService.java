package com.revs.sorter.service;

import com.revs.sorter.model.ImageMovementInfo;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImageDirService {

    public static final Set<String> DEFAULT_DIRS =
            new HashSet<>(Arrays.asList("Images", "Temp"));

    public static final String IMAGE_SORTER_BASE_DIR = "D:\\ImageSorter";

    public void createDefaultDirectories() {
        DEFAULT_DIRS.stream().forEach(dirName -> createDirectory(dirName));
    }

    public void createDirectory(String dirName) {
        File imageSorterDir = new File(IMAGE_SORTER_BASE_DIR + "\\" + dirName);
        boolean isExists = false;
        if (!imageSorterDir.exists()) {
            imageSorterDir.mkdirs();
        }
    }

    public Set<String> getAllDirectories() {
        return Stream.of(new File(IMAGE_SORTER_BASE_DIR).listFiles())
                .filter(file -> file.isDirectory() && !file.getName().equals("Images"))
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    public String moveImageToDirectory(ImageMovementInfo imageMovementInfo) {
        try {
            Files.move(Paths.get(IMAGE_SORTER_BASE_DIR + "\\Images\\" + imageMovementInfo.getImageName()),
                    Paths.get(IMAGE_SORTER_BASE_DIR + "\\" + imageMovementInfo.getDirName() + "\\" + imageMovementInfo.getImageName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
