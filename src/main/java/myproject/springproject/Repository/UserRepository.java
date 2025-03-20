package myproject.springproject.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myproject.springproject.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
} 
