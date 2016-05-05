import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class CuisineTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/pdx_foods_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteLocationsQuery = "DELETE FROM locations *;";
      String deleteCuisinesQuery = "DELETE FROM cuisines *;";
      String deleteRestaurantsQuery = "DELETE FROM cuisines *;";
      String deleteReviewsQuery = "DELETE FROM reviews *;";
      con.createQuery(deleteLocationsQuery).executeUpdate();
      con.createQuery(deleteCuisinesQuery).executeUpdate();
      con.createQuery(deleteRestaurantsQuery).executeUpdate();
      con.createQuery(deleteReviewsQuery).executeUpdate();
    }
  }

  @Test
  public void cuisine_instantiatesCorrectly_true() {
    Cuisine myCuisine = new Cuisine("Mexican");
    assertEquals(true, myCuisine instanceof Cuisine);
  }

  @Test
  public void getName_cuisineInstantiatesWithName_String() {
    Cuisine myCuisine = new Cuisine("Italian");
    assertEquals("Italian", myCuisine.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(0, Cuisine.all().size());
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame() {
    Cuisine firstCuisine = new Cuisine("Indian");
    Cuisine secondCuisine = new Cuisine("Indian");
    assertTrue(firstCuisine.equals(secondCuisine));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Cuisine myCuisine = new Cuisine("Ethiopian");
    myCuisine.save();
    assertTrue(Cuisine.all().get(0).equals(myCuisine));
  }

  @Test
  public void save_assignsIdToObject() {
    Cuisine myCuisine = new Cuisine("Spanish");
    myCuisine.save();
    Cuisine savedCuisine = Cuisine.all().get(0);
    assertEquals(myCuisine.getId(), savedCuisine.getId());
  }

  @Test
  public void find_findsCuisineInDatabase_true() {
    Cuisine myCuisine = new Cuisine("Japanese");
    myCuisine.save();
    Cuisine savedCuisine = Cuisine.find(myCuisine.getId());
    assertTrue(myCuisine.equals(savedCuisine));
  }

  @Test
  public void getRestaurants_retrievesAllRestaurantsFromDatabase_cuisinesList() {
    Cuisine myCuisine = new Cuisine("Chinese");
    myCuisine.save();
    Location myLocation = new Location("Northeast");
    myLocation.save();
    Restaurant firstRestaurant = new Restaurant("First Restaurant", myLocation.getId(), myCuisine.getId());
    firstRestaurant.save();
    Restaurant secondRestaurant = new Restaurant("Second Restaurant", myLocation.getId(), myCuisine.getId());
    secondRestaurant.save();
    Restaurant[] restaurants = new Restaurant[] { firstRestaurant, secondRestaurant };
    assertTrue(myCuisine.getRestaurants().containsAll(Arrays.asList(restaurants)));
  }

}
