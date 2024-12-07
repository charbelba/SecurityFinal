package com.example.SecurityFinal.Api.Repository;

import com.example.SecurityFinal.Api.Entity.OrganizationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRoleRepository extends JpaRepository<OrganizationRole, Long> {
    List<OrganizationRole> findByNameIn(List<String> names);


    @Query("SELECT r.name FROM OrganizationRole r " +
            "JOIN r.users u " +
            "JOIN r.organization o " +
            "WHERE u.email = :email AND o.uuid = :organizationUUID")
    List<String> findRolesByUserAndOrganization(@Param("email") String email,
                                                @Param("organizationUUID") String organizationUUID);
}