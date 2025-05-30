package linhlang.webconfig;

import linhlang.commons.CommonWebApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@CommonWebApp
@SpringBootApplication
public class WebConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebConfigApplication.class, args);
    }

}
