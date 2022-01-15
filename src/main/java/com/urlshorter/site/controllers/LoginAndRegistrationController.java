package com.urlshorter.site.controllers;

import com.urlshorter.site.models.User;
import com.urlshorter.site.other.EmailSenderService;
import com.urlshorter.site.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginAndRegistrationController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @GetMapping("/login")
    String login(){
        return "login";
    }

    @GetMapping("/registration")
    String registration(){

//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setTo("dyadyavadya639@gmail.com");
//        simpleMailMessage.setSubject("Подтверждение регистрации");
//        simpleMailMessage.setFrom("SmalDyadyaVadya123@yandex.ru");
//        simpleMailMessage.setText("Для подтверждения регистрации перейдите по ссылке:");
//
//
//        emailSenderService.sendEmail(simpleMailMessage);


        return "registration";
    }

    @PostMapping("/registration")
    String newUser(User user, Model model){
        User userFromDB = usersRepository.findByEmail(user.getEmail());

        if (userFromDB != null){
            model.addAttribute("registrationMessage", "Пользователь уже существует");
            return "registration";
        }

        user.setActive(true);
        user.setRole("user");
        user.setBlocked(false);
        usersRepository.save(user);

        model.addAttribute("registrationMessage", "Регистрация успешна");
        return "registration";
    }
}
