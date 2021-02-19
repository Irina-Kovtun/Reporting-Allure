package netology.ru.web;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CallBackTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSendValidFormUsingComplexElements() {
        $("[data-test-id='city'] input").setValue("Ка");
        $(".input__menu").find(withText("Калуга")).click();
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        LocalDate currentDate = LocalDate.now();
        LocalDate dateOfMeeting = LocalDate.now().plusDays(7);
        if (currentDate.getMonthValue() != dateOfMeeting.getMonthValue()) {
            $(".calendar__arrow_direction_right[data-step='1']").click();
        }
        $$("td.calendar__day").find(exactText(String.valueOf(dateOfMeeting.getDayOfMonth()))).click();
        $("[data-test-id='name'] input").setValue("Василий Васильев");
        $("[data-test-id='phone'] input").setValue("+12345678999");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        String dateOfMeetingFormatted = dateOfMeeting.format(ofPattern("dd.MM.yyyy"));
        $("[data-test-id=notification] .notification__content").shouldBe(visible, ofSeconds(15))
                .shouldHave(exactText("Встреча успешно забронирована на " + dateOfMeetingFormatted));
    }

    @Test
    void shouldTestHappyPath() {
        $("[data-test-id='city'] input").setValue("Москва");
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        String dateOfMeeting = LocalDate.now().plusDays(3).format(ofPattern("dd.MM.yyyy"));
        dateField.setValue(dateOfMeeting);
        $("[data-test-id='name'] input").setValue("Василий");
        $("[data-test-id='phone'] input").setValue("+12345678999");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=notification] .notification__content").shouldBe(visible, ofSeconds(15))
                .shouldHave(exactText("Встреча успешно забронирована на " + dateOfMeeting));
    }

    @Test
    void shouldWarnWhenCityFieldIsEmpty() {
        $("[data-test-id='city'] input").setValue("");
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        String dateOfMeeting = LocalDate.now().plusDays(3).format(ofPattern("dd.MM.yyyy"));
        dateField.setValue(dateOfMeeting);
        $("[data-test-id='name'] input").setValue("Василий");
        $("[data-test-id='phone'] input").setValue("+12345678999");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldWarnWhenCityIsNotAdministrativeCenter() {
        $("[data-test-id='city'] input").setValue("Егорьевск");
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        String dateOfMeeting = LocalDate.now().plusDays(3).format(ofPattern("dd.MM.yyyy"));
        dateField.setValue(dateOfMeeting);
        $("[data-test-id='name'] input").setValue("Василий");
        $("[data-test-id='phone'] input").setValue("+12345678999");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldWarnWhenCityIsInLatinLetters() {
        $("[data-test-id='city'] input").setValue("Moscow");
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        String dateOfMeeting = LocalDate.now().plusDays(3).format(ofPattern("dd.MM.yyyy"));
        dateField.setValue(dateOfMeeting);
        $("[data-test-id='name'] input").setValue("Василий");
        $("[data-test-id='phone'] input").setValue("+12345678999");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='city'] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldWarnIfDateIsNotSelected() {
        $("[data-test-id='city'] input").setValue("Москва");
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='name'] input").setValue("Василий");
        $("[data-test-id='phone'] input").setValue("+12345678999");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void shouldWarnIfMeetingDateIsEarlierThanThreeDays() {
        $("[data-test-id='city'] input").setValue("Москва");
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        String dateOfMeeting = LocalDate.now().plusDays(1).format(ofPattern("dd.MM.yyyy"));
        dateField.setValue(dateOfMeeting);
        $("[data-test-id='name'] input").setValue("Василий");
        $("[data-test-id='phone'] input").setValue("+12345678999");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldWarnIfNameFieldIsEmpty() {
        $("[data-test-id='city'] input").setValue("Москва");
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        String dateOfMeeting = LocalDate.now().plusDays(3).format(ofPattern("dd.MM.yyyy"));
        dateField.setValue(dateOfMeeting);
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id='phone'] input").setValue("+12345678999");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldWarnIfNameInLatinLetters() {
        $("[data-test-id='city'] input").setValue("Москва");
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        String dateOfMeeting = LocalDate.now().plusDays(3).format(ofPattern("dd.MM.yyyy"));
        dateField.setValue(dateOfMeeting);
        $("[data-test-id='name'] input").setValue("John Connor");
        $("[data-test-id='phone'] input").setValue("+12345678999");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldWarnIfNameIsWithNumbers() {
        $("[data-test-id='city'] input").setValue("Москва");
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        String dateOfMeeting = LocalDate.now().plusDays(3).format(ofPattern("dd.MM.yyyy"));
        dateField.setValue(dateOfMeeting);
        $("[data-test-id='name'] input").setValue("777");
        $("[data-test-id='phone'] input").setValue("+12345678999");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldWarnIfPhoneFieldIsEmpty() {
        $("[data-test-id='city'] input").setValue("Москва");
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        String dateOfMeeting = LocalDate.now().plusDays(3).format(ofPattern("dd.MM.yyyy"));
        dateField.setValue(dateOfMeeting);
        $("[data-test-id='name'] input").setValue("Василий");
        $("[data-test-id='phone'] input").setValue("");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldWarnIfPhoneWithoutPlus() {
        $("[data-test-id='city'] input").setValue("Москва");
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        String dateOfMeeting = LocalDate.now().plusDays(3).format(ofPattern("dd.MM.yyyy"));
        dateField.setValue(dateOfMeeting);
        $("[data-test-id='name'] input").setValue("Василий");
        $("[data-test-id='phone'] input").setValue("12345678999");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldWarnIfPhoneIsNot11Numbers() {
        $("[data-test-id='city'] input").setValue("Москва");
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        String dateOfMeeting = LocalDate.now().plusDays(3).format(ofPattern("dd.MM.yyyy"));
        dateField.setValue(dateOfMeeting);
        $("[data-test-id='name'] input").setValue("Василий");
        $("[data-test-id='phone'] input").setValue("+123456789");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldWarnIfCheckboxIsEmpty() {
        $("[data-test-id='city'] input").setValue("Москва");
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        String dateOfMeeting = LocalDate.now().plusDays(3).format(ofPattern("dd.MM.yyyy"));
        dateField.setValue(dateOfMeeting);
        $("[data-test-id='name'] input").setValue("Василий");
        $("[data-test-id='phone'] input").setValue("+12345678999");
        String normalColorValue = $("[data-test-id='agreement'] .checkbox__text")
                .getCssValue("color");
        $(withText("Забронировать")).click();
        $("[data-test-id='agreement'].input_invalid").should(visible);
        String invalidColorValue = $("[data-test-id='agreement'] .checkbox__text")
                .getCssValue("color");
        assertNotEquals(normalColorValue, invalidColorValue);
    }
}
