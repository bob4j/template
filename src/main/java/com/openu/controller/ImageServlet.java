package com.openu.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.openu.model.Image;
import com.openu.repository.ImageRepository;
import com.openu.util.AppContextProvider;

@Deprecated
public class ImageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.valueOf(request.getPathInfo().substring(1));
        Image image = AppContextProvider.getApplicationContext().getBean(ImageRepository.class).findOne(id);
        response.setContentType(getServletContext().getMimeType("some-name"));
        // response.setContentLength(image.getData().length);
        // response.getOutputStream().write(image.getData());
        // response.setContentLength(image.getValue().length());
        // response.getOutputStream().write(image.getValue().getBytes());
    }

}
