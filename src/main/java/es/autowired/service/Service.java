package es.autowired.service;

import java.util.Optional;

public interface Service {

    void sendFTP(String fileName);

    void callSOAPService(String url);

    void mainLogic();

}