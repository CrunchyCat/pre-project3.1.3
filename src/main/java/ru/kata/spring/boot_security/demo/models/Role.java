package ru.kata.spring.boot_security.demo.models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @Column(name = "name")
    private String name;


    public Role() {
    }


    public Role(String name) {
        this.name = name;
    }


    //------------------------GrantedAuthority-----------------------
    @Override
    public String getAuthority() {
        return name;
    }


}
