package es.autowired.async;

import java.lang.reflect.Method;

public interface AsyncExecutor {

    void executeAync(final Thread parentThread, final Object o, final Method method,final Object... params);

    void executeAync(Thread parentThread, Method method, Object... params);

}