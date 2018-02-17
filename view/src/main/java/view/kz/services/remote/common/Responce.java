package view.kz.services.remote.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "responce")
public class Responce {

    private String UID;
    private Status status;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status{
        AUTH_ERROR,
        REQ_PARAM_ERROR,
        NULL_POINTER,
        INNCORRECT_DATA,
        FILE_NOT_FOUND,
        SUCCESS,
        SERVER_EXCEPTION
    }
}
