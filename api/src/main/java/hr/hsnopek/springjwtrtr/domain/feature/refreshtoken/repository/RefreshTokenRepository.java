package hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	
	public RefreshToken findOneByJti(String jti);
	public RefreshToken findOneByRefreshToken(String refreshToken);	
	public RefreshToken findOneByUserDeviceId(Long userDeviceId);	
	public RefreshToken findOneByUserDeviceIdAndRevoked(Long id, Boolean status);

	@Query(nativeQuery = true, value="select rt.* "
			+ "from {h-schema}user_device ud "
			+ "inner join {h-schema}user_ u on u.user_id  = ud.user_id and u.active  = true "
			+ "inner join {h-schema}refresh_token rt on rt.user_device_id  = ud.user_device_id --and now() <= rt.expiry_date "
			+ "where ud.user_id = ?1 --and rt.revoked = false")
	List<RefreshToken> findAllRefreshTokensByUserId(Long userId);

	@Query(nativeQuery = true, value=
			"WITH RECURSIVE cte AS ("
			+ "select refresh_token_id, replaced_by, 1 as level "
				+ "from {h-schema}refresh_token "
				+ "where "
					+ "jti = ?1 "
			+ "union all "
				+ "select rt.refresh_token_id, rt.replaced_by, c.level + 1 "
					+ "from {h-schema}refresh_token rt "
					+ "inner join cte c ON c.replaced_by = rt.refresh_token_id "
			+ ") "
			+ "select * from cte c "
			+ "inner join {h-schema}refresh_token rt on rt.refresh_token_id = c.refresh_token_id")
	List<RefreshToken> findDescendantRefreshTokens(String jti);
	public List<RefreshToken> findByExpiryDateBeforeAndRevoked(Instant now, Boolean status);
}
