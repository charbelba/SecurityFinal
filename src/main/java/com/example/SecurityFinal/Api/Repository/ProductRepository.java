package com.example.SecurityFinal.Api.Repository;

import com.example.SecurityFinal.Api.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByUuid(String uuid);

    @Query("SELECT p FROM Product p WHERE p.uuid = :productUuid AND p.organization.id = :organization_id")
    Optional<Product> findByOrganization(@Param("productUuid") String productUuid, @Param("organization_id") long organization_id);
    @Query("SELECT p FROM Product p WHERE p.uuid = :productUuid AND p.project.id = :project_id")
    Optional<Product> findByProject(@Param("productUuid") String productUuid, @Param("project_id") long project_id);

    Page<Product> findByOrganizationId(Long organizationId, Pageable pageable);

    Page<Product> findByProjectId(Long projectId, Pageable pageable);


    Optional<Product> findByOrganization_UuidAndUuid(String organizationUuid, String productUuid);
}
