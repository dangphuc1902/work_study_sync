package com.WorkStudySync.service;

import com.WorkStudySync.entity.RoleEntity;
import com.WorkStudySync.entity.UserEntity;
import com.WorkStudySync.entity.UserRoleEntity;
import com.WorkStudySync.entity.UserRoleEntityId;
import com.WorkStudySync.entity.enums.UserTypeEnum;
import com.WorkStudySync.exception.UserAlreadyExistsException;
import com.WorkStudySync.exception.UserEmailNotExistException;
import com.WorkStudySync.payload.request.UserLoginRequest;
import com.WorkStudySync.payload.request.UserRegisterRequest;
import com.WorkStudySync.repository.RoleRepository;
import com.WorkStudySync.repository.UserRepository;
import com.WorkStudySync.repository.UserRoleRepository;
import com.WorkStudySync.service.imp.AuthorServiceImp;
import com.WorkStudySync.util.JwtUltils;
import com.google.gson.Gson;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorService implements AuthorServiceImp {
     @Autowired
     private UserRepository userRepository;
     @Autowired
     private UserRoleRepository userRoleRepository;
     @Autowired
     private RoleRepository roleRepository;
     @Autowired
     private PasswordEncoder passwordEncoder;
 //    Giúp lấy giá trị khai báo bên application.properties.
     @Value("${key.token.jwt}")
     private String strKeyToken;

     @Autowired
     private JwtUltils jwtUltils;

     private Gson gson = new Gson();

 	@Override
 	public String checkLogin(UserLoginRequest userLoginReq, HttpServletResponse response) {
 		String token = "";
 		UserEntity userEntity = userRepository.findByEmail(userLoginReq.getEmail()).orElse(null);
 		if (userEntity != null) {
 			// So sánh mật khẩu người dùng nhập vào với mật khẩu đã mã hóa trong cơ sở dữ
 			// liệu
 			if (passwordEncoder.matches(userLoginReq.getPassword(), userEntity.getPasswordHash())) 
 			{
 				// Lấy danh sách các role của user và chuyển đổi thành danh sách tên role
 				List<String> roleNames = userRoleRepository.findRoleNamesByUser(userEntity);
 				// Tạo payload để nhúng vào JWT
 				Map<String, Object> claims = new HashMap<>();
 				claims.put("email", userEntity.getEmail());
 				claims.put("roles", roleNames);
                token = jwtUltils.createToken(claims);

 				// Gửi cookie lưu email (tuỳ chọn)
 				Cookie saveUserName = new Cookie("email", userLoginReq.getEmail());
 				saveUserName.setHttpOnly(true);
 				saveUserName.setSecure(false);
 				saveUserName.setPath("/");
 				saveUserName.setMaxAge(7 * 24 * 60 * 60);
 				response.addCookie(saveUserName);
 			} else {
 				throw new RuntimeException("Password is incorrect.");
 			}
 		} else {
 			throw new UserEmailNotExistException("User with email " + userLoginReq.getEmail() + " does not exist.");
 		}
        // Sử dụng Gson để trả về thông tin user và token dưới dạng JSON
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", userEntity); // userEntity sẽ được Gson tự động chuyển sang JSON
        return gson.toJson(result);
 	}

/*
* B1: Check email exists in DB if exists -> exception
* B2: Create new UserEntity and add all info of UserRequest
*       Todo: setRoles => Find roleEntity by Name role.
* B3: Save this userEntity into DB.
* */
     @Override
     public UserEntity registerUser(UserRegisterRequest userRegister) {
    	// B1: Check email tồn tại chưa
    	    if (userRepository.existsByEmail(userRegister.getEmail())) {
    	        throw new UserAlreadyExistsException("Email " + userRegister.getEmail() + " already exists");
    	    }

    	    // B2: Tạo mới user entity
    	    UserEntity userEntity = new UserEntity();
    	    userEntity.setEmail(userRegister.getEmail());
    	    userEntity.setFullName(userRegister.getFullName());
    	    userEntity.setPasswordHash(passwordEncoder.encode(userRegister.getPassword()));
    	    
    	    // Parse kiểu enum
    	    UserTypeEnum userTypeEnum;
    	    try {
    	        userTypeEnum = UserTypeEnum.valueOf(userRegister.getUserType().toUpperCase());
    	    } catch (IllegalArgumentException ex) {
    	        throw new IllegalArgumentException("Invalid userType: " + userRegister.getUserType());
    	    }

    	    userEntity.setUserType(userTypeEnum);
    	    userEntity.setCreatedAt(LocalDateTime.now());

    	    // B3: Lấy role mặc định từ DB
    	    RoleEntity roleEntity = roleRepository.findByName("ROLE_USER")
    	        .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

    	    // B4: Save user trước để có userId (nếu bạn không dùng cascade persist)
    	    userEntity = userRepository.save(userEntity);

    	    // B5: Tạo entity trung gian user-role
    	    UserRoleEntity userRoleEntity = new UserRoleEntity();
    	    userRoleEntity.setUser(userEntity);
    	    userRoleEntity.setRole(roleEntity);
    	    userRoleEntity.setAssignedAt(LocalDateTime.now());

    	    // Tạo id kết hợp
    	    UserRoleEntityId compositeKey = new UserRoleEntityId(userEntity.getUserId(), roleEntity.getRoleId());
    	    userRoleEntity.setId(compositeKey);

    	    // B6: Lưu liên kết user-role
    	    userRoleRepository.save(userRoleEntity);

    	    // B7: Trả về user đã đăng ký (hoặc DTO tùy)
    	    return userEntity;
     }
}