package es.autowired.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonHelper {

    private static final String LOG_SEPARATOR = "\t";

    public synchronized static void log(String message, Class clazz) {
        StringBuilder finalMessage = new StringBuilder("[INFO]");
        finalMessage.append(LOG_SEPARATOR);
        final String format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'").format(new Date());
        finalMessage.append(format);
        finalMessage.append(LOG_SEPARATOR);
        Thread currentThread = Thread.currentThread();
        finalMessage.append(String.format("%-10s",currentThread.getName()));
        finalMessage.append(LOG_SEPARATOR);
        final String str = "CTID-" + currentThread.getId();
        finalMessage.append(String.format("%-10s", str));
        finalMessage.append(LOG_SEPARATOR);
        finalMessage.append(clazz != null ? clazz.getSimpleName() : "?");
        finalMessage.append(LOG_SEPARATOR);
        finalMessage.append(message);
        System.out.println(finalMessage.toString());
    }

}
