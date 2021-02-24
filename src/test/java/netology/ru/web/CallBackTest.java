package netology.ru.web;

import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.util.Random;


public class CallBackTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTestHappyPathWithFaker() {
        int firstMeeting = DataGenerator.Registration.generateRandomNumber(3,180);
        int secondMeeting = DataGenerator.Registration.generateRandomNumber(firstMeeting+1,firstMeeting+180);
        $("[data-test-id='city'] input").setValue(DataGenerator.Registration.generateClientPersonalData("ru").getCity());
        $("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(DataGenerator.Registration.generateDateOfMeeting(firstMeeting));
        $("[data-test-id='name'] input").setValue(DataGenerator.Registration.generateClientPersonalData("ru").getFullName());
        $("[data-test-id='phone'] input").setValue(DataGenerator.Registration.generateClientPersonalData("ru").getPhoneNumber());
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(visible, ofSeconds(15));
        $("[data-test-id='success-notification']>.notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + DataGenerator.Registration.generateDateOfMeeting(firstMeeting)));
        $("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(DataGenerator.Registration.generateDateOfMeeting(secondMeeting));
        $(withText("Запланировать")).click();
        $("[data-test-id='replan-notification']").shouldBe(visible);
        $("[data-test-id='replan-notification']>.notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(withText("Перепланировать")).click();
        $("[data-test-id='success-notification']>.notification__content").shouldBe(visible)
                .shouldHave(exactText("Встреча успешно запланирована на " + DataGenerator.Registration.generateDateOfMeeting(secondMeeting)));
    }
}
