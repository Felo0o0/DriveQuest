/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.drivequest.exception.FileErrorException;
import com.drivequest.service.FleetManager;
import com.drivequest.util.ThreadUtils;

/**
 * MainFrame - Frame principal de la aplicacion DriveQuest.
 * 
 * Esta clase implementa el frame principal de la aplicación, que contiene
 * todos los paneles de la interfaz gráfica y el menú de navegación.
 * Gestiona la navegación entre los distintos paneles y proporciona
 * funcionalidades comunes para la aplicación.
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class MainFrame extends JFrame {
    
    /**
    * Gestor de la flota de vehículos.
    */
    private FleetManager fleetManager;
    
    /**
    * Layout para la gestión de los paneles.
    */
    private CardLayout cardLayout;
    
    /**
    * Panel contenedor para el CardLayout.
    */
    private JPanel cardPanel;
    
    /**
    * Panel para agregar vehículos.
    */
    private AddVehiclePanel addVehiclePanel;
    
    /**
    * Panel para listar vehículos.
    */
    private VehicleListPanel vehicleListPanel;
    
    /**
    * Panel para generar facturas.
    */
    private InvoicePanel invoicePanel;
    
    /**
    * Panel para ver estadísticas.
    */
    private StatisticsPanel statisticsPanel;
    
    /**
    * Panel para gestionar arriendos.
    */
    private RentalPanel rentalPanel;
    
    /**
    * Constante para identificar el panel de agregar vehículo.
    */
    private static final String PANEL_ADD_VEHICLE = "PANEL_ADD_VEHICLE";
    
    /**
    * Constante para identificar el panel de listar vehículos.
    */
    private static final String PANEL_VEHICLE_LIST = "PANEL_VEHICLE_LIST";
    
    /**
    * Constante para identificar el panel de facturación.
    */
    private static final String PANEL_INVOICE = "PANEL_INVOICE";
    
    /**
    * Constante para identificar el panel de estadísticas.
    */
    private static final String PANEL_STATISTICS = "PANEL_STATISTICS";
    
    /**
    * Constante para identificar el panel de gestión de arriendos.
    */
    private static final String PANEL_RENTAL = "PANEL_RENTAL";
    
    /**
    * Constructor principal para el frame principal de la aplicacion.
    * 
    * Inicializa todos los componentes de la interfaz gráfica, configura
    * el menú y establece los manejadores de eventos necesarios.
    * 
    * @param fleetManager gestor de flota a utilizar
    */
    public MainFrame(FleetManager fleetManager) {
        // Validar que fleetManager no sea nulo
        if (fleetManager == null) {
            throw new IllegalArgumentException("El gestor de flota no puede ser nulo");
        }
        
        this.fleetManager = fleetManager;
        
        // Inicializar componentes y configurar la interfaz
        initComponents();
        configureMenu();
        
        // Configurar propiedades del frame
        setTitle("DriveQuest - Sistema de Gestion de Alquiler de Vehiculos");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Configurar el cierre de la aplicación
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
        
        // Mostrar el panel de lista de vehiculos por defecto
        ThreadUtils.runOnEDT(() -> showVehicleListPanel());
    }
    
    /**
    * Inicializa los componentes de la interfaz.
    * 
    * Crea los paneles principales y los agrega al layout de tarjetas
    * para permitir la navegación entre ellos.
    */
    private void initComponents() {
        // Crear el layout y panel principal
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Inicializar paneles
        addVehiclePanel = new AddVehiclePanel(fleetManager);
        vehicleListPanel = new VehicleListPanel(fleetManager, this);
        invoicePanel = new InvoicePanel(fleetManager);
        statisticsPanel = new StatisticsPanel(fleetManager);
        rentalPanel = new RentalPanel(fleetManager);
        
        // Agregar paneles al cardLayout
        cardPanel.add(addVehiclePanel, PANEL_ADD_VEHICLE);
        cardPanel.add(vehicleListPanel, PANEL_VEHICLE_LIST);
        cardPanel.add(invoicePanel, PANEL_INVOICE);
        cardPanel.add(statisticsPanel, PANEL_STATISTICS);
        cardPanel.add(rentalPanel, PANEL_RENTAL);
        
        // Agregar panel principal al frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(cardPanel, BorderLayout.CENTER);
    }
    
    /**
    * Configura el menu de la aplicacion.
    * 
    * Crea la barra de menú y agrega todos los ítems necesarios
    * con sus respectivos manejadores de eventos.
    */
    private void configureMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Vehiculos
        JMenu vehiclesMenu = new JMenu("Vehiculos");
        
        JMenuItem addVehicleMenuItem = new JMenuItem("Agregar Vehiculo");
        addVehicleMenuItem.addActionListener(e -> showAddVehiclePanel());
        
        JMenuItem listVehiclesMenuItem = new JMenuItem("Listar Vehiculos");
        listVehiclesMenuItem.addActionListener(e -> showVehicleListPanel());
        
        vehiclesMenu.add(addVehicleMenuItem);
        vehiclesMenu.add(listVehiclesMenuItem);
        
        // Menu Arriendos
        JMenu rentalsMenu = new JMenu("Arriendos");
        
        JMenuItem manageRentalsMenuItem = new JMenuItem("Gestionar Arriendos");
        manageRentalsMenuItem.addActionListener(e -> showRentalPanel());
        
        rentalsMenu.add(manageRentalsMenuItem);
        
        // Menu Facturacion
        JMenu billingMenu = new JMenu("Facturacion");
        
        JMenuItem invoiceMenuItem = new JMenuItem("Generar Factura");
        invoiceMenuItem.addActionListener(e -> showInvoicePanel());
        
        billingMenu.add(invoiceMenuItem);
        
        // Menu Estadisticas
        JMenu statsMenu = new JMenu("Estadisticas");
        
        JMenuItem statsMenuItem = new JMenuItem("Ver Estadisticas");
        statsMenuItem.addActionListener(e -> showStatisticsPanel());
        
        statsMenu.add(statsMenuItem);
        
        // Menu Ayuda
        JMenu helpMenu = new JMenu("Ayuda");
        
        JMenuItem aboutMenuItem = new JMenuItem("Acerca de");
        aboutMenuItem.addActionListener(e -> showAboutDialog());
        
        helpMenu.add(aboutMenuItem);
        
        // Menu Salir
        JMenu exitMenu = new JMenu("Salir");
        
        JMenuItem exitMenuItem = new JMenuItem("Salir de la aplicacion");
        exitMenuItem.addActionListener(e -> exitApplication());
        
        exitMenu.add(exitMenuItem);
        
        // Agregar menus a la barra de menu
        menuBar.add(vehiclesMenu);
        menuBar.add(rentalsMenu);
        menuBar.add(billingMenu);
        menuBar.add(statsMenu);
        menuBar.add(helpMenu);
        menuBar.add(exitMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
    * Muestra el panel de agregar vehiculo.
    * 
    * Cambia la vista actual al panel para agregar un nuevo vehículo.
    */
    public void showAddVehiclePanel() {
        ThreadUtils.runOnEDT(() -> {
            cardLayout.show(cardPanel, PANEL_ADD_VEHICLE);
            // Actualizar el título de la ventana
            setTitle("DriveQuest - Agregar Vehiculo");
        });
    }
    
    /**
    * Muestra el panel de lista de vehiculos.
    * 
    * Cambia la vista actual al panel que muestra la lista de vehículos,
    * actualizando previamente la lista con los datos más recientes.
    */
    public void showVehicleListPanel() {
        ThreadUtils.runOnEDT(() -> {
            // Actualizar la lista de vehículos
            vehicleListPanel.refreshVehicleList();
            cardLayout.show(cardPanel, PANEL_VEHICLE_LIST);
            // Actualizar el título de la ventana
            setTitle("DriveQuest - Lista de Vehiculos");
        });
    }
    
    /**
    * Muestra el panel de facturacion.
    * 
    * Cambia la vista actual al panel para generar facturas.
    */
    public void showInvoicePanel() {
        ThreadUtils.runOnEDT(() -> {
            cardLayout.show(cardPanel, PANEL_INVOICE);
            // Actualizar el título de la ventana
            setTitle("DriveQuest - Facturacion");
        });
    }
    
    /**
    * Muestra el panel de estadisticas.
    * 
    * Cambia la vista actual al panel que muestra las estadísticas,
    * actualizando previamente los datos mostrados.
    */
    public void showStatisticsPanel() {
        ThreadUtils.runOnEDT(() -> {
            // Actualizar las estadísticas
            statisticsPanel.refreshStatistics();
            cardLayout.show(cardPanel, PANEL_STATISTICS);
            // Actualizar el título de la ventana
            setTitle("DriveQuest - Estadisticas");
        });
    }
    
    /**
    * Muestra el panel de gestión de arriendos.
    * 
    * Cambia la vista actual al panel para gestionar arriendos de vehículos.
    */
    public void showRentalPanel() {
        ThreadUtils.runOnEDT(() -> {
            cardLayout.show(cardPanel, PANEL_RENTAL);
            // Actualizar el título de la ventana
            setTitle("DriveQuest - Gestión de Arriendos");
        });
    }
    
    /**
    * Muestra un dialogo con informacion sobre la aplicacion.
    * 
    * Presenta al usuario información sobre la versión y desarrolladores
    * de la aplicación.
    */
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "DriveQuest - Sistema de Gestion de Alquiler de Vehiculos\n" +
            "Version 1.0\n\n" +
            "Desarrollado para el curso PRY2202\n" +
            "Programacion Orientada a Objetos\n\n" +
            "© 2025 - Todos los derechos reservados",
            "Acerca de DriveQuest",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
    * Maneja el cierre de la aplicacion, guardando los datos si es necesario.
    * 
    * Pregunta al usuario si desea guardar los cambios antes de cerrar
    * la aplicación, y realiza la acción correspondiente según la respuesta.
    */
    private void exitApplication() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "¿Desea guardar los cambios antes de salir?",
            "Salir de la aplicacion",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        // Si el usuario cancela, no hacer nada
        if (option == JOptionPane.CANCEL_OPTION) {
            return;
        }
        
        // Si el usuario quiere guardar, intentar guardar los datos
        if (option == JOptionPane.YES_OPTION) {
            try {
                // Guardar los vehículos
                fleetManager.saveVehicles();
                JOptionPane.showMessageDialog(
                    this,
                    "Datos guardados exitosamente",
                    "Guardar Datos",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (FileErrorException e) {
                // Mostrar error y no cerrar la aplicación
                JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar los datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return; // No salir si hay un error al guardar
            }
        }
        
        // Cerrar la aplicación
        dispose();
        System.exit(0);
    }
    
    /**
    * Obtiene el gestor de flota utilizado por la aplicacion.
    * 
    * @return el gestor de flota
    */
    public FleetManager getFleetManager() {
        return fleetManager;
    }
    
    /**
    * Actualiza el título de la ventana según el panel activo.
    * 
    * @param title el nuevo título a establecer
    */
    public void updateWindowTitle(String title) {
        ThreadUtils.runOnEDT(() -> {
            setTitle("DriveQuest - " + title);
        });
    }
}