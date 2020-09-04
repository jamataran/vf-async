
package es.autowired;

import java.util.ArrayList;
import java.util.List;

import es.autowired.async.AsyncExecutor;
import es.autowired.async.LegacyJavaAsyncExecutor;
import es.autowired.common.CommonHelper;
import es.autowired.service.Service;
import es.autowired.service.ServiceImpl;

public class Main {

    public static void main(String[] args) throws NoSuchMethodException {
        CommonHelper.log("Inicio proceso PRINCIPAL", null);

        AsyncExecutor asyncExecutor = LegacyJavaAsyncExecutor.getInstance();
        Service service = new ServiceImpl();
        List<Object> paramTypes = new ArrayList<Object>();
        paramTypes.add(int.class);
        for (int i = 0; i <= 5; i++) {
            final int paramInteger = i * 10000;
            asyncExecutor.executeAsync(Thread.currentThread(),
                    service,
                    ServiceImpl.class.getMethod("retard", int.class),
                    paramInteger);
        }

        CommonHelper.log("Fin del Proceso Principal", null);

    }

}