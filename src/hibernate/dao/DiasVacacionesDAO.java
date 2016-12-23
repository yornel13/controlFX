package hibernate.dao;

// default package

import hibernate.model.DiasVacaciones;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Actuariales entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .Actuariales
 * @author MyEclipse Persistence Tools
 */
public class DiasVacacionesDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(DiasVacacionesDAO.class);
	// property constants

	public void save(DiasVacaciones transientInstance) {
		log.debug("saving DiasVacaciones instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(DiasVacaciones persistentInstance) {
		log.debug("deleting DiasVacaciones instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DiasVacaciones findById(java.lang.Integer id) {
		log.debug("getting DiasVacaciones instance with id: " + id);
		try {
			DiasVacaciones instance = (DiasVacaciones) getSession().get(
					"hibernate.model.DiasVacaciones", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(DiasVacaciones instance) {
		log.debug("finding DiasVacaciones instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.DiasVacaciones")
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
		log.debug("finding DiasVacaciones instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from DiasVacaciones as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all DiasVacaciones instances");
		try {
			String queryString = "from DiasVacaciones";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
        
        public DiasVacaciones findByEmpleadoId(Integer empleadoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM dias_vacaciones where usuario_id = :empleado_id and activo = true")
                    .addEntity(DiasVacaciones.class)
                    .setParameter("empleado_id", empleadoId);
            Object result = query.uniqueResult();
            return (DiasVacaciones) result;
        }
        
        public List<DiasVacaciones> findAllByEmpresaId(Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM dias_vacaciones "
                            + "JOIN usuario "
                            + "ON usuario.id = dias_vacaciones.usuario_id "
                            + "JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "where empresa_id = :empresa_id")
                    .addEntity(DiasVacaciones.class)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<DiasVacaciones>) result;
        }

	public DiasVacaciones merge(DiasVacaciones detachedInstance) {
		log.debug("merging DiasVacaciones instance");
		try {
			DiasVacaciones result = (DiasVacaciones) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(DiasVacaciones instance) {
		log.debug("attaching dirty DiasVacaciones instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DiasVacaciones instance) {
		log.debug("attaching clean DiasVacaciones instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}