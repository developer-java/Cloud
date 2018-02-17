package view.kz.services.remote.common;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.io.IOException;

@WebService
public interface RemoteServer {

    @WebMethod
    @WebResult(name = "responce")
    public Responce addFile(@WebParam(name = "rFile")RFile file) throws IOException;

    @WebMethod
    @WebResult(name = "responce")
    public GetResponce getFile(String UID);

}
