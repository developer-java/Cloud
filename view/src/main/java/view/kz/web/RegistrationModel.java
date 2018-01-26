package view.kz.web;

import view.kz.ejb.DicManagment;
import view.kz.ejb.SettingManagment;
import view.kz.ejb.UserManagment;
import view.kz.persistence.City;
import view.kz.persistence.Contry;
import view.kz.persistence.SystemUser;
import view.kz.persistence.types.UserType;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ManagedBean
@ViewScoped
public class RegistrationModel {

    @EJB
    private SettingManagment settingManagment;
    @EJB
    private DicManagment dicManagment;
    @EJB
    private UserManagment userManagment;
    @ManagedProperty(value = "#{parentUserModule}")
    ParentUserModule parentUserModule;

    public view.kz.web.ParentUserModule getParentUserModule() {
        return parentUserModule;
    }

    public void setParentUserModule(ParentUserModule parentUserModule) {
        this.parentUserModule = parentUserModule;
    }

    private Contry selectedContry;
    private City selectedCity;
    private SystemUser user;

    public List<Contry> getContry(){
        return dicManagment.getContryList();
    }
    public List<City> getCityByContry(){
        if(selectedContry!=null){
            return dicManagment.getCityByContryId(selectedContry.getId());
        }
        return Collections.emptyList();
    }

    public List<City> getCityByCountry(Long id){
        return dicManagment.getCityByContryId(id);
    }

    public void registration() throws IOException {
        getUser().setUid(UUID.randomUUID().toString());
        getUser().setCity(selectedCity);
        getUser().setParrentDir(settingManagment.getValueByParam("parentPath") + "\\"+getUser().getUid()+"\\");
        getUser().setType(UserType.DEMO);
        File file = new File(getUser().getParrentDir());
        File basket = new File(getUser().getParrentDir()+"\\basket");
        if(!file.exists()){
            file.mkdirs();
            basket.mkdirs();
        }
        userManagment.saveUser(getUser());
        getParentUserModule().setParentUser(getUser());
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+"/home/index.xhtml?faces-redirect=true");
    }

    public void listenerContry(){
        if(selectedCity!=null){
            selectedCity=null;
        }
    }

    public SystemUser getUser() {
        if(user==null){
            user=new SystemUser();
        }
        return user;
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

    public Contry getSelectedContry() {
        return selectedContry;
    }

    public void setSelectedContry(Contry selectedContry) {
        this.selectedContry = selectedContry;
    }

    public City getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(City selectedCity) {
        this.selectedCity = selectedCity;
    }
}
