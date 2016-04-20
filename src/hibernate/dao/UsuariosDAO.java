package hibernate.dao;

import hibernate.model.Usuarios;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Usuarios entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see hibernate.model.Usuarios
 * @author MyEclipse Persistence Tools
 */
public class UsuariosDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(UsuariosDAO.class);
	// property constants
	public static final String NOMBRE = "nombre";
	public static final String APELLIDO = "apellido";
	public static final String CEDULA = "cedula";
	public static final String EMAIL = "email";
	public static final String DIRECCION = "direccion";
	public static final String TELEFONO = "telefono";
	public static final String FOTO = "foto";
	public static final String ACTIVO = "activo";

	public void save(Usuarios transientInstance) {
		log.debug("saving Usuarios instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Usuarios persistentInstance) {
		log.debug("deleting Usuarios instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Usuarios findById(java.lang.Integer id) {
		log.debug("getting Usuarios instance with id: " + id);
		try {
			Usuarios instance = (Usuarios) getSession().get(
					"hibernate.model.Usuarios", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Usuarios instance) {
		log.debug("finding Usuarios instance by example");
		try {
			List results = getSession()
					.createCriteria("hibernate.model.Usuarios")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
        
        public Usuarios findByCedula(String cedula) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM usuarios where cedula = :cedula")
                    .addEntity(Usuarios.class)
                    .setParameter("cedula", cedula);
            Object result = query.uniqueResult();
            return (Usuarios) result;
        }
        
        public Usuarios findByAllCedulaAndEmpresaId(String cedula, Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM usuarios JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuarios.detalles_empleado_id "
                            + "WHERE empresa_id = :empresa_id and cedula = :cedula")
                    .addEntity(Usuarios.class)
                    .setParameter("empresa_id", empresaId)
                    .setParameter("cedula", cedula);
            Object result = query.uniqueResult();
            return (Usuarios) result;
        }
        
        public List<Usuarios> findByEmpresaId(Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM usuarios JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuarios.detalles_empleado_id WHERE empresa_id = :empresa_id")
                    .addEntity(Usuarios.class)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<Usuarios>) result;
        }

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Usuarios instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Usuarios as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByNombre(Object nombre) {
		return findByProperty(NOMBRE, nombre);
	}

	public List findByApellido(Object apellido) {
		return findByProperty(APELLIDO, apellido);
	}

	public List findByCedula(Object cedula) {
		return findByProperty(CEDULA, cedula);
	}

	public List findByEmail(Object email) {
		return findByProperty(EMAIL, email);
	}

	public List findByDireccion(Object direccion) {
		return findByProperty(DIRECCION, direccion);
	}

	public List findByTelefono(Object telefono) {
		return findByProperty(TELEFONO, telefono);
	}

	public List findByFoto(Object foto) {
		return findByProperty(FOTO, foto);
	}

	public List findByActivo(Object activo) {
		return findByProperty(ACTIVO, activo);
	}

	public List findAll() {
		log.debug("finding all Usuarios instances");
		try {
			String queryString = "from Usuarios";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Usuarios merge(Usuarios detachedInstance) {
		log.debug("merging Usuarios instance");
		try {
			Usuarios result = (Usuarios) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Usuarios instance) {
		log.debug("attaching dirty Usuarios instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Usuarios instance) {
		log.debug("attaching clean Usuarios instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}