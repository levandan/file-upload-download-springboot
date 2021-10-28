/*-------------------------------------------------------------------------------
 * All Rights Reserved. Copyright(C) MB Bank, Ltd.
 * revision : 10:08:59 PM Oct 28, 2021
 * vendor   : MB Bank, Ltd.
 * author   : anlev
 * since    : 10:08:59 PM Oct 28, 2021
 * tagId    : 10:08:59 PM Oct 28, 2021
 *-------------------------------------------------------------------------------
 * revision marking
 *-----------------------------------------------------------------------------*/

package com.mbc.cam.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mbc.cam.form.MyUploadForm;
import com.mbc.cam.service.FileStorageService;

/**
 * @author anlev
 *
 */
@Controller
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @RequestMapping(value = "/")
    public String homePage() {

        return "index";
    }

    // GET: Hiển thị trang form upload
    @RequestMapping(value = "/uploadOneFile", method = RequestMethod.GET)
    public String uploadOneFileHandler(Model model) {

        MyUploadForm myUploadForm = new MyUploadForm();
        model.addAttribute("myUploadForm", myUploadForm);

        return "uploadOneFile";
    }

    // GET: Hiển thị trang download upload
    @RequestMapping(value = "/downloadMultiFile", method = RequestMethod.GET)
    public String downloadMultiFileHandlerr(Model model) {

        Set<String> uploadedFiles = new HashSet<String>();
        List<String> fileDownloadUriList = fileStorageService.getListFileDownloadUri();

        for (String fileDownloadUri : fileDownloadUriList) {
            uploadedFiles.add(fileDownloadUri);
        }
        model.addAttribute("uploadedFiles", uploadedFiles);

        return "downloadMultiFile";
    }

    // POST: Xử lý Upload
    @RequestMapping(value = "/uploadOneFile", method = RequestMethod.POST)
    public String uploadOneFileHandlerPOST(HttpServletRequest request, Model model,
        @ModelAttribute("myUploadForm") MyUploadForm myUploadForm) {

        return this.doUpload(request, model, myUploadForm);

    }

    // GET: Hiển thị trang form upload
    @RequestMapping(value = "/uploadMultiFile", method = RequestMethod.GET)
    public String uploadMultiFileHandler(Model model) {
        MyUploadForm myUploadForm = new MyUploadForm();
        model.addAttribute("myUploadForm", myUploadForm);

        return "uploadMultiFile";
    }

    // POST: Xử lý Upload
    @RequestMapping(value = "/uploadMultiFile", method = RequestMethod.POST)
    public String uploadMultiFileHandlerPOST(HttpServletRequest request, Model model,
        @ModelAttribute("myUploadForm") MyUploadForm myUploadForm) {

        return this.doUpload(request, model, myUploadForm);

    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }

    private String doUpload(HttpServletRequest request, Model model, MyUploadForm myUploadForm) {

        String description = myUploadForm.getDescription();
        MultipartFile[] fileDatas = myUploadForm.getFileDatas();
        //
        List<String> failedFiles = new ArrayList<String>();
        Set<String> uploadedFiles = new HashSet<String>();
        for (MultipartFile fileData : fileDatas) {

            // Tên file gốc tại Client.
            String fileName = fileStorageService.storeFile(fileData);
            // String name = fileData.getOriginalFilename();
            System.out.println("Client File Name = " + fileName);

            // Dia chi download file
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
                .path(fileName).toUriString();
            uploadedFiles.add(fileDownloadUri);
        }
        model.addAttribute("description", description);
        model.addAttribute("uploadedFiles", uploadedFiles);
        model.addAttribute("failedFiles", failedFiles);
        return "uploadResult";
    }
}
