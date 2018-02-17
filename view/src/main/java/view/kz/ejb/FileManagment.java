package view.kz.ejb;

import view.kz.persistence.DicFile;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class FileManagment {
    @PersistenceContext(unitName = "unitBase")
    private EntityManager em;

    public void addFile(DicFile file){
        em.persist(file);
    }
    public String getPathByUID(String uid){
        List<String> res = em.createQuery("SELECT f.path FROM DicFile f WHERE f.uid = :uid")
                .setParameter("uid",uid)
                .getResultList();
        if(res==null || res.isEmpty()){
            return null;
        }
        return res.get(0);
    }
    public String getUidByPath(String path){
            List<String> res = em.createQuery("SELECT f.uid FROM DicFile f where f.path = :path")
                .setParameter("path",path)
                .getResultList();
        if(res==null || res.isEmpty()){
            return null;
        }
        return res.get(0);
    }
    public List<DicFile> getBasketFiles(){
        return em.createQuery("SELECT df FROM DicFile df WHERE df.isBasket = 1").getResultList();
    }
    public void delete(DicFile dicFile){
        em.remove(dicFile);
    }
}
