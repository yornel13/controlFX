/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.tableModel;

import hibernate.model.PagoMesItem;
import hibernate.model.Usuario;

/**
 *
 * @author Yornel
 */
public class DescuentoTable {
    
    private String nombre;
    private String apellido;
    private String nombres;
    private String cedula;
    private String telefono;
    private String departamento;
    private String cargo;
    private Double valor;
    private String tipo;
    private PagoMesItem pagoMesItem;
    private Usuario empleado;
    
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public PagoMesItem getPagoMesItem() {
        return pagoMesItem;
    }

    public void setPagoMesItem(PagoMesItem pagoMesItem) {
        this.pagoMesItem = pagoMesItem;
    }

    public Usuario getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Usuario empleado) {
        this.empleado = empleado;
    }
    
    
}
