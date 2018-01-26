package view.kz.web;

import view.kz.exception.EmptyRequestException;

import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;

@ManagedBean
@SessionScoped
public class RedirectModel {
    @ManagedProperty(value = "#{parentUserModule}")
    private ParentUserModule parentUserModule;

    public ParentUserModule getParentUserModule() {
        return parentUserModule;
    }

    public void setParentUserModule(view.kz.web.ParentUserModule ParentUserModule) {
        this.parentUserModule = ParentUserModule;
    }

    public void redirect() throws IOException {
        if(getParentUserModule().getParentUser()!=null){
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+"/home/index.xhtml?faces-redirect=true");
        }
    }
    public void goToMainPage() throws IOException {
        if(getParentUserModule().getParentUser()==null){
            String s = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath();
            String s1 = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            System.out.println(s + "  "+ s1);
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath());
        }
    }


}
