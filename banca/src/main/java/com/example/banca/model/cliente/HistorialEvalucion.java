package com.example.banca.model.cliente;

import java.time.LocalDate;

public class HistorialEvalucion {
    private Long id;
    private String clienteNombre;
    private String tipoCliente;
    private double montoSolicitado;
    private int plazoMeses;
    private String nivelRiesgo;
    private boolean aprobado;
    private LocalDate fechaConsulta;
}
