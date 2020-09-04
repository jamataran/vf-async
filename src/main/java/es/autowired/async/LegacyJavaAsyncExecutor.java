package es.autowired.async;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import es.autowired.common.CommonHelper;

public class LegacyJavaAsyncExecutor implements AsyncExecutor {

    private final Set<Thread> threads;

    public LegacyJavaAsyncExecutor() {
        threads = new HashSet<Thread>();
    }

    public void executeAync(Thread parentThread, final Object o, final Method method, final Object... params) {
        CommonHelper.log("[START] (" + parentThread + ", " + method + ", " + params + ")", this.getClass());
        new Thread(new Runnable() {
            public void run() {
                try {
                    method.invoke(o, params);
                } catch (IllegalAccessException e) {
                    CommonHelper.log("ERROR\t" + e.getMessage(), LegacyJavaAsyncExecutor.this.getClass());
                } catch (InvocationTargetException e) {
                    CommonHelper.log("ERROR\t" + e.getMessage(), LegacyJavaAsyncExecutor.this.getClass());
                }
            }
        }).start();

        CommonHelper.log("[END] (" + parentThread + ", " + method + ", " + params + ")", this.getClass());
    }

    public void executeAync(Thread parentThread, Method method, Object... params) {
        this.executeAync(parentThread,this,method,params);
    }
}
