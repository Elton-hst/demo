package io.github.eltonhst.infra.schedule;

import io.github.eltonhst.domain.useCase.client.ClientCreateUseCase;
import io.github.eltonhst.domain.useCase.client.ClientSearchUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

@Component
public class ActiveClientsSchedule extends AbstractScheduleRun {

    private final ClientSearchUseCase searchClient;

    public ActiveClientsSchedule(ClientSearchUseCase searchClient) {
        this.searchClient = searchClient;
    }

    @Scheduled(cron = "0 */5 * * * *")
    @Override
    protected void configureTimeToRun() throws InterruptedException {
        run();
    }

    @Override
    protected void call() throws UnknownHostException, InterruptedException {
        searchClient.logActiveClients();
    }

    @Override
    protected String getScheduleName() {
        return "Verificando a quantidade de clientes cadastrados";
    }
}
