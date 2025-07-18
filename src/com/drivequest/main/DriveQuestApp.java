/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.main;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.drivequest.dao.FileVehicleDAO;
import com.drivequest.dao.VehicleDAO;
import com.drivequest.exception.FileErrorException;
import com.drivequest.service.FleetManager;
import com.drivequest.ui.MainFrame;
import com.drivequest.util.ConfigUtils;

/**
 * DriveQuestApp - Clase principal que inicia la aplicacion DriveQuest.
 * 
 * Esta clase implementa el punto de entrada principal del sistema de gestión
 * de alquiler de vehículos DriveQuest. Se encarga de inicializar todos los
 * componentes necesarios, configurar el sistema y mostrar la interfaz gráfica.
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class DriveQuestApp {

    /**
     * Constante que indica el nombre de la aplicacion.
     */
    private static final String APP_NAME = "DriveQuest";
    
    /**
     * Constante que indica la version de la aplicacion.
     */
    private static final String APP_VERSION = "1.0";
    
    /**
     * Método principal que inicia la aplicacion.
     * 
     * Este método es el punto de entrada para la aplicación. Inicializa
     * todos los componentes necesarios, configura el look and feel,
     * carga los datos existentes y muestra la interfaz gráfica.
     * 
     * @param args argumentos de linea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        // Configurar el look and feel para una mejor experiencia visual
        configureLookAndFeel();
        
        // Iniciar la aplicación en el EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Iniciando " + APP_NAME + " versión " + APP_VERSION);
                
                // Cargar configuraciones
                ConfigUtils.loadConfiguration();
                
                // Inicializar el DAO para la persistencia
                VehicleDAO vehicleDAO = new FileVehicleDAO();
                
                // Inicializar el gestor de flota con el DAO
                FleetManager fleetManager = new FleetManager(vehicleDAO);
                
                // Cargar datos existentes
                loadVehicleData(fleetManager);
                
                // Crear y mostrar la interfaz grafica
                MainFrame mainFrame = new MainFrame(fleetManager);
                mainFrame.setVisible(true);
                
                System.out.println(APP_NAME + " iniciado exitosamente");
            } catch (Exception e) {
                handleStartupError(e);
            }
        });
    }
    
    /**
     * Configura el Look and Feel de la aplicacion.
     * 
     * Intenta utilizar el look and feel del sistema operativo para
     * una experiencia de usuario más integrada con el entorno.
     */
    private static void configureLookAndFeel() {
        try {
            // Utilizar el look and feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("Look and Feel configurado correctamente");
        } catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Error al configurar Look and Feel: " + e.getMessage());
            // Continuar con el look and feel por defecto
        }
    }
    
    /**
     * Carga los datos de vehiculos existentes.
     * 
     * Intenta cargar los vehículos desde el sistema de persistencia.
     * Si ocurre algún error, lo registra pero permite continuar la ejecución.
     * 
     * @param fleetManager el gestor de flota donde se cargarán los datos
     */
    private static void loadVehicleData(FleetManager fleetManager) {
        try {
            fleetManager.loadVehicles();
            System.out.println("Vehiculos cargados exitosamente");
        } catch (FileErrorException e) {
            System.err.println("Error al cargar vehiculos: " + e.getMessage());
            System.err.println("La aplicacion continuara sin datos previos");
            // Continuar con la aplicación aunque no se puedan cargar los datos
        }
    }
    
    /**
     * Maneja errores durante el inicio de la aplicacion.
     * 
     * Registra el error en la consola y muestra un mensaje al usuario.
     * 
     * @param e la excepcion que ocurrió durante el inicio
     */
    private static void handleStartupError(Exception e) {
        System.err.println("Error fatal al iniciar la aplicacion: " + e.getMessage());
        e.printStackTrace();
        
        JOptionPane.showMessageDialog(
            null,
            "Error al iniciar la aplicacion: " + e.getMessage() + 
            "\nRevise el log para mas detalles.",
            "Error de Inicio - " + APP_NAME,
            JOptionPane.ERROR_MESSAGE
        );
        
        // Terminar la aplicación con código de error
        System.exit(1);
    }
}