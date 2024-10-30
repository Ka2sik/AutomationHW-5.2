package ru.netology.registrationform.test;

import com.codeborne.selenide.Condition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.registrationform.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.registrationform.data.DataGenerator.Registration.getUser;
import static ru.netology.registrationform.data.DataGenerator.generateLogin;
import static ru.netology.registrationform.data.DataGenerator.generatePassword;

class AuthTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldLoginRegisteredUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет"), Condition.visible);
    }

    @Test
    void shouldShowErrorWhenUserIsNotRegistered() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"),
                        Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldShowErrorWhenUserIsBlocked() {
        var registeredUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"),
                        Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldShowErrorWhenPasswordIsWrong() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(generatePassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"),
                        Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldShowErrorWhenUsernameIsWrong() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(generateLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"),
                        Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }
}
