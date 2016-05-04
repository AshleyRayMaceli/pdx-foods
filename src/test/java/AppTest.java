import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.sql2o.*;
import org.junit.*;
import static org.fluentlenium.core.filter.FilterConstructor.*;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/pdx_foods_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteReviewsQuery = "DELETE FROM reviews *;";
      con.createQuery(deleteReviewsQuery).executeUpdate();
    }
  }

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("PDX Foods");
  }

  @Test
  public void restaurantsPageDisplaysAllRestaurants() {
    goTo("http://localhost:4567/");
    click("a", withText("View All Restaurants"));
    assertThat(pageSource().contains("Boxer Ramen"));
    assertThat(pageSource().contains("Salt and Straw"));
  }

  @Test
  public void individualRestaurantPageDisplayedById() {
    goTo("http://localhost:4567/restaurants/4");
    assertThat(pageSource().contains("Boxer Ramen"));
  }

  @Test
  public void submittedReviewIsDisplayedOnRestaurantPage() {
    goTo("http://localhost:4567/restaurants/4");
    fill("#review").with("I love this place!");
    submit("#submitReview");
    assertThat(pageSource().contains("I love this place!"));
  }
}
