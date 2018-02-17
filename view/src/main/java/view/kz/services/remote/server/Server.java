package view.kz.services.remote.server;

import view.kz.ejb.FileManagment;
import view.kz.ejb.UserManagment;
import view.kz.persistence.DicFile;
import view.kz.persistence.SystemUser;
import view.kz.services.remote.common.*;

import javax.ejb.EJB;
import javax.jws.WebService;
import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.util.UUID;

@WebService(endpointInterface = "view.kz.services.remote.common.RemoteServer",
        serviceName = "remoteAPI")
public class Server implements RemoteServer {
    @EJB
    private FileManagment managment;
    @EJB
    private UserManagment userManagment;
    @Override
    public Responce addFile(RFile file) throws IOException {
        Responce responce = new Responce();
        try {
            if(file.getData()==null || file.getData().length==0){
                responce.setStatus(Responce.Status.INNCORRECT_DATA);
                return responce;
            }
            if(file.getFileName()==null){
                responce.setStatus(Responce.Status.INNCORRECT_DATA);
                return responce;
            }
            if(file.getLogin()==null){
                responce.setStatus(Responce.Status.INNCORRECT_DATA);
                return responce;
            }
            if(file.getPassword()==null){
                responce.setStatus(Responce.Status.INNCORRECT_DATA);
                return responce;
            }
            SystemUser user = userManagment.getUserByLoginAndPass(file.getLogin(),file.getPassword());
            if(user==null){
                responce.setStatus(Responce.Status.AUTH_ERROR);
                return responce;
            }
            File newFile = new File(user.getParrentDir()+"\\"+file.getFileName());
            if(newFile.exists()){
                newFile = new File(user.getParrentDir()+"\\"+file.getFileName()+"(COPY)");
            }
            newFile.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(newFile.toPath()));
            bos.write(file.getData());
            bos.flush();
            bos.close();
            DicFile df = new DicFile();
            df.setFile(newFile);
            df.setCreate(new Date());
            df.setPath(newFile.getPath());
            String uid = UUID.randomUUID().toString().replace("-","");
            df.setUid(uid);
            managment.addFile(df);
            responce.setStatus(Responce.Status.SUCCESS);
            responce.setUID(uid);
        }catch (Exception e){
            responce.setStatus(Responce.Status.SERVER_EXCEPTION);
        }
        return responce;
    }

    @Override
    public GetResponce getFile(String UID) {
        GetResponce responce = new GetResponce();
        try{
            String pathByUID = managment.getPathByUID(UID);
            if(pathByUID==null){
                responce.setStatus(Responce.Status.NULL_POINTER);
                return responce;
            }
            File file = new File(pathByUID);
            byte[] bytes = Files.readAllBytes(file.toPath());
            if(bytes==null || bytes.length==0){
                responce.setStatus(Responce.Status.FILE_NOT_FOUND);
                return responce;
            }else{
                responce.setData(bytes);
                responce.setFileName(file.getName());
            }
        }catch (Exception e){
            responce.setStatus(Responce.Status.SERVER_EXCEPTION);
        }
        return responce;
    }
}
