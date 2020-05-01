package ru.skel2007.warren.tinkoff;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import ru.tinkoff.invest.openapi.models.Currency;

@ConfigurationProperties("tinkoff")
@ConstructorBinding
@Data
public class TinkoffProperties {

    @NotNull
    private final ApiProperties api;

    @Data
    public static class ApiProperties {

        @NotNull
        private final String token;
        @NotNull
        private final SandboxProperties sandbox;

        @Data
        public static class SandboxProperties {

            private final boolean enabled;
            @NotNull
            private final String token;
            @NotNull
            private final Map<Currency, BigDecimal> balance;

        }

    }

}
