package view.kz.web;

import view.kz.ejb.DicManagment;
import view.kz.persistence.City;
import view.kz.persistence.Contry;
import view.kz.persistence.common.IDictionary;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@ManagedBean(name = "dictionaryModel")
@SessionScoped
public class DictionaryModel {
    @EJB
    private DicManagment dicManagementEjb;

    private Converter converter = new Converter() {

        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String s) {
            try {
                if(s != null && !s.isEmpty()) {
                    String dicType = (String)component.getAttributes().get("dicType");
                    if(dicType != null) {
                        List<? extends IDictionary> result = cacheMap.get(dicType);
                        if(result == null) {
                            result = getValues(dicType);
                        }
                        Class<? extends IDictionary> cl = dicTypes.get(dicType);
                        IDictionary dic = cl.newInstance();
                        dic.setId(Long.valueOf(s));
                        int i;
                        if(result != null && ((i = result.indexOf(dic)) >= 0)) {
                            return result.get(i);
                        }
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object o) {
            if(o != null) {
                if(o instanceof IDictionary) {
                    return ((IDictionary)o).getId().toString();
                }
                else if(o instanceof String) {
                    return "";
                }
            }
            return null;
        }
    };

    private static final Map<String, Class<? extends IDictionary>> dicTypes = new HashMap<String, Class<? extends IDictionary>>();
    static {
        dicTypes.put("DIC_CONTRY", Contry.class);
        dicTypes.put("DIC_CITY", City.class);
    }
    private static final Map<String, List<? extends IDictionary>> cacheMap = new HashMap<String, List<? extends IDictionary>>();

    public Converter getConverter() {
        return converter;
    }

    public List<? extends IDictionary> getValues(String name) throws Exception {
        Class<? extends IDictionary> cl = dicTypes.get(name);
        if(cl == null) {
            throw new Exception("Unknown dictionary type " + name);
        }
        else {
            List<? extends IDictionary> result = cacheMap.get(name);
            if(result == null) {
                result = dicManagementEjb.getDictionaryValuesByType(cl);
                cacheMap.put(name, result);
            }
            return result;
        }
    }

    public String getDicValueByCode(String dicType, String code) {
        try {
            if(dicType != null) {

            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void clearCacheMap() {
        cacheMap.clear();
    }
}