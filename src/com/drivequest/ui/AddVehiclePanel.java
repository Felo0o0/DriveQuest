/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.ui;

import com.drivequest.model.CargoVehicle;
import com.drivequest.model.PassengerVehicle;
import com.drivequest.service.FleetManager;
import com.drivequest.exception.DuplicateLicensePlateException;
import com.drivequest.exception.FileErrorException;
import com.drivequest.exception.InvalidVehicleException;
import com.drivequest.util.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel para agregar nuevos vehiculos al sistema.
 * <p>
 * Implementa una interfaz grafica con formularios para crear
 * vehiculos de carga y de pasajeros.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class AddVehiclePanel extends JPanel {
    
    /** Gestor de flota para agregar vehiculos */
    private final FleetManager fleetManager;
    
    // Componentes de la interfaz
    private final JTextField txtLicensePlate;
    private final JTextField txtModel;
    private final JTextField txtYear;
    private final JTextField txtDailyPrice;
    private final JTextField txtCapacity;
    private final JComboBox<String> cmbVehicleType;
    private final JLabel lblCapacity;
    private final JButton btnAdd;
    private final JButton btnClear;
    
    /**
     * Constructor del panel para agregar vehiculos.
     * 
     * @param fleetManager Gestor de flota a utilizar
     */
    public AddVehiclePanel(FleetManager fleetManager) {
        this.fleetManager = fleetManager;
        
        // Configuración del panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Agregar Vehiculo"));
        
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        // Componentes del formulario
        formPanel.add(new JLabel("Tipo de Vehiculo:"));
        cmbVehicleType = new JComboBox<>(new String[]{"Vehiculo de Carga", "Vehiculo de Pasajeros"});
        formPanel.add(cmbVehicleType);
        
        formPanel.add(new JLabel("Patente:"));
        txtLicensePlate = new JTextField(10);
        formPanel.add(txtLicensePlate);
        
        formPanel.add(new JLabel("Modelo:"));
        txtModel = new JTextField(10);
        formPanel.add(txtModel);
        
        formPanel.add(new JLabel("Año:"));
        txtYear = new JTextField(10);
        formPanel.add(txtYear);
        
        formPanel.add(new JLabel("Precio Diario ($):"));
        txtDailyPrice = new JTextField(10);
        formPanel.add(txtDailyPrice);
        
        lblCapacity = new JLabel("Capacidad de Carga (kg):");
        formPanel.add(lblCapacity);
        txtCapacity = new JTextField(10);
        formPanel.add(txtCapacity);
        
        // Cambiar la etiqueta según el tipo de vehículo seleccionado
        cmbVehicleType.addActionListener(e -> {
            if (cmbVehicleType.getSelectedIndex() == 0) {
                lblCapacity.setText("Capacidad de Carga (kg):");
            } else {
                lblCapacity.setText("Capacidad de Pasajeros:");
            }
        });
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnAdd = new JButton("Agregar");
        btnClear = new JButton("Limpiar");
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnClear);
        
        // Agregar paneles al panel principal
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Configurar eventos
        configurarEventos();
    }
    
    /**
     * Configura los eventos de los componentes del panel.
     */
    private void configurarEventos() {
        // Evento del botón agregar
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    agregarVehiculo();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AddVehiclePanel.this,
                            "Error: Verifique los valores numericos ingresados",
                            "Error de Formato", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AddVehiclePanel.this,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Evento del botón limpiar
        btnClear.addActionListener(e -> limpiarFormulario());
    }
    
    /**
     * Agrega un nuevo vehiculo con los datos del formulario.
     * 
     * @throws NumberFormatException Si hay error en conversion de numeros
     * @throws DuplicateLicensePlateException Si la patente ya existe
     * @throws InvalidVehicleException Si los datos del vehiculo son invalidos
     * @throws FileErrorException Si hay error al guardar en archivo
     */
    private void agregarVehiculo() throws NumberFormatException, 
            DuplicateLicensePlateException, InvalidVehicleException, FileErrorException {
        
        // Obtener datos comunes
        String licensePlate = txtLicensePlate.getText().trim().toUpperCase();
        String model = txtModel.getText().trim();
        int year = Integer.parseInt(txtYear.getText().trim());
        double dailyPrice = Double.parseDouble(txtDailyPrice.getText().trim());
        int capacity = Integer.parseInt(txtCapacity.getText().trim());
        
        // Validar datos comunes
        if (!ValidationUtils.isValidLicensePlate(licensePlate)) {
            throw new InvalidVehicleException("Formato de patente invalido");
        }
        
        if (!ValidationUtils.isValidModel(model)) {
            throw new InvalidVehicleException("El modelo no puede estar vacio");
        }
        
        if (!ValidationUtils.isValidYear(year)) {
            throw new InvalidVehicleException("Año invalido");
        }
        
        if (!ValidationUtils.isValidPrice(dailyPrice)) {
            throw new InvalidVehicleException("Precio diario invalido");
        }
        
        // Crear el vehículo según el tipo
        if (cmbVehicleType.getSelectedIndex() == 0) {
            // Vehículo de carga
            if (!ValidationUtils.isValidLoadCapacity(capacity)) {
                throw new InvalidVehicleException("Capacidad de carga invalida");
            }
            
            CargoVehicle vehicle = new CargoVehicle(
                    licensePlate, model, year, dailyPrice, 0, capacity);
            
            fleetManager.addVehicle(vehicle);
            
        } else {
            // Vehículo de pasajeros
            if (!ValidationUtils.isValidPassengerCapacity(capacity)) {
                throw new InvalidVehicleException("Capacidad de pasajeros invalida");
            }
            
            PassengerVehicle vehicle = new PassengerVehicle(
                    licensePlate, model, year, dailyPrice, 0, capacity);
            
            fleetManager.addVehicle(vehicle);
        }
        
        JOptionPane.showMessageDialog(this,
                "Vehiculo agregado correctamente",
                "Exito", JOptionPane.INFORMATION_MESSAGE);
        
        limpiarFormulario();
    }
    
    /**
     * Limpia los campos del formulario.
     */
    private void limpiarFormulario() {
        txtLicensePlate.setText("");
        txtModel.setText("");
        txtYear.setText("");
        txtDailyPrice.setText("");
        txtCapacity.setText("");
        cmbVehicleType.setSelectedIndex(0);
        txtLicensePlate.requestFocus();
    }
}