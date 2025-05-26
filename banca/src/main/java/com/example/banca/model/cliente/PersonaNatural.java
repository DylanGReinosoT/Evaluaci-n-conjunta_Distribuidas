package com.example.banca.model.cliente;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Cliente")
@Getter
@Setter
public class PersonaNatural extends Cliente {

    private int edad;
    private double ingresosMensuales;


}
