package com.WorkStudySync.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import com.WorkStudySync.entity.RoleEntity;
import com.WorkStudySync.entity.UserEntity;
import com.WorkStudySync.entity.UserRoleEntity;
import com.WorkStudySync.entity.UserRoleEntityId;
import com.WorkStudySync.repository.RoleRepository;
import com.WorkStudySync.repository.UserRepository;
import com.WorkStudySync.repository.UserRoleRepository;
import com.WorkStudySync.util.JwtUltils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private JwtUltils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Kiểm tra người dùng đã tồn tại chưa
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);

        UserEntity user;
        if (existingUser.isEmpty()) {
            // Nếu chưa thì tạo mới
            user = new UserEntity();
            user.setEmail(email);
            user.setFullName(name);
            user.setPasswordHash(""); // có thể để rỗng hoặc gán mặc định

            RoleEntity role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Role not found"));

    	    UserRoleEntity userRoleEntity = new UserRoleEntity();
    	    userRoleEntity.setUser(user);
    	    userRoleEntity.setRole(role);
    	    userRoleEntity.setAssignedAt(LocalDateTime.now());

    	    // Tạo id kết hợp
    	    UserRoleEntityId compositeKey = new UserRoleEntityId(user.getUserId(), role.getRoleId());
    	    userRoleEntity.setId(compositeKey);

    	    // B6: Lưu liên kết user-role
    	    userRoleRepository.save(userRoleEntity);

            userRepository.save(user);
        } else {
            user = existingUser.get();
        }

        // Tạo JWT token
        String token = jwtUtils.createToken(Map.of("email", user.getEmail()));
        
        // Redirect về frontend với token
        response.sendRedirect("http://localhost:3000/oauth-callback?token=" + token);
    }
}

