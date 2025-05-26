package com.example.banca.model.cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "personas_juridicas")
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "cliente_id") // FK que referencia a la tabla clientes.id
public class PersonaJuridica extends Cliente {

    @Column(nullable = false)
    private int antiguedadAnios;

    @Column(nullable = false)
    private double ingresoAnual;

    private int empleados;

    public PersonaJuridica(String nombre, int puntajeCrediticio, double montoSolicitado, int plazoEnMeses,
                           int antiguedadAnios, double ingresoAnual, int empleados) {
        super(nombre, puntajeCrediticio, montoSolicitado, plazoEnMeses);
        this.antiguedadAnios = antiguedadAnios;
        this.ingresoAnual = ingresoAnual;
        this.empleados = empleados;
    }

    @Override
    public double getIngresoReferencial() {
        if (this.ingresoAnual <= 0) {
            return 0.0;
        }
        return this.ingresoAnual / 12; // Ingreso referencial mensualizado
    }

    @Override
    public boolean esAptoParaCredito() {
        // TODO: Implementar lógica específica para determinar si una persona jurídica es apta.
        // Ejemplo simple:
        if (this.antiguedadAnios < 2) { // Mínimo 2 años de antigüedad
            return false;
        }
        if (getIngresoReferencial() < 2000) { // Ingreso mensual referencial mínimo
            return false;
        }
        // Considerar ratio de endeudamiento, sector, etc.
        return getMontoDeudas() < (getIngresoReferencial() * 10); // Ejemplo muy simplificado
    }
}
