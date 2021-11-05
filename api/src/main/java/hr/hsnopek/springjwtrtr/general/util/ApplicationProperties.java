package hr.hsnopek.springjwtrtr.general.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {
	
	public static String APPLICATION_URL;
	public static String FRONTEND_URL;

	public static Boolean EMAIL_VERIFICATION_ENABLED;
	public static String JWT_SECRET;
	public static long REFRESH_TOKEN_EXPIRATION_TIME;
	public static long ACCESS_TOKEN_EXPIRATION_TIME;
	
	public static Boolean IP_WHITELISTING_ENABLED;
	public static String IP_WHITELIST;
	
	public static String LDAP_SERVER;
	public static String LDAP_BASE_DN;
	public static String LDAP_OBJECT_DN;
	public static String LDAP_SEARCH_ATTRIBUTE;
	
	public static final String[] WHITELISTED_PATHS = {"/auth/refresh**", "/auth/login**", "/user/register", "/user/email-verify**", "/totp/get-qr-code**"};


    @Value("${application.url}")
	public void setApplicationUrl(String applicationUrl) {
    	ApplicationProperties.APPLICATION_URL = applicationUrl;
	}
    @Value("${application.frontend-url}")
	public void setFrontendUrl(String frontendUrl) {
    	ApplicationProperties.FRONTEND_URL = frontendUrl;
	}
    @Value("#{new Boolean('${application.registration.email-confirmation.enabled}')}")
    public void setEmailVerificationEnabled(Boolean enabled){
    	ApplicationProperties.EMAIL_VERIFICATION_ENABLED = enabled;
    }
    @Value("${application.jwt.secret}")
	public void setJwtSecret(String jwtSecret) {
    	ApplicationProperties.JWT_SECRET = jwtSecret;
	}
    @Value("${application.jwt.refreshtoken.expiration}")
	public void setRefreshTokenExpiration(long refreshTokenExpiration) {
    	ApplicationProperties.REFRESH_TOKEN_EXPIRATION_TIME = refreshTokenExpiration;
	}
    @Value("${application.jwt.accesstoken.expiration}")
	public void setAccesTokenExpiration(long accesTokenExpiration) {
    	ApplicationProperties.ACCESS_TOKEN_EXPIRATION_TIME = accesTokenExpiration;
	}
    @Value("#{new Boolean('${application.ip-whitelisting.enabled}')}")
    public void setIpWhitelistingEnabled(Boolean enabled) {
    	ApplicationProperties.IP_WHITELISTING_ENABLED = enabled;
    }
    @Value("${application.ip-whitelist}")
	public void setIpWhitelist(String ipWhitelist) {
    	ApplicationProperties.IP_WHITELIST = ipWhitelist;
	}
    @Value("${ldap.server}")
	public void setLdapServer(String ldapServer) {
    	ApplicationProperties.LDAP_SERVER = ldapServer;
	}
    @Value("${ldap.basedn}")
	public void setLdapBaseDn(String ldapBaseDn) {
    	ApplicationProperties.LDAP_BASE_DN = ldapBaseDn;
	}
    @Value("${ldap.objectdn}")
	public void setLdapObjectDn(String ldapObjectDn) {
    	ApplicationProperties.LDAP_OBJECT_DN = ldapObjectDn;
	}
    @Value("${ldap.searchattribute}")
	public void setLdapSearchAttribute(String ldapSearchAttribute) {
    	ApplicationProperties.LDAP_SEARCH_ATTRIBUTE = ldapSearchAttribute;
	}
}
