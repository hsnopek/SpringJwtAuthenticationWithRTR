package hr.hsnopek.springjwtrtr.domain.base;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTemplate;

public abstract class BaseDAO extends BaseService {

	@Autowired
	protected SessionFactory sessionFactory;
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	protected HibernateTemplate hibernateTemplate;

}
