package view.kz.util;

import view.kz.ejb.FileManagment;
import view.kz.persistence.DicFile;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import java.io.File;
import java.util.Calendar;
import java.util.List;

@Stateless
public class Shedule {
    @EJB
    private FileManagment managment;

    @Schedule(minute = "*/1", hour = "*", persistent = false)//один раз в 10 минут
    public void deleteBasket(){
        List<DicFile> basketFiles = managment.getBasketFiles();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        for(DicFile d : basketFiles){
            if(d.getCreate().before(calendar.getTime())){
                managment.delete(d);
                File file = new File(d.getPath());
                file.delete();
                System.out.println("DELETE IS FILE: "+d.getFileName());
            }else{
                System.out.println("FILE IS NOT OLD: "+d.getFileName());
            }
        }
    }
}
