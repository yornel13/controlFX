/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control.util;

import hibernate.model.Roles;
import hibernate.model.Usuario;
import java.util.ArrayList;

/**
 *
 * @author Yornel
 */
public class Permisos {
    
    private Usuario usuario;
    private ArrayList<Roles> roles;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ArrayList<Roles> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Roles> roles) {
        this.roles = roles;
    }
    
    public Boolean getPermiso(String permisoRequerido, String nivel) {
        for (Roles rol: roles) {
            if (rol.getNombre().equalsIgnoreCase(TOTAL)) {
                return true;
            } else if (rol.getNombre().equalsIgnoreCase(permisoRequerido)) {
                if (getNivel(rol.getPermiso()) >= getNivel(nivel)) {
                    System.out.println(rol.getNombre());
                    return true;
                }
            }
        }
        return false;
    }
    
    public static final String A_EMPLEADOS = "empleados";
    public static final String A_EMPRESAS = "empresas";
    public static final String A_CLIENTES = "clientes";
    public static final String A_HORAS_EMPLEADO = "horas_empleado";
    public static final String A_ROL_DE_PAGO = "rol_de_pago";
    public static final String A_ROL_DE_USUARIO = "rol_de_usuario";
    public static final String TOTAL = "total";
    
    public static class Nivel {
        public static final String VER = "ver";
        public static final String CREAR = "crear";
        public static final String EDITAR = "editar";
        public static final String ELIMINAR = "eliminar";
    }
    
    public static Integer getNivel(String nivel) {
        
        switch(nivel) {
            case Nivel.VER:
                return 1;
            case Nivel.CREAR:
                return 2;
            case Nivel.EDITAR:
                return 3;
            case Nivel.ELIMINAR:
                return 4;
            default:
                return 0;         
        }
    }
}
