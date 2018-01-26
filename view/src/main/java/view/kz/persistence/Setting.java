package view.kz.persistence;

import view.kz.persistence.common.Identifier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SETTING")
public class Setting extends Identifier{
    @Column(name = "PARAM")
    private String param;
    @Column(name = "VALUE")
    private String value;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
