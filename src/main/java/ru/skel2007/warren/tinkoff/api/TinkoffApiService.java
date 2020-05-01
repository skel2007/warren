package ru.skel2007.warren.tinkoff.api;

import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skel2007.warren.tinkoff.TinkoffProperties;
import ru.tinkoff.invest.openapi.MarketContext;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.OperationsContext;
import ru.tinkoff.invest.openapi.OrdersContext;
import ru.tinkoff.invest.openapi.PortfolioContext;
import ru.tinkoff.invest.openapi.StreamingContext;
import ru.tinkoff.invest.openapi.UserContext;

@Service
@Slf4j
public class TinkoffApiService implements OpenApi {

    @NotNull
    private final OpenApi api;

    @Autowired
    public TinkoffApiService(
            @NotNull TinkoffProperties properties
    ) {
        this.api = TinkoffApiHelper.create(properties.getApi());
    }

    @NotNull
    @Override
    public MarketContext getMarketContext() {
        return api.getMarketContext();
    }

    @NotNull
    @Override
    public OperationsContext getOperationsContext() {
        return api.getOperationsContext();
    }

    @NotNull
    @Override
    public OrdersContext getOrdersContext() {
        return api.getOrdersContext();
    }

    @NotNull
    @Override
    public PortfolioContext getPortfolioContext() {
        return api.getPortfolioContext();
    }

    @NotNull
    @Override
    public UserContext getUserContext() {
        return api.getUserContext();
    }

    @NotNull
    @Override
    public StreamingContext getStreamingContext() {
        return api.getStreamingContext();
    }

    @Override
    public boolean hasClosed() {
        return api.hasClosed();
    }

    @PreDestroy
    @Override
    public void close() throws Exception {
        log.info("Closing Tinkoff OpenAPI");
        api.close();
    }

}
