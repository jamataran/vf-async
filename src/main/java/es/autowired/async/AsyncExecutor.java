package es.autowired.async;

import java.lang.reflect.Method;

public interface AsyncExecutor {

    void executeAync(final Thread parentThread, final Object o, final Method method,final Object... params);

    void executeAync(final Thread parentThread, final Method method, final Object... params);

    void executeAsyncStatic(final Method method, final Object... params);

    void executeAsyncStatic(final String clase, final String method, final List<Object> paramClass, final Object... params);


}