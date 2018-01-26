package view.kz.persistence.common;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)//использовать поля, а не методы get
@MappedSuperclass
public abstract class Dictionary extends Identifier implements IDictionary {

    @XmlElement(required = true, nillable = false, namespace = "", name = "code", type = String.class)
    @Basic
    @Column(name = "CODE")
    protected String code;

    @XmlElement(required = true, nillable = false, namespace = "", name = "valueRu", type = String.class)
    @Basic
    @Column(name = "VALUE_RU")
    protected String valueRu;

    @XmlElement(required = true, nillable = false, namespace = "", name = "valueKz", type = String.class)
    @Basic
    @Column(name = "VALUE_KZ")
    protected String valueKz;

    @Transient
    protected String valueEn;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValueRu() {
        return valueRu;
    }

    public void setValueRu(String valueRu) {
        this.valueRu = valueRu;
    }

    public String getValueKz() {
        return valueKz;
    }

    public void setValueKz(String valueKz) {
        this.valueKz = valueKz;
    }

    public String getValueEn() {
        return valueEn;
    }

    public void setValueEn(String valueEn) {
        this.valueEn = valueEn;
    }

    @Transient
    public String getValue() {
        return valueRu;
    }

    @Override
    public String toString() {
        return getValue();
    }
}