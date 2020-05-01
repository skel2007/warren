package ru.skel2007.warren.tinkoff.api;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.Executors;

import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import ru.skel2007.warren.tinkoff.TinkoffProperties;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.SandboxOpenApi;
import ru.tinkoff.invest.openapi.models.Currency;
import ru.tinkoff.invest.openapi.models.sandbox.CurrencyBalance;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApiFactory;

@Log
public class TinkoffApiHelper {

    @NotNull
    public static OpenApi create(@NotNull TinkoffProperties.ApiProperties properties) {
        if (properties.getSandbox().isEnabled()) {
            var factory = new OkHttpOpenApiFactory(properties.getSandbox().getToken(), log);
            var api = factory.createSandboxOpenApiClient(Executors.newSingleThreadExecutor());

            initSandboxApi(api, properties.getSandbox().getBalance());

            return api;
        }

        var factory = new OkHttpOpenApiFactory(properties.getToken(), log);
        return factory.createOpenApiClient(Executors.newSingleThreadExecutor());
    }

    private static void initSandboxApi(@NotNull SandboxOpenApi api, @NotNull Map<Currency, BigDecimal> balance) {
        api.getSandboxContext().performRegistration(null).join();

        api.getUserContext()
                .getAccounts()
                .join()
                .accounts
                .forEach(account -> balance
                        .forEach((currency, amount) -> api.getSandboxContext()
                                .setCurrencyBalance(
                                        new CurrencyBalance(currency, amount),
                                        account.brokerAccountId
                                )
                        )
                );
    }

}
