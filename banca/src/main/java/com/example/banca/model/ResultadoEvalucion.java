package com.example.banca.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString // Útil para debugging y logs
public class ResultadoEvalucion {

    private String nivelRiesgo;
    private boolean aprobado;
    private int puntajeFinal;
    private String mensaje;
    private double tasaInteres;
    private int plazoAprobado;
}