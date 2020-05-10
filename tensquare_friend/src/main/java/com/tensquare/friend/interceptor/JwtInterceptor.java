package com.tensquare.friend.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
   @Autowired
    private JwtUtil jwtUtil;
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("经过了拦截器");
        //无论如何都放行，具体还是在业务去判断
        //只负责把请求头中包含的token进行一个解析验证
        String header = request.getHeader("Authorization");
        if (header!=null&&!"".equals(header)){
            //如果有包含头信息，进行解析
            if (header.startsWith("Bearer ")){
                String token =header.substring(7);
                try {
                    Claims claims=jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if (roles!=null&&roles.equals("admin")) {
                        request.setAttribute("claims_admin",claims);
                        }
                    if (roles!=null&&roles.equals("user")) {
                        request.setAttribute("claims_user",claims);
                    }
                }catch (Exception e){throw new RuntimeException("令牌不正确");}
            }

        }

        return true;
    }
}
