package me.jiho.demorestapi.Accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER) //여러개의 enum을 가질수 있음 기본은 LAZY지만 바로 필요하므로 수정
    @Enumerated(value = EnumType.STRING)
    private Set<AccountRole> roles;
}
