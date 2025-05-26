package com.example.banca.model.cliente;

import com.example.banca.model.Deuda;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor // Lombok generará un constructor sin argumentos
@Inheritance(strategy = InheritanceType.JOINED) // Estrategia de herencia
@Table(name = "clientes") // Tabla base para clientes
public abstract class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private int puntajeCrediticio; // Se podría calcular o asignar externamente

    // mappedBy indica que la entidad Deuda es la propietaria de la relación (tiene el @ManyToOne)
    // CascadeType.ALL: operaciones en Cliente se propagan a Deuda (guardar, borrar)
    // orphanRemoval = true: si una deuda se quita de esta lista, se borra de la BD
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Deuda> deudasActuales = new ArrayList<>();

    @Column(nullable = false)
    private double montoSolicitado; // El monto que este cliente está solicitando actualmente

    @Column(nullable = false)
    private int plazoEnMeses; // El plazo que este cliente está solicitando

    // Constructor para campos comunes (subclases llamarán a super())
    protected Cliente(String nombre, int puntajeCrediticio, double montoSolicitado, int plazoEnMeses) {
        this.nombre = nombre;
        this.puntajeCrediticio = puntajeCrediticio;
        this.montoSolicitado = montoSolicitado;
        this.plazoEnMeses = plazoEnMeses;
    }

    // Métodos abstractos (a ser implementados por PersonaNatural y PersonaJuridica)
    public abstract double getIngresoReferencial();
    public abstract boolean esAptoParaCredito();

    // Método concreto
    public double getMontoDeudas() {
        if (this.deudasActuales == null || this.deudasActuales.isEmpty()) {
            return 0.0;
        }
        return this.deudasActuales.stream()
                .mapToDouble(Deuda::getMonto)
                .sum();
    }

    // Método para añadir deuda de forma conveniente y mantener la bidireccionalidad
    public void addDeuda(Deuda deuda) {
        this.deudasActuales.add(deuda);
        deuda.setCliente(this);
    }

    public void removeDeuda(Deuda deuda) {
        this.deudasActuales.remove(deuda);
        deuda.setCliente(null);
    }
}
