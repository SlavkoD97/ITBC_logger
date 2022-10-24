package com.example.ITBCloggerdemo.controllers;

import com.example.ITBCloggerdemo.model.Log;
import com.example.ITBCloggerdemo.model.LogType;
import com.example.ITBCloggerdemo.repository.UserJpaRepository;
import com.example.ITBCloggerdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

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
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        HttpSession session = request.getSession();

        token = (String)session.getAttribute("token");
        if(token == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        if (log.getMessage().length() > 1024){
            ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(null);
        }

        userRepository.insertLog(newLog, request.getHeader("token"));
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @RequestMapping(value="/api/logs/search", method=RequestMethod.GET)
    public ResponseEntity<List<Log>> getLogs (@RequestParam Map<String, String> reqParam){
        List<Log> allLogs = new LinkedList<Log>();
        List<Log> retVal = new LinkedList<Log>();

        allLogs = userRepository.getAllLogs();
        if(reqParam.get("message") != null) {
            for (var log: allLogs) {
                if(log.getMessage().equals(reqParam.get("message"))){
                    retVal.add(log);
                }
            }
        }

        /*if(reqParam.get("logType") != null) {
            for (var log: allLogs) {
                if(log.getMessage() == reqParam.get("logType")){
                    retVal.add(log);
                }
            }
        }*/
        /*if(logType != null) {
            for (var log: allLogs) {
                if(log.getMessage().equals(message)){
                    retVal.add(log);
                }
            }
        }*/
        return ResponseEntity.status(HttpStatus.CONFLICT).body(retVal);
    }

}
