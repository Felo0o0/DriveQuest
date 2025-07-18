/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.dao;

import com.drivequest.model.Vehicle;
import com.drivequest.model.CargoVehicle;
import com.drivequest.model.PassengerVehicle;
import com.drivequest.exception.FileErrorException;
import com.drivequest.util.FileUtils;

import java.util.List;
import java.util.ArrayList;
import java.io.*;

/**
 * Implementacion de VehicleDAO para persistencia en archivos.
 * <p>
 * Proporciona metodos para guardar y cargar datos de vehiculos
 * utilizando archivos de texto.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class FileVehicleDAO implements VehicleDAO {
    
    /** Ruta del archivo para almacenar vehiculos */
    private static final String VEHICLES_FILE_PATH = "vehicles.dat";
    
    /** Separador de campos en el archivo */
    private static final String FIELD_SEPARATOR = ";";
    
    /** Tipo de vehiculo de carga */
    private static final String CARGO_TYPE = "CARGO";
    
    /** Tipo de vehiculo de pasajeros */
    private static final String PASSENGER_TYPE = "PASSENGER";
    
    /**
     * Guarda una lista de vehiculos en un archivo.
     * 
     * @param vehicles Lista de vehiculos a guardar
     * @throws FileErrorException Si ocurre un error al guardar los datos
     */
    @Override
    public void saveVehicles(List<Vehicle> vehicles) throws FileErrorException {
        List<String> lines = new ArrayList<>();
        
        for (Vehicle vehicle : vehicles) {
            lines.add(vehicleToLine(vehicle));
        }
        
        FileUtils.writeLines(VEHICLES_FILE_PATH, lines, false);
    }
    
    /**
     * Carga una lista de vehiculos desde un archivo.
     * 
     * @return Lista de vehiculos cargados
     * @throws FileErrorException Si ocurre un error al cargar los datos
     */
    @Override
    public List<Vehicle> loadVehicles() throws FileErrorException {
        List<Vehicle> vehicles = new ArrayList<>();
        
        if (!FileUtils.fileExists(VEHICLES_FILE_PATH)) {
            return vehicles;
        }
        
        List<String> lines = FileUtils.readLines(VEHICLES_FILE_PATH);
        
        for (String line : lines) {
            Vehicle vehicle = lineToVehicle(line);
            if (vehicle != null) {
                vehicles.add(vehicle);
            }
        }
        
        return vehicles;
    }
    
    /**
     * Guarda un vehiculo individual en un archivo.
     * 
     * @param vehicle Vehiculo a guardar
     * @param append Si es true, agrega al final; si es false, sobrescribe
     * @throws FileErrorException Si ocurre un error al guardar los datos
     */
    @Override
    public void saveVehicle(Vehicle vehicle, boolean append) throws FileErrorException {
        List<String> lines = new ArrayList<>();
        lines.add(vehicleToLine(vehicle));
        
        FileUtils.writeLines(VEHICLES_FILE_PATH, lines, append);
    }
    
    /**
     * Actualiza la informacion de un vehiculo existente en el archivo.
     * 
     * @param updatedVehicle Vehiculo con la informacion actualizada
     * @throws FileErrorException Si ocurre un error al actualizar los datos
     */
    @Override
    public void updateVehicle(Vehicle updatedVehicle) throws FileErrorException {
        List<Vehicle> vehicles = loadVehicles();
        List<Vehicle> updatedVehicles = new ArrayList<>();
        boolean found = false;
        
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getLicensePlate().equals(updatedVehicle.getLicensePlate())) {
                updatedVehicles.add(updatedVehicle);
                found = true;
            } else {
                updatedVehicles.add(vehicle);
            }
        }
        
        if (!found) {
            throw new FileErrorException("No se encontro vehiculo con patente " + updatedVehicle.getLicensePlate());
        }
        
        saveVehicles(updatedVehicles);
    }
    
    /**
     * Elimina un vehiculo por su patente del archivo.
     * 
     * @param licensePlate Patente del vehiculo a eliminar
     * @return true si se elimino correctamente, false si no se encontro
     * @throws FileErrorException Si ocurre un error al eliminar los datos
     */
    @Override
    public boolean deleteVehicle(String licensePlate) throws FileErrorException {
        List<Vehicle> vehicles = loadVehicles();
        List<Vehicle> remainingVehicles = new ArrayList<>();
        boolean found = false;
        
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getLicensePlate().equals(licensePlate)) {
                found = true;
            } else {
                remainingVehicles.add(vehicle);
            }
        }
        
        if (found) {
            saveVehicles(remainingVehicles);
        }
        
        return found;
    }
    
    /**
     * Busca un vehiculo por su patente.
     * 
     * @param licensePlate Patente del vehiculo a buscar
     * @return El vehiculo encontrado o null si no existe
     * @throws FileErrorException Si ocurre un error al buscar los datos
     */
    @Override
    public Vehicle findVehicleByLicensePlate(String licensePlate) throws FileErrorException {
        List<Vehicle> vehicles = loadVehicles();
        
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getLicensePlate().equals(licensePlate)) {
                return vehicle;
            }
        }
        
        return null;
    }
    
    /**
     * Verifica si existe un vehiculo con la patente especificada.
     * 
     * @param licensePlate Patente a verificar
     * @return true si existe un vehiculo con esa patente, false en caso contrario
     * @throws FileErrorException Si ocurre un error al verificar los datos
     */
    @Override
    public boolean vehicleExists(String licensePlate) throws FileErrorException {
        return findVehicleByLicensePlate(licensePlate) != null;
    }
    
    /**
     * Busca vehiculos que cumplan con ciertos criterios.
     * 
     * @param year Año del vehiculo (0 para ignorar este criterio)
     * @param minPrice Precio mínimo (0 para ignorar este criterio)
     * @param maxPrice Precio máximo (0 para ignorar este criterio)
     * @param vehicleType Tipo de vehiculo ("cargo", "passenger" o null para ignorar)
     * @return Lista de vehiculos que cumplen con los criterios
     * @throws FileErrorException Si ocurre un error al buscar los datos
     */
    @Override
    public List<Vehicle> findVehiclesByCriteria(int year, double minPrice, double maxPrice, String vehicleType) 
            throws FileErrorException {
        List<Vehicle> allVehicles = loadVehicles();
        List<Vehicle> filteredVehicles = new ArrayList<>();
        
        for (Vehicle vehicle : allVehicles) {
            boolean matchesYear = year == 0 || vehicle.getYear() == year;
            boolean matchesMinPrice = minPrice == 0 || vehicle.getDailyPrice() >= minPrice;
            boolean matchesMaxPrice = maxPrice == 0 || vehicle.getDailyPrice() <= maxPrice;
            boolean matchesType = vehicleType == null || 
                                 (vehicleType.equalsIgnoreCase("cargo") && vehicle instanceof CargoVehicle) ||
                                 (vehicleType.equalsIgnoreCase("passenger") && vehicle instanceof PassengerVehicle);
            
            if (matchesYear && matchesMinPrice && matchesMaxPrice && matchesType) {
                filteredVehicles.add(vehicle);
            }
        }
        
        return filteredVehicles;
    }
    
    /**
     * Convierte un objeto Vehicle a una linea de texto para guardar en archivo.
     * 
     * @param vehicle Vehiculo a convertir
     * @return Linea de texto representando al vehiculo
     * @throws IllegalArgumentException Si el tipo de vehículo no es reconocido
     */
    private String vehicleToLine(Vehicle vehicle) {
        StringBuilder line = new StringBuilder();
        
        if (vehicle instanceof CargoVehicle) {
            CargoVehicle cargoVehicle = (CargoVehicle) vehicle;
            line.append(CARGO_TYPE).append(FIELD_SEPARATOR)
                .append(vehicle.getLicensePlate()).append(FIELD_SEPARATOR)
                .append(vehicle.getModel()).append(FIELD_SEPARATOR)
                .append(vehicle.getYear()).append(FIELD_SEPARATOR)
                .append(vehicle.getDailyPrice()).append(FIELD_SEPARATOR)
                .append(vehicle.getRentalDays()).append(FIELD_SEPARATOR)
                .append(cargoVehicle.getLoadCapacity());
        } else if (vehicle instanceof PassengerVehicle) {
            PassengerVehicle passengerVehicle = (PassengerVehicle) vehicle;
            line.append(PASSENGER_TYPE).append(FIELD_SEPARATOR)
                .append(vehicle.getLicensePlate()).append(FIELD_SEPARATOR)
                .append(vehicle.getModel()).append(FIELD_SEPARATOR)
                .append(vehicle.getYear()).append(FIELD_SEPARATOR)
                .append(vehicle.getDailyPrice()).append(FIELD_SEPARATOR)
                .append(vehicle.getRentalDays()).append(FIELD_SEPARATOR)
                .append(passengerVehicle.getPassengerCapacity());
        } else {
            throw new IllegalArgumentException("Tipo de vehículo no reconocido");
        }
        
        return line.toString();
    }
    
    /**
     * Convierte una linea de texto a un objeto Vehicle.
     * 
     * @param line Linea de texto a convertir
     * @return Objeto Vehicle creado a partir de la linea
     * @throws FileErrorException Si hay un error en el formato de la línea
     */
    private Vehicle lineToVehicle(String line) throws FileErrorException {
        try {
            String[] fields = line.split(FIELD_SEPARATOR);
            
            if (fields.length < 7) {
                throw new FileErrorException("Formato de línea incorrecto: " + line);
            }
            
            String type = fields[0];
            String licensePlate = fields[1];
            String model = fields[2];
            int year = Integer.parseInt(fields[3]);
            double dailyPrice = Double.parseDouble(fields[4]);
            int rentalDays = Integer.parseInt(fields[5]);
            int capacity = Integer.parseInt(fields[6]);
            
            if (CARGO_TYPE.equals(type)) {
                return new CargoVehicle(licensePlate, model, year, dailyPrice, rentalDays, capacity);
            } else if (PASSENGER_TYPE.equals(type)) {
                return new PassengerVehicle(licensePlate, model, year, dailyPrice, rentalDays, capacity);
            } else {
                throw new FileErrorException("Tipo de vehículo no reconocido: " + type);
            }
            
        } catch (NumberFormatException e) {
            throw new FileErrorException("Error al convertir valores numéricos: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new FileErrorException("Error en el formato de la línea: " + e.getMessage());
        }
    }
}