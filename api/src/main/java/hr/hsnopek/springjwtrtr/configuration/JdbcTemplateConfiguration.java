package hr.hsnopek.springjwtrtr.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class JdbcTemplateConfiguration {

	@Autowired
	DataSource dataSource;
	
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(this.dataSource);
	}
	
	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(this.dataSource);
	}
}
