/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.exception;

/**
 * Excepcion que se lanza cuando se intenta agregar un vehiculo con una patente
 * que ya existe en el sistema.
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class DuplicateLicensePlateException extends Exception {
    
    /**
     * Constructor que recibe un mensaje de error.
     * 
     * @param message Mensaje detallando la razon de la excepcion
     */
    public DuplicateLicensePlateException(String message) {
        super(message);
    }
    
    /**
     * Constructor con mensaje predeterminado.
     */
    public DuplicateLicensePlateException() {
        super("Error: Ya existe un vehiculo con esa patente en el sistema");
    }
}
