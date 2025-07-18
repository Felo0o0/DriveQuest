/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.drivequest.model.CargoVehicle;
import com.drivequest.model.PassengerVehicle;
import com.drivequest.model.Vehicle;
import com.drivequest.service.FleetManager;
import com.drivequest.util.ThreadUtils;

/**
 * StatisticsPanel - Panel que muestra estadísticas sobre la flota de vehículos.
 * 
 * Este panel proporciona información estadística sobre la flota de vehículos,
 * incluyendo conteos, promedios y otros datos relevantes para el análisis.
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class StatisticsPanel extends JPanel {

    /**
     * Gestor de la flota de vehículos.
     */
    private FleetManager fleetManager;
    
    /**
     * Etiqueta para el total de vehículos.
     */
    private JLabel totalVehiclesLabel;
    
    /**
     * Etiqueta para los vehículos disponibles.
     */
    private JLabel availableVehiclesLabel;
    
    /**
     * Etiqueta para los vehículos de pasajeros.
     */
    private JLabel passengerVehiclesLabel;
    
    /**
     * Etiqueta para los vehículos de carga.
     */
    private JLabel cargoVehiclesLabel;
    
    /**
     * Etiqueta para el año promedio de la flota.
     */
    private JLabel averageYearLabel;
    
    /**
     * Etiqueta para el modelo más común.
     */
    private JLabel mostCommonModelLabel;
    
    /**
     * Constructor del panel de estadísticas.
     * 
     * @param fleetManager gestor de flota para acceder a los datos
     */
    public StatisticsPanel(FleetManager fleetManager) {
        // Validar parámetros
        if (fleetManager == null) {
            throw new IllegalArgumentException("El gestor de flota no puede ser nulo");
        }
        
        this.fleetManager = fleetManager;
        
        // Inicializar componentes
        initComponents();
    }
    
    /**
     * Inicializa los componentes del panel.
     * 
     * Crea y configura todos los componentes visuales del panel,
     * incluyendo etiquetas y botones.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Título del panel
        JLabel titleLabel = new JLabel("Estadisticas de la Flota", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Panel para las estadísticas
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(6, 1, 10, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Crear las etiquetas de estadísticas
        totalVehiclesLabel = createStatsLabel("Total de vehiculos: 0");
        availableVehiclesLabel = createStatsLabel("Vehiculos disponibles: 0");
        passengerVehiclesLabel = createStatsLabel("Vehiculos de pasajeros: 0");
        cargoVehiclesLabel = createStatsLabel("Vehiculos de carga: 0");
        averageYearLabel = createStatsLabel("Año promedio de la flota: N/A");
        mostCommonModelLabel = createStatsLabel("Modelo mas comun: N/A");
        
        // Agregar etiquetas al panel
        statsPanel.add(totalVehiclesLabel);
        statsPanel.add(availableVehiclesLabel);
        statsPanel.add(passengerVehiclesLabel);
        statsPanel.add(cargoVehiclesLabel);
        statsPanel.add(averageYearLabel);
        statsPanel.add(mostCommonModelLabel);
        
        // Panel central con un borde para mejor apariencia
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        
        // Agregar panel de estadísticas al centro
        add(centerPanel, BorderLayout.CENTER);
        
        // Botón para actualizar estadísticas
        JButton refreshButton = new JButton("Actualizar Estadisticas");
        refreshButton.addActionListener(e -> refreshStatistics());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Cargar estadísticas iniciales
        refreshStatistics();
    }
    
    /**
     * Crea una etiqueta con formato para estadísticas.
     * 
     * @param text el texto inicial de la etiqueta
     * @return la etiqueta configurada
     */
    private JLabel createStatsLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }
    
    /**
     * Actualiza las estadísticas mostradas en el panel.
     * 
     * Recalcula todas las estadísticas basándose en los
     * datos actuales de la flota de vehículos.
     */
    public void refreshStatistics() {
        ThreadUtils.runOnEDT(() -> {
            List<Vehicle> vehicles = fleetManager.getAllVehicles();
            
            // Total de vehículos
            int totalVehicles = vehicles.size();
            totalVehiclesLabel.setText("Total de vehiculos: " + totalVehicles);
            
            // Vehículos disponibles
            long availableVehicles = vehicles.stream()
                    .filter(Vehicle::isAvailable)
                    .count();
            availableVehiclesLabel.setText("Vehiculos disponibles: " + availableVehicles);
            
            // Vehículos por tipo
            long passengerVehicles = vehicles.stream()
                    .filter(v -> v instanceof PassengerVehicle)
                    .count();
            passengerVehiclesLabel.setText("Vehiculos de pasajeros: " + passengerVehicles);
            
            long cargoVehicles = vehicles.stream()
                    .filter(v -> v instanceof CargoVehicle)
                    .count();
            cargoVehiclesLabel.setText("Vehiculos de carga: " + cargoVehicles);
            
            // Año promedio
            if (totalVehicles > 0) {
                double averageYear = vehicles.stream()
                        .mapToInt(Vehicle::getYear)
                        .average()
                        .orElse(0);
                averageYearLabel.setText("Año promedio de la flota: " + String.format("%.1f", averageYear));
                
                // Modelo más común
                Map<String, Long> modelCounts = vehicles.stream()
                        .collect(Collectors.groupingBy(Vehicle::getModel, Collectors.counting()));
                
                String mostCommonModel = modelCounts.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("N/A");
                
                long mostCommonModelCount = modelCounts.getOrDefault(mostCommonModel, 0L);
                
                if (mostCommonModelCount > 0) {
                    mostCommonModelLabel.setText("Modelo mas comun: " + mostCommonModel + 
                            " (" + mostCommonModelCount + " unidades)");
                } else {
                    mostCommonModelLabel.setText("Modelo mas comun: N/A");
                }
            } else {
                averageYearLabel.setText("Año promedio de la flota: N/A");
                mostCommonModelLabel.setText("Modelo mas comun: N/A");
            }
        });
    }
}