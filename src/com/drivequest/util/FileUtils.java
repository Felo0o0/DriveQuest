/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.util;

import com.drivequest.exception.FileErrorException;
import static com.drivequest.util.ConfigUtils.loadProperties;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ArrayList;

/**
 * Clase de utilidad para el manejo de archivos en el sistema.
 * <p>
 * Proporciona metodos para leer y escribir archivos de texto,
 * facilitando la persistencia de datos.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class FileUtils {
    
    /** Codificación de caracteres por defecto */
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    
    /**
     * Lee un archivo de texto y devuelve su contenido como una lista de lineas.
     * 
     * @param filePath Ruta del archivo a leer
     * @return Lista de lineas del archivo
     * @throws FileErrorException Si ocurre algun error al leer el archivo
     */
    public static List<String> readLines(String filePath) throws FileErrorException {
        return readLines(filePath, DEFAULT_CHARSET);
    }
    
    /**
     * Lee un archivo de texto con la codificación especificada y devuelve su contenido como una lista de lineas.
     * 
     * @param filePath Ruta del archivo a leer
     * @param charset Codificación de caracteres a utilizar
     * @return Lista de lineas del archivo
     * @throws FileErrorException Si ocurre algun error al leer el archivo
     */
    public static List<String> readLines(String filePath, Charset charset) throws FileErrorException {
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new FileErrorException("Error al leer el archivo: " + e.getMessage());
        }
        
        return lines;
    }
    
    /**
     * Escribe una lista de lineas en un archivo de texto.
     * 
     * @param filePath Ruta del archivo a escribir
     * @param lines Lista de lineas a escribir
     * @param append Si es true, agrega al final del archivo; si es false, sobrescribe
     * @throws FileErrorException Si ocurre algun error al escribir el archivo
     */
    public static void writeLines(String filePath, List<String> lines, boolean append) 
            throws FileErrorException {
        writeLines(filePath, lines, append, DEFAULT_CHARSET);
    }
    
    /**
     * Escribe una lista de lineas en un archivo de texto con la codificación especificada.
     * 
     * @param filePath Ruta del archivo a escribir
     * @param lines Lista de lineas a escribir
     * @param append Si es true, agrega al final del archivo; si es false, sobrescribe
     * @param charset Codificación de caracteres a utilizar
     * @throws FileErrorException Si ocurre algun error al escribir el archivo
     */
    public static void writeLines(String filePath, List<String> lines, boolean append, Charset charset) 
            throws FileErrorException {
        try (BufferedWriter buffWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, append), charset))) {
            
            for (String line : lines) {
                buffWriter.write(line);
                buffWriter.newLine();
            }
            
            buffWriter.flush();
        } catch (IOException e) {
            throw new FileErrorException("Error al escribir el archivo: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si un archivo existe.
     * 
     * @param filePath Ruta del archivo a verificar
     * @return true si el archivo existe, false en caso contrario
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }
    
    /**
     * Crea un directorio si no existe.
     * 
     * @param directoryPath Ruta del directorio a crear
     * @return true si el directorio se creo correctamente o ya existia, false en caso contrario
     */
    public static boolean createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return true;
    }
    
    /**
     * Elimina un archivo si existe.
     * 
     * @param filePath Ruta del archivo a eliminar
     * @return true si el archivo se elimino correctamente o no existia, false en caso contrario
     */
    public static boolean deleteFileIfExists(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }
    
    /**
     * Copia un archivo de origen a destino.
     * 
     * @param sourcePath Ruta del archivo de origen
     * @param targetPath Ruta del archivo de destino
     * @param overwrite Si es true, sobrescribe el archivo de destino si existe
     * @throws FileErrorException Si ocurre algun error al copiar el archivo
     */
    public static void copyFile(String sourcePath, String targetPath, boolean overwrite) 
            throws FileErrorException {
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            
            if (overwrite) {
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            } else if (!Files.exists(target)) {
                Files.copy(source, target);
            } else {
                throw new FileErrorException("El archivo de destino ya existe y no se ha especificado sobrescribir");
            }
        } catch (IOException e) {
            throw new FileErrorException("Error al copiar el archivo: " + e.getMessage());
        }
    }
    
    /**
     * Mueve un archivo de origen a destino.
     * 
     * @param sourcePath Ruta del archivo de origen
     * @param targetPath Ruta del archivo de destino
     * @param overwrite Si es true, sobrescribe el archivo de destino si existe
     * @throws FileErrorException Si ocurre algun error al mover el archivo
     */
    public static void moveFile(String sourcePath, String targetPath, boolean overwrite) 
            throws FileErrorException {
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            
            if (overwrite) {
                Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            } else if (!Files.exists(target)) {
                Files.move(source, target);
            } else {
                throw new FileErrorException("El archivo de destino ya existe y no se ha especificado sobrescribir");
            }
        } catch (IOException e) {
            throw new FileErrorException("Error al mover el archivo: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene el tamaño de un archivo en bytes.
     * 
     * @param filePath Ruta del archivo
     * @return Tamaño del archivo en bytes
     * @throws FileErrorException Si ocurre algun error al obtener el tamaño
     */
    public static long getFileSize(String filePath) throws FileErrorException {
        try {
            Path path = Paths.get(filePath);
            return Files.size(path);
        } catch (IOException e) {
            throw new FileErrorException("Error al obtener el tamaño del archivo: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene la fecha de última modificación de un archivo.
     * 
     * @param filePath Ruta del archivo
     * @return Fecha de última modificación en milisegundos desde epoch
     * @throws FileErrorException Si ocurre algun error al obtener la fecha
     */
    public static long getLastModifiedTime(String filePath) throws FileErrorException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileErrorException("El archivo no existe: " + filePath);
        }
        return file.lastModified();
    }
    
    /**
     * Lee todo el contenido de un archivo como una cadena de texto.
     * 
     * @param filePath Ruta del archivo a leer
     * @return Contenido del archivo como cadena de texto
     * @throws FileErrorException Si ocurre algun error al leer el archivo
     */
    public static String readFileAsString(String filePath) throws FileErrorException {
        return readFileAsString(filePath, DEFAULT_CHARSET);
    }
    
    /**
     * Lee todo el contenido de un archivo como una cadena de texto con la codificación especificada.
     * 
     * @param filePath Ruta del archivo a leer
     * @param charset Codificación de caracteres a utilizar
     * @return Contenido del archivo como cadena de texto
     * @throws FileErrorException Si ocurre algun error al leer el archivo
     */
    public static String readFileAsString(String filePath, Charset charset) throws FileErrorException {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            return new String(bytes, charset);
        } catch (IOException e) {
            throw new FileErrorException("Error al leer el archivo como texto: " + e.getMessage());
        }
    }
    
    /**
     * Escribe una cadena de texto en un archivo.
     * 
     * @param filePath Ruta del archivo a escribir
     * @param content Contenido a escribir
     * @param append Si es true, agrega al final del archivo; si es false, sobrescribe
     * @throws FileErrorException Si ocurre algun error al escribir el archivo
     */
    public static void writeStringToFile(String filePath, String content, boolean append) 
            throws FileErrorException {
        writeStringToFile(filePath, content, append, DEFAULT_CHARSET);
    }
    
    /**
     * Escribe una cadena de texto en un archivo con la codificación especificada.
     * 
     * @param filePath Ruta del archivo a escribir
     * @param content Contenido a escribir
     * @param append Si es true, agrega al final del archivo; si es false, sobrescribe
     * @param charset Codificación de caracteres a utilizar
     * @throws FileErrorException Si ocurre algun error al escribir el archivo
     */
    public static void writeStringToFile(String filePath, String content, boolean append, Charset charset) 
            throws FileErrorException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, append), charset))) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new FileErrorException("Error al escribir texto en el archivo: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene la extensión de un archivo.
     * 
     * @param filePath Ruta del archivo
     * @return Extensión del archivo (sin el punto) o cadena vacía si no tiene extensión
     */
    public static String getFileExtension(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
            return filePath.substring(dotIndex + 1);
        }
        return "";
    }
    
     /**
     * Carga la configuración desde el archivo.
     * Este método es un alias para loadProperties().
     * 
     * @throws IOException Si ocurre un error al leer el archivo
     */
     public static void loadConfiguration() throws IOException {
     loadProperties();
}
    
    /**
     * Obtiene el nombre de un archivo sin la ruta.
     * 
     * @param filePath Ruta del archivo
     * @return Nombre del archivo sin la ruta
     */
    public static String getFileName(String filePath) {
        int separatorIndex = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        if (separatorIndex > 0 && separatorIndex < filePath.length() - 1) {
            return filePath.substring(separatorIndex + 1);
        }
        return filePath;
    }
}