package com.example.ITBCloggerdemo.repository;

import com.example.ITBCloggerdemo.model.Client;
import com.example.ITBCloggerdemo.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SqlUserRepository implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insertClient(Client client) {

        String action = "INSERT INTO Clients ([id], username, [password], email, userType) values ('" + client.getId() + "','"
               + client.getUsername() + "','" + client.getPassword() + "','" + client.getEmail() + "','" + client.getUserType()+ "')";

        jdbcTemplate.execute(action);

    }
    @Override
    public List<Log> getAllLogs() {
        String action = "SELECT * from logs";
        return jdbcTemplate.query(
                action,
                BeanPropertyRowMapper.newInstance(Log.class)
        );
    }
    @Override
    public void insertLog (Log log, String token){
        String logType = "";
        if(log.getLogType() == 0) {
            logType = "Error";
        } else if(log.getLogType() == 1){
            logType = "Warning";
        }
        else {
            logType = "Info";
        }
    String action = "INSERT INTO LOGS (logID,message, logType, createdDate, id) values ('"+ log.getLogID() + "','" + log.getMessage() + "','" +
            log.getLogType() + "','" + log.getCreatedDate() + "','" + token + "')";

    jdbcTemplate.execute(action);
    }

    @Override
    public List<Client> getAllClients(){
        String action= "SELECT * FROM CLIENTS WHERE userType like 'user'";
        return jdbcTemplate.query(
                action,
                BeanPropertyRowMapper.newInstance(Client.class));

    }

}
