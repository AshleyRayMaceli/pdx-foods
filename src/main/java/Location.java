import java.util.List;
import org.sql2o.*;

public class Location {
  private int id;
  private String name;

  public Location(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public static List<Location> all() {
    String sql = "SELECT id, name FROM locations;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .executeAndFetch(Location.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO locations(name) VALUES (:name);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Location find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM locations WHERE id=:id;";
      Location myLocation = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Location.class);
      return myLocation;
    }
  }

  public List<Restaurant> getRestaurants() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM restaurants WHERE location_id=:id;";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Restaurant.class);
    }
  }

  @Override
  public boolean equals(Object otherLocation) {
    if (!(otherLocation instanceof Location)) {
      return false;
    } else {
      Location newLocation = (Location) otherLocation;
      return this.getName().equals(newLocation.getName());
    }
  }
}
