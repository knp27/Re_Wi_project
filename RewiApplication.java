package in.co.codeplanet.rewi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RewiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RewiApplication.class, args);
	}

}
