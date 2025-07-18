/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Clase encargada de gestionar los arriendos de vehículos por fechas.
 * <p>
 * Implementa métodos para verificar disponibilidad, registrar arriendos
 * y extender períodos de arriendo existentes.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 */
public class RentalManager {
    
    /** Mapa que almacena la patente del vehículo y sus periodos de arriendo */
    private final Map<String, Map<LocalDate, LocalDate>> rentalPeriods;
    
    /**
     * Constructor de la clase RentalManager.
     * Inicializa las colecciones para almacenar los períodos de arriendo.
     */
    public RentalManager() {
        this.rentalPeriods = new ConcurrentHashMap<>();
    }
    
    /**
     * Verifica si un vehículo está disponible en un periodo específico.
     * 
     * @param licensePlate Patente del vehículo a verificar
     * @param startDate Fecha de inicio del período a verificar
     * @param endDate Fecha de fin del período a verificar
     * @return true si el vehículo está disponible, false en caso contrario
     */
    public boolean isVehicleAvailable(String licensePlate, LocalDate startDate, LocalDate endDate) {
        if (!rentalPeriods.containsKey(licensePlate)) {
            return true; // Si no hay registros de arriendo, está disponible
        }
        
        Map<LocalDate, LocalDate> periods = rentalPeriods.get(licensePlate);
        for (Map.Entry<LocalDate, LocalDate> period : periods.entrySet()) {
            // Verificar si hay solapamiento de fechas
            if (!(endDate.isBefore(period.getKey()) || startDate.isAfter(period.getValue()))) {
                return false; // Hay solapamiento, no está disponible
            }
        }
        return true; // No hay solapamiento, está disponible
    }
    
    /**
     * Registra un nuevo arriendo.
     * 
     * @param licensePlate Patente del vehículo a arrendar
     * @param startDate Fecha de inicio del arriendo
     * @param endDate Fecha de fin del arriendo
     * @return true si se pudo registrar el arriendo, false en caso contrario
     */
    public boolean rentVehicle(String licensePlate, LocalDate startDate, LocalDate endDate) {
        if (isVehicleAvailable(licensePlate, startDate, endDate)) {
            if (!rentalPeriods.containsKey(licensePlate)) {
                rentalPeriods.put(licensePlate, new ConcurrentHashMap<>());
            }
            rentalPeriods.get(licensePlate).put(startDate, endDate);
            return true;
        }
        return false;
    }
    
    /**
     * Extiende un arriendo existente.
     * 
     * @param licensePlate Patente del vehículo
     * @param originalEndDate Fecha de fin original del arriendo
     * @param newEndDate Nueva fecha de fin del arriendo
     * @return true si se pudo extender el arriendo, false en caso contrario
     */
    public boolean extendRental(String licensePlate, LocalDate originalEndDate, LocalDate newEndDate) {
        if (!rentalPeriods.containsKey(licensePlate)) {
            return false;
        }
        
        Map<LocalDate, LocalDate> periods = rentalPeriods.get(licensePlate);
        LocalDate startDate = null;
        
        // Buscar el periodo que termina en originalEndDate
        for (Map.Entry<LocalDate, LocalDate> period : periods.entrySet()) {
            if (period.getValue().equals(originalEndDate)) {
                startDate = period.getKey();
                break;
            }
        }
        
        if (startDate == null) {
            return false; // No se encontró el periodo a extender
        }
        
        // Verificar disponibilidad para la extensión
        if (isVehicleAvailable(licensePlate, originalEndDate.plusDays(1), newEndDate)) {
            // Eliminar el periodo antiguo y agregar el nuevo
            periods.remove(startDate);
            periods.put(startDate, newEndDate);
            return true;
        }
        
        return false;
    }
    
    /**
     * Arriendo por días indefinidos (hasta X días).
     * 
     * @param licensePlate Patente del vehículo
     * @param untilDate Fecha hasta la que se arrienda
     * @return true si se pudo registrar el arriendo, false en caso contrario
     */
    public boolean rentVehicleUntil(String licensePlate, LocalDate untilDate) {
        return rentVehicle(licensePlate, LocalDate.now(), untilDate);
    }
    
    /**
     * Arriendo por días indefinidos (desde X días).
     * 
     * @param licensePlate Patente del vehículo
     * @param fromDate Fecha desde la que se arrienda
     * @param durationDays Duración en días del arriendo (0 para indefinido)
     * @return true si se pudo registrar el arriendo, false en caso contrario
     */
    public boolean rentVehicleFrom(String licensePlate, LocalDate fromDate, int durationDays) {
        LocalDate endDate = durationDays > 0 ? fromDate.plusDays(durationDays) : fromDate.plusYears(1);
        return rentVehicle(licensePlate, fromDate, endDate);
    }
    
    /**
     * Obtiene la fecha actual del sistema.
     * 
     * @return Fecha actual
     */
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
    
    /**
     * Cancela un arriendo existente.
     * 
     * @param licensePlate Patente del vehículo
     * @param startDate Fecha de inicio del arriendo a cancelar
     * @return true si se pudo cancelar el arriendo, false en caso contrario
     */
    public boolean cancelRental(String licensePlate, LocalDate startDate) {
        if (!rentalPeriods.containsKey(licensePlate)) {
            return false;
        }
        
        Map<LocalDate, LocalDate> periods = rentalPeriods.get(licensePlate);
        if (periods.containsKey(startDate)) {
            periods.remove(startDate);
            return true;
        }
        
        return false;
    }
    
    /**
     * Obtiene todos los períodos de arriendo de un vehículo.
     * 
     * @param licensePlate Patente del vehículo
     * @return Mapa con los períodos de arriendo (fecha inicio -> fecha fin)
     */
    public Map<LocalDate, LocalDate> getVehicleRentalPeriods(String licensePlate) {
        if (!rentalPeriods.containsKey(licensePlate)) {
            return new HashMap<>();
        }
        return new HashMap<>(rentalPeriods.get(licensePlate));
    }
}