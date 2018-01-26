package service.services.soap.testService;

import javax.jws.WebService;

public class W implements Worker {


    @Override
    public String getTest() {
        return "test";
    }
}
