package hr.hsnopek.springjwtrtr;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@SpringBootApplication
@EnableScheduling
public class SpringJwtAuthenticationWithRtrApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringJwtAuthenticationWithRtrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
