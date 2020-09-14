package me.jiho.demorestapi.configs;

import me.jiho.demorestapi.accounts.Account;
import me.jiho.demorestapi.accounts.AccountRole;
import me.jiho.demorestapi.accounts.AccountService;
import me.jiho.demorestapi.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @DisplayName("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {

        //Given
        String email = "jiho@email.com";
        String password = "jiho";
        Account jiho = Account.builder()
                .email(email)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(jiho);


        String clientId = "myapp";
        String clientSecret = "pass";
        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", email)
                .param("password", password)
                .param("grant_type", "password")

        )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("access_token").exists());
    }
}