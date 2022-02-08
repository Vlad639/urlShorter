package com.urlshorter.site.controllers;

import com.urlshorter.site.audit.Producer;
import com.urlshorter.site.models.Link;
import com.urlshorter.site.repositories.LinkRepository;
import com.urlshorter.site.audit.ActionEnum;
import com.urlshorter.site.audit.AuditMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
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

    Producer auditProducer = new Producer();

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
    ModelAndView deleteLinks(@RequestParam("deleted_array_links[]") List<Long> deletedLinksIds, Authentication auth){
        List<Link> links = linkRepository.findAllById(deletedLinksIds);
        linkRepository.deleteAllById(deletedLinksIds);

        for (Link link: links)
            auditProducer.produce(new AuditMessage(
                    auth.getName(),
                    ActionEnum.DELETE,
                    link
            ));

        return searchLinks(searchedLinks);
    }


    @RequestMapping("")
    String adminLinkRedactor(){

        return "links-redaktor";
    }

}
