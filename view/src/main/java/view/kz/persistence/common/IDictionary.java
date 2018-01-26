package view.kz.persistence.common;

public interface IDictionary extends IIdentifiable {

    public String getCode();

    public void setCode(String code);

    public String getValueRu();

    public void setValueRu(String valueRu);

    public String getValueKz();

    public void setValueKz(String valueKz);

    public String getValue();
}