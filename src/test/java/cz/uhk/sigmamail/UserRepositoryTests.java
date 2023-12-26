package cz.uhk.sigmamail;

import cz.uhk.sigmamail.model.User;
import cz.uhk.sigmamail.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateUser(){
        User user = new User();
        user.setUsername("jan.cerny@sigma.com");
        user.setPassword("Password123");
        user.setFirst_name("Jan");
        user.setLast_name("Černý");
        LocalDate localDate = LocalDate.of(1998, 2, 1);
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        user.setBirthdate(Date.from(zonedDateTime.toInstant()));
        user.setRole("User");

        User savedUser = userRepository.save(user);

        User existUser = testEntityManager.find(User.class, savedUser.getId());

        assertThat(existUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void testFindUserByUsername() {
        String username = "jan.cerny@sigma.com";

       User user = userRepository.findByUsername(username);

       assertThat(user).isNotNull();
    }
}
