package org.generations.demojwtsecurity.dto.request;

import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    //Dto que coge la informacion de "la pantalla de login"
    String username;
    String password;
}
