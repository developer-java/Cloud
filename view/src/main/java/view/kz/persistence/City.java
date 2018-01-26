package view.kz.persistence;

import view.kz.persistence.common.Dictionary;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;
@Entity
@Table(name = "DIC_CITY")
public class City extends Dictionary {
    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "CONTRY_ID")
    private Contry contry;

    public Contry getContry() {
        return contry;
    }

    public void setContry(Contry contry) {
        this.contry = contry;
    }
}
