package com.bookStore.SpringBootPractice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookStore.SpringBootPractice.entities.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role,Integer>{

}
