/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.tableModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Yornel
 */
public class EmpleadoTable {
    
    public SimpleIntegerProperty id = new SimpleIntegerProperty();
    public SimpleStringProperty nombre = new SimpleStringProperty();
    public SimpleStringProperty apellido = new SimpleStringProperty();
    public SimpleStringProperty cedula = new SimpleStringProperty();
    public SimpleStringProperty telefono = new SimpleStringProperty();
    public SimpleStringProperty departamento = new SimpleStringProperty();
    public SimpleStringProperty cargo = new SimpleStringProperty();

    public Integer getId() {
        return id.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public String getApellido() {
        return apellido.get();
    }

    public String getCedula() {
        return cedula.get();
    }

    public String getTelefono() {
        return telefono.get();
    }

    public String getDepartamento() {
        return departamento.get();
    }

    public String getCargo() {
        return cargo.get();
    }
   
}
