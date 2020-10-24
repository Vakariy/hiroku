package com.hackathon.team5.service;

import com.hackathon.team5.entity.CustomUser;
import com.hackathon.team5.repos.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

@Service
public class UserService {


    @Autowired
    CustomUserRepository customUserRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public CustomUser findByEmail(String email) {

        return customUserRepository.findByEmail(email);

    }

    @Transactional(readOnly = true)
    public List<CustomUser> getAllUsers() {
        return customUserRepository.findAll();
    }


/*
    @Transactional(rollbackFor = Exception.class)
    public void deleteUsers(List<Long> ids) {
        ids.forEach((id)
                -> customUserRepository.deleteById(id));
    }

    @Transactional
    public boolean addUser(String login, String passHash,
                           Role role, String email,
                           String phone,String uuid) {
        if (customUserRepository.existsByEmail(email))
            return false;

        Long idBasket = basketService.addBasket();
        CustomUser user = new CustomUser(login, passHash, role, email, phone,basketService.getBasketById(idBasket));
        customUserRepository.save(user);

        return true;
    }
*/

    @Transactional
    public boolean saveUser(CustomUser customUser) {

        if (customUserRepository.existsByEmail(customUser.getEmail())) {
            return false;
        }

        customUserRepository.save(customUser);
        return true;
    }


    @Transactional
    public void updateUser(String login, String email, String phone) {
        CustomUser user = customUserRepository.findByEmail(email);
        if (user == null)
            return;

        user.setEmail(email);
        user.setPhone(phone);

        customUserRepository.save(user);
    }

    @Transactional
    public boolean sendUrlToChangePassword(String email) {

        CustomUser user = customUserRepository.findByEmail(email);

        if (user == null) {
            return false;
        } else {

            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to iShop. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getName(),
                    user.getUuid()
            );

            mailSender.send(user.getEmail(), "Change password", message);

            return true;
        }


    }

    @Transactional
    public CustomUser findByUuid(String uuid) {
        CustomUser user = customUserRepository.findByUuid(uuid);
        return user;
    }


    @Transactional
    public boolean changePassword(CustomUser user, String password) {

        if (user == null) {
            return false;
        }


        user.setUuid(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(password));
        customUserRepository.save(user);


        return true;
    }


}
