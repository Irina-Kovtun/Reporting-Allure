package netology.ru.web;

import com.github.javafaker.Faker;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.time.format.DateTimeFormatter.ofPattern;

@Data
public class DataGenerator {

    public static class Registration {
        private Registration() {
        }

        public static ClientData generateClientPersonalData(String locale) {
            Faker faker = new Faker(new Locale("ru"));
            return new ClientData(
                    generateRandomCityFromTheList(),
                    faker.name().fullName(),
                    faker.phoneNumber().phoneNumber()
            );
        }

        public static String generateDateOfMeeting(int days) {
            String dateOfMeeting = LocalDate.now().plusDays(days).format(ofPattern("dd.MM.YYYY"));
            return dateOfMeeting;
        }

        public static String generateRandomCityFromTheList() {
            String[] cities = new String[]{
                    "Абакан", "Анадырь", "Архангельск", "Астрахань", "Барнаул", "Белгород", "Биробиджан", "Благовещенск", "Брянск",
                    "Великий Новгород", "Владивосток", "Владикавказ", "Владимир", "Волгоград", "Вологда", "Воронеж", "Горно-Алтайск",
                    "Грозный", "Екатеринбург", "Иваново", "Ижевск", "Иркутск", "Йошкар-Ола", "Казань", "Калининград", "Калуга", "Кемерово",
                    "Киров", "Кострома", "Краснодар", "Красноярск", "Курган", "Курск", "Кызыл", "Липецк", "Магадан", "Магас", "Майкоп",
                    "Махачкала", "Москва", "Мурманск", "Нальчик", "Нарьян-Мар", "Нижний Новгород", "Новосибирск", "Омск", "Орёл", "Оренбург",
                    "Пенза", "Пермь", "Петрозаводск", "Петропавловск-Камчатский", "Псков", "Ростов-на-Дону", "Рязань", "Салехард", "Самара",
                    "Санкт-Петербург", "Саранск", "Саратов", "Севастополь", "Симферополь", "Смоленск", "Ставрополь", "Сыктывкар",
                    "Тамбов", "Тверь", "Томск", "Тула", "Тюмень", "Улан-Удэ", "Ульяновск", "Уфа", "Хабаровск", "Ханты-Мансийск", "Чебоксары",
                    "Челябинск", "Черкесск", "Чита", "Элиста", "Южно-Сахалинск", "Якутск", "Ярославль"
            };
            return cities[generateRandomNumber(0, cities.length)];
        }

        public static int generateRandomNumber(int min, int max) {
            max -= min;
            return (int) (Math.random() * ++max) + min;
        }
    }
}
