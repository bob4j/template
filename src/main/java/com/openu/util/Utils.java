package com.openu.util;

import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;

public class Utils {

    public static ServletRequest getRequest() {
        return (ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

}
