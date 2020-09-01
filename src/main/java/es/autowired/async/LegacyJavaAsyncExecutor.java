package es.autowired.async;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import es.autowired.common.CommonHelper;

public class LegacyJavaAsyncExecutor implements AsyncExecutor {

    private final Set<Thread> threads;

    public LegacyJavaAsyncExecutor() {
        threads = new HashSet<>();
    }

    @Override
    public void executeAync(Thread parentThread, Object o, Method method, Object... params) {
        CommonHelper.log("[START] (" + parentThread + ", " + method + ", " + params + ")", this.getClass());
        new Thread(() -> {
            try {
                method.invoke(o, params);
            } catch (IllegalAccessException e) {
                CommonHelper.log("ERROR\t" + e.getMessage(), this.getClass());
            } catch (InvocationTargetException e) {
                CommonHelper.log("ERROR\t" + e.getMessage(), this.getClass());
            }
        }).start();

        CommonHelper.log("[END] (" + parentThread + ", " + method + ", " + params + ")", this.getClass());
    }

    @Override
    public void executeAync(Thread parentThread, Method method, Object... params) {
        this.executeAync(parentThread,this,method,params);
    }
}
