package com.openu.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;

@ManagedBean
@ViewScoped
public class FileUploadBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private Part file;

    public void save() {
        try (InputStream input = file.getInputStream()) {
            Files.copy(input, new File("/home/michael/tmp", "test").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
}