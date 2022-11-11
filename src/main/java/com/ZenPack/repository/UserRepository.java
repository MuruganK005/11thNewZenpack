package com.ZenPack.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ZenPack.model.User;
import com.ZenPack.model.ZenPackReport;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<ZenPackReport>{
	
	User findByEmail(String email);
}
