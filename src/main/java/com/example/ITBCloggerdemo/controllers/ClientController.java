package com.example.ITBCloggerdemo.controllers;

import com.example.ITBCloggerdemo.model.Client;
import com.example.ITBCloggerdemo.repository.UserJpaRepository;
import com.example.ITBCloggerdemo.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.*;

@RestController
public class ClientController {

    private UserRepository userRepository;
    private UserJpaRepository userJpaRepository;

    @Autowired
    public ClientController(UserJpaRepository userJpaRepository, UserRepository userRepository) {
        this.userJpaRepository = userJpaRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/api/clients/register")
    public ResponseEntity<Void> register(@RequestBody Client client) {
        Client newClient = new Client(client.getUsername(), client.getPassword(), client.getEmail(), "user");
       if (newClient.getUsername().length() < 3 || newClient.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (userJpaRepository.isDuplicateName(newClient.getUsername()) != 0 || userJpaRepository.isDuplicateEmail(newClient.getEmail()) != 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
       userRepository.insertClient(newClient);


        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

    @PostMapping("/api/clients/login")
    public ResponseEntity<String> login(HttpServletRequest request, HttpServletResponse response, @RequestBody Client client) {
        String account = client.getAccount();

        if (userJpaRepository.isDuplicateName(account) > 0) {
            String token = userJpaRepository.getClientByName(account);
            String password = userJpaRepository.getPasswordById(token);
            if(password.equals(client.getPassword())) {

                HttpSession session = request.getSession();
                session.setAttribute("token", token);
                return ResponseEntity.status(HttpStatus.OK).body(token);
            }
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else if (userJpaRepository.isDuplicateEmail(account) > 0) {
            String token = userJpaRepository.getClientByEmail(account);
            String password = userJpaRepository.getPasswordById(token);
            if(password.equals(client.getPassword())) {
                  response.addHeader("token", token);
                return ResponseEntity.status(HttpStatus.OK).body(token);
            }
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);


    }


}
