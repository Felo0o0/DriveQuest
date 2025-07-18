/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.ui;

import com.drivequest.model.Vehicle;
import com.drivequest.service.FleetManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel para mostrar y generar boletas de alquiler.
 * <p>
 * Permite buscar vehiculos por patente y generar boletas
 * de alquiler con detalles de costos.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public class InvoicePanel extends JPanel {
    
    /** Gestor de flota para buscar vehiculos */
    private final FleetManager fleetManager;
    
    // Componentes de la interfaz
    private final JTextField txtLicensePlate;
    private final JTextArea txtInvoice;
    private final JButton btnSearch;
    private final JButton btnClear;
    
    /**
     * Constructor del panel de boletas.
     * 
     * @param fleetManager Gestor de flota a utilizar
     */
    public InvoicePanel(FleetManager fleetManager) {
        this.fleetManager = fleetManager;
        
        // Configuración del panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Generacion de Boletas"));
        
        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.add(new JLabel("Patente:"));
        txtLicensePlate = new JTextField(10);
        searchPanel.add(txtLicensePlate);
        
        btnSearch = new JButton("Buscar");
        btnClear = new JButton("Limpiar");
        
        searchPanel.add(btnSearch);
        searchPanel.add(btnClear);
        
        // Área de texto para la boleta
        txtInvoice = new JTextArea(15, 40);
        txtInvoice.setEditable(false);
        txtInvoice.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtInvoice);
        
        // Agregar componentes al panel
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Configurar eventos
        configurarEventos();
    }
    
    /**
     * Configura los eventos de los componentes del panel.
     */
    private void configurarEventos() {
        // Evento del botón buscar
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarVehiculoYGenerarBoleta();
            }
        });
        
        // Evento del botón limpiar
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtLicensePlate.setText("");
                txtInvoice.setText("");
                txtLicensePlate.requestFocus();
            }
        });
        
        // Evento de enter en el campo de texto
        txtLicensePlate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarVehiculoYGenerarBoleta();
            }
        });
    }
    
    /**
     * Busca un vehiculo por patente y genera su boleta.
     */
    private void buscarVehiculoYGenerarBoleta() {
        String licensePlate = txtLicensePlate.getText().trim();
        
        if (licensePlate.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese una patente para buscar",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Vehicle vehicle = fleetManager.findVehicleByLicensePlate(licensePlate);
            
            if (vehicle != null) {
                String invoice = vehicle.calculateInvoice();
                txtInvoice.setText(invoice);
            } else {
                txtInvoice.setText("");
                JOptionPane.showMessageDialog(this,
                        "No se encontro vehiculo con patente " + licensePlate,
                        "Vehiculo no encontrado", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            txtInvoice.setText("");
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}