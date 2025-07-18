/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.model;

/**
 * Interfaz que define constantes fiscales y metodos para calculos de boletas
 * en el sistema de alquiler de vehiculos.
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public interface TaxCalculator {
    
    /** Porcentaje de IVA aplicado a todos los alquileres */
    double VAT = 0.19;
    
    /** Porcentaje de descuento para vehiculos de carga */
    double CARGO_DISCOUNT = 0.07;
    
    /** Porcentaje de descuento para vehiculos de pasajeros */
    double PASSENGER_DISCOUNT = 0.12;
    
    /**
     * Calcula el subtotal antes de impuestos y descuentos.
     * 
     * @return Valor del subtotal
     */
    double calculateSubtotal();
    
    /**
     * Calcula el monto de IVA aplicable.
     * 
     * @return Valor del IVA
     */
    double calculateVAT();
    
    /**
     * Calcula el descuento aplicable según el tipo de vehículo.
     * 
     * @return Valor del descuento
     */
    double calculateDiscount();
    
    /**
     * Calcula el monto total a pagar por el alquiler.
     * 
     * @return Valor total a pagar incluyendo impuestos y descuentos
     */
    double calculateTotalAmount();
    
    /**
     * Calcula y genera el detalle de la boleta para un alquiler.
     * 
     * @return String con el detalle de la boleta incluyendo subtotal, IVA, descuento y total
     */
    String calculateInvoice();
}