package org.caykhe.interactiveservice.models;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class VoteId implements Serializable {
    @Serial
    private static final long serialVersionUID = 9026443096854345784L;

    private Integer targetId;
    private Boolean targetType;
    private String username;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteId entity = (VoteId) o;
        return Objects.equals(this.targetId, entity.targetId) &&
                Objects.equals(this.targetType, entity.targetType) &&
                Objects.equals(this.username, entity.username);
    }


    @Override
    public int hashCode() {
        return Objects.hash(targetId, targetType, username);
    }

}