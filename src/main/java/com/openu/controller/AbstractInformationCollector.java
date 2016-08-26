package com.openu.controller;

import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.event.ComponentSystemEvent;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.openu.model.User;

public abstract  class  AbstractInformationCollector<R extends User> {
   
    private String username;
    private String password;
    private String passwordAgain;
    private String email;
    

    @SuppressWarnings("unchecked")
    protected static <T> T getField(ComponentSystemEvent e, String fieldName) {
        UIForm form = (UIForm) e.getComponent();
        UIInput input = (UIInput) form.findComponent(fieldName);
        if (input != null){
            return (T) input.getValue();
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * store the information that in the form to the User database
     */
    abstract protected void addFieldsToUser();
    
    public String apply() {
        addFieldsToUser();
        getRepository().save(getUser());
        doAfterApply();
        return null;
    }
    
    //should be ovveriten if needed
   protected void doAfterApply() {	
    }

abstract  PagingAndSortingRepository<R, Long> getRepository();
   
   abstract R getUser();
   
}
