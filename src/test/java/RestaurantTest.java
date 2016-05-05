import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

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

  @Test
  public void save_assignsIdToObject() {
    Restaurant myRestaurant = new Restaurant("Cool Burgahs");
    myRestaurant.save();
    Restaurant savedRestaurant = Restaurant.all().get(0);
    assertEquals(myRestaurant.getId(), savedRestaurant.getId());
  }

  @Test
  public void find_findsRestaurantInDatabase_true() {
    Restaurant myRestaurant = new Restaurant("Blue Star Donuts");
    myRestaurant.save();
    Restaurant savedRestaurant = Restaurant.find(myRestaurant.getId());
    assertTrue(myRestaurant.equals(savedRestaurant));
  }

  @Test
  public void getReviews_retrievesAllReviewsFromDatabase_reviewsList() {
    Restaurant myRestaurant = new Restaurant("Lardo");
    myRestaurant.save();
    Review firstReview = new Review("Da fries are DA BOMB.com", myRestaurant.getId());
    firstReview.save();
    Review secondReview = new Review("Da buffchik is dope", myRestaurant.getId());
    secondReview.save();
    Review[] reviews = new Review[] { firstReview, secondReview };
    assertTrue(myRestaurant.getReviews().containsAll(Arrays.asList(reviews)));
  }

  @Test
  public void getLocationId_getLocationIdFromRestaurantObject_LocationId() {
    Location myLocation = new Location("Northeast");
    myLocation.save();
    Restaurant myRestaurant = new Restaurant("Salt and Straw", myLocation.getId());
    myRestaurant.save();
    assertEquals(myLocation.getId(), myRestaurant.getLocationId());
  }

  @Test
  public void getCuisineId_getCuisineIdFromRestaurantObject_CuisineId() {
    Cuisine myCuisine = new Cuisine("Mexican");
    myCuisine.save();
    Location myLocation = new Location("Northeast");
    myLocation.save();
    Restaurant myRestaurant = new Restaurant("Salt and Straw", myLocation.getId(), myCuisine.getId());
    myRestaurant.save();
    assertEquals(myCuisine.getId(), myRestaurant.getCuisineId());
  }
}
