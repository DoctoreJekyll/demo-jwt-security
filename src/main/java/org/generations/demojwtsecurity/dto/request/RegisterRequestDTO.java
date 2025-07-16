package org.generations.demojwtsecurity.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    String username;
    String password;
    String firstName;
    String address;
    String email;
    String role;
}
