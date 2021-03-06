package hibernate.dao;

import aplicacion.control.util.Fecha;
import aplicacion.control.util.Fechas;
import hibernate.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Usuario entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see .Usuario
 * @author MyEclipse Persistence Tools
 */
public class UsuarioDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(UsuarioDAO.class);
	// property constants
	public static final String NOMBRE = "nombre";
	public static final String APELLIDO = "apellido";
	public static final String CEDULA = "cedula";
	public static final String EMAIL = "email";
	public static final String DIRECCION = "direccion";
	public static final String TELEFONO = "telefono";
	public static final String FOTO = "foto";
	public static final String ACTIVO = "activo";
	public static final String SEXO = "sexo";

	public void save(Usuario transientInstance) {
		log.debug("saving Usuario instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Usuario persistentInstance) {
		log.debug("deleting Usuario instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Usuario findById(java.lang.Integer id) {
		log.debug("getting Usuario instance with id: " + id);
		try {
			Usuario instance = (Usuario) getSession().get("hibernate.model.Usuario", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
        
        public void changeId(Integer oldId, Integer newId) {
            Query query = getSession().
                    createSQLQuery("update usuario set id = :new where id = :old")
                        .setParameter("old", oldId)
                        .setParameter("new", newId);
            query.executeUpdate();
            getSession().beginTransaction().commit();
	}
        
        public Usuario findByCedula(String cedula) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM usuario where cedula = :cedula")
                    .addEntity(Usuario.class)
                    .setParameter("cedula", cedula);
            Object result = query.uniqueResult();
            return (Usuario) result;
        }
        
        public Usuario findByCedulaActivo(String cedula) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM usuario where cedula = :cedula and activo = true")
                    .addEntity(Usuario.class)
                    .setParameter("cedula", cedula);
            Object result = query.uniqueResult();
            return (Usuario) result;
        }
        
        public Usuario findByCedulaAndEmpresaId(String cedula, Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM usuario JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "WHERE empresa_id = :empresa_id and cedula = :cedula")
                    .addEntity(Usuario.class)
                    .setParameter("empresa_id", empresaId)
                    .setParameter("cedula", cedula);
            Object result = query.uniqueResult();
            return (Usuario) result;
        }
        
        public List<Usuario> findByEmpresaId(Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM usuario JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "WHERE empresa_id = :empresa_id")
                    .addEntity(Usuario.class)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<Usuario>) result;
        }
        
        public List<Usuario> findAllByEmpresaIdActivo(Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM usuario JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "WHERE empresa_id = :empresa_id and activo = true")
                    .addEntity(Usuario.class)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<Usuario>) result;
        }
        
        public List<Usuario> findAllByEmpresaIdActivoIFVISIBLE(Integer empresaId, Fecha inicio) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM usuario JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "WHERE empresa_id = :empresa_id and activo = true")
                    .addEntity(Usuario.class)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return removeUsersUnactive(result, inicio);
        }
        
        public Integer countEmpleados(Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT COUNT(empresa_id) "
                            + "FROM usuario "
                            + "JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "WHERE empresa_id = :empresa_id "
                            + "and activo = true")
                    .setParameter("empresa_id", empresaId);
            Object result = query.uniqueResult();
            return Integer.valueOf(result.toString());
        }
        
        public List<Usuario> findAllByEmpresaYCargoActivo(Integer empresaId, Integer cargoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM usuario JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "WHERE empresa_id = :empresa_id and cargo_id = :cargo_id "
                            + "and activo = true")
                    .addEntity(Usuario.class)
                    .setParameter("cargo_id", cargoId)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<Usuario>) result;
        }
        
        public List<Usuario> findAllEmpleadosActivos() {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM usuario where activo = true "
                            + "and detalles_empleado_id is not null")
                    .addEntity(Usuario.class);
            Object result = query.list();
            return (List<Usuario>) result;
        }
        
        public List<Usuario> findAllEmpleados() {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM usuario "
                            + "where detalles_empleado_id is not null")
                    .addEntity(Usuario.class);
            Object result = query.list();
            return (List<Usuario>) result;
        }
        
        public List<Usuario> findAllEmpleadosActivosOrderByCedula() {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM usuario where activo = true "
                            + "and detalles_empleado_id is not null order by cedula")
                    .addEntity(Usuario.class);
            Object result = query.list();
            return (List<Usuario>) result;
        }
        
        public List<Usuario> findAllByEmpresaIdConDeuda(Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT DISTINCT * FROM usuario "
                            + "JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "JOIN deuda "
                            + "WHERE empresa_id = :empresa_id and activo = true GROUP BY usuario.id")
                    .addEntity(Usuario.class)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<Usuario>) result;
        }

	public List findByExample(Usuario instance) {
		log.debug("finding Usuario instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.Usuario")
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
		log.debug("finding Usuario instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Usuario as model where model."
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

	public List findBySexo(Object sexo) {
		return findByProperty(SEXO, sexo);
	}

	public List findAll() {
		log.debug("finding all Usuario instances");
		try {
			String queryString = "from Usuario";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Usuario merge(Usuario detachedInstance) {
		log.debug("merging Usuario instance");
		try {
			Usuario result = (Usuario) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Usuario instance) {
		log.debug("attaching dirty Usuario instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Usuario instance) {
		log.debug("attaching clean Usuario instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
        
        public List<Usuario> removeUsersUnactive(Object list, Fecha inicio) {
            System.out.println(inicio.toStringInverse());
            List<Usuario> users = new ArrayList<>();
            List<Usuario> usersRemove = new ArrayList<>();
            users.addAll((List<Usuario>) list);
            users.stream().forEach((user) -> {
                DateTime desactivado;
                try {
                    if (user.getDetallesEmpleado() == null 
                            || user.getDetallesEmpleado().getExtra() == null 
                            || user.getDetallesEmpleado().getExtra().isEmpty()){
                        throw new Exception(); 
                    }
                    desactivado = new DateTime(user
                            .getDetallesEmpleado().getExtra());
                    Fecha desactivadoFecha = Fechas.getFecha(desactivado);
                    desactivadoFecha.setDia("01");
                    if (!desactivadoFecha.after(inicio)) {
                        usersRemove.add(user);
                    }
                } catch (Exception e) {
                    // Nothing to do
                }
            });
            users.removeAll(usersRemove);
            return users;
        }
}