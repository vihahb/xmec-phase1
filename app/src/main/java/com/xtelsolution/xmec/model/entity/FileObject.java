package com.xtelsolution.xmec.model.entity;

import java.io.File;
import java.io.Serializable;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class FileObject implements Serializable {
    private File file;

    public FileObject() {
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}