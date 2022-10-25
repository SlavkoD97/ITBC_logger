package com.example.ITBCloggerdemo.repository;

import com.example.ITBCloggerdemo.model.Client;
import com.example.ITBCloggerdemo.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import java.util.UUID;
@Repository
public interface UserJpaRepository extends JpaRepository<Client, UUID>{

    @Query(value = "INSERT INTO Clients ([id], username, [password], email, userType) values (:id, :username, :password, :email, :userType)", nativeQuery = true)
    Void InsertClient(@Param("id") UUID Id,@Param("username") String username,@Param("email")
    String email, @Param("password") String password, @Param("userType") String userType );

    @Query(value = "SELECT COUNT(*) FROM Clients WHERE username=:username", nativeQuery = true) Integer
    isDuplicateName(@Param("username") String username);

    @Query(value = "SELECT COUNT(*) FROM Clients WHERE email=:email", nativeQuery = true) Integer
    isDuplicateEmail(@Param("email") String email);

    @Query (value = "SELECT id FROM Clients WHERE username=:username", nativeQuery = true) String
    getClientByName (@Param("username") String username);

    @Query (value = "SELECT id FROM Clients WHERE email=:email", nativeQuery = true) String
    getClientByEmail (@Param("email") String email);

    @Query (value = "SELECT password FROM Clients WHERE id=:id", nativeQuery = true) String
    getPasswordById (@Param("id") String id);

    @Query (value = "SELECT logID FROM Logs WHERE message=:message", nativeQuery = true) String
    getLogByMessage (@Param("message") String message);
}
