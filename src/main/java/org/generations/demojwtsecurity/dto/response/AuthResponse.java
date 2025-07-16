package org.generations.demojwtsecurity.dto.response;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AuthResponse {
    String token;
    String response;
}
