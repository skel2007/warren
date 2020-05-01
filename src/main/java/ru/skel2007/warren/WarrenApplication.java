package ru.skel2007.warren;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.skel2007.warren.tinkoff.TinkoffProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        TinkoffProperties.class,
})
public class WarrenApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarrenApplication.class, args);
    }

}
