package io.github.eltonhst.infra.schedule;

import java.net.UnknownHostException;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * AbstractScheduleRun
 */
@Component
@EnableScheduling
@Slf4j
public abstract class AbstractScheduleRun {
    /**
     * Configura o tempo para iniciar
     * @throws InterruptedException
     */
    protected abstract void configureTimeToRun() throws InterruptedException;

    /**
     * Método que realiza a chama para a execução do batch
     *
     *
     * @throws UnknownHostException
     * @throws InterruptedException
     */
    protected abstract void call() throws UnknownHostException, InterruptedException;

    /**
     * Obtém o nome da rotina
     *
     * @return
     */
    protected abstract String getScheduleName();

    /**
     * Executa o batch
     * @throws InterruptedException
     */
    protected void run() throws InterruptedException {
        try {
            log.info("Inicio de rotina: {}", getScheduleName());
            call();
        } catch (UnknownHostException e) {
            log.error("Rotina: {}, resultou em erros!", getScheduleName(), e);
        } finally {
            log.info("Fim da rotina: {}", getScheduleName());
        }

    }
}