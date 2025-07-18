/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.util;

import com.drivequest.model.Vehicle;
import com.drivequest.model.CargoVehicle;
import com.drivequest.model.PassengerVehicle;

import java.util.regex.Pattern;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase de utilidad para validaciones en el sistema.
 * <p>
 * Proporciona metodos estaticos para validar diferentes tipos de datos
 * relacionados con los vehiculos y el sistema de alquiler.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class ValidationUtils {
    
    /** Patron para validar patentes chilenas (formato: LLLL99 o LL9999) */
    private static final Pattern LICENSE_PLATE_PATTERN = Pattern.compile(
            "^[A-Za-z]{2,4}\\d{2,4}$");
    
    /** Patron para validar correos electrónicos */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    
    /** Patron para validar números telefónicos (formato: +56 9 XXXX XXXX o similar) */
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(\\+?56)?\\s*9\\s*\\d{4}\\s*\\d{4}$");
    
    /** Año mínimo permitido para vehículos */
    private static final int MIN_VEHICLE_YEAR = 1900;
    
    /** Capacidad máxima de carga en kilogramos */
    private static final int MAX_LOAD_CAPACITY = 20000; // 20 toneladas
    
    /** Capacidad máxima de pasajeros */
    private static final int MAX_PASSENGER_CAPACITY = 50;
    
    /** Duración máxima de alquiler en días */
    private static final int MAX_RENTAL_DAYS = 365;
    
    /**
     * Constructor privado para evitar instanciación.
     */
    private ValidationUtils() {
        // Clase de utilidad, no debe instanciarse
    }
    
    /**
     * Valida si una patente tiene el formato correcto.
     * 
     * @param licensePlate Patente a validar
     * @return true si la patente es valida, false en caso contrario
     */
    public static boolean isValidLicensePlate(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return false;
        }
        return LICENSE_PLATE_PATTERN.matcher(licensePlate).matches();
    }
    
    /**
     * Normaliza una patente (convierte a mayúsculas y elimina espacios).
     * 
     * @param licensePlate Patente a normalizar
     * @return Patente normalizada o null si la entrada es null
     */
    public static String normalizeLicensePlate(String licensePlate) {
        if (licensePlate == null) {
            return null;
        }
        return licensePlate.toUpperCase().replaceAll("\\s", "");
    }
    
    /**
     * Valida si un año es valido para un vehiculo.
     * 
     * @param year Año a validar
     * @return true si el año es valido, false en caso contrario
     */
    public static boolean isValidYear(int year) {
        int currentYear = Year.now().getValue();
        return year >= MIN_VEHICLE_YEAR && year <= currentYear;
    }
    
    /**
     * Valida si un precio diario es valido.
     * 
     * @param price Precio a validar
     * @return true si el precio es valido, false en caso contrario
     */
    public static boolean isValidPrice(double price) {
        return price > 0 && !Double.isInfinite(price) && !Double.isNaN(price);
    }
    
    /**
     * Valida si la duracion del alquiler es valida.
     * 
     * @param days Dias de alquiler a validar
     * @return true si la duracion es valida, false en caso contrario
     */
    public static boolean isValidRentalDays(int days) {
        return days > 0 && days <= MAX_RENTAL_DAYS;
    }
    
    /**
     * Valida si la capacidad de carga es valida.
     * 
     * @param capacity Capacidad de carga en kilogramos a validar
     * @return true si la capacidad es valida, false en caso contrario
     */
    public static boolean isValidLoadCapacity(int capacity) {
        return capacity > 0 && capacity <= MAX_LOAD_CAPACITY;
    }
    
    /**
     * Valida si la capacidad de pasajeros es valida.
     * 
     * @param capacity Capacidad de pasajeros a validar
     * @return true si la capacidad es valida, false en caso contrario
     */
    public static boolean isValidPassengerCapacity(int capacity) {
        return capacity > 0 && capacity <= MAX_PASSENGER_CAPACITY;
    }
    
    /**
     * Valida si un modelo es valido.
     * 
     * @param model Modelo a validar
     * @return true si el modelo es valido, false en caso contrario
     */
    public static boolean isValidModel(String model) {
        return model != null && !model.trim().isEmpty();
    }
    
    /**
     * Valida si un correo electrónico tiene formato válido.
     * 
     * @param email Correo electrónico a validar
     * @return true si el correo es válido, false en caso contrario
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Valida si un número telefónico tiene formato válido.
     * 
     * @param phone Número telefónico a validar
     * @return true si el número es válido, false en caso contrario
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Valida si un vehículo de carga es válido.
     * 
     * @param vehicle Vehículo a validar
     * @return Mapa con errores encontrados (vacío si no hay errores)
     */
    public static Map<String, String> validateCargoVehicle(CargoVehicle vehicle) {
        Map<String, String> errors = validateBaseVehicle(vehicle);
        
        if (!isValidLoadCapacity(vehicle.getLoadCapacity())) {
            errors.put("loadCapacity", "La capacidad de carga debe estar entre 1 y " + MAX_LOAD_CAPACITY + " kg");
        }
        
        return errors;
    }
    
    /**
     * Valida si un vehículo de pasajeros es válido.
     * 
     * @param vehicle Vehículo a validar
     * @return Mapa con errores encontrados (vacío si no hay errores)
     */
    public static Map<String, String> validatePassengerVehicle(PassengerVehicle vehicle) {
        Map<String, String> errors = validateBaseVehicle(vehicle);
        
        if (!isValidPassengerCapacity(vehicle.getPassengerCapacity())) {
            errors.put("passengerCapacity", "La capacidad de pasajeros debe estar entre 1 y " + MAX_PASSENGER_CAPACITY);
        }
        
        return errors;
    }
    
    /**
     * Valida los campos básicos de un vehículo.
     * 
     * @param vehicle Vehículo a validar
     * @return Mapa con errores encontrados (vacío si no hay errores)
     */
    public static Map<String, String> validateBaseVehicle(Vehicle vehicle) {
        Map<String, String> errors = new HashMap<>();
        
        if (vehicle == null) {
            errors.put("vehicle", "El vehículo no puede ser null");
            return errors;
        }
        
        if (!isValidLicensePlate(vehicle.getLicensePlate())) {
            errors.put("licensePlate", "La patente debe tener formato válido (LLLL99 o LL9999)");
        }
        
        if (!isValidModel(vehicle.getModel())) {
            errors.put("model", "El modelo no puede estar vacío");
        }
        
        if (!isValidYear(vehicle.getYear())) {
            errors.put("year", "El año debe estar entre " + MIN_VEHICLE_YEAR + " y " + Year.now().getValue());
        }
        
        if (!isValidPrice(vehicle.getDailyPrice())) {
            errors.put("dailyPrice", "El precio diario debe ser mayor que cero");
        }
        
        if (!isValidRentalDays(vehicle.getRentalDays())) {
            errors.put("rentalDays", "Los días de alquiler deben estar entre 1 y " + MAX_RENTAL_DAYS);
        }
        
        return errors;
    }
    
    /**
     * Obtiene un mensaje de error para una patente inválida.
     * 
     * @param licensePlate Patente a validar
     * @return Mensaje de error o null si la patente es válida
     */
    public static String getLicensePlateError(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return "La patente no puede estar vacía";
        }
        if (!LICENSE_PLATE_PATTERN.matcher(licensePlate).matches()) {
            return "La patente debe tener formato válido (LLLL99 o LL9999)";
        }
        return null;
    }
    
    /**
     * Obtiene un mensaje de error para un año inválido.
     * 
     * @param year Año a validar
     * @return Mensaje de error o null si el año es válido
     */
    public static String getYearError(int year) {
        int currentYear = Year.now().getValue();
        if (year < MIN_VEHICLE_YEAR) {
            return "El año no puede ser anterior a " + MIN_VEHICLE_YEAR;
        }
        if (year > currentYear) {
            return "El año no puede ser posterior al año actual (" + currentYear + ")";
        }
        return null;
    }
    
    /**
     * Obtiene un mensaje de error para un precio inválido.
     * 
     * @param price Precio a validar
     * @return Mensaje de error o null si el precio es válido
     */
    public static String getPriceError(double price) {
        if (price <= 0) {
            return "El precio debe ser mayor que cero";
        }
        if (Double.isInfinite(price) || Double.isNaN(price)) {
            return "El precio no es un valor numérico válido";
        }
        return null;
    }
    
    /**
     * Obtiene un mensaje de error para días de alquiler inválidos.
     * 
     * @param days Días a validar
     * @return Mensaje de error o null si los días son válidos
     */
    public static String getRentalDaysError(int days) {
        if (days <= 0) {
            return "Los días de alquiler deben ser mayores que cero";
        }
        if (days > MAX_RENTAL_DAYS) {
            return "Los días de alquiler no pueden exceder " + MAX_RENTAL_DAYS;
        }
        return null;
    }
    
    /**
     * Verifica si un vehículo es válido.
     * 
     * @param vehicle Vehículo a validar
     * @return true si el vehículo es válido, false en caso contrario
     */
    public static boolean isValidVehicle(Vehicle vehicle) {
        if (vehicle instanceof CargoVehicle) {
            return validateCargoVehicle((CargoVehicle) vehicle).isEmpty();
        } else if (vehicle instanceof PassengerVehicle) {
            return validatePassengerVehicle((PassengerVehicle) vehicle).isEmpty();
        } else {
            return validateBaseVehicle(vehicle).isEmpty();
        }
    }
}