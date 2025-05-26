package com.example.banca.model.cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "personas_naturales")
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "cliente_id") // FK que referencia a la tabla clientes.id
public class PersonaNatural extends Cliente {

    private int edad;

    @Column(nullable = false)
    private double ingresoMensual;

    public PersonaNatural(String nombre, int puntajeCrediticio, double montoSolicitado, int plazoEnMeses,
                          int edad, double ingresoMensual) {
        super(nombre, puntajeCrediticio, montoSolicitado, plazoEnMeses);
        this.edad = edad;
        this.ingresoMensual = ingresoMensual;
    }

    @Override
    public double getIngresoReferencial() {
        return this.ingresoMensual; // El ingreso referencial es directamente el mensual
    }

    @Override
    public boolean esAptoParaCredito() {
        // TODO: Implementar lógica específica para determinar si una persona natural es apta.
        // Ejemplo simple:
        if (this.edad < 18 || this.edad > 70) {
            return false;
        }
        if (this.ingresoMensual < 500) { // Umbral de ejemplo
            return false;
        }
        // Considerar deudas vs ingresos (capacidad de pago)
        double capacidadPagoMensual = this.ingresoMensual * 0.4; // Ejemplo: 40% del ingreso
        double cuotaEstimadaCredito = calcularCuotaEstimada(getMontoSolicitado(), getPlazoEnMeses()); // Necesitarías una función para esto
        double montoTotalDeudasActuales = getMontoDeudas(); // Esto asume que Deuda tiene un plazo y puedes calcular una cuota mensual

        // Aquí la lógica de `getMontoDeudas` debería ser más compleja si las deudas tienen diferentes plazos
        // Para simplificar, asumamos que getMontoDeudas() da un total que podemos comparar
        // o que se calcula la cuota mensual de las deudas existentes.

        // Por ahora, una lógica más simple:
        return getMontoDeudas() < (this.ingresoMensual * 5); // Ejemplo: deudas totales no superan 5 veces el ingreso mensual
    }

    // Método auxiliar de ejemplo (simplificado, no financieramente exacto)
    private double calcularCuotaEstimada(double monto, int plazo) {
        if (plazo <= 0) return Double.MAX_VALUE;
        // Esta es una simplificación extrema. Una cuota real implica tasa de interés.
        return monto / plazo;
    }
}
