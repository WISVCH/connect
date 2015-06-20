package ch.wisv.samltest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:main.xml")
public class SamltestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SamltestApplication.class, args);
    }
}
