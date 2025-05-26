package com.example.banca.model.cliente;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Cliente")
@Getter
@Setter

public class PersonaJuridica extends Cliente {
    private int antiguedadAnios;
    private double ingresAnual;
    private int empleados;
}
