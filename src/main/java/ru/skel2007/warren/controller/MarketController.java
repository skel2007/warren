package ru.skel2007.warren.controller;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.skel2007.warren.tinkoff.api.TinkoffApiService;
import ru.tinkoff.invest.openapi.models.market.Candle;
import ru.tinkoff.invest.openapi.models.market.CandleInterval;
import ru.tinkoff.invest.openapi.models.market.Orderbook;

@RestController
@RequestMapping(value = "/api/v1/market", produces = MediaType.APPLICATION_JSON_VALUE)
public class MarketController {

    @NotNull
    private final TinkoffApiService tinkoffApiService;

    @Autowired
    public MarketController(@NotNull TinkoffApiService tinkoffApiService) {
        this.tinkoffApiService = tinkoffApiService;
    }

    @GetMapping("{figi}/candles")
    @NotNull
    public List<Candle> getCandles(
            @PathVariable("figi") @NotNull String figi,
            @RequestParam("interval") @NotNull CandleInterval interval
    ) {
        var to = OffsetDateTime.now();
        var from = to.minus(Duration.ofHours(1));

        return tinkoffApiService
                .getMarketContext()
                .getMarketCandles(figi, from, to, interval)
                .join()
                .map(it -> it.candles)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("{figi}/orderbook")
    @NotNull
    public Orderbook getOrderbook(
            @PathVariable("figi") @NotNull String figi
    ) {
        return tinkoffApiService
                .getMarketContext()
                .getMarketOrderbook(figi, 1)
                .join()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
