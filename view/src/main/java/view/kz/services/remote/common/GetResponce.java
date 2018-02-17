package view.kz.services.remote.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "getResponce")
public class GetResponce extends Responce {
    private byte[] data;
    private String fileName;

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
}
