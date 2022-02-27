package com.urlshorter.site;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import com.urlshorter.site.repositories.LinkRepository;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AdminLKSeleniumTests {

    @Autowired
    LinkRepository linkRepository;

    void login(){
        open("http://localhost:8080/");
        $(By.linkText("Войти")).click();
        $(By.name("email")).setValue("somek90585@rippb.com");
        $(By.name("password")).setValue("QIIfsCR2YK#T").pressEnter();
    }

    void loginAsUserAndClearLinks(){
        String testEmail = "selayey709@porjoton.com";
        String testPassword = "hAOVG8*h4yXS";

        open("http://localhost:8080/");
        $(By.linkText("Войти")).click();
        $(By.name("email")).setValue(testEmail);
        $(By.name("password")).setValue(testPassword).pressEnter();
        $("#links_numbers_selector").selectOption("Все");

        $("#selectAll").click();
        $(".delete_button_div").click();
        $(By.xpath("//button[normalize-space()=\"ДА\"]")).click();
    }

    @Test
    void successLogin() {
        login();

        assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/admin-lk");
    }

    @Test
    void goToLinksRedactor() {
        login();
        $(By.linkText("Ссылки")).click();

        assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/admin-lk/links-redaktor");
    }

    @Test
    void goToUsersRedactor() {
        login();
        $(By.linkText("Пользователи")).click();

        assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/admin-lk/users-redaktor");
    }

    @Test
    void searchLink() {
        loginAsUserAndClearLinks();
        $("#add_new_link_button").click();
        $("#add_new_link_textbox").setValue("https://test.ru");
        $(By.xpath("//button[normalize-space()=\"Сократить\"]")).click();
        $(".exit_link_div").find(By.tagName("form")).submit();

        login();

        $(By.linkText("Ссылки")).click();
        $("#long_url_text_box").setValue("test");
        $(By.xpath("//button[normalize-space()=\"Поиск\"]")).click();

        $(".long_link_textBox").shouldHave(Condition.value("https://test.ru"));

    }

    @Test
    void deleteLink(){
        loginAsUserAndClearLinks();
        $("#add_new_link_button").click();
        $("#add_new_link_textbox").setValue("https://test.ru");
        $(By.xpath("//button[normalize-space()=\"Сократить\"]")).click();
        $(".exit_link_div").find(By.tagName("form")).submit();

        login();

        $(By.linkText("Ссылки")).click();
        $("#long_url_text_box").setValue("test");
        $(By.xpath("//button[normalize-space()=\"Поиск\"]")).click();

        $(".linkChechBox").click();

        $(".delete_button_div").click();
        $(By.xpath("//button[normalize-space()=\"ДА\"]")).click();

        $(".links_class").find(".long_link_textBox").shouldHave(not(exist));
    }

    @Test
    void simpleSearchTest(){
        login();
        $(By.linkText("Пользователи")).click();
        $("#email_text_box").setValue("selayey");
        $("#users_search_button").click();

        assertEquals($(By.xpath("//table/tbody/tr/td[2]")).getText(), "selayey709@porjoton.com");
    }

    @Test
    void roleSearch(){
        login();
        $(By.linkText("Пользователи")).click();
        $("#search_role_selector").selectOption("Администратор");
        $("#users_search_button").click();

        assertEquals($(By.xpath("//table/tbody/tr/td[2]")).getText(), "somek90585@rippb.com");
    }

    @Test
    void blockUnblockUserAndSearchHim() {
        login();
        $(By.linkText("Пользователи")).click();
        $("#email_text_box").setValue("selayey");
        $("#users_search_button").click();

        $(By.xpath("//table/tbody/tr/td[4]")).find(By.tagName("input")).click();
        $("#users_search_button").click();

        $(By.xpath("//table/tbody/tr/td[2]")).shouldHave(not(exist));

        $("#user_blocked_search").click();
        $("#email_text_box").setValue("selayey");
        $("#users_search_button").click();
        $(By.xpath("//table/tbody/tr/td[4]")).find(By.tagName("input")).click();

        $("#user_blocked_search").click();
        $("#users_search_button").click();

        assertEquals($(By.xpath("//table/tbody/tr/td[2]")).getText(), "selayey709@porjoton.com");
    }
}
