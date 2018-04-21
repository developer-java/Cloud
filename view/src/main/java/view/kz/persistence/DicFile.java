package view.kz.persistence;

import view.kz.persistence.common.Identifier;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "FILES")
public class DicFile extends Identifier {
    @Column(name = "FILENAME")
    private String fileName;
    @Column(name = "PATH")
    private String path;
    @Column(name = "UID",unique = true)
    private String uid;
    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private SystemUser user;
    @Transient
    private boolean isSelected;
    @Column(name = "BASKET_FILE")
    private boolean isBasket;
    @Column(name = "CREATED")
    private Date create;

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public boolean isBasket() {
        return isBasket;
    }

    public void setBasket(boolean idBasket) {
        this.isBasket = idBasket;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Transient
    private File file;
    @Transient
    private String fileFormat;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        fileName = file.getName();
        String[]array = fileName.replace(".","#").split("#");
        fileFormat = array[array.length-1];

        try {
            BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            setCreatedTime(attrs.creationTime());
            setModiferTime(attrs.lastModifiedTime());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.file = file;
    }

    public SystemUser getUser() {
        return user;
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

    public String getFileFormat() {
        return fileFormat.toLowerCase();
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getFileSize(){
        double size = (getFile().length() / 1024);
        if(size > 1024){
            return String.format("%s Mb",Math.round(new Double((size/1024))));
        }else{
            return String.format("%s Kb",size);
        }
    }
    @Transient
    private FileTime createdTime;
    @Transient
    private FileTime modiferTime;

    public FileTime getModiferTime() {
        return modiferTime;
    }

    public void setModiferTime(FileTime modiferTime) {
        this.modiferTime = modiferTime;
    }

    public String getCreatedTime() {
        DateFormat df = new SimpleDateFormat("MM.dd.yyyy hh:mm:ss");
        String dateCreated = df.format(createdTime.toMillis());
        return dateCreated;
    }

    public void setCreatedTime(FileTime createdTime) {
        this.createdTime = createdTime;
    }
}
