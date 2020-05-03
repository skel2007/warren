package ru.skel2007.warren.tinkoff.api;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skel2007.warren.tinkoff.TinkoffProperties;
import ru.tinkoff.invest.openapi.OpenApi;

@Configuration
public class TinkoffApiConfiguration {

    @Bean
    public OpenApi tinkoffApi(@NotNull TinkoffProperties properties) {
        return TinkoffApiHelper.create(properties.getApi());
    }

}
