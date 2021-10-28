/*-------------------------------------------------------------------------------
 * All Rights Reserved. Copyright(C) MB Bank, Ltd.
 * revision : 10:08:16 PM Oct 28, 2021
 * vendor   : MB Bank, Ltd.
 * author   : anlev
 * since    : 10:08:16 PM Oct 28, 2021
 * tagId    : 10:08:16 PM Oct 28, 2021
 *-------------------------------------------------------------------------------
 * revision marking
 *-----------------------------------------------------------------------------*/

package com.mbc.cam.form;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author anlev
 *
 */
public class MyUploadForm {

    private String description;

    // Upload files.
    private MultipartFile[] fileDatas;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile[] getFileDatas() {
        return fileDatas;
    }

    public void setFileDatas(MultipartFile[] fileDatas) {
        this.fileDatas = fileDatas;
    }
}
