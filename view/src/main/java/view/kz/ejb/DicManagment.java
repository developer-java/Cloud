package view.kz.ejb;

import view.kz.persistence.City;
import view.kz.persistence.Contry;
import view.kz.persistence.common.IDictionary;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DicManagment {
    @PersistenceContext(unitName = "unitBase")
    private EntityManager em;

    public List<City> getCityByContryId(Long id){
        return em.createQuery("SELECT c FROM City c WHERE c.contry.id = :id",City.class)
                .setParameter("id",id)
                .getResultList();
    }
    public List<Contry> getContryList(){
        return em.createQuery("SELECT c FROM Contry c", Contry.class).getResultList();
    }
    public <T extends IDictionary> List<T> getDictionaryValuesByType(Class<T> cl, String... orderParams) {
        StringBuilder orderSb = new StringBuilder();
        for (String s : orderParams) {
            if (!s.isEmpty()) {
                if (orderSb.length() > 0) {
                    orderSb.append(", ");
                }
                orderSb.append("dic.").append(s.trim());
            }
        }
        return em.createQuery("SELECT dic FROM " + cl.getName() + " dic" + (orderSb.length() > 0 ? (" ORDER BY " + orderSb.toString()) : ""), cl).getResultList();
    }
}
