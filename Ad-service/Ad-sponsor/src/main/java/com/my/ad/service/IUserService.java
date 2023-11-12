package com.my.ad.service;

import  com.my.ad.exception.AdException;
import  com.my.ad.vo.CreateUserRequest;
import  com.my.ad.vo.CreateUserResponse;


public interface IUserService {

    /**
     * <h2>创建用户</h2>
     * */
    CreateUserResponse createUser(CreateUserRequest request)
            throws AdException;
}
