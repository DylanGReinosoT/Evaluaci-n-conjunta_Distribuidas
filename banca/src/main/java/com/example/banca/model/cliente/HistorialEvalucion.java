package com.example.banca.model.cliente;

import com.example.banca.model.Deuda;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;


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
    private String clienteNombre; // Nombre del cliente en el momento de la evaluación

    @Column(nullable = false)
    private String tipoCliente; // "NATURAL" o "JURIDICA"

    @Column(nullable = false)
    private double montoSolicitado; // El monto que se evaluó

    @Column(nullable = false)
    private int plazoEnMeses; // El plazo que se evaluó

    private String nivelRiesgo; // Resultado: "BAJO", "MEDIO", "ALTO"

    private boolean aprobado; // Resultado: true/false

    @Column(nullable = false)
    private LocalDateTime fechaConsulta; // Cuándo se hizo la consulta

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false) // Relación con la entidad Cliente
    private Cliente cliente;

    // Constructor para facilitar la creación desde el servicio
    public HistorialEvalucion(String clienteNombre, String tipoCliente, double montoSolicitado,
                              int plazoEnMeses, String nivelRiesgo, boolean aprobado,
                              LocalDateTime fechaConsulta, Cliente cliente) {
        this.clienteNombre = clienteNombre;
        this.tipoCliente = tipoCliente;
        this.montoSolicitado = montoSolicitado;
        this.plazoEnMeses = plazoEnMeses;
        this.nivelRiesgo = nivelRiesgo;
        this.aprobado = aprobado;
        this.fechaConsulta = fechaConsulta;
        this.cliente = cliente;
    }
}
