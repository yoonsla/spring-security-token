package com.sy.springsecuritytoken.user.repository;

import com.sy.springsecuritytoken.user.domain.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    Optional<Account> findByEmailAndPassword(String email, String password);
}
