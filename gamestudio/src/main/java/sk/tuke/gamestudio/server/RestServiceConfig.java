package sk.tuke.gamestudio.server;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/rest")
public class RestServiceConfig extends ResourceConfig {
	
	public RestServiceConfig() {
		packages("sk.tuke.gamestudio.server");
	}
}
