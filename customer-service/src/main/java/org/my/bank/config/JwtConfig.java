package org.my.bank.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.my.bank.dto.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtConfig {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public Map<String, String> generateJwtToken(Customer customer) {
        String token = Jwts.builder()
                .setId(customer.getId().toString())
                .setSubject(customer.getCustomerName())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        Map<String, String> jwtTokenGen = new HashMap<>();
        jwtTokenGen.put("message", "Login successfully");
        jwtTokenGen.put("token", token);
        return jwtTokenGen;
    }
}