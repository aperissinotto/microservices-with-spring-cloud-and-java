package br.com.perissinotto;

import br.com.perissinotto.config.GreetingConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GreetingConfiguration.class)
public class Startup {

	static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
	}

}
