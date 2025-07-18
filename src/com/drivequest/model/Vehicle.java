/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.drivequest.model;

/**
 * Clase abstracta que representa un vehiculo generico en el sistema.
 * <p>
 * Define los atributos y comportamientos comunes a todos los tipos de vehiculos,
 * sirviendo como base para las clases especializadas de vehiculos.
 * </p>
 * 
 * @author DriveQuest Team
 * @version 1.0
 * @since 2023-07-15
 */
public abstract class Vehicle implements TaxCalculator {
    
    /** Patente del vehiculo, identificador unico */
    private String licensePlate;
    
    /** Modelo del vehiculo */
    private String model;
    
    /** Año del vehiculo */
    private int year;
    
    /** Precio diario de alquiler */
    private double dailyPrice;
    
    /** Duracion del alquiler en dias */
    private int rentalDays;
    
    /** Estado de disponibilidad del vehículo */
    private boolean available;
    
    /**
    * Constructor vacio para la clase Vehicle.
    */
    public Vehicle() {
        this.licensePlate = "";
        this.model = "";
        this.year = 0;
        this.dailyPrice = 0.0;
        this.rentalDays = 0;
        this.available = true;
    }
    
    /**
    * Constructor con parametros para la clase Vehicle.
    * 
    * @param licensePlate Patente del vehiculo
    * @param model Modelo del vehiculo
    * @param year Año del vehiculo
    * @param dailyPrice Precio diario de alquiler
    * @param rentalDays Duracion del alquiler en dias
    */
    public Vehicle(String licensePlate, String model, int year, double dailyPrice, int rentalDays) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.year = year;
        this.dailyPrice = dailyPrice;
        this.rentalDays = rentalDays;
        this.available = true;
    }
    
    /**
    * Verifica si el vehículo está disponible para alquiler.
    * 
    * @return true si el vehículo está disponible, false en caso contrario
    */
    public boolean isAvailable() {
        return available;
    }
    
    /**
    * Establece la disponibilidad del vehículo.
    * 
    * @param available true si el vehículo está disponible, false en caso contrario
    */
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    /**
    * Muestra los datos especificos del vehiculo.
    * Este metodo debe ser implementado por las clases hijas.
    * 
    * @return String con los datos formateados del vehiculo
    */
    public abstract String showData();
    
    /**
    * Calcula el subtotal del alquiler sin impuestos ni descuentos.
    * 
    * @return Subtotal del alquiler
    */
    @Override
    public double calculateSubtotal() {
        return dailyPrice * rentalDays;
    }
    
    /**
    * Calcula el monto de IVA aplicable.
    * 
    * @return Valor del IVA
    */
    @Override
    public double calculateVAT() {
        return calculateSubtotal() * VAT;
    }
    
    /**
    * Calcula el descuento aplicable según el tipo de vehículo.
    * Este método debe ser implementado por las clases hijas.
    * 
    * @return Valor del descuento
    */
    @Override
    public abstract double calculateDiscount();
    
    /**
    * Calcula el monto total a pagar por el alquiler.
    * 
    * @return Valor total a pagar incluyendo impuestos y descuentos
    */
    @Override
    public double calculateTotalAmount() {
        return calculateSubtotal() + calculateVAT() - calculateDiscount();
    }
    
    /**
    * Calcula y genera el detalle de la boleta para un alquiler.
    * 
    * @return String con el detalle de la boleta incluyendo subtotal, IVA, descuento y total
    */
    @Override
    public String calculateInvoice() {
        double subtotal = calculateSubtotal();
        double vat = calculateVAT();
        double discount = calculateDiscount();
        double total = calculateTotalAmount();
        
        StringBuilder invoice = new StringBuilder();
        invoice.append("DETALLE DE BOLETA\n");
        invoice.append("----\n");
        invoice.append(String.format("Vehículo: %s - %s (%d)\n", getLicensePlate(), getModel(), getYear()));
        invoice.append(String.format("Días de alquiler: %d\n", getRentalDays()));
        invoice.append(String.format("Precio diario: $%.2f\n\n", getDailyPrice()));
        invoice.append(String.format("Subtotal: $%.2f\n", subtotal));
        invoice.append(String.format("IVA (%.0f%%): $%.2f\n", VAT * 100, vat));
        invoice.append(String.format("Descuento: $%.2f\n", discount));
        invoice.append("----\n");
        invoice.append(String.format("TOTAL A PAGAR: $%.2f", total));
        
        return invoice.toString();
    }
    
    // Getters y Setters
    
    /**
    * Obtiene la patente del vehiculo.
    * 
    * @return Patente del vehiculo
    */
    public String getLicensePlate() {
        return licensePlate;
    }
    
    /**
    * Establece la patente del vehiculo.
    * 
    * @param licensePlate Nueva patente del vehiculo
    */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    
    /**
    * Obtiene el modelo del vehiculo.
    * 
    * @return Modelo del vehiculo
    */
    public String getModel() {
        return model;
    }
    
    /**
    * Establece el modelo del vehiculo.
    * 
    * @param model Nuevo modelo del vehiculo
    */
    public void setModel(String model) {
        this.model = model;
    }
    
    /**
    * Obtiene el año del vehiculo.
    * 
    * @return Año del vehiculo
    */
    public int getYear() {
        return year;
    }
    
    /**
    * Establece el año del vehiculo.
    * 
    * @param year Nuevo año del vehiculo
    */
    public void setYear(int year) {
        if (year < 0) {
            throw new IllegalArgumentException("El año no puede ser negativo");
        }
        this.year = year;
    }
    
    /**
    * Obtiene el precio diario de alquiler.
    * 
    * @return Precio diario de alquiler
    */
    public double getDailyPrice() {
        return dailyPrice;
    }
    
    /**
    * Establece el precio diario de alquiler.
    * 
    * @param dailyPrice Nuevo precio diario de alquiler
    */
    public void setDailyPrice(double dailyPrice) {
        if (dailyPrice < 0) {
            throw new IllegalArgumentException("El precio diario no puede ser negativo");
        }
        this.dailyPrice = dailyPrice;
    }
    
    /**
    * Obtiene la duracion del alquiler en dias.
    * 
    * @return Duracion del alquiler en dias
    */
    public int getRentalDays() {
        return rentalDays;
    }
    
    /**
    * Establece la duracion del alquiler en dias.
    * 
    * @param rentalDays Nueva duracion del alquiler en dias
    */
    public void setRentalDays(int rentalDays) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("La duración del alquiler debe ser al menos 1 día");
        }
        this.rentalDays = rentalDays;
    }
    
    /**
    * Determina si este alquiler es considerado de larga duracion (7 dias o mas).
    * 
    * @return true si el alquiler es de larga duracion, false en caso contrario
    */
    public boolean isLongTermRental() {
        return rentalDays >= 7;
    }
    
    /**
    * Sobrescribe el metodo toString para obtener una representacion en texto del vehiculo.
    * 
    * @return Representacion en texto del vehiculo
    */
    @Override
    public String toString() {
        return "Patente: " + licensePlate + ", Modelo: " + model + ", Año: " + year + 
        ", Precio diario: $" + dailyPrice + ", Dias de alquiler: " + rentalDays;
    }
    
    /**
    * Sobrescribe el método equals para comparar vehículos por su patente.
    * 
    * @param obj Objeto a comparar
    * @return true si los vehículos tienen la misma patente, false en caso contrario
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass().getSuperclass() != obj.getClass().getSuperclass()) return false;
        
        Vehicle vehicle = (Vehicle) obj;
        return licensePlate != null && licensePlate.equals(vehicle.licensePlate);
    }
}