package com.royasoftware.repository;

import com.royasoftware.model.Account;
import com.royasoftware.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT a FROM Role a WHERE a.code = :code")
    Role findByCode(@Param("code") String code);

}
