import java.util.List;
import org.sql2o.*;

public class Restaurant {
  private int id;
  private String name;
  private int location_id;
  private int cuisine_id;

  public Restaurant(String name) {
    this.name = name;
  }

  public Restaurant(String name, int location_id) {
    this.name = name;
    this.location_id = location_id;
  }

  public Restaurant(String name, int location_id, int cuisine_id) {
    this.name = name;
    this.location_id = location_id;
    this.cuisine_id = cuisine_id;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public int getLocationId() {
    return location_id;
  }

  public int getCuisineId() {
    return cuisine_id;
  }

  public String getLocationName() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT * FROM locations WHERE id=:location_id;";
    Location fetchedLocation = con.createQuery(sql)
      .addParameter("location_id", this.location_id)
      .executeAndFetchFirst(Location.class);
      return fetchedLocation.getName();
    }
  }

  public String getCuisineName() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM cuisines WHERE id=:cuisine_id;";
      Cuisine fetchedCuisine = con.createQuery(sql)
        .addParameter("cuisine_id", this.cuisine_id)
        .executeAndFetchFirst(Cuisine.class);
        return fetchedCuisine.getName();
    }
  }

  public static List<Restaurant> all() {
    String sql = "SELECT id, name FROM restaurants;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .executeAndFetch(Restaurant.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO restaurants(name, location_id, cuisine_id) VALUES (:name, :location_id, :cuisine_id);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("location_id", this.location_id)
        .addParameter("cuisine_id", this.cuisine_id)
        .executeUpdate()
        .getKey();
    }
  }

  public static Restaurant find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM restaurants WHERE id=:id;";
      Restaurant myRestaurant = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Restaurant.class);
      return myRestaurant;
    }
  }

  public List<Review> getReviews() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM reviews WHERE restaurant_id=:id;";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Review.class);
    }
  }

  @Override
  public boolean equals(Object otherRestaurant) {
    if (!(otherRestaurant instanceof Restaurant)) {
      return false;
    } else {
      Restaurant newRestaurant = (Restaurant) otherRestaurant;
      return this.getName().equals(newRestaurant.getName());
    }
  }
}
