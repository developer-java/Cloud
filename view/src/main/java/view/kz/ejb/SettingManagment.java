package view.kz.ejb;

import view.kz.persistence.Setting;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class SettingManagment {
    @PersistenceContext(unitName = "unitBase")
    private EntityManager em;

    private Map<String,String> params;

    public Map<String, String> getParams() {
        if(params==null){
            List<Setting> settings = em.createQuery("SELECT s FROM Setting s").getResultList();
            params = new HashMap<>();
            for(Setting s : settings){
                params.put(s.getParam(),s.getValue());
            }
        }
        return params;
    }

    public String getValueByParam(String key){
        return getParams().get(key);
    }
}
