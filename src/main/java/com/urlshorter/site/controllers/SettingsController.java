package com.urlshorter.site.controllers;

import com.urlshorter.site.audit.AuditProducer;
import com.urlshorter.site.models.User;
import com.urlshorter.site.other.CheckPasswordResult;
import com.urlshorter.site.other.CheckerPassword;
import com.urlshorter.site.repositories.UsersRepository;
import com.urlshorter.site.audit.ActionEnum;
import com.urlshorter.site.audit.AuditMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SettingsController {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    AuditProducer auditProducer;

    @RequestMapping("/to-lk")
    String toAccount(Authentication auth){
        User user = usersRepository.findByEmail(auth.getName());

        if (user.getRole().equals("user")){
            return "redirect:user-lk";
        }

        if (user.getRole().equals("admin"))
            return "redirect:admin-lk";

        return "settings";

    }

    @RequestMapping("/change-pass")
    String changePassword(Model model,
                          @RequestParam("old_pass") String oldPass,
                          @RequestParam("new_pass") String newPass,
                          @RequestParam("new_pass_2") String newPassRepeat,
                          Authentication auth)
    {
        String changePassMessage;

        User user = usersRepository.findByEmail(auth.getName());
        String currentPass = user.getPassword();

        if (!oldPass.equals(currentPass)){
            changePassMessage = "Старый пароль введён неверно!";
        }
        else
        if (!newPass.equals(newPassRepeat)){
            changePassMessage = "Новые пароли не совпадают!";
        }
        else {

            CheckPasswordResult checkPasswordResult = CheckerPassword.checkPassword(newPass);
            changePassMessage = checkPasswordResult.message;

            if (checkPasswordResult.passwordIsOk){
                user.setPassword(newPass);
                usersRepository.save(user);
                changePassMessage = "Пароль успешно изменён!";

                model.addAttribute("messageColorProp", "color: green");
                model.addAttribute("changePasswordMessage", changePassMessage);

                auditProducer.produce(
                        new AuditMessage(
                                user.getEmail(),
                                ActionEnum.CHANGE_PASSWORD,
                                "OK")
                );

                return "settings";
            }
        }

        model.addAttribute("messageColorProp", "color: red");
        model.addAttribute("changePasswordMessage", changePassMessage);

        return "settings";
    }
}
