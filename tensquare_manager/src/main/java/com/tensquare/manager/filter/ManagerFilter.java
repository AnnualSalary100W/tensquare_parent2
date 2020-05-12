package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private HttpServletRequest request;
    /**
     * 在请求前pre，请求后post执行
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 多个过滤器的执行瞬息，数字越小，表示越先执行...
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 当前过滤器是否开启
     * @return
     */

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器内执行的操作return任何object都继续执行
     * setSsendzullResponse(false)表示不再执行
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("经过过滤器");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        if(request.getMethod().equals("OPTIONS")) {
            return null;
        }
        String url=request.getRequestURL().toString(); if(url.indexOf("/admin/login")>0){ System.out.println("登陆页面"+url); return null; }

        String header = request.getHeader("Authorization");
        if (header!=null&&!"".equals(header)){
            if (header.startsWith("Bearer ")){
                String token=header.substring(7);
                try{
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if (roles.equals("admin")){
                        //把头信息转发，放行
                        requestContext.addZuulRequestHeader("Authorization",header);
                        return null;
                    }

                }catch (Exception e){
                    requestContext.setSendZuulResponse(false);
                }
            }
        }
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(403);//http状态码     
    requestContext.setResponseBody("无权访问.");
    requestContext.getResponse().setContentType("text/html;charset=UTF-8");

        return null;
    }
}
