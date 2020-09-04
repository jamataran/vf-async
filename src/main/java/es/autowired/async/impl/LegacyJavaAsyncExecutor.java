package es.autowired.async.impl;

import static es.autowired.common.CommonHelper.log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import es.autowired.async.AsyncExecutor;
import es.autowired.provider.arch.exception.LegacyJavaAsyncException;

/**
 * Executes {@link Method} asynchronously
 *
 * @author José Antonio Matarán <jamataran@gmail.com>
 */
@SuppressWarnings("Convert2Lambda")
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
     * Executes a {@link Method} of an instance of an {@link Object} asynchronously
     *
     * @param parentThread {@link Thread} that request execution
     * @param o            Instance of method to execute
     * @param method       {@link Method} to execute
     * @param params       {@link Method#parameters}
     */
    @Override
    public void executeAsyncNonStatic(Thread parentThread, Object o, Method method, Object... params) {
        log("[START] (" + parentThread + ", " + method + ", " + params + ")", this.getClass());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    log("New thread created " + Thread.currentThread() + ". Invoking method", LegacyJavaAsyncExecutor.this.getClass());
                    method.invoke(o, params);
                    log("Invocation finished. Closing thread " + Thread.currentThread() + ".", LegacyJavaAsyncExecutor.this.getClass());
                } catch (IllegalAccessException e) {
                    throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());
                } catch (InvocationTargetException e) {
                    throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());
                }
            }
        }).start();

        log("[END] (" + parentThread + ", " + method + ", " + params + ")", this.getClass());
    }


    /**
     * Executes method (parameter 'method') of the class 'clase' with params asynchronously
     *
     * @param method - Method to execute
     * @param params - Method parameters
     */
    @Override
    public void executeAsyncStatic(Method method, Object... params) {
        log("[START] (" + method + ", " + params + ")", this.getClass());
        final Thread parentThread = Thread.currentThread();
        new Thread(new Runnable() {
            @Override
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
     * Executes method (parameter 'method') of the class 'clase' with params asynchronously
     *
     * @param clazz      - Class of the method to execute
     * @param methodName - Method to execute
     * @param params     - Method parameters
     */
    @Override
    public void executeAsyncStaticWithMethodName(String clazz, String methodName, List<Object> paramClass, Object... params) {
        log("[START] (" + clazz + ", " + methodName + ", " + paramClass + ", " + params + ")", this.getClass());
        final Thread parentThread = Thread.currentThread();
        new Thread(new Runnable() {
            @Override
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

    /**
     * Executes a {@link Method} asynchronously (without previously instance)
     *
     * @param parentThread {@link Thread} that request execution
     * @param method       {@link Method} to execute
     * @param params       {@link Method#parameters}
     */
    @Override
    public void executeAsyncNonStatic(Thread parentThread, Method method, Object... params) {
        final Class<?> declaringClass = method.getDeclaringClass();
        try {
            final Object instance = declaringClass.getDeclaredConstructor().newInstance();
            this.executeAsyncNonStatic(parentThread, instance, method, params);
        } catch (InstantiationException e) {
            throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());
        } catch (IllegalAccessException e) {
            throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());
        } catch (InvocationTargetException e) {
            throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());
        } catch (NoSuchMethodException e) {
            throw new LegacyJavaAsyncException(e, parentThread, Thread.currentThread());
        }
    }
}
