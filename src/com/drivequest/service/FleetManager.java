/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.service;

import com.drivequest.model.Vehicle;
import com.drivequest.model.CargoVehicle;
import com.drivequest.model.PassengerVehicle;
import com.drivequest.dao.VehicleDAO;
import com.drivequest.dao.FileVehicleDAO;
import com.drivequest.exception.DuplicateLicensePlateException;
import com.drivequest.exception.FileErrorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Clase encargada de gestionar la flota de vehiculos.
 * <p>
 * Implementa metodos para agregar, listar y filtrar vehiculos,
 * validando la unicidad de las patentes y proporcionando operaciones
 * sobre la coleccion de vehiculos.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class FleetManager {
    
    /** Lista sincronizada de vehiculos */
    private final List<Vehicle> vehicles;
    
    /** Mapa para validacion rapida de patentes */
    private final Map<String, Boolean> licensePlateMap;
    
    /** Objeto DAO para acceso a datos */
    private final VehicleDAO vehicleDAO;
    
    /** Gestor de arriendos por fechas */
    private final RentalManager rentalManager;
    
    /**
    * Constructor de la clase FleetManager.
    * Inicializa las colecciones y carga los datos existentes.
    * 
    * @throws FileErrorException Si ocurre un error al cargar los datos iniciales
    */
    public FleetManager() throws FileErrorException {
        // Utilizamos colecciones sincronizadas para seguridad en entornos multihilo
        this.vehicles = Collections.synchronizedList(new ArrayList<>());
        this.licensePlateMap = new ConcurrentHashMap<>();
        this.vehicleDAO = new FileVehicleDAO();
        this.rentalManager = new RentalManager();
        
        loadVehicles();
    }
    
    /**
    * Constructor de la clase FleetManager que acepta un DAO específico.
    * Inicializa las colecciones y carga los datos existentes.
    * 
    * @param vehicleDAO El DAO a utilizar para acceso a datos
    * @throws FileErrorException Si ocurre un error al cargar los datos iniciales
    */
    public FleetManager(VehicleDAO vehicleDAO) throws FileErrorException {
        // Utilizamos colecciones sincronizadas para seguridad en entornos multihilo
        this.vehicles = Collections.synchronizedList(new ArrayList<>());
        this.licensePlateMap = new ConcurrentHashMap<>();
        this.vehicleDAO = vehicleDAO;
        this.rentalManager = new RentalManager();
        
        loadVehicles();
    }
    
    /**
    * Agrega un vehiculo a la flota, validando que la patente sea unica.
    * 
    * @param vehicle Vehiculo a agregar
    * @throws DuplicateLicensePlateException Si ya existe un vehiculo con la misma patente
    * @throws FileErrorException Si ocurre un error al guardar los datos
    */
    public void addVehicle(Vehicle vehicle) throws DuplicateLicensePlateException, FileErrorException {
        String licensePlate = vehicle.getLicensePlate().toUpperCase();
        
        // Validar unicidad de patente
        synchronized (licensePlateMap) {
            if (licensePlateMap.containsKey(licensePlate)) {
                throw new DuplicateLicensePlateException(
                "Ya existe un vehiculo con la patente " + licensePlate);
            }
            
            // Agregar a la lista y al mapa
            vehicles.add(vehicle);
            licensePlateMap.put(licensePlate, true);
            
            // Persistir el vehiculo
            vehicleDAO.saveVehicle(vehicle, true);
        }
    }
    
    /**
    * Obtiene la lista completa de vehiculos.
    * 
    * @return Lista de todos los vehiculos
    */
    public List<Vehicle> listVehicles() {
        synchronized (vehicles) {
            return new ArrayList<>(vehicles);
        }
    }
    
    /**
    * Obtiene la lista completa de vehículos.
    * Alias para listVehicles() para mantener compatibilidad.
    * 
    * @return Lista de todos los vehículos
    */
    public List<Vehicle> getAllVehicles() {
        return listVehicles();
    }
    
    /**
    * Obtiene la lista de vehiculos con arriendos de larga duracion.
    * 
    * @return Lista de vehiculos con arriendos de 7 dias o mas
    */
    public List<Vehicle> listLongTermRentals() {
        synchronized (vehicles) {
            return vehicles.stream()
            .filter(Vehicle::isLongTermRental)
            .collect(Collectors.toList());
        }
    }
    
    /**
    * Busca un vehiculo por su patente.
    * 
    * @param licensePlate Patente del vehiculo a buscar
    * @return Vehiculo encontrado o null si no existe
    */
    public Vehicle findVehicleByLicensePlate(String licensePlate) {
        String normalizedPlate = licensePlate.toUpperCase();
        
        synchronized (vehicles) {
            return vehicles.stream()
            .filter(v -> v.getLicensePlate().equalsIgnoreCase(normalizedPlate))
            .findFirst()
            .orElse(null);
        }
    }
    
    /**
    * Busca vehiculos que cumplan con ciertos criterios.
    * 
    * @param year Año del vehiculo (0 para ignorar este criterio)
    * @param minPrice Precio mínimo (0 para ignorar este criterio)
    * @param maxPrice Precio máximo (0 para ignorar este criterio)
    * @param vehicleType Tipo de vehiculo ("cargo", "passenger" o null para ignorar)
    * @return Lista de vehiculos que cumplen con los criterios
    */
    public List<Vehicle> findVehiclesByCriteria(int year, double minPrice, double maxPrice, String vehicleType) {
        synchronized (vehicles) {
            return vehicles.stream()
            .filter(v -> year == 0 || v.getYear() == year)
            .filter(v -> minPrice == 0 || v.getDailyPrice() >= minPrice)
            .filter(v -> maxPrice == 0 || v.getDailyPrice() <= maxPrice)
            .filter(v -> vehicleType == null || 
            (vehicleType.equalsIgnoreCase("cargo") && v instanceof CargoVehicle) ||
            (vehicleType.equalsIgnoreCase("passenger") && v instanceof PassengerVehicle))
            .collect(Collectors.toList());
        }
    }
    
    /**
    * Actualiza los datos de un vehiculo existente.
    * 
    * @param vehicle Vehiculo con los datos actualizados
    * @throws FileErrorException Si ocurre un error al actualizar los datos
    */
    public void updateVehicle(Vehicle vehicle) throws FileErrorException {
        synchronized (vehicles) {
            // Buscar el índice del vehículo
            int index = -1;
            for (int i = 0; i < vehicles.size(); i++) {
                if (vehicles.get(i).getLicensePlate().equalsIgnoreCase(vehicle.getLicensePlate())) {
                    index = i;
                    break;
                }
            }
            
            if (index >= 0) {
                vehicles.set(index, vehicle);
                vehicleDAO.updateVehicle(vehicle);
            } else {
                throw new FileErrorException("No se encontro vehiculo con patente " + vehicle.getLicensePlate());
            }
        }
    }
    
    /**
    * Elimina un vehiculo de la flota.
    * 
    * @param licensePlate Patente del vehiculo a eliminar
    * @return true si se elimino correctamente, false si no se encontro
    * @throws FileErrorException Si ocurre un error al eliminar los datos
    */
    public boolean removeVehicle(String licensePlate) throws FileErrorException {
        String normalizedPlate = licensePlate.toUpperCase();
        
        synchronized (vehicles) {
            boolean removed = vehicles.removeIf(v -> 
            v.getLicensePlate().equalsIgnoreCase(normalizedPlate));
            
            if (removed) {
                licensePlateMap.remove(normalizedPlate);
                vehicleDAO.deleteVehicle(licensePlate);
            }
            
            return removed;
        }
    }
    
    /**
    * Carga los vehiculos desde el medio de persistencia.
    * 
    * @throws FileErrorException Si ocurre un error al cargar los datos
    */
    public void loadVehicles() throws FileErrorException {
        List<Vehicle> loadedVehicles = vehicleDAO.loadVehicles();
        
        synchronized (vehicles) {
            vehicles.clear();
            licensePlateMap.clear();
            
            for (Vehicle vehicle : loadedVehicles) {
                String licensePlate = vehicle.getLicensePlate().toUpperCase();
                vehicles.add(vehicle);
                licensePlateMap.put(licensePlate, true);
            }
        }
    }
    
    /**
    * Guarda todos los vehículos en el medio de persistencia.
    * Alias para saveAllVehicles() para mantener compatibilidad.
    * 
    * @throws FileErrorException Si ocurre un error al guardar los datos
    */
    public void saveVehicles() throws FileErrorException {
        saveAllVehicles();
    }
    
    /**
    * Obtiene la cantidad de vehiculos en la flota.
    * 
    * @return Cantidad de vehiculos
    */
    public int getVehicleCount() {
        synchronized (vehicles) {
            return vehicles.size();
        }
    }
    
    /**
    * Obtiene la cantidad de vehiculos con arriendos de larga duracion.
    * 
    * @return Cantidad de vehiculos con arriendos de 7 dias o mas
    */
    public int getLongTermRentalCount() {
        synchronized (vehicles) {
            return (int) vehicles.stream()
            .filter(Vehicle::isLongTermRental)
            .count();
        }
    }
    
    /**
    * Verifica si una patente ya existe en el sistema.
    * 
    * @param licensePlate Patente a verificar
    * @return true si la patente ya existe, false en caso contrario
    */
    public boolean licensePlateExists(String licensePlate) {
        String normalizedPlate = licensePlate.toUpperCase();
        
        synchronized (licensePlateMap) {
            return licensePlateMap.containsKey(normalizedPlate);
        }
    }
    
    /**
    * Obtiene estadísticas de precios de los vehículos.
    * 
    * @return Objeto con estadísticas como precio promedio, mínimo, máximo, etc.
    */
    public DoubleSummaryStatistics getPriceStatistics() {
        synchronized (vehicles) {
            return vehicles.stream()
            .mapToDouble(Vehicle::getDailyPrice)
            .summaryStatistics();
        }
    }
    
    /**
    * Obtiene la cantidad de vehículos por tipo.
    * 
    * @return Mapa con la cantidad de vehículos por tipo
    */
    public Map<String, Long> getVehicleCountByType() {
        synchronized (vehicles) {
            Map<String, Long> countByType = new HashMap<>();
            
            long cargoCount = vehicles.stream()
            .filter(v -> v instanceof CargoVehicle)
            .count();
            
            long passengerCount = vehicles.stream()
            .filter(v -> v instanceof PassengerVehicle)
            .count();
            
            countByType.put("Carga", cargoCount);
            countByType.put("Pasajeros", passengerCount);
            
            return countByType;
        }
    }
    
    /**
    * Obtiene la cantidad de vehículos por año.
    * 
    * @return Mapa con la cantidad de vehículos por año
    */
    public Map<Integer, Long> getVehicleCountByYear() {
        synchronized (vehicles) {
            return vehicles.stream()
            .collect(Collectors.groupingBy(
            Vehicle::getYear,
            Collectors.counting()
            ));
        }
    }
    
    /**
    * Guarda todos los vehículos en el medio de persistencia.
    * 
    * @throws FileErrorException Si ocurre un error al guardar los datos
    */
    public void saveAllVehicles() throws FileErrorException {
        synchronized (vehicles) {
            vehicleDAO.saveVehicles(new ArrayList<>(vehicles));
        }
    }
    
    /**
     * Verifica si un vehículo está disponible en un periodo específico.
     * 
     * @param licensePlate Patente del vehículo
     * @param startDate Fecha de inicio del periodo
     * @param endDate Fecha de fin del periodo
     * @return true si el vehículo está disponible, false en caso contrario
     */
    public boolean isVehicleAvailable(String licensePlate, LocalDate startDate, LocalDate endDate) {
        Vehicle vehicle = findVehicleByLicensePlate(licensePlate);
        if (vehicle == null || !vehicle.isAvailable()) {
            return false;
        }
        return rentalManager.isVehicleAvailable(licensePlate, startDate, endDate);
    }
    
    /**
     * Registra un nuevo arriendo de vehículo.
     * 
     * @param licensePlate Patente del vehículo
     * @param startDate Fecha de inicio del arriendo
     * @param endDate Fecha de fin del arriendo
     * @return true si se pudo registrar el arriendo, false en caso contrario
     */
    public boolean rentVehicle(String licensePlate, LocalDate startDate, LocalDate endDate) {
        Vehicle vehicle = findVehicleByLicensePlate(licensePlate);
        if (vehicle != null && vehicle.isAvailable()) {
            boolean success = rentalManager.rentVehicle(licensePlate, startDate, endDate);
            if (success) {
                // Calcular días de arriendo
                long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
                vehicle.setRentalDays((int) days);
                vehicle.setAvailable(false);
                try {
                    updateVehicle(vehicle);
                } catch (FileErrorException e) {
                    // Si hay error al actualizar, revertir el arriendo
                    rentalManager.cancelRental(licensePlate, startDate);
                    return false;
                }
            }
            return success;
        }
        return false;
    }
    
    /**
     * Extiende un arriendo existente.
     * 
     * @param licensePlate Patente del vehículo
     * @param originalEndDate Fecha de fin original
     * @param newEndDate Nueva fecha de fin
     * @return true si se pudo extender el arriendo, false en caso contrario
     */
    public boolean extendRental(String licensePlate, LocalDate originalEndDate, LocalDate newEndDate) {
        Vehicle vehicle = findVehicleByLicensePlate(licensePlate);
        if (vehicle != null) {
            boolean success = rentalManager.extendRental(licensePlate, originalEndDate, newEndDate);
            if (success) {
                // Recalcular días de arriendo
                Map<LocalDate, LocalDate> periods = rentalManager.getVehicleRentalPeriods(licensePlate);
                for (Map.Entry<LocalDate, LocalDate> period : periods.entrySet()) {
                    if (period.getValue().equals(newEndDate)) {
                        long days = java.time.temporal.ChronoUnit.DAYS.between(period.getKey(), newEndDate) + 1;
                        vehicle.setRentalDays((int) days);
                        try {
                            updateVehicle(vehicle);
                        } catch (FileErrorException e) {
                            // Si hay error al actualizar, revertir la extensión
                            rentalManager.extendRental(licensePlate, newEndDate, originalEndDate);
                            return false;
                        }
                        break;
                    }
                }
            }
            return success;
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
     * Finaliza un arriendo y marca el vehículo como disponible.
     * 
     * @param licensePlate Patente del vehículo
     * @param startDate Fecha de inicio del arriendo a finalizar
     * @return true si se pudo finalizar el arriendo, false en caso contrario
     */
    public boolean finishRental(String licensePlate, LocalDate startDate) {
        Vehicle vehicle = findVehicleByLicensePlate(licensePlate);
        if (vehicle != null && !vehicle.isAvailable()) {
            boolean success = rentalManager.cancelRental(licensePlate, startDate);
            if (success) {
                vehicle.setAvailable(true);
                try {
                    updateVehicle(vehicle);
                } catch (FileErrorException e) {
                    // Si hay error al actualizar, revertir la cancelación
                    Map<LocalDate, LocalDate> periods = rentalManager.getVehicleRentalPeriods(licensePlate);
                    if (!periods.isEmpty()) {
                        vehicle.setAvailable(false);
                    }
                    return false;
                }
            }
            return success;
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
        return rentalManager.getVehicleRentalPeriods(licensePlate);
    }
}