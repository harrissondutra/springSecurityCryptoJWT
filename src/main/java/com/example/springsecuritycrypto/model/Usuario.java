package com.example.springsecuritycrypto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "usuario")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;


}
