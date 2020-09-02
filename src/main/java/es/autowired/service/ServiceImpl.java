package es.autowired.service;

import java.util.Optional;
import java.util.UUID;

import es.autowired.async.AsyncExecutor;
import es.autowired.async.LegacyJavaAsyncExecutor;
import es.autowired.common.CommonHelper;

public class ServiceImpl implements Service {
    @Override
    public Optional<String> retard(int retard) {

        try {
            CommonHelper.log("INVOCACION (Comienza parada) => retard(" + retard + ")", this.getClass());
            Thread.sleep(retard);
            Service nextInstance = new ServiceImpl();
            AsyncExecutor asyncExecutor = new LegacyJavaAsyncExecutor();
            try {
                asyncExecutor.executeAync(Thread.currentThread(), nextInstance, ServiceImpl.class.getMethod("retard2", int.class), retard);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            CommonHelper.log("FIN (Fin parada) => retard(" + retard + ")", this.getClass());
        } catch (InterruptedException e) {
            System.err.println("Error\t" + e.getMessage());
        }
        return Optional.of("retard\t" + UUID.randomUUID().toString());
    }

    @Override
    public Optional<String> retard2(int retard) {
        try {
            Thread.sleep(retard);
        } catch (InterruptedException e) {
            System.err.println("Error\t" + e.getMessage());
        }
        return Optional.of("retard2" + UUID.randomUUID().toString());
    }
}
