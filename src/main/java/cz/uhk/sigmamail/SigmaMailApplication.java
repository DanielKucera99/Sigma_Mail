package cz.uhk.sigmamail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "cz.uhk.sigmamail.model")
public class SigmaMailApplication {

    public static void main(String[] args) {
        SpringApplication.run(SigmaMailApplication.class, args);
    }

}
