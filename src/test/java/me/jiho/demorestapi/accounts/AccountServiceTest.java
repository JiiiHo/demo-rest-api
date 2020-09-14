package me.jiho.demorestapi.accounts;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {


    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername() {

        // Given\
        String password = "jiho";
        String username = "jiho@email.com";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();

        this.accountService.saveAccount(account);


        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        Assertions.assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }

    @Test
    @DisplayName("유저네임 조회 실패")
    public void findByUsernameFail() {
        String username = "random@email.com";
        try {
            accountService.loadUserByUsername(username);
            fail("supposed to be failed");
        }catch (UsernameNotFoundException e) {
            Assertions.assertThat(e.getMessage()).containsSequence(username);
        }
    }

}