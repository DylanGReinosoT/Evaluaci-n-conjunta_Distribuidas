package com.example.banca.model.riesgo;


import com.example.banca.model.ResultadoEvalucion;
import com.example.banca.model.cliente.Cliente;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

public class EvaluadorRiesgoAlto extends EvaluadorRiesgo {

    @Override
    public boolean aplica(Cliente cliente) {
        // Ejemplo: se considera si el puntaje crediticio es bajo
        return cliente.getPuntajeCrediticio() < 650;
    }

    @Override
    public ResultadoEvalucion evaluar(Cliente cliente) {
        int puntajeFinal = calcularPuntajeFinal(cliente);
        ResultadoEvalucion resultado = determinarNivelYCondiciones(puntajeFinal, cliente); // Nivel y aptitud base

        resultado.setPuntajeFinal(puntajeFinal);

        // Para el evaluador "Alto", la regla del PDF dice que < 60 es "No Apto".
        // determinarNivelYCondiciones ya setea aprobado=false si el nivel es ALTO.
        if (resultado.isAprobado()) {
            // Esto no debería ocurrir si el puntaje < 60 resulta en ALTO y no aprobado.
            // Si de alguna manera un cliente "Alto" (por puntaje crediticio) termina con puntaje > 60
            // y es apto, se le podrían dar condiciones muy restrictivas.
            // Pero sigamos la tabla: < 60 es NO.
            // Por lo tanto, si isAprobado es true, algo está raro o el puntaje mejoró mucho.
            // Por seguridad, si este evaluador fue seleccionado y el cliente aún aprueba,
            // se le dan condiciones de riesgo MEDIO como mucho.
            resultado.setNivelRiesgo("MEDIO"); // Máximo nivel de aprobación si mejora
            resultado.setMensaje("Cliente apto bajo condiciones especiales y revisión manual.");
            resultado.setTasaInteres(15.0); // Tasa alta
            resultado.setPlazoAprobado(Math.min(cliente.getPlazoEnMeses(), 12)); // Plazo corto
        } else {
            // Ya viene con aprobado=false si el nivel es ALTO o no es apto por otras reglas
            resultado.setMensaje("Cliente no apto para préstamo."); // Mensaje genérico de rechazo
            // El PDF para el ejemplo de Persona Jurídica con riesgo alto (puntaje 35) da este mensaje.
            resultado.setTasaInteres(0);
            resultado.setPlazoAprobado(0);
        }
        return resultado;
    }
}
