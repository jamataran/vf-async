package es.autowired.service;


import static es.autowired.common.CommonHelper.log;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import es.autowired.async.AsyncExecutor;
import es.autowired.async.impl.LegacyJavaAsyncExecutor;

/**
 * Simulates business logic.
 */
public class ServiceImpl implements Service {

    public static final int RETARDO_FTP = 12000;
    public static final int RETARDO_SOAP = 1200;
    private final AsyncExecutor asyncExecutor;
    public static final int NUMERO_CONTRATOS = 5;

    public ServiceImpl(AsyncExecutor asyncExecutor) {
        this.asyncExecutor = asyncExecutor;
    }

    @Override
    public void sendFTP(String fileName) {
        log("Enviando " + fileName + " por FTP", this.getClass());
        try {
            Thread.sleep(RETARDO_FTP);
            log(fileName + " enviado. Comunicando por WS...", this.getClass());
            try {
                this.asyncExecutor.executeAsyncNonStatic(Thread.currentThread(), this, ServiceImpl.class.getMethod("sendFTP", String.class), fileName);
            } catch (NoSuchMethodException e) {
                logException(e);
            }
            log(fileName + " FTP Procesado correctamente (Enviado y comunicado)", this.getClass());


        } catch (InterruptedException e) {
            logException(e);
        }

    }

    @Override
    public void callSOAPService(String url) {
        log("Llamando a " + url + "[SOAP]", this.getClass());
        try {
            Thread.sleep(RETARDO_SOAP);
            log(url + "[SOAP COMPLETED]", this.getClass());

        } catch (InterruptedException e) {
            logException(e);
        }
    }

    @Override
    public void mainLogic() {
        log("Iniciando lógica principal....", this.getClass());
        log("Gerando contratos....", this.getClass());
        List<String> contratosImprimir = new LinkedList<>();
        for (int i = 0; i < NUMERO_CONTRATOS; i++) {
            final String contratoGenerado = "CONT_2020".concat(String.format("%06d", i)).concat(".pdf");
            contratosImprimir.add(contratoGenerado);
        }
        log("Contratos generados!", this.getClass());
        log("El resto de opciones se lanzarán de forma asíncrona", this.getClass());
        log("Realizando peticiones FTP...", this.getClass());
        for (String contrato : contratosImprimir) {
            try {
                this.asyncExecutor.executeAsyncNonStatic(Thread.currentThread(), this, ServiceImpl.class.getMethod("sendFTP", String.class), contrato);
            } catch (NoSuchMethodException e) {
                logException(e);
            }
        }
        log("Peticiones FTP lanzadas...", this.getClass());
        log("Realizando llamadas SOAP...", this.getClass());
        for (String contrato : contratosImprimir) {
            try {
                this.asyncExecutor.executeAsyncNonStatic(Thread.currentThread(), this, ServiceImpl.class.getMethod("callSOAPService", String.class), "https://ws-soap/upload/".concat(contrato));
            } catch (NoSuchMethodException e) {
                logException(e);
            }
        }
        log("Fin de las llamadas SOAP...", this.getClass());
        log("\uD83C\uDF89 Fin del proceso. Hilo principal disponible de nuevo!", this.getClass());


    }

    private void logException(Exception e) {
        log("ERROR\t" + e.getMessage(), this.getClass());
    }
}
