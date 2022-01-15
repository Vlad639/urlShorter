package com.urlshorter.site.controllers;

import com.urlshorter.site.other.CheckPassword;
import com.urlshorter.site.other.CheckPasswordResult;
import com.urlshorter.site.other.EmailSenderService;
import com.urlshorter.site.other.UrlCoderAndDecoder;
import com.urlshorter.site.models.Link;
import com.urlshorter.site.models.User;
import com.urlshorter.site.repositories.LinkRepository;
import com.urlshorter.site.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/user-lk")
public class UsersController {

    private User user;
    private int currentPage = 1;
    private int linksOnPage = 10;
    private int userLinksSize = 0;
    private boolean showAllLinks = false;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    LinkRepository linkRepository;

    ModelAndView getInstrumentAndLinks(Authentication authentication){
        ModelAndView modelAndView = new ModelAndView("user-lk::instrument_and_links_table");

        user = usersRepository.findByEmail(authentication.getName());
        List<Link> userAllLinks = user.getLinks();
        List<Link> modelLinks = new ArrayList<>();


        if (linksOnPage == -1){
            linksOnPage = Integer.MAX_VALUE;
            showAllLinks = true;
        }
        else showAllLinks = false;

        int startIndex = (currentPage-1) * linksOnPage;
        int finishIndex = startIndex + linksOnPage;
        userLinksSize = userAllLinks.size();

        if (finishIndex > userLinksSize)
            finishIndex = userLinksSize;

        for (int i = startIndex; i < finishIndex; i++) {
            modelLinks.add(userAllLinks.get(i));
        }

        int maxPagesNumber = userLinksSize / linksOnPage;
        if (userLinksSize % linksOnPage != 0)
            maxPagesNumber += 1;

        if (maxPagesNumber == 0)
            maxPagesNumber = 1;

        modelAndView.addObject("links", modelLinks);
        modelAndView.addObject("pageNumber", currentPage + "/" + maxPagesNumber );

        if (!showAllLinks) {
            modelAndView.addObject("linkNumbersInSelector", linksOnPage);
        } else {
            linksOnPage = -1;
            modelAndView.addObject("linkNumbersInSelector", -1);
        }

        modelAndView.addObject("userIsBlocked", user.isBlocked());

        return modelAndView;
    }

    @RequestMapping("/search")
    ModelAndView searchLink(@RequestParam("linkFragment") String linkFragment, Authentication auth){

        ModelAndView modelAndView = new ModelAndView("user-lk::instrument_and_links_table");

        if (linkFragment.isEmpty()){
            return getInstrumentAndLinks(auth);
        }

        List<Link> userAllLinks = user.getLinks();
        List<Link> foundedLinks = new ArrayList<>();

        for (Link link: userAllLinks)
            if (link.getLongLink().toLowerCase().contains(linkFragment.toLowerCase()) ||
                    link.getUrlToken().toLowerCase().contains(linkFragment.toLowerCase())){
                foundedLinks.add(link);
            }

        currentPage = 1;

        modelAndView.addObject("links", foundedLinks);
        modelAndView.addObject("linkNumbersInSelector", -1);
        modelAndView.addObject("userIsBlocked", user.isBlocked());

        return modelAndView;
    }

    @RequestMapping("")
    String UserAccount(Model model, Authentication authentication){

        user = usersRepository.findByEmail(authentication.getName());
        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("userIsBlocked", user.isBlocked());

        return "user-lk";
    }

    @RequestMapping("/add-new-link")
    ModelAndView addNewLink(@RequestParam("longLink") String longLink, Authentication auth){

        if (!user.isBlocked()) {

            Link link = new Link(user, "", longLink);
            linkRepository.save(link);

            long linkId = link.getId();
            link.setUrlToken(UrlCoderAndDecoder.hashids.encode(linkId));

            linkRepository.save(link);
        }

        return getInstrumentAndLinks(auth);
    }

    @RequestMapping("/update-link")
    ModelAndView updateLongLink(
            @RequestParam("link_id") Long linkId,
            @RequestParam("new_long_link") String newLongLink,
            Authentication auth
    ){


        if (!user.isBlocked()) {
            Link link = linkRepository.getById(linkId);
            link.setLongLink(newLongLink);
            linkRepository.save(link);
        }

        return getInstrumentAndLinks(auth);
    }

    @RequestMapping("/delete-links")
    ModelAndView deleteLinks(@RequestParam("deleted_array_links[]") List<Long> deletedLinksIds, Authentication auth){
        linkRepository.deleteAllById(deletedLinksIds);

        return getInstrumentAndLinks(auth);
    }

    @RequestMapping("/prev-links-page")
    ModelAndView prevLinksPage(Authentication auth){
        if (currentPage >= 2)
            currentPage -= 1;

        return getInstrumentAndLinks(auth);
    }

    @RequestMapping("/next-links-page" )
    ModelAndView nextLinksPage(Authentication auth){
        if (currentPage * linksOnPage < userLinksSize)
            currentPage += 1;

        return getInstrumentAndLinks(auth);
    }

    @RequestMapping("/change-links-page")
    ModelAndView changeLinksPageNumbers(@RequestParam("links_on_page") Integer linksOnPage, Authentication auth){
        this.linksOnPage = linksOnPage;
        currentPage = 1;

        return getInstrumentAndLinks(auth);
    }

    @RequestMapping("/settings")
    String settingsPage(Model model){
        model.addAttribute("userEmail", user.getEmail());

        return "settings";
    }

    @RequestMapping("/change-pass")
    String changePassword(Model model,
                          @RequestParam("old_pass") String oldPass,
                          @RequestParam("new_pass") String newPass,
                          @RequestParam("new_pass_2") String newPassRepeat)
    {
        String changePassMessage;
        String currentPass = user.getPassword();

        if (!oldPass.equals(currentPass)){
            changePassMessage = "Старый пароль введён неверно!";
        }
        else
        if (!newPass.equals(newPassRepeat)){
            changePassMessage = "Новые пароли не совпадают!";
        }
        else {

            CheckPasswordResult checkPasswordResult = CheckPassword.checkPassword(newPass);
            changePassMessage = checkPasswordResult.message;

            if (checkPasswordResult.passwordIsOk){
                user.setPassword(newPass);
                usersRepository.save(user);
                changePassMessage = "Пароль успешно изменён!";

                model.addAttribute("messageColorProp", "color: green");
                model.addAttribute("changePasswordMessage", changePassMessage);

                return "settings";
            }
        }

        model.addAttribute("messageColorProp", "color: red");
        model.addAttribute("changePasswordMessage", changePassMessage);

        return "settings";
    }

}