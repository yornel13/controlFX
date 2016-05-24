/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.tableModel;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
    public SimpleStringProperty empresa = new SimpleStringProperty();
    public SimpleIntegerProperty dias = new SimpleIntegerProperty();
    public SimpleIntegerProperty horas = new SimpleIntegerProperty();
    public SimpleIntegerProperty suplementarias = new SimpleIntegerProperty();
    public SimpleIntegerProperty sobreTiempo = new SimpleIntegerProperty();
    public SimpleDoubleProperty actuarial1 = new SimpleDoubleProperty();
    public SimpleDoubleProperty actuarial2 = new SimpleDoubleProperty();
    public SimpleDoubleProperty quincenal = new SimpleDoubleProperty();
    public SimpleDoubleProperty totalMontoDeudas = new SimpleDoubleProperty();
    public SimpleIntegerProperty totalDeudas = new SimpleIntegerProperty();
    public SimpleBooleanProperty acumulaDecimos = new SimpleBooleanProperty();

    public Integer getDias() {
        return dias.get();
    }

    public Integer getHoras() {
        return horas.get();
    }

    public Integer getSuplementarias() {
        return suplementarias.get();
    }

    public Integer getSobreTiempo() {
        return sobreTiempo.get();
    }
    
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
    
    public String getEmpresa() {
        return empresa.get();
    }
    
    public Double getActuarial1() {
        return actuarial1.get();
    }
    
    public Double getActuarial2() {
        return actuarial2.get();
    }
    
    public Double getQuincenal() {
        return quincenal.get();
    }
    
    public Double getTotalMontoDeudas() {
        return totalMontoDeudas.get();
    }
    
    public Boolean getAcumulaDecimos() {
        return acumulaDecimos.get();
    }

    public Integer getTotalDeudas() {
        return totalDeudas.get();
    }
    
    
   
}
