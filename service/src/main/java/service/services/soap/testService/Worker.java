package service.services.soap.testService;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name = "a", targetNamespace = "http://common.dip.com/")
public interface Worker {

    @WebMethod
    String getTest();
}
