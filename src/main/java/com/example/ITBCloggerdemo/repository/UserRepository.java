package com.example.ITBCloggerdemo.repository;

import com.example.ITBCloggerdemo.model.Client;
import com.example.ITBCloggerdemo.model.Log;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {

    void insertClient(Client client);

    void insertLog (Log log, String token);

    List<Log> getAllLogs();

    List<Client> getAllClients();


}
