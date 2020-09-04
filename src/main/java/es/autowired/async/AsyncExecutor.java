package es.autowired.async;

import java.lang.reflect.Method;
import java.util.List;

public interface AsyncExecutor {

<<<<<<< HEAD
    void executeAsyncNonStatic(Thread parentThread, Object o, Method method, Object... params);
=======
    void executeAync(final Thread parentThread, final Object o, final Method method,final Object... params);
>>>>>>> upstream/development

    void executeAsyncStatic(Method method, Object... params);

    void executeAsyncStaticWithMethodName(String clase, String method, List<Object> paramClass, Object... params);

    void executeAsyncNonStatic(Thread parentThread, Method method, Object... params);

}