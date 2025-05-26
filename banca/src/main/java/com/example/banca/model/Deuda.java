package com.example.banca.model;


import com.example.banca.model.cliente.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "deuda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Deuda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private double monto;

    @Column(nullable = false)
    private int plazoMeses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Constructor sin ID y sin Cliente (para cuando se crea una deuda nueva antes de asociarla)
    public Deuda(double monto, int plazoMeses) {
        this.monto = monto;
        this.plazoMeses = plazoMeses;
    }

}
