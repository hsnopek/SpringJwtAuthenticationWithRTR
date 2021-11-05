package hr.hsnopek.springjwtrtr.security.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import javax.naming.ldap.InitialLdapContext;

import hr.hsnopek.springjwtrtr.general.util.ApplicationProperties;
import hr.hsnopek.springjwtrtr.security.authenticationtokens.LdapAuthenticationToken;
import hr.hsnopek.springjwtrtr.security.exceptions.CustomAuthenticationException;
import hr.hsnopek.springjwtrtr.security.model.LdapUser;

public class LdapAuthenticationProvider implements AuthenticationProvider {

	public LdapAuthenticationProvider() {
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			LdapContext ctx = createLdapContext(authentication);
			NamingEnumeration<?> results = searchBySearchAttribute(
					ctx, 
					ApplicationProperties.LDAP_OBJECT_DN, 
					String.format(ApplicationProperties.LDAP_SEARCH_ATTRIBUTE, authentication.getName())
					);
			
			LdapUser ldapUser = mapAttributesToUser(results, authentication);
			
			if(ldapUser == null)
				throw new UsernameNotFoundException("User not found in LDAP.");
			else {
				LdapAuthenticationToken ldapAuthenticationToken = new LdapAuthenticationToken(ldapUser, authentication.getCredentials());
				ldapAuthenticationToken.setAuthenticated(true);
				return ldapAuthenticationToken;
			}
		} catch (Exception e) {
			throw new CustomAuthenticationException("Ldap authentication failed!");
		}
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
        return authentication.equals(LdapAuthenticationToken.class);
	}

	private NamingEnumeration<?> searchBySearchAttribute(LdapContext ctx, String objectDN, String searchAttribute) throws NamingException {
		NamingEnumeration<?> results = ctx.search(
				objectDN,
				searchAttribute, 
				searchControls()
			);
		return results;
	}

	private LdapContext createLdapContext(Authentication authentication) throws NamingException {
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		props.put(Context.PROVIDER_URL, ApplicationProperties.LDAP_SERVER);
		props.put(Context.SECURITY_PRINCIPAL, ApplicationProperties.LDAP_BASE_DN);
		props.put(Context.SECURITY_CREDENTIALS, authentication.getCredentials());

		LdapContext ctx = new InitialLdapContext(props, null);
		return ctx;
	}

	private LdapUser mapAttributesToUser(NamingEnumeration<?> results, Authentication authentication) throws NamingException {
		
		List<LdapUser> ldapUserList = new ArrayList<>();
		
		while (results.hasMore()) {
			SearchResult result = (SearchResult) results.next();
			Attributes attributes = result.getAttributes();
			
			String mail = (String) attributes.get("mail").get();
			String firstName = attributes.get("cn").get().toString().split(StringUtils.SPACE)[0];
			String lastName = (String) attributes.get("sn").get();

			
			LdapUser ldapUser = new LdapUser();
			ldapUser.setEmail(mail);
			ldapUser.setUsername(authentication.getName());
			ldapUser.setFirstName(firstName);
			ldapUser.setLastName(lastName);
			ldapUser.setPassword((String)authentication.getCredentials());

			ldapUserList.add(ldapUser);
		}

		results.close();

		return ldapUserList.stream()
				.filter(x -> x.getUsername().equals(authentication.getName()))
				.findFirst()
				.orElse(null);
	}

	private SearchControls searchControls() {
	    SearchControls searchControls = new SearchControls();
	    searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		return searchControls;

	}

}
