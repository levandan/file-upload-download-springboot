/*-------------------------------------------------------------------------------
 * All Rights Reserved. Copyright(C) MB Bank, Ltd.
 * revision : 10:24:23 PM Oct 28, 2021
 * vendor   : MB Bank, Ltd.
 * author   : anlev
 * since    : 10:24:23 PM Oct 28, 2021
 * tagId    : 10:24:23 PM Oct 28, 2021
 *-------------------------------------------------------------------------------
 * revision marking
 *-----------------------------------------------------------------------------*/

package com.mbc.cam.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mbc.cam.exception.FileNotFoundException;
import com.mbc.cam.exception.FileStorageException;
import com.mbc.cam.property.FileStorageProperties;

/**
 * @author anlev
 *
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        }
        catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
                ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        }
        catch (DirectoryNotEmptyException dex) {
            throw new FileStorageException("DirectoryNotEmptyException file " + fileName + ". Please try again!", dex);
        }
        catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
            else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        }
        catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }

    public List<String> getListFileDownloadUri() {

        List<String> fileDownloadUriList = new ArrayList<String>();
        File dir = new File(fileStorageLocation.toString());
        File[] directoryListing = dir.listFiles();

        if (directoryListing != null) {
            for (File child : directoryListing) {
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
                    .path(child.getName()).toUriString();
                if (!fileDownloadUri.isEmpty()) {
                    fileDownloadUriList.add(fileDownloadUri);
                }
            }
        }

        return fileDownloadUriList;
    }
}
