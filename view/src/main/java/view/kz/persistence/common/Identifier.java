package view.kz.persistence.common;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by IntelliJ IDEA.
 * User: Magjan
 * Date: 03.09.17
 */
@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)//использовать поля, а не методы get
@MappedSuperclass
public abstract class Identifier implements IIdentifiable {

    @XmlTransient
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    protected Long id;

    @XmlTransient
    @Transient
    protected String toString;

    @XmlTransient
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Identifier that = (Identifier) o;

        if (getId() != null) {
            return getId().equals(that.getId());
        } else {
            return super.equals(o);
        }
    }

    public int hashCode() {
        return getId() != null ? getId().hashCode() : super.hashCode();
    }

    public String toString() {
        if (toString == null) {
            toString = generateToString();
        }
        return toString;
    }

    protected String generateToString() {
        return this.getClass().getName() + "/" + (id != null ? id.toString() : null);
    }
}
