
package es.autowired;

import java.util.ArrayList;
import java.util.List;

import es.autowired.async.AsyncExecutor;
import es.autowired.async.LegacyJavaAsyncExecutor;
import es.autowired.async.exception.AsyncExecutorException;
import es.autowired.common.CommonHelper;
import es.autowired.service.Service;
import es.autowired.service.ServiceImpl;

public class Main {

    private static final AsyncExecutor asyncExecutor = LegacyJavaAsyncExecutor.getInstance();

    public static void main(String[] args) throws NoSuchMethodException {

        CommonHelper.log("Inicio proceso PRINCIPAL", null);

        Service service = new ServiceImpl();

        try {
            asyncExecutor.executeAsync(5,Thread.currentThread(), service, ServiceImpl.class.getMethod("run"));
        } catch (AsyncExecutorException asyncExecutorException) {
            CommonHelper.log("Control excepcion en ejecucion asincrona", null);
            asyncExecutor.executeAsync(5, Thread.currentThread(), service, ServiceImpl.class.getMethod("retard", int.class), 12);
        }

        CommonHelper.log("fin proceso PRINCIPAL", null);

    }

}