package view.kz.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "locaelModule")
@SessionScoped
public class LocaleModule {
    private String locale = "ru";

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    private void IS_KAZ(){
        this.locale = "kaz";
    }

    private void IS_RUS(){
        this.locale = "ru";
    }

}
