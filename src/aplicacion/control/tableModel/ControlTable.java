/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.tableModel;

import hibernate.model.Usuario;

/**
 *
 * @author Yornel
 */
public class ControlTable {
    
    private Integer id;
    private Usuario usuario;
    private String cliente;
    private String fecha;
    private Double horasSuplementarias;
    private Double horasExtras;
    private String descanso;
    private String dia;

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDescanso() {
        return descanso;
    }

    public void setDescanso(String descanso) {
        this.descanso = descanso;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuarios() {
        return usuario;
    }

    public void setUsuarios(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Double getHorasSuplementarias() {
        return horasSuplementarias;
    }

    public void setHorasSuplementarias(Double horasSuplementarias) {
        this.horasSuplementarias = horasSuplementarias;
    }

    public Double getHorasExtras() {
        return horasExtras;
    }

    public void setHorasExtras(Double horasExtras) {
        this.horasExtras = horasExtras;
    }
    
    
}
