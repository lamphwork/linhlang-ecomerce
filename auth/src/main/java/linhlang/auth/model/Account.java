package linhlang.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private String id;
    private String username;
    private String password;
    private Status status;
    private Set<String> roles;
    private LocalDateTime createTime;

    public enum Status {
        ACTIVE, BLOCKED
    }

    public static Account newAccount(String username, String password, String role) {
        return new Account(
                UUID.randomUUID().toString(),
                username, password,
                Status.ACTIVE,
                Collections.singleton(role),
                LocalDateTime.now()
        );
    }
}
