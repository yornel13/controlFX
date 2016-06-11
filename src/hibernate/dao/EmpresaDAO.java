package hibernate.dao;

// default package

import hibernate.model.Empresa;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Empresa entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Empresa
 * @author MyEclipse Persistence Tools
 */
public class EmpresaDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(EmpresaDAO.class);
	// property constants
	public static final String NOMBRE = "nombre";
	public static final String SIGLAS = "siglas";
	public static final String NUMERACION = "numeracion";
	public static final String TELEFONO1 = "telefono1";
	public static final String TELEFONO2 = "telefono2";
	public static final String FAX = "fax";
	public static final String EMAIL = "email";
	public static final String DIRECCION = "direccion";
	public static final String WEB = "web";
	public static final String LOGO = "logo";
	public static final String OBSERVACION = "observacion";
	public static final String TIPO = "tipo";
	public static final String DIA_CORTE_PAGO = "diaCortePago";
	public static final String ACTIVO = "activo";

	public void save(Empresa transientInstance) {
		log.debug("saving Empresa instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Empresa persistentInstance) {
		log.debug("deleting Empresa instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Empresa findById(java.lang.Integer id) {
		log.debug("getting Empresa instance with id: " + id);
		try {
			Empresa instance = (Empresa) getSession().get("hibernate.model.Empresa", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public void changeId(Integer oldId, Integer newId) {
            Query query = getSession().
                    createSQLQuery("update empresa set id = :new where id = :old")
                        .setParameter("old", oldId)
                        .setParameter("new", newId);
            query.executeUpdate();
            getSession().beginTransaction().commit();
	}

	public List findByExample(Empresa instance) {
		log.debug("finding Empresa instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.Empresa")
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
		log.debug("finding Empresa instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Empresa as model where model."
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

	public List findBySiglas(Object siglas) {
		return findByProperty(SIGLAS, siglas);
	}

	public List findByNumeracion(Object numeracion) {
		return findByProperty(NUMERACION, numeracion);
	}

	public List findByTelefono1(Object telefono1) {
		return findByProperty(TELEFONO1, telefono1);
	}

	public List findByTelefono2(Object telefono2) {
		return findByProperty(TELEFONO2, telefono2);
	}

	public List findByFax(Object fax) {
		return findByProperty(FAX, fax);
	}

	public List findByEmail(Object email) {
		return findByProperty(EMAIL, email);
	}

	public List findByDireccion(Object direccion) {
		return findByProperty(DIRECCION, direccion);
	}

	public List findByWeb(Object web) {
		return findByProperty(WEB, web);
	}

	public List findByLogo(Object logo) {
		return findByProperty(LOGO, logo);
	}

	public List findByObservacion(Object observacion) {
		return findByProperty(OBSERVACION, observacion);
	}

	public List findByTipo(Object tipo) {
		return findByProperty(TIPO, tipo);
	}

	public List findByDiaCortePago(Object diaCortePago) {
		return findByProperty(DIA_CORTE_PAGO, diaCortePago);
	}

	public List findByActivo(Object activo) {
		return findByProperty(ACTIVO, activo);
	}

	public List findAll() {
		log.debug("finding all Empresa instances");
		try {
			String queryString = "from Empresa";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Empresa merge(Empresa detachedInstance) {
		log.debug("merging Empresa instance");
		try {
			Empresa result = (Empresa) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Empresa instance) {
		log.debug("attaching dirty Empresa instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Empresa instance) {
		log.debug("attaching clean Empresa instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}