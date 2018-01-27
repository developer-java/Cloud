package view.kz.util;

import view.kz.persistence.DicFile;

import javax.faces.context.FacesContext;
import java.io.File;

public class Utils {

    public static String getImagePath(DicFile file){
        String image = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/resources/img/";
        if(file==null){
            return null;
        }
        if(file.getFile().isFile()){
            if(file.getFileFormat().equals("jpg")){
                image+="jpg.png";
            }else if(file.getFileFormat().equals("png")){
                image+="png.png";
            }
            else if(file.getFileFormat().equals("pdf")){
                image+="pdf.png";
            }
            else if(file.getFileFormat().equals("zip")){
                image+="zip.png";
            }
            else if(file.getFileFormat().equals("xls")){
                image+="xls.png";
            }
            else if(file.getFileFormat().equals("gif")){
                image+="gif.png";
            }
            else if(file.getFileFormat().equals("rar")){
                image+="rar.png";
            }
            else if (file.getFileFormat().equals("wav")){
                image+="wav.png";
            }
            else if (file.getFileFormat().equals("wmv")){
                image+="wmv.png";
            }
            else if (file.getFileFormat().equals("proj")){
                image+="proj.png";
            }
            else if (file.getFileFormat().equals("rtf")){
                image+="rtf.png";
            }
            else if(file.getFileFormat().equals("exe")){
                image+="exe.png";
            }
            else if(file.getFileFormat().equals("apk")){
                image+="apk.png";
            }
            else if(file.getFileFormat().equals("doc") || file.getFileFormat().equals("docx")){
                image+="office.png";
            }
            else if(file.getFileFormat().equals("txt")){
                image+="text.png";
            }
            else if(file.getFileFormat().equals("jar") || file.getFileFormat().equals("java") ){
                image+="java.png";
            }
            else if(file.getFileFormat().equals("ini")){
                image+="conf.jpg";
            }
            else if(file.getFileFormat().equals("html")||file.getFileFormat().equals("xhtml")||file.getFileFormat().equals("php")){
                image+="web.png";
            }
            else if(file.getFileFormat().equals("css")){
                image+="css.png";
            }
            else{
                image+="file.png";
            }
        }else{
            image+="folder.png";
        }
        return image;
    }

    public static String handleFilename(File file){
        if(file.getName().length()>17){
            return file.getName().substring(0,14)+"...";
        }
        return file.getName();
    }
}
