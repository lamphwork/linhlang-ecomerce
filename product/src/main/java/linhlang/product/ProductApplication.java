package linhlang.product;

import linhlang.commons.Starter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductApplication {

    public static void main(String[] args) {
        Starter.webApp(ProductApplication.class).run(args);
    }

}
