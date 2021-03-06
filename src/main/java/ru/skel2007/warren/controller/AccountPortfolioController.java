package ru.skel2007.warren.controller;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;
import ru.tinkoff.invest.openapi.models.portfolio.PortfolioCurrencies;

@RestController
@RequestMapping(value = "/api/v1/accounts/{broker_account_id}/portfolio", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountPortfolioController {

    @NotNull
    private final OpenApi tinkoffApi;

    @Autowired
    public AccountPortfolioController(@NotNull OpenApi tinkoffApi) {
        this.tinkoffApi = tinkoffApi;
    }

    @GetMapping("positions")
    @NotNull
    public Flux<Portfolio.PortfolioPosition> getPortfolioPositions(
            @PathVariable("broker_account_id") @NotNull String brokerAccountId
    ) {
        CompletableFuture<Portfolio> portfolio = tinkoffApi
                .getPortfolioContext()
                .getPortfolio(brokerAccountId);

        return Mono.fromFuture(portfolio)
                .map(it -> it.positions)
                .flatMapMany(Flux::fromIterable);
    }

    @GetMapping("currencies")
    @NotNull
    public Flux<PortfolioCurrencies.PortfolioCurrency> getPortfolioCurrencies(
            @PathVariable("broker_account_id") @NotNull String brokerAccountId
    ) {
        CompletableFuture<PortfolioCurrencies> currencies = tinkoffApi
                .getPortfolioContext()
                .getPortfolioCurrencies(brokerAccountId);

        return Mono.fromFuture(currencies)
                .map(it -> it.currencies)
                .flatMapMany(Flux::fromIterable);
    }


}
