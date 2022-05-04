package br.com.daniel.java.quarkus.general.adapter.datastore.entitys;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Fruta extends PanacheEntity {
    public String nome;
    public double qtde;
}
