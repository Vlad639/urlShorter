package com.urlshorter.site.controllers;

import com.urlshorter.site.config.PassEncoder;
import com.urlshorter.site.models.User;
import com.urlshorter.site.other.*;
import com.urlshorter.site.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginAndRegistrationController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    PassEncoder passwordEncoder;

    CheckPasswordResult checkPassword(String password){

        return CheckerPassword.checkPassword(password);
    }


    @GetMapping("/get-new-pass")
    String resetPassword(@RequestParam("email") String email, Model model){

        User user = usersRepository.findByEmail(email);

        if ( user == null){
            model.addAttribute("resetPasswordMessage", "Пользователь с такой почтой не существует!");
        }
        else {
            String newPassword = PasswordGenerator.generatePassword();

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Восстановление пароля url Shorter");
            simpleMailMessage.setFrom("SmalDyadyaVadya123@yandex.ru");
            simpleMailMessage.setText("Ваш новый пароль: " + newPassword);

            user.setPassword(newPassword);
            usersRepository.save(user);

            emailSenderService.sendEmail(simpleMailMessage);
            model.addAttribute("resetPasswordMessage", "Новый пароль отправлен на почту.");
        }

        return "reset-password";
    }


    @GetMapping("/reset-password")
    String resetPasswordPage(){

        return "reset-password";
    }

    @GetMapping("/login-fail")
    String loginFailPage(Model model){
        model.addAttribute("failLoginMessage", "Некорректные даные для входа!");

        return "login";
    }

    @GetMapping("/login")
    String loginPage(){

        return "login";
    }

    @GetMapping("/registration")
    String registrationPage(){

        return "registration";
    }

    @GetMapping("/confirm-account")
        String confirmAccount(@RequestParam("token") String token){

        User confirmUser = usersRepository.findByToken(token);
        confirmUser.setActive(true);
        usersRepository.save(confirmUser);

        return "succes-confirm";
    }


    @GetMapping("/reg-new-user")
    ModelAndView newUser(@RequestParam("email") String email,
                         @RequestParam("password") String password
    ){

        ModelAndView modelAndView = new ModelAndView("registration::reg_message_fragm");

        User userFromDB = usersRepository.findByEmail(email);

        if (userFromDB != null){
            modelAndView.addObject("regMessage", "Пользователь с такой почтой уже существует!");
            return modelAndView;
        }

        if (checkPassword(password).passwordIsOk){
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.passwordEncoder().encode(password));
            newUser.setActive(false);
            newUser.setRole("user");
            newUser.setBlocked(false);
            usersRepository.save(newUser);

            String confirmToken = RegistrationConfirmTokenGenerator.hashids.encode(newUser.getId());
            newUser.setToken(confirmToken);
            usersRepository.save(newUser);

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Подтверждение регистрации url Shorter");
            simpleMailMessage.setFrom("SmalDyadyaVadya123@yandex.ru");
            simpleMailMessage.setText("Для подтверждения регистрации перейдите по ссылке: "
            +"http://localhost:8080/confirm-account?token="+confirmToken);

            emailSenderService.sendEmail(simpleMailMessage);

        }
        else {
            modelAndView.addObject("regMessage", checkPassword(password).message);
        }

        return modelAndView;
    }
}
