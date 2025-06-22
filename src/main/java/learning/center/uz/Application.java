package learning.center.uz;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {


	public static void main(String[] args) {

//		Dotenv dotenv = Dotenv.load();
//
//		// Set to Spring's environment
//		System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
//		System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
//		System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
//		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
//		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
//		System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));

		SpringApplication.run(Application.class, args);
	}

}
