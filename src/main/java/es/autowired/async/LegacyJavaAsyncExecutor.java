package es.autowired.async;

import static es.autowired.common.CommonHelper.log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.autowired.common.CommonHelper;
import es.autowired.async.exception.LegacyJavaAsyncException;

/**
 * Old-fashioned java version compatible implementation of {@link AsyncExecutor}
 */
public class LegacyJavaAsyncExecutor implements AsyncExecutor {

    private static volatile LegacyJavaAsyncExecutor instance;

    /**
     * Instantiation is forbidden
     */
    private LegacyJavaAsyncExecutor() {
    }

    /**
     * Gets an unique instance of {@link LegacyJavaAsyncExecutor} (singleton)
     *
     * @return Instance
     */
    public static LegacyJavaAsyncExecutor getInstance() {
        if (instance == null) {
            synchronized (LegacyJavaAsyncExecutor.class) {
                if (instance == null)
                    instance = new LegacyJavaAsyncExecutor();
            }
        }
        return instance;
    }

    /**
     * Execute asynchronously the method received by parameter
     *
     * @param parentThread Parent thread
     * @param o            Instance of method class
     * @param method       Method to execute
     * @param params       Params to execute the method
     */
    public void executeAsync(final Thread parentThread, final Object o, final Method method, final Object... params) {
        CommonHelper.log("[START] (" + parentThread + ", " + method + ", " + params + ")", this.getClass());
        new Thread(new Runnable() {
            public void run() {
                try {
                    method.invoke(o, params);

                } catch (IllegalAccessException e) {
                    throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());

                } catch (InvocationTargetException e) {
                    throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());
                }
            }
        }).start();

        CommonHelper.log("[END] (" + parentThread + ", " + method + ", " + params + ")", this.getClass());
    }

    /**
     * Execute asynchronously the method received by parameter
     *
     * @param parentThread Parent thread
     * @param method       Method to execute
     * @param params       Params to execute the method
     */
    public void executeAsync(Thread parentThread, Method method, Object... params) {
        this.executeAsync(parentThread, this, method, params);
    }

    /**
     * Execute asynchronously the method received by parameter
     *
     * @param method Method to execute
     * @param params Params to execute the method
     */
    public void executeAsyncStatic(final Method method, final Object... params) {
        log("[START] (" + method + ", " + params + ")", this.getClass());
        final Thread parentThread = Thread.currentThread();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Object invoke = method.invoke(null, params);
                    log("[RESULT] (" + invoke + ")", this.getClass());
                } catch (IllegalAccessException e) {
                    throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());
                } catch (InvocationTargetException e) {
                    throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());
                }
            }
        }).start();

        log("[END] (" + method + ", " + params + ")", this.getClass());
    }

    /**
     * Execute asynchronously the method received by parameter
     *
     * @param clazz      Classname of the method
     * @param methodName Name of the method to execute
     * @param paramClass Params type of the method
     * @param params     Params to execute the method
     */
    public void executeAsyncStatic(final String clazz, final String methodName, final List<Class> paramClass, final Object... params) {
        log("[START] (" + clazz + ", " + methodName + ", " + paramClass + ", " + params + ")", this.getClass());
        final Thread parentThread = Thread.currentThread();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Method method = Class.forName(clazz).getMethod(methodName, paramClass.toArray(new Class<?>[paramClass.size()]));
                    Object invoke = method.invoke(null, params);
                    log("[RESULT] (" + invoke + ")", this.getClass());

                } catch (NoSuchMethodException e) {
                    throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());

                } catch (IllegalAccessException e) {
                    throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());

                } catch (InvocationTargetException e) {
                    throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());

                } catch (ClassNotFoundException e) {
                    throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());
                }
            }
        }).start();

        log("[END] (" + clazz + methodName + ", " + paramClass + ", " + params + ")", this.getClass());
    }
}
