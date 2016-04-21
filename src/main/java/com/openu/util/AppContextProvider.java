package com.openu.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Useful in case a non-spring class requires the spring application context
 */
@Component(AppContextProvider.BEAN_NAME)
public class AppContextProvider implements ApplicationContextAware {

    public static final String BEAN_NAME = "appContextProvider";
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        AppContextProvider.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
