package com.resturant.management.utility.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;

import java.security.Key;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    private static final String SECRET = "7a9cb3e5823e670285650ab8ae2f7b972b08d4927758790564e0e809c0ee7eb8";
    private static final long EXPIRATION_TIME =  86400 * 1000;
    public static String generateToken(UserDetails userDetails) {
        log.info("subject{}",userDetails.getUsername());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(),SignatureAlgorithm.HS256)
                .compact();

    }
   public static String extractUserName(String token) {

        return extractCliam(token,Claims::getSubject);
    }
    private static Key getSignKey(){
        byte[] key= Decoders.BASE64.decode(SECRET);
       return Keys.hmacShaKeyFor(key);
    }
    public  static  boolean isTokenValid(String token,UserDetails userDetails)
    {
        final String userName=extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

    private static  boolean isTokenExpired(String token)
    {
        return  extractExpirationToken(token).before(new Date());
    }
    private static  Date extractExpirationToken(String token)
    {
        return extractCliam(token,Claims::getExpiration);
    }


    private static <T> T extractCliam(String token, Function<Claims, T> claimResolvers)
    {
        final Claims claims=extractAllClaims(token);
        return claimResolvers.apply(claims);
    }
    private static  Claims extractAllClaims(String token){
        log.info("claims{}",Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody());
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }
}
