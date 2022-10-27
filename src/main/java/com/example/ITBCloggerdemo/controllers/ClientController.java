package com.example.ITBCloggerdemo.controllers;

import com.example.ITBCloggerdemo.model.Client;
import com.example.ITBCloggerdemo.model.Client1;
import com.example.ITBCloggerdemo.repository.UserJpaRepository;
import com.example.ITBCloggerdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

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
            if (password.equals(client.getPassword())) {

                HttpSession session = request.getSession();
                session.setAttribute("token", token);
                return ResponseEntity.status(HttpStatus.OK).body(token);

            } else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else if (userJpaRepository.isDuplicateEmail(account) > 0) {
            String token = userJpaRepository.getClientByEmail(account);
            String password = userJpaRepository.getPasswordById(token);
            if (password.equals(client.getPassword())) {
                response.addHeader("token", token);
                return ResponseEntity.status(HttpStatus.OK).body(token);
            } else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);


    }

    @GetMapping("/api/clients")
    public ResponseEntity<List<Client1>> Clients(HttpServletRequest request, HttpServletResponse response) {


        String token = null;
        List<Client> retVal;
        List<Client1> retVal1 = new ArrayList<>();
        HttpSession session = request.getSession();
        String RetVal = "";
        token = (String) session.getAttribute("token");
        String id =
                userJpaRepository.getTypeById(token);

        if (id == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        if (id.equals("admin")) {
            retVal = userRepository.getAllClients();

            for (var x : retVal) {
                String numberOfLogs = userJpaRepository.getNumberOfLogs(x.getId().toString());
                retVal1.add(new Client1(x.getId(), x.getUsername(), x.getEmail(), x.getUserType(), numberOfLogs));

            }

            return ResponseEntity.status(HttpStatus.OK).body(retVal1);
        }
        if (!id.equals("user") || id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
    @PatchMapping("/api/clients/{clientId}/reset-password")
    public ResponseEntity<Void> updatePassword(@PathVariable("clientId") String id, @RequestBody Client clientToUpdate, HttpServletRequest request) {

        String token = null;
        HttpSession session = request.getSession();
        token = (String) session.getAttribute("token");
        String adminId =
                userJpaRepository.getTypeById(token);
        if (adminId.equals("admin")) {
            userJpaRepository.updatePasswordById(clientToUpdate.getPassword(), id);

            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else if (adminId== null) {


            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
