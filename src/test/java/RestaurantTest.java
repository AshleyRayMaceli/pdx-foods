import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RestaurantTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/pdx_foods_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteReviewsQuery = "DELETE FROM reviews *;";
      String deleteRestaurantsQuery = "DELETE FROM restaurants *;";
      con.createQuery(deleteReviewsQuery).executeUpdate();
      con.createQuery(deleteRestaurantsQuery).executeUpdate();
    }
  }

  @Test
  public void restaurant_instantiatesCorrectly_true() {
    Restaurant myRestaurant = new Restaurant("Pine State");
    assertEquals(true, myRestaurant instanceof Restaurant);
  }

  @Test
  public void getName_restaurantInstantiatesWithName_String() {
    Restaurant myRestaurant = new Restaurant("Boxer Ramen");
    assertEquals("Boxer Ramen", myRestaurant.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(0, Restaurant.all().size());
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame() {
    Restaurant firstRestaurant = new Restaurant("Ruby Jewel");
    Restaurant secondRestaurant = new Restaurant("Ruby Jewel");
    assertTrue(firstRestaurant.equals(secondRestaurant));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Restaurant myRestaurant = new Restaurant("Tom's Bistro");
    myRestaurant.save();
    assertTrue(Restaurant.all().get(0).equals(myRestaurant));
  }
}
