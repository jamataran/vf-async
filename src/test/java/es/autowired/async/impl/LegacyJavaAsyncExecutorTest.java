package es.autowired.async.impl;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import es.autowired.async.AsyncExecutor;
import es.autowired.async.LegacyJavaAsyncExecutor;
import es.autowired.service.ServiceImpl;

@SuppressWarnings("DefaultAnnotationParam")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceImpl.class})
public class LegacyJavaAsyncExecutorTest {

    private static final int EXAMPLE_INT = 1;
    private static final String STATIC_METHOD = "valueOf";
    private static final String EXAMPLE_STRING = "cadena";
    private static final String GET_LENGTH_METHOD = "getLength";
    private static final String STRING_CLASS = "java.lang.String";
    private static final int WANTED_NUMBER_OF_INVOCATIONS = 1;
    private static final String SERVICE_CLASS = "es.autowired.service.ServiceImpl";
    private static final String STATIC_SERVICE_METHOD = "retard3";

    private final AsyncExecutor asyncExecutor = LegacyJavaAsyncExecutor.getInstance();

    @Test(expected = Test.None.class)
    public void executeAsyncNonStatic() throws NoSuchMethodException {
        Method getLength = LegacyJavaAsyncExecutorTest.class.getMethod(GET_LENGTH_METHOD, String.class);
        asyncExecutor.executeAsync(Thread.currentThread(), this, getLength, EXAMPLE_STRING);

    }

    @Test(expected = Test.None.class)
    public void executeAsyncStatic() throws NoSuchMethodException {
        Method valueOf = String.class.getMethod(STATIC_METHOD, int.class);
        asyncExecutor.executeAsyncStatic(valueOf, EXAMPLE_INT);
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

    @Test
    public void executeAsyncNonStaticWithVerify() throws NoSuchMethodException {
        LegacyJavaAsyncExecutorTest mock = mock(LegacyJavaAsyncExecutorTest.class);

        Method getLength = mock.getClass().getMethod(GET_LENGTH_METHOD, String.class);
        asyncExecutor.executeAsync(Thread.currentThread(), mock, getLength, EXAMPLE_STRING);

        verify(mock, times(WANTED_NUMBER_OF_INVOCATIONS)).getLength(anyString());
    }

    @Test
    public void executeAsyncStaticWithVerify() {
        PowerMockito.mockStatic(ServiceImpl.class);

        List<Class> paramTypes = new ArrayList<Class>();
        paramTypes.add(int.class);

        asyncExecutor.executeAsyncStatic(SERVICE_CLASS, STATIC_SERVICE_METHOD, paramTypes, EXAMPLE_INT);

        verifyStatic(Mockito.times(WANTED_NUMBER_OF_INVOCATIONS));
    }

    public String getLength(String cadena) {
        return cadena.concat(" - ").concat(String.valueOf(cadena.length()));
    }
}