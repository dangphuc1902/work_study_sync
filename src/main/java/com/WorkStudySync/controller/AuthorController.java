package com.WorkStudySync.controller;

import java.io.IOException;

import com.WorkStudySync.entity.UserEntity;
import com.WorkStudySync.exception.UserAlreadyExistsException;
import com.WorkStudySync.payload.request.UserLoginRequest;
import com.WorkStudySync.payload.request.UserRegisterRequest;
import com.WorkStudySync.payload.response.BaseResponse;
import com.WorkStudySync.service.imp.AuthorServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/author")
@CrossOrigin // Link nào vào cũng được.
// @CrossOrigin("link") // Chỉ được một link
public class AuthorController {
    /*
     * Do Password lưu trữ trong database là chuỗi mã hoá dạng BCrypt cho nên không
     * dùng password như điều kiện Where
     *
     * Bước 1: Viết câu truy vấn lấy dữ liệu đăng nhập dựa trên username.
     * Bước 2: Lấy dữ liệu password trả ra từ bước 1 và kiểm tra xem password lưu
     * trữ trong database với password người dùng truyền lên.
     * Bước 3: Nếu 2 mật khẩu match thì sẽ tạo ra token, nếu không giống thì đăng
     * nhập thất bại.
     * - Tạo ra Key để mã hoá và giải mã.
     */
    @Autowired
    private AuthorServiceImp authorServiceImp;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest userRegister) {

        try {
            UserEntity usersEntity = authorServiceImp.registerUser(userRegister);
            BaseResponse baseResponse = new BaseResponse();
            if (usersEntity != null) {
				baseResponse.setStatusCode(200);
				baseResponse.setData(usersEntity);
	            baseResponse.setMessage("Registration successful!");
			} else {
				baseResponse.setStatusCode(401);
	            baseResponse.setMessage("Registration Unsuccessful!");
			}
            baseResponse.setStatusCode(HttpStatus.CREATED.value());
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        // Private key: Tạo ra key để mã hoá và sau đó lưu vo application.properties.
        // SecretKey secretKey = Jwts.SIG.HS256.key().build();
        // String key = Encoders.BASE64.encode(secretKey.getEncoded());
        // System.out.println("Check key :" + key);
        String token = authorServiceImp.checkLogin(userLoginRequest, response);
        System.out.println("Token: " + token);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(token.trim().length() > 0 ? 200 : 401);
        baseResponse.setData(token.trim().length() > 0 ? token : "Đăng nập thất bại");
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/oauth2/authorize/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String redirectUri = "https://accounts.google.com/o/oauth2/v2/auth?client_id=1011781828728-8o13btj3j3n3aute4d58g6am4eks25ti.apps.googleusercontent.com&redirect_uri=http://localhost:8080/author/oauth2/callback/google&response_type=code&scope=openid%20email%20profile";
        response.sendRedirect(redirectUri);
    }

    @GetMapping("/oauth2/callback/google")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam("code") String code) {
        // TODO: Implement logic here
        return ResponseEntity.ok("Google callback received. Code: " + code);
    }

}
