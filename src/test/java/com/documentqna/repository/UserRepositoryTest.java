package com.documentqna.repository;

import com.documentqna.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setEmail("test@example.com");
        user1.setName("Test User");
        // set other required fields if any

        userRepository.save(user1);
    }

    @Test
    void testFindByEmail_existingEmail_returnsUser() {
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.get().getName()).isEqualTo("Test User");
    }

    @Test
    void testFindByEmail_nonExistingEmail_returnsEmpty() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertThat(foundUser).isNotPresent();
    }

    @Test
    void testExistsByEmail_existingEmail_returnsTrue() {
        boolean exists = userRepository.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByEmail_nonExistingEmail_returnsFalse() {
        boolean exists = userRepository.existsByEmail("noone@example.com");

        assertThat(exists).isFalse();
    }
}
