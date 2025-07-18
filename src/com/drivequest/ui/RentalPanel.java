/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.ui;

import com.drivequest.model.Vehicle;
import com.drivequest.service.FleetManager;
import com.drivequest.util.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Panel para la gestión de arriendos de vehículos.
 * <p>
 * Proporciona una interfaz para arrendar vehículos, extender arriendos,
 * verificar disponibilidad y finalizar arriendos.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 */
public class RentalPanel extends JPanel {
    
    private final FleetManager fleetManager;
    private final JTextField licensePlateField;
    private final JTextField startDateField;
    private final JTextField endDateField;
    private final JTextArea resultArea;
    private final DateTimeFormatter dateFormatter;
    
    /**
     * Constructor de la clase RentalPanel.
     * 
     * @param fleetManager Gestor de flota a utilizar
     */
    public RentalPanel(FleetManager fleetManager) {
        this.fleetManager = fleetManager;
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        setLayout(new BorderLayout());
        
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("Patente:"));
        licensePlateField = new JTextField();
        formPanel.add(licensePlateField);
        
        formPanel.add(new JLabel("Fecha inicio (dd/mm/aaaa):"));
        startDateField = new JTextField();
        formPanel.add(startDateField);
        
        formPanel.add(new JLabel("Fecha fin (dd/mm/aaaa):"));
        endDateField = new JTextField();
        formPanel.add(endDateField);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton rentButton = new JButton("Arrendar");
        rentButton.addActionListener(e -> rentVehicle());
        buttonPanel.add(rentButton);
        
        JButton extendButton = new JButton("Extender");
        extendButton.addActionListener(e -> extendRental());
        buttonPanel.add(extendButton);
        
        JButton checkButton = new JButton("Verificar disponibilidad");
        checkButton.addActionListener(e -> checkAvailability());
        buttonPanel.add(checkButton);
        
        JButton finishButton = new JButton("Finalizar arriendo");
        finishButton.addActionListener(e -> finishRental());
        buttonPanel.add(finishButton);
        
        // Área de resultados
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        
        // Agregar componentes al panel principal
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }
    
    /**
     * Arrenda un vehículo con las fechas especificadas.
     */
    private void rentVehicle() {
        try {
            String licensePlate = licensePlateField.getText().trim();
            if (!ValidationUtils.isValidLicensePlate(licensePlate)) {
                showError("Patente inválida");
                return;
            }
            
            LocalDate startDate = parseDate(startDateField.getText().trim());
            LocalDate endDate = parseDate(endDateField.getText().trim());
            
            if (startDate == null || endDate == null) {
                showError("Formato de fecha inválido. Use dd/mm/aaaa");
                return;
            }
            
            if (startDate.isAfter(endDate)) {
                showError("La fecha de inicio no puede ser posterior a la fecha de fin");
                return;
            }
            
            boolean success = fleetManager.rentVehicle(licensePlate, startDate, endDate);
            if (success) {
                resultArea.setText("Vehículo arrendado correctamente\n");
                showRentalPeriods(licensePlate);
            } else {
                showError("No se pudo arrendar el vehículo. Verifique disponibilidad y fechas");
            }
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }
    
    /**
     * Extiende un arriendo existente.
     */
    private void extendRental() {
        try {
            String licensePlate = licensePlateField.getText().trim();
            if (!ValidationUtils.isValidLicensePlate(licensePlate)) {
                showError("Patente inválida");
                return;
            }
            
            LocalDate originalEndDate = parseDate(startDateField.getText().trim());
            LocalDate newEndDate = parseDate(endDateField.getText().trim());
            
            if (originalEndDate == null || newEndDate == null) {
                showError("Formato de fecha inválido. Use dd/mm/aaaa");
                return;
            }
            
            if (newEndDate.isBefore(originalEndDate) || newEndDate.isEqual(originalEndDate)) {
                showError("La nueva fecha de fin debe ser posterior a la fecha original");
                return;
            }
            
            boolean success = fleetManager.extendRental(licensePlate, originalEndDate, newEndDate);
            if (success) {
                resultArea.setText("Arriendo extendido correctamente\n");
                showRentalPeriods(licensePlate);
            } else {
                showError("No se pudo extender el arriendo. Verifique fechas y disponibilidad");
            }
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }
    
    /**
     * Verifica la disponibilidad de un vehículo en un periodo.
     */
    private void checkAvailability() {
        try {
            String licensePlate = licensePlateField.getText().trim();
            if (!ValidationUtils.isValidLicensePlate(licensePlate)) {
                showError("Patente inválida");
                return;
            }
            
            LocalDate startDate = parseDate(startDateField.getText().trim());
            LocalDate endDate = parseDate(endDateField.getText().trim());
            
            if (startDate == null || endDate == null) {
                showError("Formato de fecha inválido. Use dd/mm/aaaa");
                return;
            }
            
            if (startDate.isAfter(endDate)) {
                showError("La fecha de inicio no puede ser posterior a la fecha de fin");
                return;
            }
            
            boolean available = fleetManager.isVehicleAvailable(licensePlate, startDate, endDate);
            if (available) {
                resultArea.setText("El vehículo está disponible para el período seleccionado\n");
            } else {
                resultArea.setText("El vehículo NO está disponible para el período seleccionado\n");
            }
            
            showRentalPeriods(licensePlate);
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }
    
    /**
     * Finaliza un arriendo existente.
     */
    private void finishRental() {
        try {
            String licensePlate = licensePlateField.getText().trim();
            if (!ValidationUtils.isValidLicensePlate(licensePlate)) {
                showError("Patente inválida");
                return;
            }
            
            LocalDate startDate = parseDate(startDateField.getText().trim());
            
            if (startDate == null) {
                showError("Formato de fecha inválido. Use dd/mm/aaaa");
                return;
            }
            
            boolean success = fleetManager.finishRental(licensePlate, startDate);
            if (success) {
                resultArea.setText("Arriendo finalizado correctamente\n");
                showRentalPeriods(licensePlate);
            } else {
                showError("No se pudo finalizar el arriendo. Verifique la fecha de inicio");
            }
        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }
    
    /**
     * Muestra los períodos de arriendo de un vehículo.
     * 
     * @param licensePlate Patente del vehículo
     */
    private void showRentalPeriods(String licensePlate) {
        Map<LocalDate, LocalDate> periods = fleetManager.getVehicleRentalPeriods(licensePlate);
        
        if (periods.isEmpty()) {
            resultArea.append("\nEl vehículo no tiene períodos de arriendo registrados");
            return;
        }
        
        resultArea.append("\nPeríodos de arriendo para " + licensePlate + ":\n");
        for (Map.Entry<LocalDate, LocalDate> period : periods.entrySet()) {
            resultArea.append("Desde: " + period.getKey().format(dateFormatter) + 
                             " Hasta: " + period.getValue().format(dateFormatter) + "\n");
        }
    }
    
    /**
     * Convierte un string a fecha.
     * 
     * @param dateStr String con formato dd/mm/aaaa
     * @return Objeto LocalDate o null si el formato es inválido
     */
    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Muestra un mensaje de error.
     * 
     * @param message Mensaje de error
     */
    private void showError(String message) {
        resultArea.setText("ERROR: " + message);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}