package com.tensquare.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

public class ParseJwtTest {
    public static void main(String[] args) {
        //此处可以try，catch，然后防止过期时间报错
        Claims claims = Jwts.parser().setSigningKey("itcast")

                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLlsI_kuqwiLCJpYXQiOjE1ODY0ODI5MjAsImV4cCI6MTU4NjQ4Mjk3MCwicm9sZSI6ImFkbWluIn0.k9uBj7c-qlBsMWwrhWSDKZZ3GZbxc51NeSWaImb0jFo")
                .getBody();
        System.out.println("用户id："+claims.getId());
        System.out.println("用户名："+claims.getSubject());
        System.out.println("登录时间："+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt()));
        System.out.println("过期时间："+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getExpiration()));
        System.out.println("用户角色："+claims.get("role"));
    }
}
