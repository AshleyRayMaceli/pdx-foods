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

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
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
      String sql = "INSERT INTO restaurants(name) VALUES (:name);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Restaurant find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM restaurants WHERE id=:id";
      Restaurant myRestaurant = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Restaurant.class);
      return myRestaurant;
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
