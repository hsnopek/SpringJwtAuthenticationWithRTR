package hr.hsnopek.springjwtrtr.security.batch;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.entity.RefreshToken;
import hr.hsnopek.springjwtrtr.domain.feature.refreshtoken.service.RefreshTokenService;

@Component
public class RevokedTokenCleanupJob {
	
	@Autowired
	RefreshTokenService refreshTokenService;
	
	@Scheduled(fixedDelay = 1000 * 60 * 10)
	@Transactional
	public void cleanOldRefreshTokens() {
		List<RefreshToken> refreshTokens = refreshTokenService
				.findByExpiryDateBeforeAndRevoked(Instant.now().minus(10, ChronoUnit.DAYS), Boolean.TRUE);
		refreshTokenService.deleteRefreshTokens(refreshTokens);
	}
}
