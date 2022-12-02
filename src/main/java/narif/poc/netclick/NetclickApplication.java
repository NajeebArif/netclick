package narif.poc.netclick;

import narif.poc.netclick.config.NonReactiveTypes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactor.blockhound.BlockHound;

@SpringBootApplication
@EnableJpaRepositories
@EnableR2dbcRepositories(excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
		classes = NonReactiveTypes.class))
public class NetclickApplication {

	public static void main(String[] args) {

		BlockHound.builder()
				.install();
		SpringApplication.run(NetclickApplication.class, args);

	}

}
