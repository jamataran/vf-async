package es.autowired.async;

import java.lang.reflect.Method;

public interface AsyncExecutor {

    void executeAync(Thread parentThread, Object o, Method method, Object... params);

    void executeAync(Thread parentThread, Method method, Object... params);

}