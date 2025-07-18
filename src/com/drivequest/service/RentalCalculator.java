/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.service;

import com.drivequest.model.Vehicle;
import com.drivequest.model.CargoVehicle;
import com.drivequest.model.PassengerVehicle;

/**
 * Clase para realizar calculos relacionados con el alquiler de vehiculos.
 * <p>
 * Proporciona metodos para calcular costos, descuentos y generar resumenes
 * de alquiler para diferentes tipos de vehiculos.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class RentalCalculator {
    
    /**
     * Calcula el costo total de un alquiler incluyendo impuestos y descuentos.
     * 
     * @param vehicle Vehiculo para el calculo
     * @return Costo total del alquiler
     * @throws IllegalArgumentException Si el vehículo es null
     */
    public static double calculateTotalCost(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("El vehículo no puede ser null");
        }
        return vehicle.calculateTotalAmount();
    }
    
    /**
     * Genera un resumen del alquiler con los principales datos.
     * 
     * @param vehicle Vehiculo para generar el resumen
     * @return String con el resumen del alquiler
     * @throws IllegalArgumentException Si el vehículo es null
     */
    public static String generateRentalSummary(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("El vehículo no puede ser null");
        }
        
        StringBuilder summary = new StringBuilder();
        
        summary.append("RESUMEN DE ALQUILER\n");
        summary.append("-------------------\n");
        summary.append(String.format("Patente: %s\n", vehicle.getLicensePlate()));
        summary.append(String.format("Modelo: %s (%d)\n", vehicle.getModel(), vehicle.getYear()));
        summary.append(String.format("Días de alquiler: %d\n", vehicle.getRentalDays()));
        summary.append(String.format("Precio diario: $%.2f\n", vehicle.getDailyPrice()));
        summary.append("Tipo: ");
        
        if (vehicle instanceof CargoVehicle) {
            CargoVehicle cargoVehicle = (CargoVehicle) vehicle;
            summary.append("Vehículo de Carga\n");
            summary.append(String.format("Capacidad: %d kg\n", cargoVehicle.getLoadCapacity()));
        } else if (vehicle instanceof PassengerVehicle) {
            PassengerVehicle passengerVehicle = (PassengerVehicle) vehicle;
            summary.append("Vehículo de Pasajeros\n");
            summary.append(String.format("Capacidad: %d pasajeros\n", passengerVehicle.getPassengerCapacity()));
        }
        
        summary.append("\nDETALLE DE COSTOS\n");
        summary.append("-------------------\n");
        summary.append(String.format("Subtotal: $%.2f\n", vehicle.calculateSubtotal()));
        summary.append(String.format("IVA (%.0f%%): $%.2f\n", vehicle.VAT * 100, vehicle.calculateVAT()));
        summary.append(String.format("Descuento: $%.2f\n", vehicle.calculateDiscount()));
        summary.append("-------------------\n");
        summary.append(String.format("TOTAL A PAGAR: $%.2f\n", vehicle.calculateTotalAmount()));
        
        if (vehicle.isLongTermRental()) {
            summary.append("\n* Este es un alquiler de larga duración (7 días o más)\n");
        }
        
        return summary.toString();
    }
    
    /**
     * Compara el costo de dos opciones de alquiler y devuelve la diferencia.
     * 
     * @param vehicle1 Primera opción de alquiler
     * @param vehicle2 Segunda opción de alquiler
     * @return Diferencia de costo (positiva si vehicle1 es más caro)
     * @throws IllegalArgumentException Si algún vehículo es null
     */
    public static double compareCosts(Vehicle vehicle1, Vehicle vehicle2) {
        if (vehicle1 == null || vehicle2 == null) {
            throw new IllegalArgumentException("Los vehículos no pueden ser null");
        }
        
        return vehicle1.calculateTotalAmount() - vehicle2.calculateTotalAmount();
    }
    
    /**
     * Genera un informe comparativo entre dos opciones de alquiler.
     * 
     * @param vehicle1 Primera opción de alquiler
     * @param vehicle2 Segunda opción de alquiler
     * @return String con la comparación detallada
     * @throws IllegalArgumentException Si algún vehículo es null
     */
    public static String generateComparisonReport(Vehicle vehicle1, Vehicle vehicle2) {
        if (vehicle1 == null || vehicle2 == null) {
            throw new IllegalArgumentException("Los vehículos no pueden ser null");
        }
        
        StringBuilder report = new StringBuilder();
        double cost1 = vehicle1.calculateTotalAmount();
        double cost2 = vehicle2.calculateTotalAmount();
        double difference = cost1 - cost2;
        
        report.append("COMPARACIÓN DE ALQUILERES\n");
        report.append("-------------------------\n\n");
        
        report.append("OPCIÓN 1:\n");
        report.append(String.format("- %s (%s)\n", vehicle1.getModel(), vehicle1.getLicensePlate()));
        report.append(String.format("- Tipo: %s\n", vehicle1 instanceof CargoVehicle ? "Carga" : "Pasajeros"));
        report.append(String.format("- Días: %d\n", vehicle1.getRentalDays()));
        report.append(String.format("- Costo total: $%.2f\n\n", cost1));
        
        report.append("OPCIÓN 2:\n");
        report.append(String.format("- %s (%s)\n", vehicle2.getModel(), vehicle2.getLicensePlate()));
        report.append(String.format("- Tipo: %s\n", vehicle2 instanceof CargoVehicle ? "Carga" : "Pasajeros"));
        report.append(String.format("- Días: %d\n", vehicle2.getRentalDays()));
        report.append(String.format("- Costo total: $%.2f\n\n", cost2));
        
        report.append("RESULTADO:\n");
        if (difference > 0) {
            report.append(String.format("La opción 1 es $%.2f más cara que la opción 2\n", Math.abs(difference)));
        } else if (difference < 0) {
            report.append(String.format("La opción 2 es $%.2f más cara que la opción 1\n", Math.abs(difference)));
        } else {
            report.append("Ambas opciones tienen el mismo costo\n");
        }
        
        return report.toString();
    }
    
    /**
     * Calcula el costo diario promedio de un alquiler (incluyendo impuestos y descuentos).
     * 
     * @param vehicle Vehículo para el cálculo
     * @return Costo diario promedio
     * @throws IllegalArgumentException Si el vehículo es null o los días de alquiler son 0
     */
    public static double calculateAverageDailyCost(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("El vehículo no puede ser null");
        }
        
        if (vehicle.getRentalDays() <= 0) {
            throw new IllegalArgumentException("Los días de alquiler deben ser mayores que cero");
        }
        
        return vehicle.calculateTotalAmount() / vehicle.getRentalDays();
    }
}