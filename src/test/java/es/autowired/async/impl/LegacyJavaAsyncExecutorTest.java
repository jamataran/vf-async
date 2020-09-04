package es.autowired.async.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import es.autowired.async.AsyncExecutor;
import es.autowired.async.LegacyJavaAsyncExecutor;

@SuppressWarnings("DefaultAnnotationParam")
public class LegacyJavaAsyncExecutorTest {

    private static final int EXAMPLE_INT = 1;
    private static final String STATIC_METHOD = "valueOf";
    private static final String EXAMPLE_STRING = "cadena";
    private static final String GET_LENGTH_METHOD = "getLength";
    private static final String STRING_CLASS = "java.lang.String";

    private final AsyncExecutor asyncExecutor = LegacyJavaAsyncExecutor.getInstance();

    @Test(expected = Test.None.class)
    public void executeAsyncNonStatic() throws NoSuchMethodException {
        Method getLength = LegacyJavaAsyncExecutorTest.class.getMethod(GET_LENGTH_METHOD, String.class);
        asyncExecutor.executeAsync(Thread.currentThread(), this, getLength, EXAMPLE_STRING);

    }

    @Test(expected = Test.None.class)
    public void executeAsyncStatic() throws NoSuchMethodException {
        Method trimWhitespace = String.class.getMethod(STATIC_METHOD, int.class);
        asyncExecutor.executeAsyncStatic(trimWhitespace, EXAMPLE_INT);
    }

    @Test(expected = Test.None.class)
    public void executeAsyncStaticWithMethodName() {
        List<Class> paramTypes = new ArrayList<Class>();
        paramTypes.add(int.class);

        asyncExecutor.executeAsyncStatic(STRING_CLASS, STATIC_METHOD, paramTypes, EXAMPLE_INT);
    }

    @Test(expected = Test.None.class)
    public void executeAsyncNonStaticWithoutObject() throws NoSuchMethodException {
        asyncExecutor.executeAsync(Thread.currentThread(), String.class.getMethod("getBytes"), null);
    }

    public String getLength(String cadena) {
        return cadena.concat(" - ").concat(String.valueOf(cadena.length()));
    }
}