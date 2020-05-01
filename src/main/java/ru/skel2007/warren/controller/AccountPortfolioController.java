package ru.skel2007.warren.controller;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skel2007.warren.tinkoff.api.TinkoffApiService;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;
import ru.tinkoff.invest.openapi.models.portfolio.PortfolioCurrencies;

@RestController
@RequestMapping(value = "/api/v1/accounts/{broker_account_id}/portfolio", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountPortfolioController {

    @NotNull
    private final TinkoffApiService tinkoffApiService;

    @Autowired
    public AccountPortfolioController(@NotNull TinkoffApiService tinkoffApiService) {
        this.tinkoffApiService = tinkoffApiService;
    }

    @GetMapping("positions")
    @NotNull
    public List<Portfolio.PortfolioPosition> getPortfolioPositions(
            @PathVariable("broker_account_id") @NotNull String brokerAccountId
    ) {
        return tinkoffApiService
                .getPortfolioContext()
                .getPortfolio(brokerAccountId)
                .join()
                .positions;
    }

    @GetMapping("currencies")
    @NotNull
    public List<PortfolioCurrencies.PortfolioCurrency> getPortfolioCurrencies(
            @PathVariable("broker_account_id") @NotNull String brokerAccountId
    ) {
        return tinkoffApiService
                .getPortfolioContext()
                .getPortfolioCurrencies(brokerAccountId)
                .join()
                .currencies;
    }


}
