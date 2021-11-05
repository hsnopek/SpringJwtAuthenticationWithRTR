package hr.hsnopek.springjwtrtr.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

@Configuration
public class ConverterConfiguration {

	@Bean
	public HttpMessageConverter<java.awt.image.BufferedImage> bufferedImageConverter() {
		return new BufferedImageHttpMessageConverter();
	}
}
