package hr.hsnopek.springjwtrtr.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

import hr.hsnopek.springjwtrtr.general.localization.CustomLocaleResolver;

@Configuration
public class LocalizationConfiguration {
	
	  @Bean(name = "messageSource")
	  public ResourceBundleMessageSource getMessageSource() {
	      ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	      messageSource.setBasename("i18n/messages");
	      messageSource.setDefaultEncoding("UTF-8");
	      return messageSource;
	  }
	  
	  @Bean
	  public LocaleResolver localeResolver() {
	      LocaleResolver lr = new CustomLocaleResolver();
	      return lr;
	  }
	   
}
