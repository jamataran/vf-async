package es.autowired.async.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import es.autowired.async.AsyncExecutor;

public class LegacyJavaAsyncExecutorTest {

    private static final int EXAMPLE_INT = 1;
    private static final String STATIC_METHOD = "valueOf";
    private static final String EXAMPLE_STRING = "cadena";
    private static final String GET_LENGTH_METHOD = "getLength";
    private static final String STRING_CLASS = "java.lang.String";

    private final AsyncExecutor asyncExecutor = LegacyJavaAsyncExecutor.getInstance();


    // TODO: comprobar que se ha hecho en otro hilo
    @Test
    public void executeAsyncNonStatic() throws NoSuchMethodException {
        Method getLength = LegacyJavaAsyncExecutorTest.class.getMethod(GET_LENGTH_METHOD, String.class);
        asyncExecutor.executeAsyncNonStatic(Thread.currentThread(), this, getLength, EXAMPLE_STRING);

    }

    // TODO: comprobar que se ha hecho en otro hilo
    @Test
    public void executeAsyncStatic() throws NoSuchMethodException {
        Method trimWhitespace = String.class.getMethod(STATIC_METHOD, int.class);
        asyncExecutor.executeAsyncStatic(trimWhitespace, EXAMPLE_INT);
    }


    // TODO: comprobar que se ha hecho en otro hilo
    @Test
    public void executeAsyncStaticWithMethodName() {
        List<Object> paramTypes = new ArrayList<>();
        paramTypes.add(int.class);

        asyncExecutor.executeAsyncStaticWithMethodName(STRING_CLASS, STATIC_METHOD, paramTypes, EXAMPLE_INT);
    }

    // TODO: comprobar que se ha hecho en otro hilo
    @Test
    public void executeAsyncNonStaticWithoutObject() throws NoSuchMethodException {
        Method getLength = LegacyJavaAsyncExecutorTest.class.getMethod(GET_LENGTH_METHOD, String.class);
        asyncExecutor.executeAsyncNonStatic(Thread.currentThread(), getLength, EXAMPLE_STRING);

    }

    public String getLength(String cadena) {
        return cadena.concat(" - ").concat(String.valueOf(cadena.length()));
    }
}