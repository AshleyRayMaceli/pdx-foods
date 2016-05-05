import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class LocationTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/pdx_foods_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteLocationsQuery = "DELETE FROM locations *;";
      String deleteRestaurantsQuery = "DELETE FROM restaurants *;";
      String deleteReviewsQuery = "DELETE FROM reviews *;";
      con.createQuery(deleteLocationsQuery).executeUpdate();
      con.createQuery(deleteRestaurantsQuery).executeUpdate();
      con.createQuery(deleteReviewsQuery).executeUpdate();
    }
  }

  @Test
  public void restaurant_instantiatesCorrectly_true() {
    Location myLocation = new Location("Northeast");
    assertEquals(true, myLocation instanceof Location);
  }

  @Test
  public void getName_restaurantInstantiatesWithName_String() {
    Location myLocation = new Location("Northeast");
    assertEquals("Northeast", myLocation.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(0, Location.all().size());
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame() {
    Location firstLocation = new Location("Downtown");
    Location secondLocation = new Location("Downtown");
    assertTrue(firstLocation.equals(secondLocation));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Location myLocation = new Location("Southeast");
    myLocation.save();
    assertTrue(Location.all().get(0).equals(myLocation));
  }

  @Test
  public void save_assignsIdToObject() {
    Location myLocation = new Location("Northwest");
    myLocation.save();
    Location savedLocation = Location.all().get(0);
    assertEquals(myLocation.getId(), savedLocation.getId());
  }

  @Test
  public void find_findsLocationInDatabase_true() {
    Location myLocation = new Location("Pearl District");
    myLocation.save();
    Location savedLocation = Location.find(myLocation.getId());
    assertTrue(myLocation.equals(savedLocation));
  }

  @Test
  public void getRestaurants_retrievesAllRestaurantsFromDatabase_restaurantsList() {
    Location myLocation = new Location("Rose Quarter");
    myLocation.save();
    Restaurant firstRestaurant = new Restaurant("First Restaurant", myLocation.getId());
    firstRestaurant.save();
    Restaurant secondRestaurant = new Restaurant("Second Restaurant", myLocation.getId());
    secondRestaurant.save();
    Restaurant[] restaurants = new Restaurant[] { firstRestaurant, secondRestaurant };
    assertTrue(myLocation.getRestaurants().containsAll(Arrays.asList(restaurants)));
  }

}
