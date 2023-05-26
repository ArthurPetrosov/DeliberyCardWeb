package ru.netology.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DeliveryCardWebTest {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    String deliveryDate = generateDate(3);

    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldSendHappyPathOrder() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement'] span").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .should(exactText("Встреча успешно забронирована на " + deliveryDate));
    }

    @Test
    void shouldSendHappyPathOrderSearchCityOfListTake1() {
        $("[data-test-id='city'] input").setValue("Мос");
        $$(".menu-item__control").first().click();
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement'] span").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .should(exactText("Встреча успешно забронирована на " + deliveryDate));
    }

    @Test
    void shouldSendOrderWithWrongCity() {
        $("[data-test-id='city'] input").setValue("Китайск");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement'] span").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='city'].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(5))
                .should(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldSendOrderWithWrongCityOnEnglish() {
        $("[data-test-id='city'] input").setValue("Moscow");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement'] span").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='city'].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(5))
                .should(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldSendOrderWithWrongNameOnEnglish() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Ivanov Ivan");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement'] span").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='name'].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(5))
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldSendHappyPathOrderNameWithDefis() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Иванов-Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement'] span").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .should(exactText("Встреча успешно забронирована на " + deliveryDate));
    }

    @Test
    void shouldSendOrderWithWrongPhone() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+7999999999");
        $("[data-test-id='agreement'] span").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='phone'].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(5))
                .shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldSendOrderWithoutCheckBox() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='agreement'].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(5))
                .shouldHave(text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldSendOrderWithCityEmpty() {
        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement'] span").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='city'].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(5))
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendOrderWithNameEmpty() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement'] span").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='name'].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(5))
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendCityOrderWithPhoneEmpty() {
        $("[data-test-id='city'] input").setValue("Москва");
        String deliveryDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(deliveryDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("");
        $("[data-test-id='agreement'] span").click();
        $x("//*[contains(text(), 'Забронировать')]").click();
        $("[data-test-id='phone'].input_invalid")
                .shouldBe(visible, Duration.ofSeconds(5))
                .shouldHave(text("Поле обязательно для заполнения"));
    }


}
