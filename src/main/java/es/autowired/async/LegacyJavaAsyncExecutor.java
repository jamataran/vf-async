package es.autowired.async;

import static es.autowired.common.CommonHelper.log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import es.autowired.async.exception.AsyncExecutorException;
import es.autowired.common.CommonHelper;

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
        CommonHelper.log("[START] (" + parentThread + ", " + method + ", " + Arrays.toString(params) + ")", this.getClass());
        new Thread(new Runnable() {
            public void run() {
                try {
                    method.invoke(o, params);

                } catch (IllegalAccessException e) {
                    throw new AsyncExecutorException(e, parentThread, Thread.currentThread());

                } catch (InvocationTargetException e) {
                    handleThreadExcpetion(parentThread, e);

                } catch (RuntimeException e) {
                    handleThreadExcpetion(parentThread, e);
                }
            }
        }).start();

        CommonHelper.log("[END] (" + parentThread + ", " + method + ", " + Arrays.toString(params) + ")", this.getClass());
    }


    /**
     * Execute asynchronously the method received by parameter
     *
     * @param parentThread Parent thread
     * @param method       Method to execute
     * @param params       Params to execute the method
     */
    public void executeAsync(Thread parentThread, Method method, Object... params) throws NoSuchMethodException {
        final Class<?> declaringClass = method.getDeclaringClass();
        try {
            final Object instance = declaringClass.getDeclaredConstructor().newInstance();
            this.executeAsync(parentThread, instance, method, params);

        } catch (InstantiationException e) {
            handleThreadExcpetion(parentThread, e);

        } catch (IllegalAccessException e) {
            handleThreadExcpetion(parentThread, e);

        } catch (InvocationTargetException e) {
            handleThreadExcpetion(parentThread, e);
        } catch (RuntimeException e) {
            handleThreadExcpetion(parentThread, e);
        }

    }

    /**
     * Execute asynchronously the method received by parameter
     *
     * @param method Method to execute
     * @param params Params to execute the method
     */
    public void executeAsyncStatic(final Method method, final Object... params) {
        log("[START] (" + method + ", " + Arrays.toString(params) + ")", this.getClass());
        final Thread parentThread = Thread.currentThread();
        new Thread(new Runnable() {
            public void run() {
                try {
                    method.invoke(null, params);
                } catch (IllegalAccessException e) {
                    throw new AsyncExecutorException(e, parentThread, Thread.currentThread());
                } catch (InvocationTargetException e) {
                    handleThreadExcpetion(parentThread, e);
                } catch (RuntimeException e) {
                    handleThreadExcpetion(parentThread, e);
                }

            }
        }).start();

        log("[END] (" + method + ", " + Arrays.toString(params) + ")", this.getClass());
    }

    /**
     * Execute asynchronously the method received by parameter
     *
     * @param clazz      Classname of the method
     * @param methodName Name of the method to execute
     * @param paramClass Params type of the method
     * @param params     Params to execute the method
     */
    public void executeAsyncStatic(final String clazz, final String methodName, @SuppressWarnings("rawtypes") final List<Class> paramClass, final Object... params) {
        log("[START] (" + clazz + ", " + methodName + ", " + paramClass + ", " + params + ")", this.getClass());
        final Thread parentThread = Thread.currentThread();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Method method = Class.forName(clazz).getMethod(methodName, paramClass.toArray(new Class<?>[paramClass.size()]));
                    method.invoke(null, params);

                } catch (NoSuchMethodException e) {
                    throw new AsyncExecutorException(e, parentThread, Thread.currentThread());

                } catch (IllegalAccessException e) {
                    throw new AsyncExecutorException(e, parentThread, Thread.currentThread());

                } catch (InvocationTargetException e) {
                    handleThreadExcpetion(parentThread, e);

                } catch (RuntimeException e) {
                    handleThreadExcpetion(parentThread, e);

                } catch (ClassNotFoundException e) {
                    throw new AsyncExecutorException(e, parentThread, Thread.currentThread());
                }

            }
        }).start();

        log("[END] (" + clazz + methodName + ", " + paramClass + ", " + Arrays.toString(params) + ")", this.getClass());
    }

    /**
     * Execute asynchronously the method received by parameter
     *
     * @param parentThread Parent thread
     * @param o            Instance of method class
     * @param method       Method to execute
     * @param params       Params to execute the method
     */
    public void executeAsync(final Integer retriesNumber, final Thread parentThread, final Object o, final Method method, final Object... params) {
        CommonHelper.log("[START] (" + parentThread + ", " + method + ", " + Arrays.toString(params) + ")", this.getClass());
        new Thread(new Runnable() {
            public void run() {
                try {

                    int retry = 0;
                    while(true){
                        try {
                            method.invoke(o, params);
                            break;
                        } catch (Exception ge) {
                            if (++retry == retriesNumber) throw ge;
                        }
                    }

                } catch (IllegalAccessException e) {
                    throw new AsyncExecutorException(e, parentThread, Thread.currentThread());

                } catch (InvocationTargetException e) {
                    handleThreadExcpetion(parentThread, e);
                    return;
                } catch (Exception e) {
                    handleThreadExcpetion(parentThread, e);
                }
            }
        }).start();

        CommonHelper.log("[END] (" + parentThread + ", " + method + ", " + Arrays.toString(params) + ")", this.getClass());
    }


    /**
     * Execute asynchronously the method received by parameter
     *
     * @param parentThread Parent thread
     * @param method       Method to execute
     * @param params       Params to execute the method
     */
    public void executeAsync(final Integer retriesNumber, Thread parentThread, Method method, Object... params) throws NoSuchMethodException {
        final Class<?> declaringClass = method.getDeclaringClass();
        try {
            final Object instance = declaringClass.getDeclaredConstructor().newInstance();
            this.executeAsync(retriesNumber, parentThread, instance, method, params);

        } catch (InstantiationException e) {
            handleThreadExcpetion(parentThread, e);

        } catch (IllegalAccessException e) {
            handleThreadExcpetion(parentThread, e);

        } catch (InvocationTargetException e) {
            handleThreadExcpetion(parentThread, e);
        } catch (RuntimeException e) {
            handleThreadExcpetion(parentThread, e);
        }

    }

    /**
     * Execute asynchronously the method received by parameter
     *
     * @param method Method to execute
     * @param params Params to execute the method
     */
    public void executeAsyncStatic(final Integer retriesNumber, final Method method, final Object... params) {
        log("[START] (" + method + ", " + Arrays.toString(params) + ")", this.getClass());
        final Thread parentThread = Thread.currentThread();
        new Thread(new Runnable() {
            public void run() {
                try {

                    int retry = 0;
                    while(true){
                        try {
                            method.invoke(null, params);
                            break;
                        } catch (Exception ge) {
                            if (++retry == retriesNumber) throw ge;
                        }
                    }

                } catch (IllegalAccessException e) {
                    throw new AsyncExecutorException(e, parentThread, Thread.currentThread());
                } catch (InvocationTargetException e) {
                    handleThreadExcpetion(parentThread, e);
                } catch (RuntimeException e) {
                    handleThreadExcpetion(parentThread, e);
                } catch (Exception e) {
                    handleThreadExcpetion(parentThread, e);
                }

            }
        }).start();

        log("[END] (" + method + ", " + Arrays.toString(params) + ")", this.getClass());
    }

    /**
     * Execute asynchronously the method received by parameter
     *
     * @param clazz      Classname of the method
     * @param methodName Name of the method to execute
     * @param paramClass Params type of the method
     * @param params     Params to execute the method
     */
    public void executeAsyncStatic(final Integer retriesNumber, final String clazz, final String methodName, @SuppressWarnings("rawtypes") final List<Class> paramClass, final Object... params) {
        log("[START] (" + clazz + ", " + methodName + ", " + paramClass + ", " + params + ")", this.getClass());
        final Thread parentThread = Thread.currentThread();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Method method = Class.forName(clazz).getMethod(methodName, paramClass.toArray(new Class<?>[paramClass.size()]));

                    int retry = 0;
                    while(true){
                        try {
                            method.invoke(null, params);
                            break;
                        } catch (Exception ge) {
                            if (++retry == retriesNumber) throw ge;
                        }
                    }

                } catch (NoSuchMethodException e) {
                    throw new AsyncExecutorException(e, parentThread, Thread.currentThread());

                } catch (IllegalAccessException e) {
                    throw new AsyncExecutorException(e, parentThread, Thread.currentThread());

                } catch (InvocationTargetException e) {
                    handleThreadExcpetion(parentThread, e);

                } catch (RuntimeException e) {
                    handleThreadExcpetion(parentThread, e);

                } catch (ClassNotFoundException e) {
                    throw new AsyncExecutorException(e, parentThread, Thread.currentThread());
                } catch (Exception e) {
                    handleThreadExcpetion(parentThread, e);
                }

            }
        }).start();

        log("[END] (" + clazz + methodName + ", " + paramClass + ", " + Arrays.toString(params) + ")", this.getClass());
    }


    /**
     * Handles common Thread exceptions
     *
     * @param parentThread Parent thread
     * @param e            Exepcion
     */
    private void handleThreadExcpetion(Thread parentThread, Exception e) {
        throw new AsyncExecutorException(e, parentThread, Thread.currentThread());
    }
}
