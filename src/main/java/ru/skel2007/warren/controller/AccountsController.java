package ru.skel2007.warren.controller;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skel2007.warren.tinkoff.api.TinkoffApiService;
import ru.tinkoff.invest.openapi.models.user.BrokerAccount;

@RestController
@RequestMapping(value = "/api/v1/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountsController {

    @NotNull
    private final TinkoffApiService tinkoffApiService;

    @Autowired
    public AccountsController(@NotNull TinkoffApiService tinkoffApiService) {
        this.tinkoffApiService = tinkoffApiService;
    }

    @GetMapping
    @NotNull
    public List<BrokerAccount> getAccounts() {
        return tinkoffApiService
                .getUserContext()
                .getAccounts()
                .join()
                .accounts;
    }

}
