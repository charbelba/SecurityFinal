package com.example.SecurityFinal.Api.Repository;


import com.example.SecurityFinal.Api.Entity.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<UserRole, Long> {


}