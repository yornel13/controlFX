package hibernate.dao;

// default package

import hibernate.model.Seguro;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Seguro entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Seguro
 * @author MyEclipse Persistence Tools
 */
public class SeguroDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(SeguroDAO.class);
	// property constants
	public static final String NOMBRE = "nombre";
	public static final String VALOR = "valor";
	public static final String ACTIVO = "activo";

	public void save(Seguro transientInstance) {
		log.debug("saving Seguro instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Seguro persistentInstance) {
		log.debug("deleting Seguro instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Seguro findById(java.lang.Integer id) {
		log.debug("getting Seguro instance with id: " + id);
		try {
			Seguro instance = (Seguro) getSession().get("hibernate.model.Seguro", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public Seguro findByEmpresaId(Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM seguro where empresa_id = :empresa_id and activo = true")
                    .addEntity(Seguro.class)
                    .setParameter("empresa_id", empresaId);
            Object result = query.uniqueResult();
            return (Seguro) result;
        }
        
        public Seguro findByClienteId(Integer clienteId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM seguro where cliente_id = :cliente_id and activo = true")
                    .addEntity(Seguro.class)
                    .setParameter("cliente_id", clienteId);
            Object result = query.uniqueResult();
            return (Seguro) result;
        }
        
        public Seguro findAdministrativo() {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM seguro where nombre = 'administrativo' and activo = true")
                    .addEntity(Seguro.class);
            Object result = query.uniqueResult();
            return (Seguro) result;
        }

	public List findByExample(Seguro instance) {
		log.debug("finding Seguro instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.Seguro")
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
		log.debug("finding Seguro instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Seguro as model where model."
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
		log.debug("finding all Seguro instances");
		try {
			String queryString = "from Seguro";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Seguro merge(Seguro detachedInstance) {
		log.debug("merging Seguro instance");
		try {
			Seguro result = (Seguro) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Seguro instance) {
		log.debug("attaching dirty Seguro instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Seguro instance) {
		log.debug("attaching clean Seguro instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}