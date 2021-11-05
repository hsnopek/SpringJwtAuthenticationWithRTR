package hr.hsnopek.springjwtrtr.domain.feature.user.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hr.hsnopek.springjwtrtr.domain.feature.user.dto.UserDTO;
import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;

@Component
public class UserDTOMapper {

	@Autowired
	ModelMapper modelMapper;
	
	public UserDTO map(User user) {
		return modelMapper.map(user, UserDTO.class);
	}
}
