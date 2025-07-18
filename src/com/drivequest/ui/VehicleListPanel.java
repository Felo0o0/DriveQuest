/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.drivequest.exception.FileErrorException;
import com.drivequest.model.CargoVehicle;
import com.drivequest.model.PassengerVehicle;
import com.drivequest.model.Vehicle;
import com.drivequest.service.FleetManager;
import com.drivequest.util.ThreadUtils;

/**
 * VehicleListPanel - Panel que muestra la lista de vehículos registrados en el sistema.
 * 
 * Este panel permite visualizar todos los vehículos registrados en el sistema,
 * así como realizar operaciones como editar o eliminar un vehículo seleccionado.
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class VehicleListPanel extends JPanel {

    /**
    * Gestor de la flota de vehículos.
    */
    private FleetManager fleetManager;
    
    /**
    * Referencia al frame principal.
    */
    private MainFrame mainFrame;
    
    /**
    * Tabla para mostrar los vehículos.
    */
    private JTable vehicleTable;
    
    /**
    * Modelo de datos para la tabla.
    */
    private DefaultTableModel tableModel;
    
    /**
    * Botón para editar vehículo.
    */
    private JButton editButton;
    
    /**
    * Botón para eliminar vehículo.
    */
    private JButton deleteButton;
    
    /**
    * Constructor del panel de lista de vehículos.
    * 
    * @param fleetManager gestor de flota para acceder a los datos
    * @param mainFrame referencia al frame principal para navegación
    */
    public VehicleListPanel(FleetManager fleetManager, MainFrame mainFrame) {
        // Validar parámetros
        if (fleetManager == null) {
            throw new IllegalArgumentException("El gestor de flota no puede ser nulo");
        }
        if (mainFrame == null) {
            throw new IllegalArgumentException("El frame principal no puede ser nulo");
        }
        
        this.fleetManager = fleetManager;
        this.mainFrame = mainFrame;
        
        // Inicializar componentes
        initComponents();
    }
    
    /**
    * Inicializa los componentes del panel.
    * 
    * Crea la tabla para mostrar los vehículos y los botones para
    * las acciones disponibles sobre ellos.
    */
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Título del panel
        JLabel titleLabel = new JLabel("Lista de Vehiculos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Modelo de tabla y tabla
        String[] columnNames = {"Patente", "Modelo", "Año", "Precio Diario", "Tipo", "Capacidad", "Disponible"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición directa en la tabla
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Para que la columna de disponibilidad muestre checkbox
                return columnIndex == 6 ? Boolean.class : String.class;
            }
        };
        
        vehicleTable = new JTable(tableModel);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehicleTable.getSelectionModel().addListSelectionListener(e -> updateButtonState());
        vehicleTable.setAutoCreateRowSorter(true);
        
        // Ajustar ancho de columnas
        vehicleTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Patente
        vehicleTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Modelo
        vehicleTable.getColumnModel().getColumn(2).setPreferredWidth(60);  // Año
        vehicleTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Precio Diario
        vehicleTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Tipo
        vehicleTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Capacidad
        vehicleTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Disponible
        
        // Configurar la tabla con scroll
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
        
        editButton = new JButton("Editar");
        editButton.setEnabled(false);
        editButton.addActionListener(e -> editSelectedVehicle());
        
        deleteButton = new JButton("Eliminar");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> deleteSelectedVehicle());
        
        JButton manageRentalsButton = new JButton("Gestionar Arriendos");
        manageRentalsButton.addActionListener(e -> mainFrame.showRentalPanel());
        
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(manageRentalsButton);
        
        // Botón para actualizar la lista
        JButton refreshButton = new JButton("Actualizar Lista");
        refreshButton.addActionListener(e -> refreshVehicleList());
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Cargar datos iniciales
        refreshVehicleList();
    }
    
    /**
    * Actualiza el estado de los botones según la selección en la tabla.
    * 
    * Habilita o deshabilita los botones de acción dependiendo de
    * si hay un vehículo seleccionado.
    */
    private void updateButtonState() {
        ThreadUtils.runOnEDT(() -> {
            boolean rowSelected = vehicleTable.getSelectedRow() != -1;
            editButton.setEnabled(rowSelected);
            deleteButton.setEnabled(rowSelected);
        });
    }
    
    /**
    * Actualiza la lista de vehículos en la tabla.
    * 
    * Obtiene la lista actualizada de vehículos desde el gestor
    * de flota y la muestra en la tabla.
    */
    public void refreshVehicleList() {
        ThreadUtils.runOnEDT(() -> {
            // Limpiar la tabla
            tableModel.setRowCount(0);
            
            // Obtener todos los vehículos y agregarlos a la tabla
            List<Vehicle> vehicles = fleetManager.getAllVehicles();
            for (Vehicle vehicle : vehicles) {
                String type = (vehicle instanceof PassengerVehicle) ? "Pasajeros" : "Carga";
                String capacity;
                
                if (vehicle instanceof PassengerVehicle) {
                    capacity = ((PassengerVehicle) vehicle).getPassengerCapacity() + " pasajeros";
                } else if (vehicle instanceof CargoVehicle) {
                    capacity = ((CargoVehicle) vehicle).getLoadCapacity() + " kg";
                } else {
                    capacity = "N/A";
                }
                
                Object[] rowData = {
                    vehicle.getLicensePlate(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    String.format("$%.2f", vehicle.getDailyPrice()),
                    type,
                    capacity,
                    vehicle.isAvailable()
                };
                
                tableModel.addRow(rowData);
            }
            
            // Actualizar estado de los botones
            updateButtonState();
            
            // Mostrar mensaje si no hay vehículos
            if (vehicles.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "No hay vehiculos registrados en el sistema.",
                    "Lista Vacia",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
    }
    
    /**
    * Edita el vehículo seleccionado.
    * 
    * Actualmente esta funcionalidad no está implementada y muestra
    * un mensaje informativo al usuario.
    */
    private void editSelectedVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow != -1) {
            // Convertir el índice de la fila si la tabla está ordenada
            int modelRow = vehicleTable.convertRowIndexToModel(selectedRow);
            String licensePlate = (String) tableModel.getValueAt(modelRow, 0);
            
            // Aquí implementar la lógica para editar el vehículo
            JOptionPane.showMessageDialog(
                this,
                "La edicion de vehiculos no esta implementada en esta version.",
                "Funcionalidad no disponible",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    
    /**
    * Elimina el vehículo seleccionado.
    * 
    * Solicita confirmación al usuario y, si confirma, elimina
    * el vehículo del sistema.
    */
    private void deleteSelectedVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow != -1) {
            // Convertir el índice de la fila si la tabla está ordenada
            int modelRow = vehicleTable.convertRowIndexToModel(selectedRow);
            String licensePlate = (String) tableModel.getValueAt(modelRow, 0);
            
            int option = JOptionPane.showConfirmDialog(
                this,
                "¿Esta seguro de eliminar el vehiculo con patente " + licensePlate + "?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (option == JOptionPane.YES_OPTION) {
                try {
                    fleetManager.removeVehicle(licensePlate);
                    refreshVehicleList();
                    JOptionPane.showMessageDialog(
                        this,
                        "Vehiculo eliminado exitosamente",
                        "Eliminacion exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (FileErrorException e) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error al guardar cambios en archivo: " + e.getMessage(),
                        "Error de archivo",
                        JOptionPane.ERROR_MESSAGE
                    );
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error al eliminar el vehiculo: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }
}