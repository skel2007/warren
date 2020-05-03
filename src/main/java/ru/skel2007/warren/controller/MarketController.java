package ru.skel2007.warren.controller;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.models.market.Candle;
import ru.tinkoff.invest.openapi.models.market.CandleInterval;
import ru.tinkoff.invest.openapi.models.market.HistoricalCandles;
import ru.tinkoff.invest.openapi.models.market.Orderbook;

@RestController
@RequestMapping(value = "/api/v1/market", produces = MediaType.APPLICATION_JSON_VALUE)
public class MarketController {

    @NotNull
    private final OpenApi tinkoffApi;

    @Autowired
    public MarketController(@NotNull OpenApi tinkoffApi) {
        this.tinkoffApi = tinkoffApi;
    }

    @GetMapping("{figi}/candles")
    @NotNull
    public Flux<Candle> getCandles(
            @PathVariable("figi") @NotNull String figi,
            @RequestParam("interval") @NotNull CandleInterval interval
    ) {
        var to = OffsetDateTime.now();
        var from = to.minus(Duration.ofHours(1));

        CompletableFuture<Optional<HistoricalCandles>> candles = tinkoffApi
                .getMarketContext()
                .getMarketCandles(figi, from, to, interval);

        return Mono.fromFuture(candles)
                .flatMap(Mono::justOrEmpty)
                .switchIfEmpty(Mono.error(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(it -> it.candles)
                .flatMapMany(Flux::fromIterable);
    }

    @GetMapping("{figi}/orderbook")
    @NotNull
    public Mono<Orderbook> getOrderbook(
            @PathVariable("figi") @NotNull String figi
    ) {
        CompletableFuture<Optional<Orderbook>> orderbook = tinkoffApi
                .getMarketContext()
                .getMarketOrderbook(figi, 1);

        return Mono.fromFuture(orderbook)
                .flatMap(Mono::justOrEmpty)
                .switchIfEmpty(Mono.error(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

}
