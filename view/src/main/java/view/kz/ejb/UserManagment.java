package view.kz.ejb;

import view.kz.persistence.SystemUser;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UserManagment {

    @PersistenceContext(unitName = "unitBase")
    private EntityManager em;

    public SystemUser getUserByLoginAndPass(String login, String password){
        List<SystemUser> users = em.createQuery("SELECT u FROM SystemUser u WHERE u.login = :login AND u.password = :password")
                .setParameter("login",login)
                .setParameter("password",password)
                    .getResultList();
        if(users!=null && !users.isEmpty()){
            return users.get(0);
        }
        return null;
    }

    public void saveUser(SystemUser user){
        em.persist(user);
    }

}
