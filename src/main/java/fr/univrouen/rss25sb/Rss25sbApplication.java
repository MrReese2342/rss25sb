package fr.univrouen.rss25sb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "fr.univrouen.rss25sb.repository")
@EntityScan(basePackages = "fr.univrouen.rss25sb.model")
public class Rss25sbApplication {

    public static void main(String[] args) {
        SpringApplication.run(Rss25sbApplication.class, args);
    }
}
