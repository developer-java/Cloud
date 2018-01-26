package view.kz.persistence.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedSuperclass
public abstract class AFileAttach extends Identifier implements IFileAttach {
    @Column(name = "FILENAME")
    protected String fileName;
    @Column(name = "CONTENT_TYPE")
    protected String contentType;
    @Column(name = "MD5")
    protected String md5;
    @Column(name = "UID")
    protected String uid;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        if (fileName != null) {
            String[] parts = fileName.split("\\\\");
            if (parts != null && parts.length > 0) {
                fileName = parts[parts.length - 1];
            }
        }
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Transient
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Transient
    public String getRelativeFilePath() {
        return uid;
    }

    @Transient
    protected String generateRelativeFilePath(Date date, String courtCode, String requestUid) {
        return sdf.format(date) + "/" + courtCode + "/" + requestUid + "/";
    }

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
}