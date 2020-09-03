package es.autowired.service;

public interface Service {

    void sendFTP(String fileName);

    void callSOAPService(String url);

    void mainLogic(int callType);

}