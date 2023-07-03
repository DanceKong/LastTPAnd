package com.example.endp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private String id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String address;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return name;
    }

    public String getName() {
        return name;
    }


}