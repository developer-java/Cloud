package view.kz.persistence.common;

import javax.persistence.*;

@MappedSuperclass
public abstract class AAttach extends Identifier {
    @Column(name = "FILENAME")
    private String fileName;
    @Column(name = "CONTENT_TYPE")
    private String contentType;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "DATA")
    private byte[] data;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Transient
    public long getMbSize(){
        return data == null ? 0 : data.length/1024/1024;
    }

    @Transient
    public long getKbSize(){
        return data == null ? 0 : data.length/1024;
    }
}