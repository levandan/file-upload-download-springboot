/*-------------------------------------------------------------------------------
 * All Rights Reserved. Copyright(C) MB Bank, Ltd.
 * revision : 10:24:56 PM Oct 28, 2021
 * vendor   : MB Bank, Ltd.
 * author   : anlev
 * since    : 10:24:56 PM Oct 28, 2021
 * tagId    : 10:24:56 PM Oct 28, 2021
 *-------------------------------------------------------------------------------
 * revision marking
 *-----------------------------------------------------------------------------*/

package com.mbc.cam.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author anlev
 *
 */
@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
