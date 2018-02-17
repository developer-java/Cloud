package view.kz.services.remote.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rFile")
public class RFile {
    private byte[] data;
    private String fileName;
    private String login;
    private String password;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
}
