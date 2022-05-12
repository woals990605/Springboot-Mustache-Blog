package site.metacoding.springbootmustacheblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringbootMustacheBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMustacheBlogApplication.class, args);
	}

}
