package ru.kinopoisk;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class WebTest {

    //Use @ValueSource
    @ValueSource(strings = {"Друзья", "Декстер"})
    @ParameterizedTest(name = " Результат поиска в Скорее всего, вы ищете: {0}")
    void serialsSearcTest(String testData) {
        open("https://www.kinopoisk.ru/");
        $("input[type='text']").setValue(testData);
        $("button[type='submit']").click();
        $(".element.most_wanted").shouldHave(text(testData + " (сериал)"));
    }

    //Use @CsvSource

    @CsvSource(value = {
            "Друзья, 1994 – 2004",
            "Декстер, 2006 – 2013",
    })
    @ParameterizedTest(name = " Результат поиска  содержат год - \"{1}\" для сериала \"{0}\"")
    void genreMatchesTheResultTest(String testData, String expectedResult) {
        open("https://www.kinopoisk.ru/");
        $("input[type='text']").setValue(testData);
        $("button[type='submit']").click();
        $(".element.most_wanted").shouldHave(text(expectedResult));
    }

    static Stream<Arguments> genreMatchesTest() {
        return Stream.of(
                Arguments.of("Друзья", List.of("комедия", "мелодрама")),
                Arguments.of("Декстер", List.of("триллер", "драма", "криминал", "детектив"))

        );
    }

    @MethodSource("genreMatchesTest")
    @ParameterizedTest(name = " Страница сериала - \"{0}\" содержит жанры \"{1}\"")
    void genreMatchesTheResultTest(String testData, List<String> expectedGenre) {
        open("https://www.kinopoisk.ru/");
        $("input[type='text']").setValue(testData);
        $("[data-tid=\"c5eaf2c6\"]").shouldHave(text(testData)).click();
        $$(".styles_root__5PEXQ [data-tid=\"603f73a4\"]").shouldHave(CollectionCondition.texts(expectedGenre));
    }

}


