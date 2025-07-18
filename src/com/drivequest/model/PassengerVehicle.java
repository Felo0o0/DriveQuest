/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.model;

/**
 * Clase que representa un vehiculo de pasajeros en el sistema.
 * <p>
 * Extiende la clase abstracta Vehicle e implementa sus metodos especificos
 * para los vehiculos destinados al transporte de pasajeros.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class PassengerVehicle extends Vehicle {
    
    /** Capacidad maxima de pasajeros */
    private int passengerCapacity;
    
    /**
     * Constructor vacio para la clase PassengerVehicle.
     */
    public PassengerVehicle() {
        super();
        this.passengerCapacity = 0;
    }
    
    /**
     * Constructor con parametros para la clase PassengerVehicle.
     * 
     * @param licensePlate Patente del vehiculo
     * @param model Modelo del vehiculo
     * @param year Año del vehiculo
     * @param dailyPrice Precio diario de alquiler
     * @param rentalDays Duracion del alquiler en dias
     * @param passengerCapacity Capacidad maxima de pasajeros
     */
    public PassengerVehicle(String licensePlate, String model, int year, double dailyPrice, 
                            int rentalDays, int passengerCapacity) {
        super(licensePlate, model, year, dailyPrice, rentalDays);
        this.passengerCapacity = passengerCapacity;
    }
    
    /**
     * Obtiene la capacidad maxima de pasajeros del vehiculo.
     * 
     * @return Capacidad maxima de pasajeros
     */
    public int getPassengerCapacity() {
        return passengerCapacity;
    }
    
    /**
     * Establece la capacidad maxima de pasajeros del vehiculo.
     * 
     * @param passengerCapacity Nueva capacidad maxima de pasajeros
     */
    public void setPassengerCapacity(int passengerCapacity) {
        if (passengerCapacity < 0) {
            throw new IllegalArgumentException("La capacidad de pasajeros no puede ser negativa");
        }
        this.passengerCapacity = passengerCapacity;
    }
    
    /**
     * Implementa el metodo abstracto para mostrar los datos especificos
     * del vehiculo de pasajeros.
     * 
     * @return String con los datos formateados del vehiculo de pasajeros
     */
    @Override
    public String showData() {
        return toString() + ", Capacidad de pasajeros: " + passengerCapacity;
    }
    
    /**
     * Implementa el método abstracto para calcular el descuento
     * específico para vehículos de pasajeros.
     * 
     * @return Valor del descuento
     */
    @Override
    public double calculateDiscount() {
        double discount = calculateSubtotal() * PASSENGER_DISCOUNT;
        
        // Bonificación adicional para vehículos con alta capacidad de pasajeros
        if (isLongTermRental() && passengerCapacity > 15) {
            discount += 8000; // Bonificación extra para vehículos de gran capacidad en alquileres largos
        } else if (passengerCapacity > 8) {
            discount += 3000; // Bonificación para vehículos de capacidad media
        }
        
        return discount;
    }
    
    /**
     * Sobreescribe el metodo toString para obtener una representacion en texto del vehiculo de pasajeros.
     * 
     * @return Representacion en texto del vehiculo de pasajeros
     */
    @Override
    public String toString() {
        return super.toString() + " [Vehiculo de Pasajeros]";
    }
}