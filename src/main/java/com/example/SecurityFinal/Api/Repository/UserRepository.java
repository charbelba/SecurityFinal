package com.example.SecurityFinal.Api.Repository;

import com.example.SecurityFinal.Api.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {

    boolean existsByEmail(String email);

    Optional<UserInfo> findByEmail(String email);


    @Query("SELECT u FROM UserInfo u JOIN u.projects p WHERE p.id = :projectId")
    List<UserInfo> findAllUsersByProjectId(@Param("projectId") Long projectId);
    @Query("SELECT u FROM UserInfo u JOIN u.organizations o WHERE o.id = :organizationId")
    List<UserInfo> findAllUsersByOrganizationId(@Param("organizationId") Long organizationId);
    @Query("SELECT u FROM UserInfo u JOIN u.organizations o WHERE u.email = :email AND o.id = :organizationId")
    Optional<UserInfo> findByEmailAndOrganizationId(@Param("email") String email, @Param("organizationId") Long organizationId);


    @Query("SELECT u FROM UserInfo u JOIN u.projects o WHERE u.email = :email AND o.id = :projectId")
    Optional<UserInfo> findByEmailAndProjectId(@Param("email") String email, @Param("projectId") Long projectId);
}
