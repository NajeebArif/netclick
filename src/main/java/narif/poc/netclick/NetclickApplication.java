package narif.poc.netclick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class NetclickApplication {

	public static void main(String[] args) {

		BlockHound.builder()
				.install();
		SpringApplication.run(NetclickApplication.class, args);

	}

}
