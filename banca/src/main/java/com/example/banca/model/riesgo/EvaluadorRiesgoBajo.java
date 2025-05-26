package com.example.banca.model.riesgo;

import com.example.banca.model.ResultadoEvalucion;
import com.example.banca.model.cliente.Cliente;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

public class EvaluadorRiesgoBajo extends EvaluadorRiesgo {

    @Override
    public boolean aplica(Cliente cliente) {
        // Ejemplo: Este evaluador es "considerado" si el puntaje crediticio inicial es muy bueno
        // La selección final podría ser más compleja en el servicio.
        // O, si solo hay un flujo de evaluación, este método podría siempre devolver true
        // y la diferenciación se hace completamente dentro de evaluar() y determinarNivelYCondiciones().
        // Por ahora, asumamos que hay una pre-selección o que el servicio tiene una lista
        // y prueba cuál `aplica`.
        return cliente.getPuntajeCrediticio() >= 750; // Umbral de ejemplo para "considerar" este evaluador
    }

    @Override
    public ResultadoEvalucion evaluar(Cliente cliente) {
        int puntajeFinal = calcularPuntajeFinal(cliente);
        ResultadoEvalucion resultado = determinarNivelYCondiciones(puntajeFinal, cliente); // Obtiene nivel y si está aprobado base

        resultado.setPuntajeFinal(puntajeFinal); // Aseguramos que el puntaje esté

        if (resultado.isAprobado()) {
            if ("BAJO".equals(resultado.getNivelRiesgo())) {
                resultado.setMensaje("Cliente apto para préstamo con condiciones preferenciales.");
                resultado.setTasaInteres(6.5); // Según ejemplo PDF
                resultado.setPlazoAprobado(cliente.getPlazoEnMeses()); // Asumimos que se aprueba el solicitado
            } else {
                // Si un cliente con puntaje alto (>750) cae en MEDIO después de penalizaciones
                resultado.setNivelRiesgo("MEDIO"); // Reajustar si es necesario
                resultado.setMensaje("Cliente apto para préstamo con condiciones ajustadas.");
                resultado.setTasaInteres(8.0); // Tasa para Medio
                resultado.setPlazoAprobado(cliente.getPlazoEnMeses());
            }
        } else {
            if ("ALTO".equals(resultado.getNivelRiesgo())) {
                resultado.setMensaje("Cliente no apto para préstamo.");
            } else if (!cliente.esAptoParaCredito()) {
                resultado.setMensaje("Cliente no cumple con los requisitos básicos de elegibilidad.");
                // Asegurar que si no es apto, el estado sea de rechazo
                resultado.setNivelRiesgo("ALTO"); // O un estado específico de no apto
                resultado.setAprobado(false);
            } else {
                resultado.setMensaje("Préstamo no aprobado. Puntaje insuficiente o riesgo elevado.");
            }
            resultado.setTasaInteres(0);
            resultado.setPlazoAprobado(0);
        }
        return resultado;
    }
}
