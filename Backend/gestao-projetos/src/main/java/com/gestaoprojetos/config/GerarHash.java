package com.gestaoprojetos.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String raw = "senha123";
        String hash = encoder.encode(raw);
        System.out.println(hash);
    }
}
