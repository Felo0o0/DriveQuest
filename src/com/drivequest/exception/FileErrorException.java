/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.exception;

/**
 * Excepcion que se lanza cuando ocurre un error al manipular archivos.
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class FileErrorException extends Exception {
    
    /**
    * Constructor que recibe un mensaje de error.
    * 
    * @param message Mensaje detallando la razon de la excepcion
    */
    public FileErrorException(String message) {
    super(message);
    }
    
    /**
    * Constructor con mensaje predeterminado.
    */
    public FileErrorException() {
    super("Error: Ocurrio un problema al manipular el archivo");
    }
    
    /**
    * Constructor que recibe un mensaje de error y la causa original.
    * 
    * @param message Mensaje detallando la razón de la excepción
    * @param cause Excepción original que causó el error
    */
    public FileErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}