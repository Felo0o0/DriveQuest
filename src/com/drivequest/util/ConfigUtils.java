/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.util;

import com.drivequest.exception.FileErrorException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Clase de utilidad para manejar configuraciones de la aplicacion.
 * <p>
 * Proporciona metodos para leer y escribir propiedades
 * de configuracion desde un archivo.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class ConfigUtils {
    
    /** Nombre del archivo de configuración por defecto */
    private static final String DEFAULT_CONFIG_FILE = "config.properties";
    
    /** Ruta actual del archivo de configuración */
    private static String configFile = DEFAULT_CONFIG_FILE;
    
    /** Propiedades de configuración */
    private static Properties properties;
    
    static {
    properties = new Properties();
    try {
    loadProperties();
    } catch (IOException e) {
    System.err.println("Error al cargar configuracion: " + e.getMessage());
    // Creamos un Properties vacío para evitar NullPointerException
    properties = new Properties();
    }
    }
    
    /**
    * Constructor privado para evitar instanciación.
    */
    private ConfigUtils() {
    // Clase de utilidad, no debe instanciarse
    }
    
     /**
     * Carga la configuración desde el archivo por defecto.
     * 
     * @return Properties con la configuración cargada
     * @throws FileErrorException Si ocurre un error al cargar el archivo
     */
     public static Properties loadConfiguration() throws FileErrorException {
     try {
        loadProperties();
        return getAllProperties();
     } catch (IOException e) {
        throw new FileErrorException("Error al cargar la configuración: " + e.getMessage());
     }
}
    
    /**
    * Establece la ruta del archivo de configuración y recarga las propiedades.
    * 
    * @param filePath Ruta del archivo de configuración
    * @throws IOException Si ocurre un error al cargar el archivo
    */
    public static void setConfigFile(String filePath) throws IOException {
    configFile = filePath;
    loadProperties();
    }
    
    /**
    * Carga las propiedades de configuracion desde el archivo.
    * 
    * @throws IOException Si ocurre un error al leer el archivo
    */
    public static void loadProperties() throws IOException {
    properties.clear();
    
    if (FileUtils.fileExists(configFile)) {
    try (FileInputStream fis = new FileInputStream(configFile)) {
    properties.load(fis);
    }
    } else {
    System.out.println("Archivo de configuracion no encontrado, se creara uno nuevo al guardar");
    }
    }
    
    /**
    * Guarda las propiedades de configuracion en el archivo.
    * 
    * @throws IOException Si ocurre un error al escribir el archivo
    */
    public static void saveProperties() throws IOException {
    try (FileOutputStream fos = new FileOutputStream(configFile)) {
    properties.store(fos, "DriveQuest Configuration");
    }
    }
    
    /**
    * Obtiene el valor de una propiedad.
    * 
    * @param key Clave de la propiedad
    * @param defaultValue Valor por defecto si la propiedad no existe
    * @return Valor de la propiedad o el valor por defecto
    */
    public static String getProperty(String key, String defaultValue) {
    return properties.getProperty(key, defaultValue);
    }
    
    /**
    * Establece el valor de una propiedad y guarda los cambios.
    * 
    * @param key Clave de la propiedad
    * @param value Valor de la propiedad
    * @throws IOException Si ocurre un error al guardar
    */
    public static void setProperty(String key, String value) throws IOException {
    properties.setProperty(key, value);
    saveProperties();
    }
    
    /**
    * Establece el valor de una propiedad sin guardar los cambios.
    * Útil para establecer múltiples propiedades y luego guardar una sola vez.
    * 
    * @param key Clave de la propiedad
    * @param value Valor de la propiedad
    */
    public static void setPropertyWithoutSaving(String key, String value) {
    properties.setProperty(key, value);
    }
    
    /**
    * Obtiene el valor de una propiedad como entero.
    * 
    * @param key Clave de la propiedad
    * @param defaultValue Valor por defecto si la propiedad no existe
    * @return Valor de la propiedad como entero o el valor por defecto
    */
    public static int getIntProperty(String key, int defaultValue) {
    String value = properties.getProperty(key);
    if (value == null) {
    return defaultValue;
    }
    
    try {
    return Integer.parseInt(value);
    } catch (NumberFormatException e) {
    return defaultValue;
    }
    }
    
    /**
    * Establece el valor de una propiedad como entero y guarda los cambios.
    * 
    * @param key Clave de la propiedad
    * @param value Valor de la propiedad como entero
    * @throws IOException Si ocurre un error al guardar
    */
    public static void setIntProperty(String key, int value) throws IOException {
    properties.setProperty(key, String.valueOf(value));
    saveProperties();
    }
    
    /**
    * Obtiene el valor de una propiedad como booleano.
    * 
    * @param key Clave de la propiedad
    * @param defaultValue Valor por defecto si la propiedad no existe
    * @return Valor de la propiedad como booleano o el valor por defecto
    */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
    String value = properties.getProperty(key);
    if (value == null) {
    return defaultValue;
    }
    
    return Boolean.parseBoolean(value);
    }
    
    /**
    * Establece el valor de una propiedad como booleano y guarda los cambios.
    * 
    * @param key Clave de la propiedad
    * @param value Valor de la propiedad como booleano
    * @throws IOException Si ocurre un error al guardar
    */
    public static void setBooleanProperty(String key, boolean value) throws IOException {
    properties.setProperty(key, String.valueOf(value));
    saveProperties();
    }
    
    /**
    * Obtiene el valor de una propiedad como double.
    * 
    * @param key Clave de la propiedad
    * @param defaultValue Valor por defecto si la propiedad no existe
    * @return Valor de la propiedad como double o el valor por defecto
    */
    public static double getDoubleProperty(String key, double defaultValue) {
    String value = properties.getProperty(key);
    if (value == null) {
    return defaultValue;
    }
    
    try {
    return Double.parseDouble(value);
    } catch (NumberFormatException e) {
    return defaultValue;
    }
    }
    
    /**
    * Establece el valor de una propiedad como double y guarda los cambios.
    * 
    * @param key Clave de la propiedad
    * @param value Valor de la propiedad como double
    * @throws IOException Si ocurre un error al guardar
    */
    public static void setDoubleProperty(String key, double value) throws IOException {
    properties.setProperty(key, String.valueOf(value));
    saveProperties();
    }
    
    /**
    * Obtiene el valor de una propiedad como long.
    * 
    * @param key Clave de la propiedad
    * @param defaultValue Valor por defecto si la propiedad no existe
    * @return Valor de la propiedad como long o el valor por defecto
    */
    public static long getLongProperty(String key, long defaultValue) {
    String value = properties.getProperty(key);
    if (value == null) {
    return defaultValue;
    }
    
    try {
    return Long.parseLong(value);
    } catch (NumberFormatException e) {
    return defaultValue;
    }
    }
    
    /**
    * Establece el valor de una propiedad como long y guarda los cambios.
    * 
    * @param key Clave de la propiedad
    * @param value Valor de la propiedad como long
    * @throws IOException Si ocurre un error al guardar
    */
    public static void setLongProperty(String key, long value) throws IOException {
    properties.setProperty(key, String.valueOf(value));
    saveProperties();
    }
    
    /**
    * Elimina una propiedad y guarda los cambios.
    * 
    * @param key Clave de la propiedad a eliminar
    * @throws IOException Si ocurre un error al guardar
    */
    public static void removeProperty(String key) throws IOException {
    properties.remove(key);
    saveProperties();
    }
    
    /**
    * Verifica si existe una propiedad.
    * 
    * @param key Clave de la propiedad a verificar
    * @return true si la propiedad existe, false en caso contrario
    */
    public static boolean hasProperty(String key) {
    return properties.containsKey(key);
    }
    
    /**
    * Obtiene todas las propiedades.
    * 
    * @return Objeto Properties con todas las propiedades
    */
    public static Properties getAllProperties() {
    // Devolvemos una copia para evitar modificaciones externas
    Properties copy = new Properties();
    copy.putAll(properties);
    return copy;
    }
    
    /**
    * Establece múltiples propiedades y guarda los cambios.
    * 
    * @param newProperties Propiedades a establecer
    * @throws IOException Si ocurre un error al guardar
    */
    public static void setProperties(Properties newProperties) throws IOException {
    for (String key : newProperties.stringPropertyNames()) {
    properties.setProperty(key, newProperties.getProperty(key));
    }
    saveProperties();
    }
    
}