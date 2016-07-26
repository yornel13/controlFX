/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hibernate.dao;

import hibernate.model.Foto;
import java.util.List;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Yornel
 */
public class FotoDAO extends BaseHibernateDAO {
    
    private static final Logger log = LoggerFactory
			.getLogger(FotoDAO.class);
    
    public void save(Foto transientInstance) {
        log.debug("saving Foto instance");
        try {
                getSession().save(transientInstance);
                log.debug("save successful");
        } catch (RuntimeException re) {
                log.error("save failed", re);
                throw re;
        }
    }

    public void delete(Foto persistentInstance) {
        log.debug("deleting Foto instance");
        try {
                getSession().delete(persistentInstance);
                log.debug("delete successful");
        } catch (RuntimeException re) {
                log.error("delete failed", re);
                throw re;
        }
    }

    public Foto findById(java.lang.Integer id) {
        log.debug("getting Foto instance with id: " + id);
        try {
                Foto instance = (Foto) getSession().get(
                                "hibernate.model.Foto", id);
                return instance;
        } catch (RuntimeException re) {
                log.error("get failed", re);
                throw re;
        }
    }
    
    public Foto findByEmpleadoId(Integer UsuarioId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM foto "
                            + "WHERE usuario_id = :usuario_id")
                    .addEntity(Foto.class)
                    .setParameter("usuario_id", UsuarioId);
            Object result = query.uniqueResult();
            return (Foto) result;
	}

    public List findAll() {
        log.debug("finding all Foto instances");
        try {
                String queryString = "from Foto";
                Query queryObject = getSession().createQuery(queryString);
                return queryObject.list();
        } catch (RuntimeException re) {
                log.error("find all failed", re);
                throw re;
        }
    }
}
