package es.autowired;

import es.autowired.async.AsyncExecutor;
import es.autowired.async.impl.LegacyJavaAsyncExecutor;
import es.autowired.service.Service;
import es.autowired.service.ServiceImpl;

public class Main {


    public static final int NUMERO_EJECUCIONES = 5;

    public static void main(String[] args) {

        // Example
        AsyncExecutor asyncExecutor = LegacyJavaAsyncExecutor.getInstance();
        Service service = new ServiceImpl(asyncExecutor);

        for (int i = 0; i < NUMERO_EJECUCIONES; i++) {
            service.mainLogic(3);
        }

    }

}