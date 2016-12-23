package hibernate.dao;

// default package

import hibernate.model.Bonos;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for Bonos
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see .Bonos
 * @author MyEclipse Persistence Tools
 */
public class BonosDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(BonosDAO.class);
	// property constants

	public void save(Bonos transientInstance) {
		log.debug("saving Bonos instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Bonos persistentInstance) {
		log.debug("deleting Bonos instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Bonos findById(java.lang.Integer id) {
		log.debug("getting Bonos instance with id: " + id);
		try {
			Bonos instance = (Bonos) getSession().get("hibernate.model.Bonos", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public Bonos findByRolId(Integer rolId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM bonos "
                            + "where rol_cliente_id = :rol_cliente_id")
                    .addEntity(Bonos.class)
                    .setParameter("rol_cliente_id", rolId);
            Object result = query.uniqueResult();
            return (Bonos) result;
        }
        
        public List<Bonos> findAllByClienteIdAndEmpresaId(Integer clienteId, Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM bonos "
                            + "JOIN usuario "
                            + "ON usuario.id = bonos.usuario_id "
                            + "JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "where cliente_id = :cliente_id "
                            + "and empresa_id = :empresa_id "
                            + "and pagado = false")
                    .addEntity(Bonos.class)
                    .setParameter("cliente_id", clienteId)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<Bonos>) result;
        }
        
        public List<Bonos> findAllByClienteNullAndEmpresaId(Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM bonos "
                            + "JOIN usuario "
                            + "ON usuario.id = bonos.usuario_id "
                            + "JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "where cliente_id IS NULL "
                            + "and empresa_id = :empresa_id "
                            + "and pagado = false")
                    .addEntity(Bonos.class)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<Bonos>) result;
        }
        
        public List<Bonos> findAllPagadoByClienteIdAndEmpleadoId(Integer clienteId, Integer empleadoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM bonos "
                            + "where usuario_id = :usuario_id "
                            + "and cliente_id = :cliente_id "
                            + "and pagado = true")
                    .addEntity(Bonos.class)
                    .setParameter("cliente_id", clienteId)
                    .setParameter("usuario_id", empleadoId);
            Object result = query.list();
            return (List<Bonos>) result;
        }
        
        public Bonos findByClienteIdAndEmpleadoId(Integer clienteId, Integer empleadoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM bonos "
                            + "where usuario_id = :usuario_id "
                            + "and cliente_id = :cliente_id "
                            + "and pagado = false")
                    .addEntity(Bonos.class)
                    .setParameter("cliente_id", clienteId)
                    .setParameter("usuario_id", empleadoId);
            Object result = query.uniqueResult();
            return (Bonos) result;
        }
        
        public List<Bonos> findAllPagadoByClienteNullAndEmpleadoId(Integer empleadoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM bonos "
                            + "where usuario_id = :usuario_id "
                            + "and cliente_id is NULL "
                            + "and pagado = true")
                    .addEntity(Bonos.class)
                    .setParameter("usuario_id", empleadoId);
            Object result = query.list();
            return (List<Bonos>) result;
        }
        
        public Bonos findByClienteNullAndEmpleadoId(Integer empleadoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM bonos "
                            + "where usuario_id = :usuario_id "
                            + "and cliente_id is NULL "
                            + "and pagado = false")
                    .addEntity(Bonos.class)
                    .setParameter("usuario_id", empleadoId);
            Object result = query.uniqueResult();
            return (Bonos) result;
        }

	public List findByExample(Bonos instance) {
		log.debug("finding Bonos instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.Bonos")
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
		log.debug("finding Bonos instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Bonos as model where model."
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
		log.debug("finding all Bonos instances");
		try {
			String queryString = "from Bonos";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Bonos merge(Bonos detachedInstance) {
		log.debug("merging Bonos instance");
		try {
			Bonos result = (Bonos) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Bonos instance) {
		log.debug("attaching dirty Bonos instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Bonos instance) {
		log.debug("attaching clean Bonos instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}