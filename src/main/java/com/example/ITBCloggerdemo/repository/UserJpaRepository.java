package com.example.ITBCloggerdemo.repository;

import com.example.ITBCloggerdemo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Query (value = "SELECT userType FROM Clients WHERE id=:id", nativeQuery = true) String
    getTypeById (@Param("id") String id);
    @Transactional
    @Modifying
    @Query(value = "UPDATE Clients SET password =:password WHERE id =:id", nativeQuery = true) void
    updatePasswordById (@Param ("password") String password, @Param("id") String id);

    @Query(value = "SELECT COUNT(*) FROM LOGS WHERE id=:id", nativeQuery = true) String
    getNumberOfLogs (@Param("id") String id);


}
