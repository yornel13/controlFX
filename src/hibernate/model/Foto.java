/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hibernate.model;

/**
 *
 * @author Yornel
 */
public class Foto implements java.io.Serializable  {
    
    private Integer id;
    private byte[] foto;
    private Usuario usuario;

    public Foto() {
    }
    
    public Foto(Integer id, byte[] foto, Usuario usuario) {
        this.id = id;
        this.foto = foto;
        this.usuario = usuario;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
}
