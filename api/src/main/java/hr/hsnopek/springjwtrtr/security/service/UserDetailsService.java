package hr.hsnopek.springjwtrtr.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hr.hsnopek.springjwtrtr.domain.base.BaseService;
import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;
import hr.hsnopek.springjwtrtr.domain.feature.user.repository.UserRepository;
import hr.hsnopek.springjwtrtr.general.localization.Message;
import hr.hsnopek.springjwtrtr.general.service.Translator;
import hr.hsnopek.springjwtrtr.security.model.UserPrincipal;

@Service
public class UserDetailsService extends BaseService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElse(null);
		
		if(user == null)
			throw new UsernameNotFoundException(Translator.toLocale(Message.ERROR_USER_DOESNT_EXIST));
			
		return new UserPrincipal(user);
	}

}
