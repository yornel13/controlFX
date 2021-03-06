package hibernate.dao;

// default package

import hibernate.model.RolCliente;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for Pago
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see .Pago
 * @author MyEclipse Persistence Tools
 */
public class RolClienteDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(RolClienteDAO.class);
	// property constants
	public static final String SUELDO = "sueldo";
	public static final String DIAS = "dias";
	public static final String HORAS_NORMALES = "horasNormales";
	public static final String HORAS_SUPLEMENTARIAS = "horasSuplementarias";
	public static final String HORAS_SOBRE_TIEMPO = "horasSobreTiempo";
	public static final String TOTAL_HORAS_EXTRAS = "totalHorasExtras";
	public static final String SALARIO = "salario";
	public static final String MONTO_HORAS_SUPLEMENTARIAS = "montoHorasSuplementarias";
	public static final String MONTO_HORAS_SOBRE_TIEMPO = "montoHorasSobreTiempo";
	public static final String BONO = "bono";
	public static final String TRANSPORTE = "transporte";
	public static final String TOTAL_BONOS = "totalBonos";
        public static final String VACACIONES = "vacaciones";
	public static final String SUBTOTAL = "subtotal";
	public static final String DECIMO_TERCERO = "decimoTercero";
	public static final String DECIMO_CUARTO = "decimoCuarto";
	public static final String JUBILACION_PATRONAL = "jubilacionPatronal";
	public static final String APORTE_PATRONAL = "aportePatronal";
	public static final String SEGUROS = "seguros";
	public static final String UNIFORMES = "uniformes";
	public static final String TOTAL_INGRESO = "totalIngreso";
	public static final String EMPLEADO = "empleado";
	public static final String CEDULA = "cedula";
	public static final String EMPRESA = "empresa";
	public static final String CLIENTE_NOMBRE = "clienteNombre";

	public void save(RolCliente transientInstance) {
		log.debug("saving Pago instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(RolCliente persistentInstance) {
		log.debug("deleting Pago instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RolCliente findById(java.lang.Integer id) {
		log.debug("getting Pago instance with id: " + id);
		try {
			RolCliente instance = (RolCliente) getSession().get("hibernate.model.RolCliente", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RolCliente instance) {
		log.debug("finding Pago instance by example");
		try {
			List results = getSession().createCriteria("hibernate.model.RolCliente")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
        
        public List<RolCliente> findAllByFechaAndEmpleadoId(String fecha, Integer empleadoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM rol_cliente "
                            + "where inicio = :fecha "
                            + "and usuario_id = :usuario_id")
                    .addEntity(RolCliente.class)
                    .setParameter("fecha", fecha)
                    .setParameter("usuario_id", empleadoId);
            Object result = query.list();
            return (List<RolCliente>) result;
        }
        
        public List<RolCliente> findAllByFechaAndEmpleadoIdConCliente(String fecha, Integer empleadoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM rol_cliente "
                            + "where inicio = :fecha "
                            + "and usuario_id = :usuario_id "
                            + "and cliente_id IS NOT NULL")
                    .addEntity(RolCliente.class)
                    .setParameter("fecha", fecha)
                    .setParameter("usuario_id", empleadoId);
            Object result = query.list();
            return (List<RolCliente>) result;
        }
        
        public List<RolCliente> findAllByFechaAndEmpleadoIdSinCliente(String fecha, Integer empleadoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM rol_cliente "
                            + "where inicio = :fecha "
                            + "and usuario_id = :usuario_id "
                            + "and cliente_id IS NULL")
                    .addEntity(RolCliente.class)
                    .setParameter("fecha", fecha)
                    .setParameter("usuario_id", empleadoId);
            Object result = query.list();
            return (List<RolCliente>) result;
        }
        
        public RolCliente findByFechaAndEmpleadoIdAndClienteId(String fecha, Integer empleadoId, Integer clienteId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM rol_cliente "
                            + "where inicio = :fecha "
                            + "and usuario_id = :usuario_id "
                            + "and cliente_id = :cliente_id")
                    .addEntity(RolCliente.class)
                    .setParameter("fecha", fecha)
                    .setParameter("usuario_id", empleadoId)
                    .setParameter("cliente_id", clienteId);
            Object result = query.uniqueResult();
            return (RolCliente) result;
        }
        
        public RolCliente findByFechaAndEmpleadoIdSinCliente(String fecha, Integer empleadoId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM rol_cliente "
                            + "where inicio = :fecha "
                            + "and usuario_id = :usuario_id "
                            + "and cliente_id IS NULL")
                    .addEntity(RolCliente.class)
                    .setParameter("fecha", fecha)
                    .setParameter("usuario_id", empleadoId);
            Object result = query.uniqueResult();
            return (RolCliente) result;
        }
        
        public RolCliente findByFechaAndEmpleadoIdAndDetalles(String fecha, Integer empleadoId, String detalles) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM rol_cliente "
                            + "where inicio = :fecha "
                            + "and usuario_id = :usuario_id "
                            + "and detalles = :detalles "
                            + "and cliente_id IS NULL")
                    .addEntity(RolCliente.class)
                    .setParameter("fecha", fecha)
                    .setParameter("detalles", detalles)
                    .setParameter("usuario_id", empleadoId);
            Object result = query.uniqueResult();
            return (RolCliente) result;
        }
        
        public List<RolCliente> findAllByFechaAndClienteId(String fechaInicio, Integer clienteId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM rol_cliente "
                            + "where inicio = :fecha "
                            + "and cliente_id = :cliente_id")
                    .addEntity(RolCliente.class)
                    .setParameter("fecha", fechaInicio)
                    .setParameter("cliente_id", clienteId);
            Object result = query.list();
            return (List<RolCliente>) result;
        }
        
        public List<RolCliente> findAllByFechaSinCliente(String fechaInicio) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM rol_cliente "
                            + "where inicio = :fecha "
                            + "and cliente_id IS NULL")
                    .addEntity(RolCliente.class)
                    .setParameter("fecha", fechaInicio);
            Object result = query.list();
            return (List<RolCliente>) result;
        }
        
        public List<RolCliente> findAllByFechaAndClienteIdAndEmpresaId(String fecha, 
                Integer clienteId, Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM rol_cliente "
                            + "JOIN usuario "
                            + "ON usuario.id = rol_cliente.usuario_id "
                            + "JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "where inicio = :fecha "
                            + "and cliente_id = :cliente_id "
                            + "and empresa_id = :empresa_id")
                    .addEntity(RolCliente.class)
                    .setParameter("fecha", fecha)
                    .setParameter("cliente_id", clienteId)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<RolCliente>) result;
        }
        
        public List<RolCliente> findAllByFechaAndEmpresaIdSinCliente(String fecha, 
                Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM rol_cliente "
                            + "JOIN usuario "
                            + "ON usuario.id = rol_cliente.usuario_id "
                            + "JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "where inicio = :fecha "
                            + "and cliente_id IS NULL "
                            + "and empresa_id = :empresa_id")
                    .addEntity(RolCliente.class)
                    .setParameter("fecha", fecha)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<RolCliente>) result;
        }
        
        public List<RolCliente> findAllByEntreFechaAndEmpresaId(String fecha, 
                Integer empresaId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM rol_cliente "
                            + "JOIN usuario "
                            + "ON usuario.id = rol_cliente.usuario_id "
                            + "JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "where inicio <= :fecha "
                            + "and finalizo >= :fecha "
                            + "and empresa_id = :empresa_id")
                    .addEntity(RolCliente.class)
                    .setParameter("fecha", fecha)
                    .setParameter("empresa_id", empresaId);
            Object result = query.list();
            return (List<RolCliente>) result;
        }
        
        public List<RolCliente> findAllByEntreFechaAndEmpresaIdAndEmpleadoId(String fecha, 
                Integer empresaId, Integer usuarioId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM rol_cliente "
                            + "JOIN usuario "
                            + "ON usuario.id = rol_cliente.usuario_id "
                            + "JOIN detalles_empleado "
                            + "ON detalles_empleado.id = usuario.detalles_empleado_id "
                            + "where inicio <= :fecha "
                            + "and finalizo >= :fecha "
                            + "and empresa_id = :empresa_id "
                            + "and usuario_id = :usuario_id")
                    .addEntity(RolCliente.class)
                    .setParameter("fecha", fecha)
                    .setParameter("empresa_id", empresaId)
                    .setParameter("usuario_id", usuarioId);
            Object result = query.list();
            return (List<RolCliente>) result;
        }

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Pago instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from RolCliente as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySueldo(Object sueldo) {
		return findByProperty(SUELDO, sueldo);
	}

	public List findByDias(Object dias) {
		return findByProperty(DIAS, dias);
	}

	public List findByHorasNormales(Object horasNormales) {
		return findByProperty(HORAS_NORMALES, horasNormales);
	}

	public List findByHorasSuplementarias(Object horasSuplementarias) {
		return findByProperty(HORAS_SUPLEMENTARIAS, horasSuplementarias);
	}

	public List findByHorasSobreTiempo(Object horasSobreTiempo) {
		return findByProperty(HORAS_SOBRE_TIEMPO, horasSobreTiempo);
	}

	public List findByTotalHorasExtras(Object totalHorasExtras) {
		return findByProperty(TOTAL_HORAS_EXTRAS, totalHorasExtras);
	}

	public List findBySalario(Object salario) {
		return findByProperty(SALARIO, salario);
	}

	public List findByMontoHorasSuplementarias(Object montoHorasSuplementarias) {
		return findByProperty(MONTO_HORAS_SUPLEMENTARIAS,
				montoHorasSuplementarias);
	}

	public List findByMontoHorasSobreTiempo(Object montoHorasSobreTiempo) {
		return findByProperty(MONTO_HORAS_SOBRE_TIEMPO, montoHorasSobreTiempo);
	}

	public List findByBono(Object bono) {
		return findByProperty(BONO, bono);
	}

	public List findByTransporte(Object transporte) {
		return findByProperty(TRANSPORTE, transporte);
	}

	public List findByTotalBonos(Object totalBonos) {
		return findByProperty(TOTAL_BONOS, totalBonos);
	}
        
        public List findByVaciones(Object vacaciones) {
		return findByProperty(VACACIONES, vacaciones);
	}

	public List findBySubtotal(Object subtotal) {
		return findByProperty(SUBTOTAL, subtotal);
	}

	public List findByDecimoTercero(Object decimoTercero) {
		return findByProperty(DECIMO_TERCERO, decimoTercero);
	}

	public List findByDecimoCuarto(Object decimoCuarto) {
		return findByProperty(DECIMO_CUARTO, decimoCuarto);
	}

	public List findByJubilacionPatronal(Object jubilacionPatronal) {
		return findByProperty(JUBILACION_PATRONAL, jubilacionPatronal);
	}

	public List findByAportePatronal(Object aportePatronal) {
		return findByProperty(APORTE_PATRONAL, aportePatronal);
	}

	public List findBySeguros(Object seguros) {
		return findByProperty(SEGUROS, seguros);
	}

	public List findByUniformes(Object uniformes) {
		return findByProperty(UNIFORMES, uniformes);
	}

	public List findByTotalIngreso(Object totalIngreso) {
		return findByProperty(TOTAL_INGRESO, totalIngreso);
	}

	public List findByEmpleado(Object empleado) {
		return findByProperty(EMPLEADO, empleado);
	}

	public List findByCedula(Object cedula) {
		return findByProperty(CEDULA, cedula);
	}

	public List findByEmpresa(Object empresa) {
		return findByProperty(EMPRESA, empresa);
	}

	public List findByClienteNombre(Object clienteNombre) {
		return findByProperty(CLIENTE_NOMBRE, clienteNombre);
	}

	public List findAll() {
		log.debug("finding all Pago instances");
		try {
			String queryString = "from Pago";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
        
        public List<RolCliente> findByClienteId(Integer clienteId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago where cliente_id = :cliente_id")
                    .addEntity(RolCliente.class)
                    .setParameter("cliente_id", clienteId);
            Object result = query.list();
            return (List<RolCliente>) result;
        }
        
        public List<RolCliente> findAllSinCliente() {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM pago where cliente_id IS NULL")
                    .addEntity(RolCliente.class);
            Object result = query.list();
            return (List<RolCliente>) result;
        }
        
        public List<RolCliente> findAllByUsuarioId(Integer usuarioId) {
            Query query = getSession().
                    createSQLQuery("SELECT * FROM rol_cliente where usuario_id = :usuario_id")
                    .addEntity(RolCliente.class)
                    .setParameter("usuario_id", usuarioId);
            Object result = query.list();
            return (List<RolCliente>) result;
        }

	public RolCliente merge(RolCliente detachedInstance) {
		log.debug("merging Pago instance");
		try {
			RolCliente result = (RolCliente) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RolCliente instance) {
		log.debug("attaching dirty Pago instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RolCliente instance) {
		log.debug("attaching clean Pago instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}