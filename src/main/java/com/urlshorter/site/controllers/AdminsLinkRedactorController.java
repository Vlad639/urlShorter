package com.urlshorter.site.controllers;

import com.urlshorter.site.models.Link;
import com.urlshorter.site.models.User;
import com.urlshorter.site.other.CheckPassword;
import com.urlshorter.site.other.CheckPasswordResult;
import com.urlshorter.site.repositories.LinkRepository;
import com.urlshorter.site.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin-lk/links-redaktor")
public class AdminsLinkRedactorController {

    private  String searchedLinks = "";

    @Autowired
    LinkRepository linkRepository;

    @RequestMapping("/search")
    ModelAndView searchLinks(@RequestParam("longUrlFragment") String linkFragment){

        ModelAndView modelAndView = new ModelAndView("links-redaktor::links_table_div");

        if (linkFragment.isEmpty()){
            return new ModelAndView("links-redaktor::links_table_div");
        }

        searchedLinks = linkFragment;
        List<Link> foundedLinks = linkRepository.findLinksByLongLinkLike("%" + linkFragment + "%");

        modelAndView.addObject("links", foundedLinks);

        return modelAndView;
    }

    @RequestMapping("/delete-links")
    ModelAndView deleteLinks(@RequestParam("deleted_array_links[]") List<Long> deletedLinksIds){
        linkRepository.deleteAllById(deletedLinksIds);

        return searchLinks(searchedLinks);
    }


    @RequestMapping("")
    String adminLinkRedactor(){

        return "links-redaktor";
    }

}
