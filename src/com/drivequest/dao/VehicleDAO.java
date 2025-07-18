/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.dao;

import com.drivequest.model.Vehicle;
import com.drivequest.exception.FileErrorException;
import java.util.List;

/**
 * Interfaz que define operaciones de acceso a datos para vehiculos.
 * <p>
 * Define metodos para guardar, cargar, actualizar y eliminar datos 
 * de vehiculos, implementando el patron DAO (Data Access Object).
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public interface VehicleDAO {
    
    /**
     * Guarda una lista de vehiculos en un medio de persistencia.
     * 
     * @param vehicles Lista de vehiculos a guardar
     * @throws FileErrorException Si ocurre un error al guardar los datos
     */
    void saveVehicles(List<Vehicle> vehicles) throws FileErrorException;
    
    /**
     * Carga una lista de vehiculos desde un medio de persistencia.
     * 
     * @return Lista de vehiculos cargados
     * @throws FileErrorException Si ocurre un error al cargar los datos
     */
    List<Vehicle> loadVehicles() throws FileErrorException;
    
    /**
     * Guarda un vehiculo individual en un medio de persistencia.
     * 
     * @param vehicle Vehiculo a guardar
     * @param append Si es true, agrega al final; si es false, sobrescribe
     * @throws FileErrorException Si ocurre un error al guardar los datos
     */
    void saveVehicle(Vehicle vehicle, boolean append) throws FileErrorException;
    
    /**
     * Actualiza la informacion de un vehiculo existente.
     * 
     * @param vehicle Vehiculo con la informacion actualizada
     * @throws FileErrorException Si ocurre un error al actualizar los datos
     */
    void updateVehicle(Vehicle vehicle) throws FileErrorException;
    
    /**
     * Elimina un vehiculo por su patente.
     * 
     * @param licensePlate Patente del vehiculo a eliminar
     * @return true si se elimino correctamente, false si no se encontro
     * @throws FileErrorException Si ocurre un error al eliminar los datos
     */
    boolean deleteVehicle(String licensePlate) throws FileErrorException;
    
    /**
     * Busca un vehiculo por su patente.
     * 
     * @param licensePlate Patente del vehiculo a buscar
     * @return El vehiculo encontrado o null si no existe
     * @throws FileErrorException Si ocurre un error al buscar los datos
     */
    Vehicle findVehicleByLicensePlate(String licensePlate) throws FileErrorException;
    
    /**
     * Verifica si existe un vehiculo con la patente especificada.
     * 
     * @param licensePlate Patente a verificar
     * @return true si existe un vehiculo con esa patente, false en caso contrario
     * @throws FileErrorException Si ocurre un error al verificar los datos
     */
    boolean vehicleExists(String licensePlate) throws FileErrorException;
    
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
    List<Vehicle> findVehiclesByCriteria(int year, double minPrice, double maxPrice, String vehicleType) 
            throws FileErrorException;
}