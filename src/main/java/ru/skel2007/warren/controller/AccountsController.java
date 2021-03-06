package ru.skel2007.warren.controller;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.models.user.AccountsList;
import ru.tinkoff.invest.openapi.models.user.BrokerAccount;

@RestController
@RequestMapping(value = "/api/v1/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountsController {

    @NotNull
    private final OpenApi tinkoffApi;

    @Autowired
    public AccountsController(@NotNull OpenApi tinkoffApi) {
        this.tinkoffApi = tinkoffApi;
    }

    @GetMapping
    @NotNull
    public Flux<BrokerAccount> getAccounts() {
        CompletableFuture<AccountsList> accounts = tinkoffApi
                .getUserContext()
                .getAccounts();

        return Mono.fromFuture(accounts)
                .map(it -> it.accounts)
                .flatMapMany(Flux::fromIterable);
    }

}
