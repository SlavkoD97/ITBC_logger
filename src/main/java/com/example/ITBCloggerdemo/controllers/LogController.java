package com.example.ITBCloggerdemo.controllers;

import com.example.ITBCloggerdemo.model.Log;
import com.example.ITBCloggerdemo.repository.UserJpaRepository;
import com.example.ITBCloggerdemo.repository.UserRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class LogController {

    private UserRepository userRepository;
    private UserJpaRepository userJpaRepository;



    @Autowired
    public LogController(UserJpaRepository userJpaRepository, UserRepository userRepository) {
        this.userJpaRepository = userJpaRepository;
        this.userRepository = userRepository;

    }

    @PostMapping("/api/logs/create")
    public ResponseEntity<Void> create (HttpServletRequest request,HttpServletResponse response, @RequestBody Log log){
        log.setCreatedDate(java.time.LocalDate.now());
        String token = null;
        Log newLog = new Log(log.getMessage(), log.getLogType(), log.getCreatedDate());
        if(log.getLogType() > 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        HttpSession session = request.getSession();

        token = (String)session.getAttribute("token");
        if(token == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        if (log.getMessage().length() > 1024){
            ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(null);
        }

        userRepository.insertLog(newLog, token);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @RequestMapping(value="/api/logs/search", method=RequestMethod.GET)
    public ResponseEntity<List<Log>> getLogs (HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> reqParam) {
        List<Log> allLogs = new LinkedList<Log>();
        List<Log> retVal = new LinkedList<Log>();

        //poruka1
        allLogs = userRepository.getAllLogs();
        //poruka1 10.10 0
        //poruka2 12.10
        //poruka1 15.10
        String token = null;
        HttpSession session = request.getSession();

        token = (String)session.getAttribute("token");

        if(token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (reqParam.get("logType") != null){
            if (Integer.parseInt(reqParam.get("logType")) > 2 || Integer.parseInt(reqParam.get("logType")) < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }
        LocalDate date;
        if (reqParam.get("dateFrom") != null){
            try {
                date = LocalDate.parse(reqParam.get("dateFrom"));
            }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        }

        if (reqParam.get("dateTo") != null){
            try {
                date = LocalDate.parse(reqParam.get("dateTo"));
            }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        }
        JSONParser parser = new JSONParser("log.getLogID()");
        for(var log : allLogs) {
                if ((reqParam.get("message") == null) || (reqParam.get("message").equals(log.getMessage()))) {
                    if ((reqParam.get("logType") == null) || Integer.parseInt(reqParam.get("logType")) == log.getLogType()) {
                        if ((reqParam.get("dateTo") == null) || log.getCreatedDate().isBefore(LocalDate.parse(reqParam.get("dateTo")))) {
                            if ((reqParam.get("dateFrom") == null) || log.getCreatedDate().isAfter(LocalDate.parse(reqParam.get("dateFrom")))) {

                                retVal.add(log);
                            }

                        }
                    }
                }
            }




            return ResponseEntity.status(HttpStatus.OK).body(retVal);
        }



}
