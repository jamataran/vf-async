package es.autowired.service;

import static es.autowired.common.CommonHelper.log;

import java.util.Random;
import java.util.UUID;

import es.autowired.async.AsyncExecutor;
import es.autowired.async.LegacyJavaAsyncExecutor;

public class ServiceImpl implements Service {

    public String retard(int retard) {

        try {
            log("INVOCACION (Comienza parada) => retard(" + retard + ")", this.getClass());
            Thread.sleep(retard);

            Service nextInstance = new ServiceImpl(); // xbsubirDocumentum
            AsyncExecutor asyncExecutor = LegacyJavaAsyncExecutor.getInstance();

            try {
                asyncExecutor.executeAsync(Thread.currentThread(), nextInstance, ServiceImpl.class.getMethod("retard2", int.class), retard);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                public void run() {
                    log("HOLA", ServiceImpl.this.getClass());
                }
            }).start();

            log("FIN (Fin parada) => retard(" + retard + ")", this.getClass());
        } catch (InterruptedException e) {
            System.err.println("Error\t" + e.getMessage());
        }
        return "retard\t" + UUID.randomUUID().toString();
    }

    public String retard2(int retard) {
        try {
            Thread.sleep(retard);
        } catch (InterruptedException e) {
            System.err.println("Error\t" + e.getMessage());
        }
        return "retard2" + UUID.randomUUID().toString();
    }

    public static String retard3(int retard) {
        try {
            Thread.sleep(retard);
        } catch (InterruptedException e) {
            System.err.println("Error\t" + e.getMessage());
        }
        return "retard3" + UUID.randomUUID().toString();
    }

    @Override
    public void run() {
        log("run()", this.getClass());

        log("Parando el hilo", this.getClass());
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final boolean ejecucionCorrecta = new Random().nextBoolean();
        log("Ejecución correcta " + ejecucionCorrecta, this.getClass());

        if (!ejecucionCorrecta) {
            log("Ejecutando error...", this.getClass());
            throw new RuntimeException("💣💣... Ejecución excepcionada");
        }


    }
}
