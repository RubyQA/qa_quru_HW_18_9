
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;

import java.util.stream.Stream;


public class BBCNewsTest {
    @BeforeAll
    static void beforeAll() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
    }

    @ParameterizedTest
    @ValueSource(strings = { "covid", "brexit", "climate" })
    void testSearchResults(String query) {
        open("https://www.bbc.com/news");
        $(".orbit-search-button-icon-with-text").click();
        $("#search-input").setValue(query).pressEnter();
        String value = $(By.xpath("//*[@id=\"main-content\"]/div[4]/div/div/nav/div/div/div[3]/div/ol/li[14]/div/a/div"))
                .getText();
        int intValue = Integer.parseInt(value);
        assert intValue > 5;
    }

    @ParameterizedTest
    @CsvSource({"https://www.bbc.com/news/world-us-canada-65452940, AI 'godfather' Geoffrey Hinton warns of dangers as he quits Google",
            "https://www.bbc.com/news/science-environment-65399580, Climate change: Satellite maps warming impact on global glaciers",
            "https://www.bbc.com/news/science-environment-65403381, Climate change: Spain breaks record temperature for April"})
    public void testHeadlines(String url, String expectedHeadline) {
        open(url);
        $("#main-heading").shouldHave(Condition.text(expectedHeadline));
    }

    @ParameterizedTest
    @MethodSource("getCategories")
    public void testCategoryPage(String category) {
        String url = "https://www.bbc.com/news/" + category;
        open(url);
        SelenideElement ul = $("#orb-modules > header > div:nth-child(2) > div:nth-child(2) > div.gs-u-display-none.gs-u-display-block\\@m");
        ElementsCollection liElements = ul.$$("li");
        liElements.shouldHave(sizeGreaterThanOrEqual(4));
    }

    static Stream<String> getCategories() {
        return Stream.of("world", "uk", "business", "technology");
    }
}