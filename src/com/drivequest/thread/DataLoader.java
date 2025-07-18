/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.thread;

import com.drivequest.model.Vehicle;
import com.drivequest.dao.VehicleDAO;
import com.drivequest.exception.FileErrorException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Clase que implementa la carga de datos de vehiculos usando hilos.
 * <p>
 * Permite cargar datos de vehiculos de forma concurrente para
 * mejorar el rendimiento en sistemas con muchos registros.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class DataLoader implements Runnable {
    
    /** Objeto DAO para acceso a datos */
    private final VehicleDAO vehicleDAO;
    
    /** Lista donde se cargarán los vehículos */
    private final List<Vehicle> targetList;
    
    /** Filtro opcional para los vehículos */
    private Predicate<Vehicle> filter;
    
    /** Callback opcional para notificar progreso */
    private Consumer<Integer> progressCallback;
    
    /** Callback opcional para notificar finalización */
    private Consumer<Boolean> completionCallback;
    
    /** Indicador de finalización correcta */
    private volatile boolean completed = false;
    
    /** Mensaje de error si ocurre alguno */
    private volatile String errorMessage = null;
    
    /** Cantidad de vehículos cargados */
    private volatile int loadedCount = 0;
    
    /**
     * Constructor de la clase DataLoader.
     * 
     * @param vehicleDAO Objeto DAO para acceso a datos
     * @param targetList Lista donde se cargarán los vehículos
     */
    public DataLoader(VehicleDAO vehicleDAO, List<Vehicle> targetList) {
        this.vehicleDAO = vehicleDAO;
        this.targetList = targetList instanceof CopyOnWriteArrayList ? 
                targetList : new CopyOnWriteArrayList<>(targetList);
    }
    
    /**
     * Establece un filtro para los vehículos a cargar.
     * 
     * @param filter Predicado que determina qué vehículos incluir
     * @return this para encadenamiento de métodos
     */
    public DataLoader withFilter(Predicate<Vehicle> filter) {
        this.filter = filter;
        return this;
    }
    
    /**
     * Establece un callback para notificar el progreso de la carga.
     * 
     * @param callback Consumidor que recibe el número de vehículos cargados
     * @return this para encadenamiento de métodos
     */
    public DataLoader withProgressCallback(Consumer<Integer> callback) {
        this.progressCallback = callback;
        return this;
    }
    
    /**
     * Establece un callback para notificar la finalización de la carga.
     * 
     * @param callback Consumidor que recibe true si la carga fue exitosa, false en caso contrario
     * @return this para encadenamiento de métodos
     */
    public DataLoader withCompletionCallback(Consumer<Boolean> callback) {
        this.completionCallback = callback;
        return this;
    }
    
    /**
     * Implementacion del metodo run para ejecucion del hilo.
     * Carga los vehiculos desde la fuente de datos.
     */
    @Override
    public void run() {
        try {
            // Verificar si el hilo ha sido interrumpido
            if (Thread.currentThread().isInterrupted()) {
                errorMessage = "Carga interrumpida";
                completed = false;
                notifyCompletion(false);
                return;
            }
            
            List<Vehicle> loadedVehicles = vehicleDAO.loadVehicles();
            loadedCount = 0;
            
            // Limpiar la lista destino
            targetList.clear();
            
            // Aplicar filtro si existe
            for (Vehicle vehicle : loadedVehicles) {
                // Verificar interrupción periódicamente
                if (Thread.currentThread().isInterrupted()) {
                    errorMessage = "Carga interrumpida";
                    completed = false;
                    notifyCompletion(false);
                    return;
                }
                
                if (filter == null || filter.test(vehicle)) {
                    targetList.add(vehicle);
                    loadedCount++;
                    
                    // Notificar progreso si hay callback
                    if (progressCallback != null) {
                        progressCallback.accept(loadedCount);
                    }
                }
            }
            
            completed = true;
            notifyCompletion(true);
        } catch (FileErrorException e) {
            errorMessage = "Error al cargar datos: " + e.getMessage();
            completed = false;
            notifyCompletion(false);
        } catch (Exception e) {
            errorMessage = "Error inesperado: " + e.getMessage();
            completed = false;
            notifyCompletion(false);
        }
    }
    
    /**
     * Notifica la finalización de la carga si hay un callback registrado.
     * 
     * @param success true si la carga fue exitosa, false en caso contrario
     */
    private void notifyCompletion(boolean success) {
        if (completionCallback != null) {
            completionCallback.accept(success);
        }
    }
    
    /**
     * Verifica si la carga se completo correctamente.
     * 
     * @return true si la carga se completo, false en caso contrario
     */
    public boolean isCompleted() {
        return completed;
    }
    
    /**
     * Obtiene el mensaje de error si ocurrio alguno.
     * 
     * @return Mensaje de error o null si no hubo error
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Obtiene la cantidad de vehículos cargados.
     * 
     * @return Cantidad de vehículos cargados
     */
    public int getLoadedCount() {
        return loadedCount;
    }
}