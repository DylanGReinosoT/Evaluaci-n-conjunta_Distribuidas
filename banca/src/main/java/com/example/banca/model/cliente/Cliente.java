package com.example.banca.model.cliente;

import com.example.banca.model.Deuda;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int puntageCrediticio;
    private List<Deuda> deudas;
    private double montoSolicitado;
    private int plazoMeses;
}
