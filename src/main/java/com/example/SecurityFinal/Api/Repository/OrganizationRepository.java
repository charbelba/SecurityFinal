package com.example.SecurityFinal.Api.Repository;


import com.example.SecurityFinal.Api.Entity.Organization;
import com.example.SecurityFinal.Api.Entity.UserInfo;
import com.example.SecurityFinal.Api.Model.Response.OrganizationViewResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsByUuid(String uuid);

    boolean existsByOwner(UserInfo owner);
    Optional<Organization> findByUuid(String uuid);

    @Query("SELECT new com.example.SecurityFinal.Api.Model.Response.OrganizationViewResponseDTO("
            + "o.uuid, o.name, "
            + "COUNT(DISTINCT u), "
            + "COUNT(DISTINCT p), "
            + "o.owner.email) "
            + "FROM Organization o "
            + "JOIN o.users u "
            + "LEFT JOIN o.projects p "
            + "WHERE u.email = :userEmail "
            + "GROUP BY o.uuid, o.name, o.owner.email")
    List<OrganizationViewResponseDTO> getOrganizationViewsByUser(@Param("userEmail") String userEmail);




}
