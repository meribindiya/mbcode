package com.mb.persistance;

import javax.persistence.*;

@Entity
@Table(name = "tbl_constants")
public class Constants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String property;

    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Constants{" +
                "id=" + id +
                ", property='" + property + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
