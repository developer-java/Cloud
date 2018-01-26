package view.kz.web;

import view.kz.ejb.DicManagment;
import view.kz.ejb.SettingManagment;
import view.kz.ejb.UserManagment;
import view.kz.persistence.DicFile;
import view.kz.persistence.SystemUser;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(value = "setting")
@SessionScoped
public class SettingModel {
    @EJB
    private SettingManagment settingManagment;
    @EJB
    private DicManagment dicManagment;
    @EJB
    private UserManagment userManagment;
    @ManagedProperty(value = "#{parentUserModule}")
    private ParentUserModule parentUserModule;
    private SystemUser user;

    public SystemUser getUser() {
        if(user==null){
            user = getParentUserModule().getParentUser();
        }
        return user;
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

    public ParentUserModule getParentUserModule() {
        return parentUserModule;
    }

    public void setParentUserModule(ParentUserModule parentUserModule) {
        this.parentUserModule = parentUserModule;
    }



    public class Setting{

    }
}
