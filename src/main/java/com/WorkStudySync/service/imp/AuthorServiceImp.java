package com.WorkStudySync.service.imp;

 import com.WorkStudySync.entity.UserEntity;
import com.WorkStudySync.payload.request.UserLoginRequest;
import com.WorkStudySync.payload.request.UserRegisterRequest;

import jakarta.servlet.http.HttpServletResponse;


public interface AuthorServiceImp {

     String checkLogin(UserLoginRequest userLoginRequest, HttpServletResponse response);
     UserEntity registerUser(UserRegisterRequest userRegister);
    
}
