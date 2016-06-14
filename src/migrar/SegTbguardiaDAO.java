package migrar;

// default package

import migrar.SegTbguardia;
import java.util.Date;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * SegTbguardia entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see .SegTbguardia
 * @author MyEclipse Persistence Tools
 */
public class SegTbguardiaDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(SegTbguardiaDAO.class);
	// property constants
	public static final String CODI_EMPRESA = "codiEmpresa";
	public static final String CEDU_GUARDIA = "ceduGuardia";
	public static final String APEL_GUARDIA = "apelGuardia";
	public static final String NOMB_GUARDIA = "nombGuardia";
	public static final String FON1_GUARDIA = "fon1Guardia";
	public static final String FON2_GUARDIA = "fon2Guardia";
	public static final String PROV_GUARDIA = "provGuardia";
	public static final String CANT_GUARDIA = "cantGuardia";
	public static final String PARR_GUARDIA = "parrGuardia";
	public static final String DOMI_GUARDIA = "domiGuardia";
	public static final String UGPS_GUARDIA = "ugpsGuardia";
	public static final String TCAS_GUARDIA = "tcasGuardia";
	public static final String TCON_GUARDIA = "tconGuardia";
	public static final String LMIL_GUARDIA = "lmilGuardia";
	public static final String IESS_GUARDIA = "iessGuardia";
	public static final String ECIV_GUARDIA = "ecivGuardia";
	public static final String CONY_GUARDIA = "conyGuardia";
	public static final String OCON_GUARDIA = "oconGuardia";
	public static final String CTRA_GUARDIA = "ctraGuardia";
	public static final String MAIL_GUARDIA = "mailGuardia";
	public static final String ALIB_GUARDIA = "alibGuardia";
	public static final String PFAM_GUARDIA = "pfamGuardia";
	public static final String OBSE_GUARDIA = "obseGuardia";
	public static final String STAT_GUARDIA = "statGuardia";
	public static final String COND_GUARDIA = "condGuardia";
	public static final String SUPE_GUARDIA = "supeGuardia";
	public static final String CONFI_GUARDIA = "confiGuardia";
	public static final String HORA_CREACION = "horaCreacion";
	public static final String HORA_MODIFICA = "horaModifica";
	public static final String CODI_USUARIOSIST = "codiUsuariosist";
	public static final String SUELDO = "sueldo";
	public static final String ACERCADE_MI = "acercadeMi";
	public static final String SEXO_GUARDIA = "sexoGuardia";
	public static final String FOTO = "foto";
	public static final String TIPO_GUARDIA = "tipoGuardia";
	public static final String LISTA_NEGRA = "listaNegra";
	public static final String MOTIVO_LISTA_NEGRA = "motivoListaNegra";
	public static final String MOTIVO_BAJA = "motivoBaja";
	public static final String ID_CARGO = "idCargo";
	public static final String APEL2_GUARDIA = "apel2Guardia";

	public void save(SegTbguardia transientInstance) {
		log.debug("saving SegTbguardia instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SegTbguardia persistentInstance) {
		log.debug("deleting SegTbguardia instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SegTbguardia findById(java.lang.Integer id) {
		log.debug("getting SegTbguardia instance with id: " + id);
		try {
			SegTbguardia instance = (SegTbguardia) getSession().get(
					"SegTbguardia", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SegTbguardia instance) {
		log.debug("finding SegTbguardia instance by example");
		try {
			List results = getSession().createCriteria("SegTbguardia")
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
		log.debug("finding SegTbguardia instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from SegTbguardia as model where model."
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

	public List findByCeduGuardia(Object ceduGuardia) {
		return findByProperty(CEDU_GUARDIA, ceduGuardia);
	}

	public List findByApelGuardia(Object apelGuardia) {
		return findByProperty(APEL_GUARDIA, apelGuardia);
	}

	public List findByNombGuardia(Object nombGuardia) {
		return findByProperty(NOMB_GUARDIA, nombGuardia);
	}

	public List findByFon1Guardia(Object fon1Guardia) {
		return findByProperty(FON1_GUARDIA, fon1Guardia);
	}

	public List findByFon2Guardia(Object fon2Guardia) {
		return findByProperty(FON2_GUARDIA, fon2Guardia);
	}

	public List findByProvGuardia(Object provGuardia) {
		return findByProperty(PROV_GUARDIA, provGuardia);
	}

	public List findByCantGuardia(Object cantGuardia) {
		return findByProperty(CANT_GUARDIA, cantGuardia);
	}

	public List findByParrGuardia(Object parrGuardia) {
		return findByProperty(PARR_GUARDIA, parrGuardia);
	}

	public List findByDomiGuardia(Object domiGuardia) {
		return findByProperty(DOMI_GUARDIA, domiGuardia);
	}

	public List findByUgpsGuardia(Object ugpsGuardia) {
		return findByProperty(UGPS_GUARDIA, ugpsGuardia);
	}

	public List findByTcasGuardia(Object tcasGuardia) {
		return findByProperty(TCAS_GUARDIA, tcasGuardia);
	}

	public List findByTconGuardia(Object tconGuardia) {
		return findByProperty(TCON_GUARDIA, tconGuardia);
	}

	public List findByLmilGuardia(Object lmilGuardia) {
		return findByProperty(LMIL_GUARDIA, lmilGuardia);
	}

	public List findByIessGuardia(Object iessGuardia) {
		return findByProperty(IESS_GUARDIA, iessGuardia);
	}

	public List findByEcivGuardia(Object ecivGuardia) {
		return findByProperty(ECIV_GUARDIA, ecivGuardia);
	}

	public List findByConyGuardia(Object conyGuardia) {
		return findByProperty(CONY_GUARDIA, conyGuardia);
	}

	public List findByOconGuardia(Object oconGuardia) {
		return findByProperty(OCON_GUARDIA, oconGuardia);
	}

	public List findByCtraGuardia(Object ctraGuardia) {
		return findByProperty(CTRA_GUARDIA, ctraGuardia);
	}

	public List findByMailGuardia(Object mailGuardia) {
		return findByProperty(MAIL_GUARDIA, mailGuardia);
	}

	public List findByAlibGuardia(Object alibGuardia) {
		return findByProperty(ALIB_GUARDIA, alibGuardia);
	}

	public List findByPfamGuardia(Object pfamGuardia) {
		return findByProperty(PFAM_GUARDIA, pfamGuardia);
	}

	public List findByObseGuardia(Object obseGuardia) {
		return findByProperty(OBSE_GUARDIA, obseGuardia);
	}

	public List findByStatGuardia(Object statGuardia) {
		return findByProperty(STAT_GUARDIA, statGuardia);
	}

	public List findByCondGuardia(Object condGuardia) {
		return findByProperty(COND_GUARDIA, condGuardia);
	}

	public List findBySupeGuardia(Object supeGuardia) {
		return findByProperty(SUPE_GUARDIA, supeGuardia);
	}

	public List findByConfiGuardia(Object confiGuardia) {
		return findByProperty(CONFI_GUARDIA, confiGuardia);
	}

	public List findByHoraCreacion(Object horaCreacion) {
		return findByProperty(HORA_CREACION, horaCreacion);
	}

	public List findByHoraModifica(Object horaModifica) {
		return findByProperty(HORA_MODIFICA, horaModifica);
	}

	public List findByCodiUsuariosist(Object codiUsuariosist) {
		return findByProperty(CODI_USUARIOSIST, codiUsuariosist);
	}

	public List findBySueldo(Object sueldo) {
		return findByProperty(SUELDO, sueldo);
	}

	public List findByAcercadeMi(Object acercadeMi) {
		return findByProperty(ACERCADE_MI, acercadeMi);
	}

	public List findBySexoGuardia(Object sexoGuardia) {
		return findByProperty(SEXO_GUARDIA, sexoGuardia);
	}

	public List findByFoto(Object foto) {
		return findByProperty(FOTO, foto);
	}

	public List findByTipoGuardia(Object tipoGuardia) {
		return findByProperty(TIPO_GUARDIA, tipoGuardia);
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

	public List findByIdCargo(Object idCargo) {
		return findByProperty(ID_CARGO, idCargo);
	}

	public List findByApel2Guardia(Object apel2Guardia) {
		return findByProperty(APEL2_GUARDIA, apel2Guardia);
	}
        
        public List findAllLimited() {
           Query query = getSession().
                    createSQLQuery("SELECT * FROM seg_tbguardia limit 51,500")
                    .addEntity(SegTbguardia.class);
            Object result = query.list();
            return (List) result; 
        }

	public List findAll() {
		log.debug("finding all SegTbguardia instances");
		try {
			String queryString = "from SegTbguardia";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SegTbguardia merge(SegTbguardia detachedInstance) {
		log.debug("merging SegTbguardia instance");
		try {
			SegTbguardia result = (SegTbguardia) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SegTbguardia instance) {
		log.debug("attaching dirty SegTbguardia instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SegTbguardia instance) {
		log.debug("attaching clean SegTbguardia instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}