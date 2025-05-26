package com.example.banca.model.riesgo;


import com.example.banca.model.ResultadoEvalucion;
import com.example.banca.model.cliente.Cliente;
import com.example.banca.model.cliente.PersonaJuridica;
import com.example.banca.model.cliente.PersonaNatural;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

public abstract class EvaluadorRiesgo {

    protected static final int PUNTAJE_INICIAL = 100;

    // Constructor (opcional, si no se necesita estado específico en la superclase)
    // public EvaluadorRiesgo() {}

    /**
     * Determina si este evaluador es aplicable a un cliente específico.
     * @param cliente El cliente a evaluar.
     * @return true si este evaluador puede manejar al cliente, false de lo contrario.
     */
    public abstract boolean aplica(Cliente cliente);

    /**
     * Realiza la evaluación de riesgo para el cliente.
     * @param cliente El cliente a evaluar.
     * @return El resultado de la evaluación.
     */
    public abstract ResultadoEvalucion evaluar(Cliente cliente);

    /**
     * Calcula el puntaje final basado en el puntaje inicial y las penalizaciones.
     * @param cliente El cliente para el cual calcular el puntaje.
     * @return El puntaje final.
     */
    protected int calcularPuntajeFinal(Cliente cliente) {
        int puntaje = PUNTAJE_INICIAL;

        // Penalización por Puntaje Crediticio
        if (cliente.getPuntajeCrediticio() < 650) {
            puntaje -= 30;
        }

        // Penalizaciones específicas por tipo de cliente
        if (cliente instanceof PersonaNatural natural) {
            double ingresoMensual = natural.getIngresoReferencial(); // Es el ingreso mensual
            if (ingresoMensual > 0) { // Evitar división por cero
                // Penalización por Deudas (Natural)
                if ((cliente.getMontoDeudas() / ingresoMensual) > 0.40) {
                    puntaje -= 15;
                }
                // Penalización por Monto Solicitado (Natural)
                if ((cliente.getMontoSolicitado() / ingresoMensual) > 0.50) {
                    puntaje -= 10;
                }
            }
        } else if (cliente instanceof PersonaJuridica juridica) {
            double ingresoAnual = juridica.getIngresoAnual(); // Necesitamos el anual para estas reglas
            if (ingresoAnual > 0) { // Evitar división por cero
                // Penalización por Deudas (Jurídica)
                if ((cliente.getMontoDeudas() * 12 / ingresoAnual) > 0.35) { // (MontoDeudas mensualizadas / ingresoAnual) > 0.35
                    // OJO: getMontoDeudas() es el total, no la cuota mensual.
                    // La regla es "Deudas > 35% del ingreso anual".
                    // Si getMontoDeudas es el *total* de deudas, la comparación es directa.
                    // Si la interpretamos como "cuota mensual de deudas > 35% del ingreso anual/12",
                    // la lógica sería diferente. Asumiré que es "Monto total deudas > 35% del ingreso anual"
                    // Para ser consistentes con el PDF: "Deudas > 35% del ingreso anual (jurídica)"
                    // El monto total de deudas vs el ingreso anual.
                    if (cliente.getMontoDeudas() > (ingresoAnual * 0.35)) {
                        puntaje -= 20;
                    }
                }
                // Penalización por Monto Solicitado (Jurídica)
                if ((cliente.getMontoSolicitado() / ingresoAnual) > 0.30) {
                    puntaje -= 15;
                }
            }
            // Penalización implícita por Antigüedad (Persona Jurídica)
            // Esta regla "cliente no apto" se maneja mejor en PersonaJuridica.esAptoParaCredito()
            // pero el PDF la pone junto a las penalizaciones. Si un cliente no es apto,
            // la evaluación debería detenerse o resultar en rechazo directo.
            // Aquí solo aplicamos la penalización si sigue siendo apto tras la verificación de `esAptoParaCredito`.
            // Sin embargo, el PDF lo da como una condición que *impide* la aprobación.
            // Lo dejaremos para `esAptoParaCredito`.
        }
        return Math.max(0, puntaje); // El puntaje no debe ser menor que 0
    }

    /**
     * Determina el nivel de riesgo y si es apto basado en el puntaje final.
     * @param puntajeFinal El puntaje final calculado.
     * @param cliente El cliente (para verificar si es apto por otras condiciones)
     * @return Un objeto ResultadoEvalucion parcialmente llenado con nivelRiesgo, aprobado.
     */
    protected ResultadoEvalucion determinarNivelYes(int puntajeFinal, Cliente cliente) {
        ResultadoEvalucion resultadoParcial = new ResultadoEvalucion();
        resultadoParcial.setPuntajeFinal(puntajeFinal);

        boolean esAptoPorReglasEspecificas = cliente.esAptoParaCredito();

        if (puntajeFinal >= 80) {
            resultadoParcial.setNivelRiesgo("BAJO");
            resultadoParcial.setAprobado(esAptoPorReglasEspecificas); // Solo aprueba si cumple condiciones de aptitud base
        } else if (puntajeFinal >= 60) {
            resultadoParcial.setNivelRiesgo("MEDIO");
            resultadoParcial.setAprobado(esAptoPorReglasEspecificas);
        } else {
            resultadoParcial.setNivelRiesgo("ALTO");
            resultadoParcial.setAprobado(false); // Alto riesgo nunca se aprueba según la tabla
        }

        // Si no es apto por reglas específicas (ej. antigüedad de PersonaJuridica), se rechaza independientemente del puntaje.
        if (!esAptoPorReglasEspecificas) {
            resultadoParcial.setAprobado(false);
            // Si el puntaje era bueno, pero no es apto, el nivel de riesgo se mantiene,
            // pero el mensaje reflejará la no aptitud.
            if (resultadoParcial.getNivelRiesgo() == null ) { // Si no se asignó antes (puntaje < 60 ya es ALTO)
                resultadoParcial.setNivelRiesgo("ALTO"); // Si no es apto, se considera alto riesgo para el mensaje.
            }
        }
        return resultadoParcial;
    }
}
