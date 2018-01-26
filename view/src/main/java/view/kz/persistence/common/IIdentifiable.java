package view.kz.persistence.common;

import java.io.Serializable;

public interface IIdentifiable extends Serializable {
    Long getId();

    void setId(Long id);
}
