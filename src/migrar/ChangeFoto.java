/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package migrar;

import hibernate.HibernateSessionFactory;
import hibernate.dao.FotoDAO;
import hibernate.dao.UsuarioDAO;
import hibernate.model.Foto;
import hibernate.model.Usuario;
import java.util.ArrayList;

/**
 *
 * @author Yornel
 */
public class ChangeFoto {
    
    public static void main(String[]args) {
        
        UsuarioDAO usedao = new UsuarioDAO();
        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) usedao.findAll();
        
        for (Usuario user: usuarios) {
            Foto foto = new Foto();
            foto.setFoto(user.getFoto());
            foto.setUsuario(user);
            new FotoDAO().save(foto);
            
            user.setFoto(null);
            
            System.out.println("Foto " + foto.getId());
        }
        
        HibernateSessionFactory.getSession().flush();
        
        System.out.println("Guardado completado");
    }
}
