package com.urlshorter.site;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import io.cucumber.java.PendingException;
import io.cucumber.java.ru.*;
import org.openqa.selenium.By;

import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyStepdefs {

    private static String longUrl;
    private static String login;
    private static String password;

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

    @Дано("логин для входа - {string}, пароль - {string}")
    public void логинДляВходаПароль(String login, String password) {
        MyStepdefs.login = login;
        MyStepdefs.password = password;
    }

    @Тогда("успешная авторизация")
    public void успешнаяАвторизация() {
        open("http://localhost:8080/");
        $(By.linkText("Войти")).click();
        $(By.name("email")).setValue(login);
        $(By.name("password")).setValue(password).pressEnter();

        assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/admin-lk");
    }

    @Тогда("неудачная авторизация")
    public void неудачнаяАвторизация() {
        open("http://localhost:8080/");
        $(By.linkText("Войти")).click();
        $(By.name("email")).setValue(login);
        $(By.name("password")).setValue(password).pressEnter();

        assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/login-fail");
    }

    @Тогда("Удалить все ссылки пользователя")
    public void удалитьВсеСсылкиПользователя() {
        $("#links_numbers_selector").selectOption("Все");
        $("#selectAll").click();
        $(".delete_button_div").click();
        $(By.xpath("//button[normalize-space()=\"ДА\"]")).click();
    }

    @Когда("пользователь нажимает на кнопку создания новой ссылки")
    public void пользовательНажимаетНаКнопкуСозданияНовойСсылки() {
        $("#add_new_link_button").click();
    }

    @Тогда("ввести в текстовое поле создания новой ссылки - {string}")
    public void ввестиВТекстовоеПолеСозданияНовойСсылки(String longUrl) {
        MyStepdefs.longUrl = longUrl;
        $("#add_new_link_textbox").setValue(longUrl);
    }

    @И("нажать кнопку сократить")
    public void нажатьКнопкуСократить() {
        $(By.xpath("//button[normalize-space()=\"Сократить\"]")).click();
    }

    @То("проверяем, появилась ли ссылка в интерфейсе")
    public void проверяемПоявиласьЛиСсылкаВИнтерфейсе() {
        assert Objects.equals($(".link_edit").findAll(".long_link_textBox").find(value(longUrl)).getValue(), longUrl);
    }

    @Допустим("пользователь выбирает первую ссылку")
    public void пользовательВыбираетПервуюСсылку() {
        $(By.name("select_link")).click();
    }

    @Допустим("пользователь создаёт короткую ссылку - {string}")
    public void пользовательСоздаётКороткуюСсылку(String longUrl) {
        MyStepdefs.longUrl = longUrl;

        $("#add_new_link_button").click();
        $("#add_new_link_textbox").setValue(longUrl);
        $(By.xpath("//button[normalize-space()=\"Сократить\"]")).click();
    }

    @Затем("пользователь нажимает на кнопку удаления")
    public void пользовательНажимаетНаКнопкуУдаления() {
        $(".delete_button_div").click();
    }

    @Затем("пользователь подтверждает удаление ссылки")
    public void пользовательПодтверждаетУдалениеСсылки() {
        $(By.xpath("//button[normalize-space()=\"ДА\"]")).click();
    }

    @Тогда("проверяем, исчезла ли ссылка из интерфейса")
    public void проверяемИсчезлаЛиСсылкаИзИнтерфейса() {
        $(".links_class").find(".long_link_textBox").shouldHave(not(exist));
    }

    @Затем("пользователь вводит в поле поиска ссылки - {string}")
    public void пользовательВводитВПолеПоискаСсылки(String searchLink) {
        longUrl = searchLink;
        $("#search_text_box").setValue(searchLink);
    }

    @Затем("пользователь нажимает на кнопку поиска")
    public void пользовательНажимаетНаКнопкуПоиска() {
        $(By.xpath("//button[normalize-space()=\"Поиск\"]")).click();
    }

    @Затем("пользователь вводит в поле редактирования ссылки - {string}")
    public void пользовательВводитВПолеРедактированияСсылки(String newLink) {
        longUrl = newLink;
        $(".links_class").findAll(".long_link_textBox").get(0).setValue(newLink);
    }

    @Затем("пользователь нажимает кнопку обновить")
    public void пользовательНажимаетКнопкуОбновить() {
        $(".links_class").findAll(".link_edit").get(0).find(By.tagName("button")).click();
    }

    @Тогда("проверяем, обновилась ли ссылка в интерфейсе")
    public void проверяемОбновиласьЛиСсылкаВИнтерфейсе() {
        WebDriverRunner.getWebDriver().navigate().refresh();

        assertEquals($(".links_class").findAll(".long_link_textBox").find(value(longUrl)).getValue(), longUrl);
    }

    @Тогда("пользователь нажимает на ссылку настройки")
    public void пользовательНажимаетНаСсылкуНастройки() {
        $(By.linkText("Настройки")).click();
    }

    @Затем("проверяем, что пользователь перешёл в настройки")
    public void проверяемЧтоПользовательПерешёлВНастройки() {
        assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/user-lk/settings");
    }

    @Тогда("пользователь нажимает перейти в личный кабинет")
    public void пользовательНажимаетПерейтиВЛичныйКабинет() {
        $(By.linkText("Личный кабинет")).click();
    }

    @Затем("проеряем, чо пользователь перешёл в личный кабинет")
    public void проеряемЧоПользовательПерешёлВЛичныйКабинет() {
        assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/user-lk");
    }

    @Тогда("залогиниться как пользователь и удалить ссылки")
    public void залогинитьсяКакПользовательИУдалитьСсылки() {
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

    @Тогда("перейти в раздел ссылки")
    public void перейтиВРазделСсылки() {
        $(By.linkText("Ссылки")).click();

        assert WebDriverRunner.getWebDriver().getCurrentUrl().equals("http://localhost:8080/admin-lk/links-redaktor");
    }

    @Затем("выйти из личного кабинета")
    public void выйтиИзЛичногоКабинета() {
        $(".exit_link_div").find(By.tagName("form")).submit();
    }

    @Затем("ввести в текстовое поле поиска ссылок - {string}")
    public void ввестиВТекстовоеПолеПоискаСсылок(String searchLink) {
        longUrl = searchLink;
        $("#long_url_text_box").setValue(searchLink);
    }

    @Затем("нажать кнопку поиск")
    public void нажатьКнопкуПоиск() {
        $(By.xpath("//button[normalize-space()=\"Поиск\"]")).click();
    }

    @Тогда("проверям, появилсь ли ссылка в результате поиска")
    public void проверямПоявилсьЛиСсылкаВРезультатеПоиска() {
        $(".long_link_textBox").shouldHave(Condition.value(longUrl));
    }

    @Затем("выбираем первую ссылку")
    public void выбираемПервуюСсылку() {
        $(".linkChechBox").click();
    }

    @Затем("нажимаем кнопку удалить ссылки")
    public void нажимаемКнопкуУдалитьСсылки() {
        $(".delete_button_div").click();
    }

    @Затем("нажимаем кнопку ДА")
    public void нажимаемКнопкуДА() {
        $(By.xpath("//button[normalize-space()=\"ДА\"]")).click();
    }

    @Тогда("проверяем, исчезла ли ссылка из результатов поиска")
    public void проверяемИсчезлаЛиСсылкаИзРезультатовПоиска() {
        $(".links_class").find(".long_link_textBox").shouldHave(not(exist));
    }

    @Тогда("перейти в раздел пользователи")
    public void перейтиВРазделПользователи() {
        $(By.linkText("Пользователи")).click();
    }

    @Затем("установить роль пользователя при поиске {string}")
    public void установитьРольПользователяПриПоиске(String role) {
        $("#search_role_selector").selectOption(role);
    }

    @Тогда("проверяем, нашёлся ли пользователь с почтой - {string}")
    public void проверяемНашёлсяЛиПользовательСПочтой(String userEmail) {
        assertEquals($(By.xpath("//table/tbody/tr/td[2]")).getText(), userEmail);
    }

    @Затем("ввести в текстовое поле поиска пользователей - {string}")
    public void ввестиВТекстовоеПолеПоискаПользователей(String userEmail) {
        $("#email_text_box").setValue(userEmail);
    }

    @Затем("нажать кнопку поиска пользователей")
    public void нажатьКнопкуПоискаПользователей() {
        $("#users_search_button").click();
    }

    @Затем("нажать на чекбокс блокировки первого пользователя")
    public void нажатьНаЧекбоксБлокировкиПервогоПользователя() {
        $(By.xpath("//table/tbody/tr/td[4]")).find(By.tagName("input")).click();
    }

    @Затем("проверяем, что пользователь исчез из интерфейса незаблокированных пользователей")
    public void проверяемЧтоПользовательИсчезИзИнтерфейсаНезаблокированныхПользователей() {
        $(By.xpath("//table/tbody/tr/td[2]")).shouldHave(not(exist));
    }

    @Затем("нажимаем чекбокс для поиска заблокированных пользователей")
    public void нажимаемЧекбоксДляПоискаЗаблокированныхПользователей() {
        $("#user_blocked_search").click();
    }

    @Тогда("проверяем, что первый найденный пользователь имеет почту - {string}")
    public void проверяемЧтоПервыйНайденныйПользовательИмеетПочту(String userEmail) {
        assertEquals($(By.xpath("//table/tbody/tr/td[2]")).getText(), userEmail);
    }
}