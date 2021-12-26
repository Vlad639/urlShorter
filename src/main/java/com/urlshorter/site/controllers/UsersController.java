package com.urlshorter.site.controllers;

import com.urlshorter.site.models.UrlCoderAndDecoder;
import com.urlshorter.site.models.Link;
import com.urlshorter.site.models.User;
import com.urlshorter.site.repositories.LinkRepository;
import com.urlshorter.site.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user-lk")
public class UsersController {


    private User user;

    private void setUser(){
        user = usersRepository.getById(1L);
    }

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    LinkRepository linkRepository;

    @RequestMapping("")
    String UserAccount(Model model){
        setUser();
        model.addAttribute("links", user.getLinks());
        return "user-lk";
    }

    @RequestMapping("/add-new-link")
    String addNewLink(Model model, @RequestParam("longLink") String longLink){
        Link link = new Link(user,"",longLink);
        linkRepository.save(link);
        long linkId = link.getId();
        link.setUrlToken(UrlCoderAndDecoder.hashids.encode(linkId));
        linkRepository.save(link);

        return "redirect:" + "";
    }
}
