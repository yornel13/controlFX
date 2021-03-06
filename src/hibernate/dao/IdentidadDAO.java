package hibernate.dao;

// default package

import hibernate.model.Identidad;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Identidad entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Identidad
 * @author MyEclipse Persistence Tools
 */
public class IdentidadDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(IdentidadDAO.class);
	// property constants
	public static final String NOMBRE_USUARIO = "nombreUsuario";
	public static final String CONTRASENA = "contrasena";
	public static final String ACTIVO = "activo";

	public void save(Identidad transientInstance) {
		log.debug("saving Identidad instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Identidad persistentInstance) {
		log.debug("deleting Identidad instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Identidad findById(java.lang.Integer id) {
		log.debug("getting Identidad instance with id: " + id);
		try {
			Identidad instance = (Identidad) getSession().get("hibernate.model.Identidad", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public List<Identidad> findAllActivo() {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM identidad where activo = true")
                    .addEntity(Identidad.class);
            Object result = query.list();
            return (List<Identidad>) result;
	}

	public List findByExample(Identidad instance) {
		log.debug("finding Identidad instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.Identidad")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Identidad instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Identidad as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByNombreUsuario(Object nombreUsuario) {
		return findByProperty(NOMBRE_USUARIO, nombreUsuario);
	}
        
        public Identidad findByNombreUsuarioActivo(String nombreUsuario) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM identidad where nombre_usuario = :nombre_usuario "
                            + "and activo = true")
                    .addEntity(Identidad.class)
                    .setParameter("nombre_usuario", nombreUsuario);
            Object result = query.uniqueResult();
            return (Identidad) result;
	}
        
        public Identidad findByUsuarioIdActivo(Integer usuarioId) {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM identidad where usuario_id = :usuario_id "
                            + "and activo = true")
                    .addEntity(Identidad.class)
                    .setParameter("usuario_id", usuarioId);
            Object result = query.uniqueResult();
            return (Identidad) result;
	}

	public List findByContrasena(Object contrasena) {
		return findByProperty(CONTRASENA, contrasena);
	}

	public List findByActivo(Object activo) {
		return findByProperty(ACTIVO, activo);
	}

	public List findAll() {
		log.debug("finding all Identidad instances");
		try {
			String queryString = "from Identidad";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Identidad merge(Identidad detachedInstance) {
		log.debug("merging Identidad instance");
		try {
			Identidad result = (Identidad) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Identidad instance) {
		log.debug("attaching dirty Identidad instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Identidad instance) {
		log.debug("attaching clean Identidad instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}