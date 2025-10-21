package com.WorkStudySync.filter;

import com.WorkStudySync.payload.response.RoleResponse;
import com.WorkStudySync.util.JwtUltils;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomJwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUltils jwtUltils;
     private Gson gson = new Gson();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerAuthen = request.getHeader("Authorization");
        if (headerAuthen != null && headerAuthen.trim().length() > 0){
            String token = headerAuthen.substring(7);
            // TODO: note giải mã token
            String data = jwtUltils.decryptToke(token);

            if (data != null){
            	RoleResponse role = gson.fromJson(data, RoleResponse.class);
                List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getName());
                authorityList.add(simpleGrantedAuthority);
                // TODO: tạo chứng thực cho sercurity biết là đã hợp lệ và bypass được tất cả các filter của scurity.
                UsernamePasswordAuthenticationToken authen =
                        new UsernamePasswordAuthenticationToken("","",authorityList);

                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authen);
            }
        }

        filterChain.doFilter(request,response);// Cho chạy tiếp
    }
}
