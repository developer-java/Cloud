package service.services.soap.LoginService.server;

import service.services.soap.LoginService.common.LoginService;
import service.services.soap.LoginService.common.User;

import javax.ejb.EJB;

public class Worker implements LoginService {


    @Override
    public User doLogin(User user) {
        return null;
    }
}
