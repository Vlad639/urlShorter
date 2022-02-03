package com.urlshorter.site.controllers;

import com.urlshorter.site.workwithkafka.*;
import com.urlshorter.site.other.UrlCoderAndDecoder;
import com.urlshorter.site.models.Link;
import com.urlshorter.site.models.User;
import com.urlshorter.site.repositories.LinkRepository;
import com.urlshorter.site.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/user-lk")
public class UsersController {

    private static User user;
    private int currentPage = 1;
    private int linksOnPage = 10;
    private int userLinksSize = 0;
    private boolean showAllLinks = false;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    ProducerService producerService;


    public static void SetUser(User newUser){
        user = newUser;
    }

    ModelAndView getInstrumentAndLinks(){
        ModelAndView modelAndView = new ModelAndView("user-lk::instrument_and_links_table");

        user = usersRepository.findByEmail(user.getEmail());

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
    ModelAndView searchLink(@RequestParam("linkFragment") String linkFragment){

        ModelAndView modelAndView = new ModelAndView("user-lk::instrument_and_links_table");

        if (linkFragment.isEmpty()){
            return getInstrumentAndLinks();
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
    String UserAccount(Model model){

        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("userIsBlocked", user.isBlocked());

        if (user.isBlocked())
            model.addAttribute("blockMessage", "Вы заблокированы!");

        return "user-lk";
    }

    @RequestMapping("/add-new-link")
    ModelAndView addNewLink(@RequestParam("longLink") String longLink){

        if (!user.isBlocked()) {

            Link link = new Link(user, "", longLink);
            linkRepository.save(link);

            long linkId = link.getId();
            link.setUrlToken(UrlCoderAndDecoder.hashids.encode(linkId));

            linkRepository.save(link);

            producerService.produce(new KafkaMessage(user.getEmail(), ActionEnum.CREATE, link));
        }

        return getInstrumentAndLinks();
    }

    @RequestMapping("/update-link")
    ModelAndView updateLongLink(
            @RequestParam("link_id") Long linkId,
            @RequestParam("new_long_link") String newLongLink
    ){

        if (!user.isBlocked()) {
            Link link = linkRepository.getById(linkId);
            Link oldLink = new Link(user, link.getUrlToken(), link.getLongLink());

            link.setLongLink(newLongLink);
            linkRepository.save(link);

            producerService.produce(
                    new KafkaMessage(
                            user.getEmail(),
                            ActionEnum.UPDATE_LINK,
                            oldLink,
                            newLongLink
                    )
            );

        }

        return getInstrumentAndLinks();
    }

    @RequestMapping("/delete-links")
    ModelAndView deleteLinks(@RequestParam("deleted_array_links[]") List<Long> deletedLinksIds){
        List<Link> deletedLinks = linkRepository.findAllById(deletedLinksIds);
        linkRepository.deleteAllById(deletedLinksIds);

        for (Link link: deletedLinks) {
            producerService.produce(new KafkaMessage(
                    user.getEmail(),
                    ActionEnum.DELETE,
                    link
            ));
        }

        return getInstrumentAndLinks();
    }

    @RequestMapping("/prev-links-page")
    ModelAndView prevLinksPage(){
        if (currentPage >= 2)
            currentPage -= 1;

        return getInstrumentAndLinks();
    }

    @RequestMapping("/next-links-page" )
    ModelAndView nextLinksPage(){
        if (currentPage * linksOnPage < userLinksSize)
            currentPage += 1;

        return getInstrumentAndLinks();
    }

    @RequestMapping("/change-links-page")
    ModelAndView changeLinksPageNumbers(@RequestParam("links_on_page") Integer linksOnPage){
        this.linksOnPage = linksOnPage;
        currentPage = 1;

        return getInstrumentAndLinks();
    }

    @RequestMapping("/settings")
    String settingsPage(Model model){
        model.addAttribute("userEmail", user.getEmail());

        return "settings";
    }

}
