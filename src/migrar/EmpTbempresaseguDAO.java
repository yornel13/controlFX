package migrar;



// default package

import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * EmpTbempresasegu entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .EmpTbempresasegu
 * @author MyEclipse Persistence Tools
 */
public class EmpTbempresaseguDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(EmpTbempresaseguDAO.class);
	// property constants
	public static final String NOMB_EMPRESA = "nombEmpresa";
	public static final String NRUC_EMPRESA = "nrucEmpresa";
	public static final String SIGL_EMPRESA = "siglEmpresa";
	public static final String DIRE_EMPRESA = "direEmpresa";
	public static final String FON1_EMPRESA = "fon1Empresa";
	public static final String FON2_EMPRESA = "fon2Empresa";
	public static final String NFAX_EMPRESA = "nfaxEmpresa";
	public static final String PBOX_EMPRESA = "pboxEmpresa";
	public static final String MAIL_EMPRESA = "mailEmpresa";
	public static final String WEBS_EMPRESA = "websEmpresa";
	public static final String REPR_EMPRESA = "reprEmpresa";
	public static final String FONR_EMPRESA = "fonrEmpresa";
	public static final String RDIR_EMPRESA = "rdirEmpresa";
	public static final String TIPO_EMPRESA = "tipoEmpresa";
	public static final String ID_PROVINCIA = "idProvincia";
	public static final String ID_CANTON = "idCanton";
	public static final String LOGO_EMPRESA = "logoEmpresa";
	public static final String OBSE_EMPRESA = "obseEmpresa";
	public static final String STAT_EMPRESA = "statEmpresa";
	public static final String HORA_CREACION = "horaCreacion";
	public static final String HORA_MODIFI = "horaModifi";
	public static final String ID_USUARIO_SIST = "idUsuarioSist";

	public void save(EmpTbempresasegu transientInstance) {
		log.debug("saving EmpTbempresasegu instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(EmpTbempresasegu persistentInstance) {
		log.debug("deleting EmpTbempresasegu instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EmpTbempresasegu findById(java.lang.Integer id) {
		log.debug("getting EmpTbempresasegu instance with id: " + id);
		try {
			EmpTbempresasegu instance = (EmpTbempresasegu) getSession().get(
					"EmpTbempresasegu", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(EmpTbempresasegu instance) {
		log.debug("finding EmpTbempresasegu instance by example");
		try {
			List results = getSession().createCriteria("EmpTbempresasegu")
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
		log.debug("finding EmpTbempresasegu instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from EmpTbempresasegu as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByNombEmpresa(Object nombEmpresa) {
		return findByProperty(NOMB_EMPRESA, nombEmpresa);
	}

	public List findByNrucEmpresa(Object nrucEmpresa) {
		return findByProperty(NRUC_EMPRESA, nrucEmpresa);
	}

	public List findBySiglEmpresa(Object siglEmpresa) {
		return findByProperty(SIGL_EMPRESA, siglEmpresa);
	}

	public List findByDireEmpresa(Object direEmpresa) {
		return findByProperty(DIRE_EMPRESA, direEmpresa);
	}

	public List findByFon1Empresa(Object fon1Empresa) {
		return findByProperty(FON1_EMPRESA, fon1Empresa);
	}

	public List findByFon2Empresa(Object fon2Empresa) {
		return findByProperty(FON2_EMPRESA, fon2Empresa);
	}

	public List findByNfaxEmpresa(Object nfaxEmpresa) {
		return findByProperty(NFAX_EMPRESA, nfaxEmpresa);
	}

	public List findByPboxEmpresa(Object pboxEmpresa) {
		return findByProperty(PBOX_EMPRESA, pboxEmpresa);
	}

	public List findByMailEmpresa(Object mailEmpresa) {
		return findByProperty(MAIL_EMPRESA, mailEmpresa);
	}

	public List findByWebsEmpresa(Object websEmpresa) {
		return findByProperty(WEBS_EMPRESA, websEmpresa);
	}

	public List findByReprEmpresa(Object reprEmpresa) {
		return findByProperty(REPR_EMPRESA, reprEmpresa);
	}

	public List findByFonrEmpresa(Object fonrEmpresa) {
		return findByProperty(FONR_EMPRESA, fonrEmpresa);
	}

	public List findByRdirEmpresa(Object rdirEmpresa) {
		return findByProperty(RDIR_EMPRESA, rdirEmpresa);
	}

	public List findByTipoEmpresa(Object tipoEmpresa) {
		return findByProperty(TIPO_EMPRESA, tipoEmpresa);
	}

	public List findByIdProvincia(Object idProvincia) {
		return findByProperty(ID_PROVINCIA, idProvincia);
	}

	public List findByIdCanton(Object idCanton) {
		return findByProperty(ID_CANTON, idCanton);
	}

	public List findByLogoEmpresa(Object logoEmpresa) {
		return findByProperty(LOGO_EMPRESA, logoEmpresa);
	}

	public List findByObseEmpresa(Object obseEmpresa) {
		return findByProperty(OBSE_EMPRESA, obseEmpresa);
	}

	public List findByStatEmpresa(Object statEmpresa) {
		return findByProperty(STAT_EMPRESA, statEmpresa);
	}

	public List findByHoraCreacion(Object horaCreacion) {
		return findByProperty(HORA_CREACION, horaCreacion);
	}

	public List findByHoraModifi(Object horaModifi) {
		return findByProperty(HORA_MODIFI, horaModifi);
	}

	public List findByIdUsuarioSist(Object idUsuarioSist) {
		return findByProperty(ID_USUARIO_SIST, idUsuarioSist);
	}

	public List findAll() {
		log.debug("finding all EmpTbempresasegu instances");
		try {
			String queryString = "from EmpTbempresasegu";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
        
        public List<EmpTbempresasegu> findAllEmpresas() {
		Query query = getSession().
                    createSQLQuery("SELECT * FROM emp_tbempresasegu")
                        .addEntity(EmpTbempresasegu.class);
            Object result = query.list();
            return (List<EmpTbempresasegu>) result;
	}

	public EmpTbempresasegu merge(EmpTbempresasegu detachedInstance) {
		log.debug("merging EmpTbempresasegu instance");
		try {
			EmpTbempresasegu result = (EmpTbempresasegu) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(EmpTbempresasegu instance) {
		log.debug("attaching dirty EmpTbempresasegu instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EmpTbempresasegu instance) {
		log.debug("attaching clean EmpTbempresasegu instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}