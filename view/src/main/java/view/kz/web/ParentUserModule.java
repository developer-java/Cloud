package view.kz.web;

import view.kz.ejb.UserManagment;
import view.kz.persistence.SystemUser;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;

@ManagedBean
@SessionScoped
public class ParentUserModule {
    @EJB
    private UserManagment managment;

    private SystemUser parentUser;
    private String login;
    private String password;

    public String doLogout() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().invalidateSession();
        parentUser = null;
        return "/index.xhtml?faces-redirect=true";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void goLogin() throws IOException {
        this.parentUser = managment.getUserByLoginAndPass(login,password);
        if(parentUser==null){
            FacesContext.getCurrentInstance().addMessage("mainForm:sign", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Пользователь не найден!", null));
            return;
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+"/home/index.xhtml?faces-redirect=true");
    }
    public SystemUser getParentUser() {
        return parentUser;
    }

    public void setParentUser(SystemUser parentUser) {
        this.parentUser = parentUser;
    }

    public void exit() throws IOException {
        parentUser = null;
        FacesContext.getCurrentInstance().getExternalContext().redirect("");
    }
}
