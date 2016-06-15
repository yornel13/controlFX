package hibernate.dao;

// default package

import hibernate.model.Uniforme;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Uniforme entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Uniforme
 * @author MyEclipse Persistence Tools
 */
public class UniformeDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(UniformeDAO.class);
	// property constants
	public static final String NOMBRE = "nombre";
	public static final String VALOR = "valor";
	public static final String ACTIVO = "activo";

	public void save(Uniforme transientInstance) {
		log.debug("saving Uniforme instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Uniforme persistentInstance) {
		log.debug("deleting Uniforme instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Uniforme findById(java.lang.Integer id) {
		log.debug("getting Uniforme instance with id: " + id);
		try {
			Uniforme instance = (Uniforme) getSession().get("hibernate.model.Uniforme", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public Uniforme findByEmpresaId(Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM uniforme where empresa_id = :empresa_id and activo = true")
                    .addEntity(Uniforme.class)
                    .setParameter("empresa_id", empresaId);
            Object result = query.uniqueResult();
            return (Uniforme) result;
        }
        
        public Uniforme findByClienteId(Integer clienteId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM uniforme where cliente_id = :cliente_id and activo = true")
                    .addEntity(Uniforme.class)
                    .setParameter("cliente_id", clienteId);
            Object result = query.uniqueResult();
            return (Uniforme) result;
        }

	public List findByExample(Uniforme instance) {
		log.debug("finding Uniforme instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.Uniforme")
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
		log.debug("finding Uniforme instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Uniforme as model where model."
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

	public List findByValor(Object valor) {
		return findByProperty(VALOR, valor);
	}

	public List findByActivo(Object activo) {
		return findByProperty(ACTIVO, activo);
	}

	public List findAll() {
		log.debug("finding all Uniforme instances");
		try {
			String queryString = "from Uniforme";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Uniforme merge(Uniforme detachedInstance) {
		log.debug("merging Uniforme instance");
		try {
			Uniforme result = (Uniforme) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Uniforme instance) {
		log.debug("attaching dirty Uniforme instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Uniforme instance) {
		log.debug("attaching clean Uniforme instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}