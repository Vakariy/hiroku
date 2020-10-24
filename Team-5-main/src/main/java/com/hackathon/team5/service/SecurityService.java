package com.hackathon.team5.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);

}
