package hr.hsnopek.springjwtrtr.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.module.jsr310.Jsr310Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ModelMapperConfiguration {

	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.registerModule(new Jsr310Module());
		return modelMapper;
	}
	
	@Bean
	ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.findAndRegisterModules();
	    return objectMapper;
	}
}
