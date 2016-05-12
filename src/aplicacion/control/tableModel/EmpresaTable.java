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
public class EmpresaTable {
    
    public SimpleIntegerProperty id = new SimpleIntegerProperty();
    public SimpleStringProperty siglas = new SimpleStringProperty();
    public SimpleStringProperty nombre = new SimpleStringProperty();
    public SimpleStringProperty numeracion = new SimpleStringProperty();
    public SimpleIntegerProperty diaCortePago = new SimpleIntegerProperty();
    public SimpleStringProperty creacion = new SimpleStringProperty();
    public SimpleStringProperty tipo = new SimpleStringProperty();
    
    public Integer getId() {
        return id.get();
    }

    public String getNombre() {
        return nombre.get();
    }
    
    public String getTipo() {
        return tipo.get();
    }

    public String getSiglas() {
        return siglas.get();
    }

    public String getNumeracion() {
        return numeracion.get();
    }

    public Integer getDiaCortePago() {
        return diaCortePago.get();
    }

    public String getCreacion() {
        return creacion.get();
    }
}
