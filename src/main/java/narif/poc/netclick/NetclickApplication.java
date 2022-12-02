package narif.poc.netclick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactor.blockhound.BlockHound;

@SpringBootApplication
@EnableJpaRepositories
@EnableR2dbcRepositories
public class NetclickApplication {

	public static void main(String[] args) {

		BlockHound.builder()
				.install();
		SpringApplication.run(NetclickApplication.class, args);

	}

}
