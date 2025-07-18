/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.exception;

/**
 * Excepcion que se lanza cuando se intenta crear o manipular un vehiculo con datos invalidos.
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class InvalidVehicleException extends Exception {
    
    /**
     * Constructor que recibe un mensaje de error.
     * 
     * @param message Mensaje detallando la razon de la excepcion
     */
    public InvalidVehicleException(String message) {
        super(message);
    }
    
    /**
     * Constructor con mensaje predeterminado.
     */
    public InvalidVehicleException() {
        super("Error: Los datos del vehiculo son invalidos");
    }
}
