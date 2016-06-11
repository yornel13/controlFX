package migrar;

// default package

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;


/**
 * Data access object (DAO) for domain model
 * @author MyEclipse Persistence Tools
 */
public class BaseHibernateDAO implements IBaseHibernateDAO {
	
	public Session getSession() {
		return new Configuration()
               .configure("hibernate.cfg_2.xml").buildSessionFactory().openSession(); 
	}
	
}