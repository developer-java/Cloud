package view.kz.persistence.common;

/**
 * User: Magjan
 * Date: 05.09.2017
 */
public interface IFileAttach {

    String getFileName();

    void setFileName(String fileName);

    String getContentType();

    void setContentType(String contentType);

    String getMd5();

    void setMd5(String md5);

    String getUid();

    void setUid(String uid);

    byte[] getData();

    void setData(byte[] data);

    String getRelativeFilePath();
}
