package view.kz.util;

import java.io.File;

public class Utils {

    public static String handleFilename(File file){
        if(file.getName().length()>17){
            return file.getName().substring(0,14)+"...";
        }
        return file.getName();
    }
    public static String handleFilename(String filename){
        if(filename.length()>17){
            return filename.substring(0,14)+"...";
        }
        return filename;
    }
}
