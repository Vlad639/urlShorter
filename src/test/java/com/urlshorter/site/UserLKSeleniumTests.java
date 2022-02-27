package com.urlshorter.site;

import com.codeborne.selenide.WebDriverRunner;
import com.urlshorter.site.repositories.LinkRepository;
import com.urlshorter.site.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserLKSeleniumTests {

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	LinkRepository linkRepository;


	void loginAndClearLinks(){
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
	void goToLoginPage(){
		open("http://localhost:8080/");
		$(By.linkText("Войти")).click();

		assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/login");

	}

	@Test
	void goToRegistrationPage(){
		open("http://localhost:8080/");
		$(By.linkText("Регистрация")).click();

		assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/registration");
	}

	@Test
	void successLogin(){
		loginAndClearLinks();
		$("#user_name").shouldHave(text("selayey709@porjoton.com"));

	}

	@Test
	void failLogin(){
		open("http://localhost:8080/");
		$(By.linkText("Войти")).click();
		$(By.name("email")).setValue("___");
		$(By.name("password")).setValue("___").pressEnter();

		assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/login-fail");

	}

	@Test
	void addNewLink(){
		loginAndClearLinks();
		$("#add_new_link_button").click();
		$("#add_new_link_textbox").setValue("https://yandex.ru/");
		$(By.xpath("//button[normalize-space()=\"Сократить\"]")).click();

		assert Objects.equals($(".link_edit").findAll(".long_link_textBox").find(value("https://yandex.ru/")).getValue(), "https://yandex.ru/");
	}

	@Test
	void deleteLink(){
		loginAndClearLinks();
		$("#add_new_link_button").click();
		$("#add_new_link_textbox").setValue("testDelete");
		$(By.xpath("//button[normalize-space()=\"Сократить\"]")).click();


		$(By.name("select_link")).click();
		$(".delete_button_div").click();
		$(By.xpath("//button[normalize-space()=\"ДА\"]")).click();

		$(".links_class").find(".long_link_textBox").shouldHave(not(exist));

	}

	@Test
	void updateLink() {
		loginAndClearLinks();

		$("#add_new_link_button").click();
		$("#add_new_link_textbox").setValue("https://yandex.ru/");
		$(By.xpath("//button[normalize-space()=\"Сократить\"]")).click();

		$(".links_class").findAll(".long_link_textBox").get(0).setValue("testUpdateText");
		$(".links_class").findAll(".link_edit").get(0).find(By.tagName("button")).click();

		assertEquals($(".links_class").findAll(".long_link_textBox").find(value("testUpdateText")).getValue(), "testUpdateText");
	}


	@Test
	void goToSettings() {
		loginAndClearLinks();
		$(By.linkText("Настройки")).click();

		assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/user-lk/settings");
	}

	@Test
	void goToLK(){
		loginAndClearLinks();
		$(By.linkText("Настройки")).click();
		$(By.linkText("Личный кабинет")).click();

		assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/user-lk");
	}

	@Test
	void searchLinks() {
		loginAndClearLinks();

		$("#add_new_link_button").click();
		$("#add_new_link_textbox").setValue("https://yandex.ru/");
		$(By.xpath("//button[normalize-space()=\"Сократить\"]")).click();

		$("#search_text_box").setValue("yandex");
		$(By.xpath("//button[normalize-space()=\"Поиск\"]")).click();

		assertEquals($(".links_class").findAll(".long_link_textBox").find(value("https://yandex.ru/")).getValue(), "https://yandex.ru/");
	}

	@Test
	void logout() {
		loginAndClearLinks();
		$(".exit_link_div").find(By.tagName("form")).submit();

		assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/login?logout");
	}


}
