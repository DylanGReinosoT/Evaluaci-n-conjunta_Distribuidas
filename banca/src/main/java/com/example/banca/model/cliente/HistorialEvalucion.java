package com.example.banca.model.cliente;

import com.example.banca.model.Deuda;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "historial_evaluaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class HistorialEvalucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private int puntajeCrediticio;
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Deuda> deudasActuales = new ArrayList<>();

    @Column(nullable = false)
    private double montoSolicitado;
    @Column(nullable = false)
    private int plazoEnMeses;


    protected void Cliente(String nombre, int puntajeCrediticio, double montoSolicitado, int plazoEnMeses) {
        this.nombre = nombre;
        this.puntajeCrediticio = puntajeCrediticio;
        this.montoSolicitado = montoSolicitado;
        this.plazoEnMeses = plazoEnMeses;
    }


    public double getIngresoReferencial() {
        return 0;
    }

    public boolean esAptoParaCredito() {
        return false;
    }


    public double getMontoDeudas() {
        if (this.deudasActuales == null || this.deudasActuales.isEmpty()) {
            return 0.0;
        }
        return this.deudasActuales.stream()
                .mapToDouble(Deuda::getMonto)
                .sum();
    }


    public void addDeuda(Deuda deuda) {
        this.deudasActuales.add(deuda);
        deuda.setCliente(this);
    }

    public void removeDeuda(Deuda deuda) {
        this.deudasActuales.remove(deuda);
        deuda.setCliente(null);
    }
}
