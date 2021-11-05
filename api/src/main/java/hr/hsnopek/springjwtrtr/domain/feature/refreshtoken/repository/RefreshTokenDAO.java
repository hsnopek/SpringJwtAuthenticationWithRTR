package hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import hr.hsnopek.springjwtrtr.domain.base.BaseDAO;
import hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.entity.RefreshToken;

@Repository
public class RefreshTokenDAO extends BaseDAO {

	public List<RefreshToken> findAll() {
		List<RefreshToken> refreshTokens = null;
		
		try {
			refreshTokens = jdbcTemplate.query("select * from refresh_token", new RowMapper<RefreshToken>() {

				@Override
				public RefreshToken mapRow(ResultSet rs, int arg1) throws SQLException {
					RefreshToken rt = new RefreshToken();
					
					rt.setId(rs.getLong("refresh_token_id"));
					rt.setRefreshToken(rs.getString("refresh_token"));
					rt.setReplacedBy(rs.getLong("replaced_by"));
					rt.setRevoked(rs.getBoolean("revoked"));
					rt.setUserDevice(null);
					rt.setJti(rs.getString("jti"));
					rt.setExpiryDate(rs.getTimestamp("expiry_date").toInstant());
														
					return rt;
				}
				
			});
		} catch (DataAccessException e) {
			throw e;
		}
		
		return refreshTokens;
	}
}
