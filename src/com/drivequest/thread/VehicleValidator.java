/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.thread;

import com.drivequest.model.Vehicle;
import com.drivequest.model.CargoVehicle;
import com.drivequest.model.PassengerVehicle;
import com.drivequest.util.ValidationUtils;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * Clase que implementa la validacion de vehiculos usando hilos.
 * <p>
 * Permite validar vehiculos de forma concurrente para mejorar
 * el rendimiento en sistemas con muchos registros.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class VehicleValidator implements Runnable {
    
    /** Cola de vehiculos a validar */
    private final BlockingQueue<Vehicle> vehiclesToValidate;
    
    /** Mapa para almacenar resultados de validacion */
    private final ConcurrentMap<String, Boolean> validationResults;
    
    /** Mapa para almacenar detalles de errores de validación */
    private final ConcurrentMap<String, Map<String, String>> validationErrors;
    
    /** Callback para notificar cuando se valida un vehículo */
    private BiConsumer<Vehicle, Boolean> validationCallback;
    
    /** Contador de vehículos validados */
    private final AtomicInteger validatedCount = new AtomicInteger(0);
    
    /** Contador de vehículos inválidos */
    private final AtomicInteger invalidCount = new AtomicInteger(0);
    
    /** Tiempo de espera para obtener vehículos de la cola en milisegundos */
    private final long pollTimeoutMs;
    
    /** Indica si el hilo debe continuar ejecutandose */
    private volatile boolean running = true;
    
    /**
     * Constructor de la clase VehicleValidator.
     * 
     * @param vehiclesToValidate Cola de vehiculos a validar
     * @param validationResults Mapa para almacenar resultados de validacion
     */
    public VehicleValidator(BlockingQueue<Vehicle> vehiclesToValidate, 
                            ConcurrentMap<String, Boolean> validationResults) {
        this(vehiclesToValidate, validationResults, 500);
    }
    
    /**
     * Constructor de la clase VehicleValidator con timeout configurable.
     * 
     * @param vehiclesToValidate Cola de vehiculos a validar
     * @param validationResults Mapa para almacenar resultados de validacion
     * @param pollTimeoutMs Tiempo de espera para obtener vehículos de la cola en milisegundos
     */
    public VehicleValidator(BlockingQueue<Vehicle> vehiclesToValidate, 
                            ConcurrentMap<String, Boolean> validationResults,
                            long pollTimeoutMs) {
        this.vehiclesToValidate = vehiclesToValidate;
        this.validationResults = validationResults;
        this.validationErrors = new ConcurrentHashMap<>();
        this.pollTimeoutMs = Math.max(100, pollTimeoutMs); // Mínimo 100ms
    }
    
    /**
     * Establece un callback para notificar cuando se valida un vehículo.
     * 
     * @param callback Función que recibe el vehículo y el resultado de la validación
     * @return this para encadenamiento de métodos
     */
    public VehicleValidator withValidationCallback(BiConsumer<Vehicle, Boolean> callback) {
        this.validationCallback = callback;
        return this;
    }
    
     /**
     * Implementacion del metodo run para ejecucion del hilo.
     * Procesa y valida vehiculos de la cola.
     */
     @Override
     public void run() {
     while (running && !Thread.currentThread().isInterrupted()) {
        try {
            // Intentar obtener un vehículo de la cola con timeout
            Vehicle vehicle = vehiclesToValidate.poll(pollTimeoutMs, TimeUnit.MILLISECONDS);
            
            if (vehicle == null) {
                continue;
            }
            
            // Procesar el vehículo
            processVehicle(vehicle);
            
        } catch (InterruptedException e) {
            // Manejar la interrupción
            Thread.currentThread().interrupt();
            running = false;
        } catch (Exception e) {
            // Capturar cualquier excepción inesperada pero continuar procesando
            System.err.println("Error al validar vehículo: " + e.getMessage());
        }
    }
    }

     /**
     * Procesa un vehículo, validándolo y almacenando los resultados.
     * 
     * @param vehicle Vehículo a procesar
     */
     private void processVehicle(Vehicle vehicle) {
     String licensePlate = vehicle.getLicensePlate();
     Map<String, String> errors = validateVehicleDetailed(vehicle);
     boolean isValid = errors.isEmpty();
    
     // Almacenar resultados
     validationResults.put(licensePlate, isValid);
     if (!isValid) {
        validationErrors.put(licensePlate, errors);
        invalidCount.incrementAndGet();
     }
    
     validatedCount.incrementAndGet();
    
     // Notificar si hay callback
     if (validationCallback != null) {
        validationCallback.accept(vehicle, isValid);
     }
     }
    
    /**
     * Detiene la ejecucion del hilo.
     */
    public void stop() {
        running = false;
    }
    
    /**
     * Valida todos los aspectos de un vehiculo.
     * 
     * @param vehicle Vehiculo a validar
     * @return true si el vehiculo es valido, false en caso contrario
     */
    private boolean validateVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return false;
        }
        
        return ValidationUtils.isValidLicensePlate(vehicle.getLicensePlate())
                && ValidationUtils.isValidModel(vehicle.getModel())
                && ValidationUtils.isValidYear(vehicle.getYear())
                && ValidationUtils.isValidPrice(vehicle.getDailyPrice())
                && ValidationUtils.isValidRentalDays(vehicle.getRentalDays());
    }
    
    /**
     * Valida todos los aspectos de un vehiculo y devuelve detalles de errores.
     * 
     * @param vehicle Vehiculo a validar
     * @return Mapa con detalles de errores (vacío si no hay errores)
     */
    private Map<String, String> validateVehicleDetailed(Vehicle vehicle) {
        Map<String, String> errors = new HashMap<>();
        
        if (vehicle == null) {
            errors.put("vehicle", "El vehículo es nulo");
            return errors;
        }
        
        String licensePlate = vehicle.getLicensePlate();
        if (!ValidationUtils.isValidLicensePlate(licensePlate)) {
            errors.put("licensePlate", "Patente inválida: " + licensePlate);
        }
        
        String model = vehicle.getModel();
        if (!ValidationUtils.isValidModel(model)) {
            errors.put("model", "Modelo inválido: " + (model == null ? "null" : model));
        }
        
        int year = vehicle.getYear();
        if (!ValidationUtils.isValidYear(year)) {
            errors.put("year", "Año inválido: " + year);
        }
        
        double dailyPrice = vehicle.getDailyPrice();
        if (!ValidationUtils.isValidPrice(dailyPrice)) {
            errors.put("dailyPrice", "Precio diario inválido: " + dailyPrice);
        }
        
        int rentalDays = vehicle.getRentalDays();
        if (!ValidationUtils.isValidRentalDays(rentalDays)) {
            errors.put("rentalDays", "Días de alquiler inválidos: " + rentalDays);
        }
        
        // Validaciones específicas según el tipo de vehículo
        if (vehicle instanceof CargoVehicle) {
            CargoVehicle cargoVehicle = (CargoVehicle) vehicle;
            int loadCapacity = cargoVehicle.getLoadCapacity();
            if (!ValidationUtils.isValidLoadCapacity(loadCapacity)) {
                errors.put("loadCapacity", "Capacidad de carga inválida: " + loadCapacity);
            }
        } else if (vehicle instanceof PassengerVehicle) {
            PassengerVehicle passengerVehicle = (PassengerVehicle) vehicle;
            int passengerCapacity = passengerVehicle.getPassengerCapacity();
            if (!ValidationUtils.isValidPassengerCapacity(passengerCapacity)) {
                errors.put("passengerCapacity", "Capacidad de pasajeros inválida: " + passengerCapacity);
            }
        }
        
        return errors;
    }
    
    /**
     * Obtiene el número de vehículos validados.
     * 
     * @return Número de vehículos validados
     */
    public int getValidatedCount() {
        return validatedCount.get();
    }
    
    /**
     * Obtiene el número de vehículos inválidos.
     * 
     * @return Número de vehículos inválidos
     */
    public int getInvalidCount() {
        return invalidCount.get();
    }
    
    /**
     * Obtiene los detalles de errores de validación para un vehículo.
     * 
     * @param licensePlate Patente del vehículo
     * @return Mapa con detalles de errores o null si no hay errores o el vehículo no existe
     */
    public Map<String, String> getValidationErrors(String licensePlate) {
        return validationErrors.get(licensePlate);
    }
    
    /**
     * Obtiene todos los errores de validación.
     * 
     * @return Mapa con todos los errores de validación
     */
    public ConcurrentMap<String, Map<String, String>> getAllValidationErrors() {
        return new ConcurrentHashMap<>(validationErrors);
    }
    
    /**
     * Limpia los resultados y errores de validación.
     */
    public void clearResults() {
        validationResults.clear();
        validationErrors.clear();
        validatedCount.set(0);
        invalidCount.set(0);
    }
}