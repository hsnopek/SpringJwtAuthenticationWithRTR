package hr.hsnopek.springjwtrtr.security.configuration;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordValidator;
import org.passay.WhitespaceRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import hr.hsnopek.springjwtrtr.general.util.ApplicationProperties;
import hr.hsnopek.springjwtrtr.security.entrypoints.JwtAuthenticationEntryPoint;
import hr.hsnopek.springjwtrtr.security.entrypoints.SimpleAccessDeniedHandler;
import hr.hsnopek.springjwtrtr.security.filter.JWTAuthenticationFilter;
import hr.hsnopek.springjwtrtr.security.providers.JwtAuthenticationProvider;
import hr.hsnopek.springjwtrtr.security.providers.LdapAuthenticationProvider;
import hr.hsnopek.springjwtrtr.security.util.JWTTokenUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;
	private final JWTTokenUtil jwtTokenUtil;

	public SecurityConfiguration(UserDetailsService userDetailsService,
			JWTTokenUtil jwtTokenUtil) {
		this.userDetailsService = userDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.authenticationProvider(daoAuthenticationProvider())
		.authenticationProvider(jwtAuthenticationProvider())
		.authenticationEventPublisher(defaultAuthenticationEventPublisher())
		.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder());
	}

	@Bean
	public DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher() {
	    return new DefaultAuthenticationEventPublisher();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	/**
	 * This authentication provider will authenticate user after he was logged in
	 * with help of JWT that is passed in HTTP request. 
	 * 
	 * It will authenticate user.
	 * 
	 * @return {@link JwtAuthenticationProvider}
	 */
	@Bean
	public JwtAuthenticationProvider jwtAuthenticationProvider() {
		return new JwtAuthenticationProvider(jwtTokenUtil);
	}
	
	/**
	 * This authentication provider will authenticate Ldap users
	 * with help of JWT that is passed in HTTP request. 
	 * 
	 * @return {@link JwtAuthenticationProvider}
	 */
	@Bean
	public LdapAuthenticationProvider ldapAuthenticationProvider() {
		return new LdapAuthenticationProvider();
	}

	/**
	 * This authentication provider will authenticate the user during initial login with the help of @UserdetailsService. 
	 * It is based on the validating the user with the username and password.
	 * @return {@link DaoAuthenticationProvider}
	 */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

	@Override
	public void configure(WebSecurity web) throws Exception {
		
	    final String[] SWAGGER_WHITELIST = {
	            "/v2/api-docs",
	            "/v3/api-docs",  
	            "/swagger-resources/**", 
	            "/swagger-ui/**",
	             };
	    
	    final String[] STATIC_RESOURCES_WHITELIST = {
	            "/static/**",
	    		"/*.ico",
	    		"**.json"
	             };
	
        web.ignoring()
        .antMatchers(SWAGGER_WHITELIST)
        .antMatchers(STATIC_RESOURCES_WHITELIST)
        .antMatchers(ApplicationProperties.WHITELISTED_PATHS);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.exceptionHandling()
			.accessDeniedHandler(new SimpleAccessDeniedHandler())
			.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
		.and()
		.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
		.antMatchers(ApplicationProperties.WHITELISTED_PATHS).permitAll()
		.antMatchers("/user/{id}").hasRole("USER")
		.antMatchers("/user/principal").hasRole("USER")
		.antMatchers("/auth/revoke-token/**").hasAnyRole("USER", "ADMIN")
		.antMatchers("/totp/registration-confirm-secret**").hasAnyRole("USER", "ADMIN")
		.anyRequest().authenticated()
		.and()
		.addFilterAfter(new JWTAuthenticationFilter(jwtTokenUtil, authenticationManagerBean()), ExceptionTranslationFilter.class);
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public PasswordValidator passwordValidator() {
		return new PasswordValidator(
				new LengthRule(8, 16),
				new CharacterRule(EnglishCharacterData.Digit, 1),
				new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
				new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
				new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false),
				new WhitespaceRule());
	}

}
