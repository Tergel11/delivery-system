package mn.delivery.system;

//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@OpenAPIDefinition(info = @Info(title = "Manage API", description = "Manage API Documentation", version = "v1.0"))
public class StarterManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarterManageApplication.class, args);
    }
}
