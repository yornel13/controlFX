/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.control;

/**
 *
 * @author user 01
 */
public class Permisos {
    
    private Integer empresa;
    private Integer configutacion;

    public Permisos(Integer empresa, Integer configutacion) {
        this.empresa = empresa;
        this.configutacion = configutacion;
    }
    
    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public Integer getConfigutacion() {
        return configutacion;
    }

    public void setConfigutacion(Integer configutacion) {
        this.configutacion = configutacion;
    }
            
    
}
