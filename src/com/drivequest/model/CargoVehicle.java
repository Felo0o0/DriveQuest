/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.model;

/**
 * Clase que representa un vehiculo de carga en el sistema.
 * <p>
 * Extiende la clase abstracta Vehicle e implementa sus metodos especificos
 * para los vehiculos destinados al transporte de carga.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class CargoVehicle extends Vehicle {
    
    /** Capacidad de carga en kilogramos */
    private int loadCapacity;
    
    /**
     * Constructor vacio para la clase CargoVehicle.
     */
    public CargoVehicle() {
        super();
        this.loadCapacity = 0;
    }
    
    /**
     * Constructor con parametros para la clase CargoVehicle.
     * 
     * @param licensePlate Patente del vehiculo
     * @param model Modelo del vehiculo
     * @param year Año del vehiculo
     * @param dailyPrice Precio diario de alquiler
     * @param rentalDays Duracion del alquiler en dias
     * @param loadCapacity Capacidad de carga en kilogramos
     */
    public CargoVehicle(String licensePlate, String model, int year, double dailyPrice, 
                         int rentalDays, int loadCapacity) {
        super(licensePlate, model, year, dailyPrice, rentalDays);
        this.loadCapacity = loadCapacity;
    }
    
    /**
     * Obtiene la capacidad de carga del vehiculo.
     * 
     * @return Capacidad de carga en kilogramos
     */
    public int getLoadCapacity() {
        return loadCapacity;
    }
    
    /**
     * Establece la capacidad de carga del vehiculo.
     * 
     * @param loadCapacity Nueva capacidad de carga en kilogramos
     */
    public void setLoadCapacity(int loadCapacity) {
        if (loadCapacity < 0) {
            throw new IllegalArgumentException("La capacidad de carga no puede ser negativa");
        }
        this.loadCapacity = loadCapacity;
    }
    
    /**
     * Implementa el metodo abstracto para mostrar los datos especificos
     * del vehiculo de carga.
     * 
     * @return String con los datos formateados del vehiculo de carga
     */
    @Override
    public String showData() {
        return toString() + ", Capacidad de carga: " + loadCapacity + " kg";
    }
    
    /**
     * Implementa el método abstracto para calcular el descuento
     * específico para vehículos de carga.
     * 
     * @return Valor del descuento
     */
    @Override
    public double calculateDiscount() {
        double discount = calculateSubtotal() * CARGO_DISCOUNT;
        
        // Bonificación adicional para alquileres de larga duración
        if (isLongTermRental() && loadCapacity > 5000) {
            discount += 5000; // Bonificación extra para vehículos de gran capacidad en alquileres largos
        }
        
        return discount;
    }
    
    /**
     * Sobreescribe el metodo toString para obtener una representacion en texto del vehiculo de carga.
     * 
     * @return Representacion en texto del vehiculo de carga
     */
    @Override
    public String toString() {
        return super.toString() + " [Vehiculo de Carga]";
    }
}