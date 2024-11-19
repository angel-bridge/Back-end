package angel_bridge.angel_bridge_server;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AngelBridgeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AngelBridgeServerApplication.class, args);
	}

}
