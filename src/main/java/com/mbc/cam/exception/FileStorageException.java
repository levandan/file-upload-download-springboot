/*-------------------------------------------------------------------------------
 * All Rights Reserved. Copyright(C) MB Bank, Ltd.
 * revision : 10:25:53 PM Oct 28, 2021
 * vendor   : MB Bank, Ltd.
 * author   : anlev
 * since    : 10:25:53 PM Oct 28, 2021
 * tagId    : 10:25:53 PM Oct 28, 2021
 *-------------------------------------------------------------------------------
 * revision marking
 *-----------------------------------------------------------------------------*/

package com.mbc.cam.exception;

/**
 * @author anlev
 *
 */
public class FileStorageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
