package com.kemalaydin.routemanagement.security.repository;

import com.kemalaydin.routemanagement.security.repository.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {
}

