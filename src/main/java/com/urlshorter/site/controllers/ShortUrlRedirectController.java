package com.urlshorter.site.controllers;

import com.urlshorter.site.models.UrlCoderAndDecoder;
import com.urlshorter.site.repositories.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ShortUrlRedirectController {

    @Autowired
    LinkRepository linkRepository;

    @RequestMapping(value = "/{shortUrl}", method = RequestMethod.GET)
    public String redirect(@PathVariable("shortUrl") String shortUrl){
        try {
            long urlId = UrlCoderAndDecoder.hashids.decode(shortUrl)[0];
            String longUrl = linkRepository.getById(urlId).getLongLink();
            return "redirect:" + longUrl;

        }
        catch (Exception e){
            return "/link-not-found";
        }
    }
}
