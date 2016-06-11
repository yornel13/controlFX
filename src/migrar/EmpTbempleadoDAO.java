package migrar;



import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * EmpTbempleado entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .EmpTbempleado
 * @author MyEclipse Persistence Tools
 */
public class EmpTbempleadoDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(EmpTbempleadoDAO.class);
	// property constants
	public static final String CODI_EMPRESA = "codiEmpresa";
	public static final String CODI_DEPARTAMENT = "codiDepartament";
	public static final String CODI_CARGO = "codiCargo";
	public static final String CEDU_EMPLEADO = "ceduEmpleado";
	public static final String CODI_TIPOEMPLE = "codiTipoemple";
	public static final String APAT_EMPLEADO = "apatEmpleado";
	public static final String AMAT_EMPLEADO = "amatEmpleado";
	public static final String NOMB_EMPLEADO = "nombEmpleado";
	public static final String NPRO_EMPLEADO = "nproEmpleado";
	public static final String NCAN_EMPLEADO = "ncanEmpleado";
	public static final String NPAR_EMPLEADO = "nparEmpleado";
	public static final String NREC_EMPLEADO = "nrecEmpleado";
	public static final String ECIV_EMPLEADO = "ecivEmpleado";
	public static final String SEXO_EMPLEADO = "sexoEmpleado";
	public static final String CONY_EMPLEADO = "conyEmpleado";
	public static final String DIRE_EMPLEADO = "direEmpleado";
	public static final String FONO_EMPLEADO = "fonoEmpleado";
	public static final String LMIL_EMPLEADO = "lmilEmpleado";
	public static final String IESS_EMPLEADO = "iessEmpleado";
	public static final String MAIL_EMPLEADO = "mailEmpleado";
	public static final String OBSE_EMPLEADO = "obseEmpleado";
	public static final String STAT_EMPLEADO = "statEmpleado";
	public static final String FOTO_EMPLEADO = "fotoEmpleado";
	public static final String HORA_CREACION = "horaCreacion";
	public static final String CODI_USUARIOSIST = "codiUsuariosist";
	public static final String ACERCADE_MI = "acercadeMi";
	public static final String SUELDO = "sueldo";
	public static final String TELF2 = "telf2";
	public static final String OCUP_CONY = "ocupCony";
	public static final String TRAB_CONY = "trabCony";
	public static final String UGPS = "ugps";
	public static final String LISTA_NEGRA = "listaNegra";
	public static final String MOTIVO_LISTA_NEGRA = "motivoListaNegra";
	public static final String MOTIVO_BAJA = "motivoBaja";
	public static final String CLAVE = "clave";

	public void save(EmpTbempleado transientInstance) {
		log.debug("saving EmpTbempleado instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(EmpTbempleado persistentInstance) {
		log.debug("deleting EmpTbempleado instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EmpTbempleado findById(java.lang.Integer id) {
		log.debug("getting EmpTbempleado instance with id: " + id);
		try {
			EmpTbempleado instance = (EmpTbempleado) getSession().get(
					"EmpTbempleado", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(EmpTbempleado instance) {
		log.debug("finding EmpTbempleado instance by example");
		try {
			List results = getSession().createCriteria("EmpTbempleado")
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
		log.debug("finding EmpTbempleado instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from EmpTbempleado as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCodiEmpresa(Object codiEmpresa) {
		return findByProperty(CODI_EMPRESA, codiEmpresa);
	}

	public List findByCodiDepartament(Object codiDepartament) {
		return findByProperty(CODI_DEPARTAMENT, codiDepartament);
	}

	public List findByCodiCargo(Object codiCargo) {
		return findByProperty(CODI_CARGO, codiCargo);
	}

	public List findByCeduEmpleado(Object ceduEmpleado) {
		return findByProperty(CEDU_EMPLEADO, ceduEmpleado);
	}

	public List findByCodiTipoemple(Object codiTipoemple) {
		return findByProperty(CODI_TIPOEMPLE, codiTipoemple);
	}

	public List findByApatEmpleado(Object apatEmpleado) {
		return findByProperty(APAT_EMPLEADO, apatEmpleado);
	}

	public List findByAmatEmpleado(Object amatEmpleado) {
		return findByProperty(AMAT_EMPLEADO, amatEmpleado);
	}

	public List findByNombEmpleado(Object nombEmpleado) {
		return findByProperty(NOMB_EMPLEADO, nombEmpleado);
	}

	public List findByNproEmpleado(Object nproEmpleado) {
		return findByProperty(NPRO_EMPLEADO, nproEmpleado);
	}

	public List findByNcanEmpleado(Object ncanEmpleado) {
		return findByProperty(NCAN_EMPLEADO, ncanEmpleado);
	}

	public List findByNparEmpleado(Object nparEmpleado) {
		return findByProperty(NPAR_EMPLEADO, nparEmpleado);
	}

	public List findByNrecEmpleado(Object nrecEmpleado) {
		return findByProperty(NREC_EMPLEADO, nrecEmpleado);
	}

	public List findByEcivEmpleado(Object ecivEmpleado) {
		return findByProperty(ECIV_EMPLEADO, ecivEmpleado);
	}

	public List findBySexoEmpleado(Object sexoEmpleado) {
		return findByProperty(SEXO_EMPLEADO, sexoEmpleado);
	}

	public List findByConyEmpleado(Object conyEmpleado) {
		return findByProperty(CONY_EMPLEADO, conyEmpleado);
	}

	public List findByDireEmpleado(Object direEmpleado) {
		return findByProperty(DIRE_EMPLEADO, direEmpleado);
	}

	public List findByFonoEmpleado(Object fonoEmpleado) {
		return findByProperty(FONO_EMPLEADO, fonoEmpleado);
	}

	public List findByLmilEmpleado(Object lmilEmpleado) {
		return findByProperty(LMIL_EMPLEADO, lmilEmpleado);
	}

	public List findByIessEmpleado(Object iessEmpleado) {
		return findByProperty(IESS_EMPLEADO, iessEmpleado);
	}

	public List findByMailEmpleado(Object mailEmpleado) {
		return findByProperty(MAIL_EMPLEADO, mailEmpleado);
	}

	public List findByObseEmpleado(Object obseEmpleado) {
		return findByProperty(OBSE_EMPLEADO, obseEmpleado);
	}

	public List findByStatEmpleado(Object statEmpleado) {
		return findByProperty(STAT_EMPLEADO, statEmpleado);
	}

	public List findByFotoEmpleado(Object fotoEmpleado) {
		return findByProperty(FOTO_EMPLEADO, fotoEmpleado);
	}

	public List findByHoraCreacion(Object horaCreacion) {
		return findByProperty(HORA_CREACION, horaCreacion);
	}

	public List findByCodiUsuariosist(Object codiUsuariosist) {
		return findByProperty(CODI_USUARIOSIST, codiUsuariosist);
	}

	public List findByAcercadeMi(Object acercadeMi) {
		return findByProperty(ACERCADE_MI, acercadeMi);
	}

	public List findBySueldo(Object sueldo) {
		return findByProperty(SUELDO, sueldo);
	}

	public List findByTelf2(Object telf2) {
		return findByProperty(TELF2, telf2);
	}

	public List findByOcupCony(Object ocupCony) {
		return findByProperty(OCUP_CONY, ocupCony);
	}

	public List findByTrabCony(Object trabCony) {
		return findByProperty(TRAB_CONY, trabCony);
	}

	public List findByUgps(Object ugps) {
		return findByProperty(UGPS, ugps);
	}

	public List findByListaNegra(Object listaNegra) {
		return findByProperty(LISTA_NEGRA, listaNegra);
	}

	public List findByMotivoListaNegra(Object motivoListaNegra) {
		return findByProperty(MOTIVO_LISTA_NEGRA, motivoListaNegra);
	}

	public List findByMotivoBaja(Object motivoBaja) {
		return findByProperty(MOTIVO_BAJA, motivoBaja);
	}

	public List findByClave(Object clave) {
		return findByProperty(CLAVE, clave);
	}

	public List findAll() {
		log.debug("finding all EmpTbempleado instances");
		try {
			String queryString = "from EmpTbempleado";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public EmpTbempleado merge(EmpTbempleado detachedInstance) {
		log.debug("merging EmpTbempleado instance");
		try {
			EmpTbempleado result = (EmpTbempleado) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(EmpTbempleado instance) {
		log.debug("attaching dirty EmpTbempleado instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EmpTbempleado instance) {
		log.debug("attaching clean EmpTbempleado instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}