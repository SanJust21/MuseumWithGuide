package com.example.MuseumTicketing.DTO.AdminScanner;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String token;

    private String refreshToken;

}