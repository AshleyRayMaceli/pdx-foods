
import java.util.List;
import org.sql2o.*;

public class Review {
  private int id;
  private String description;

  public Review(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO reviews(description) VALUES (:description)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("description", this.description)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Review> all() {
    String sql = "SELECT id, description FROM reviews;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Review.class);
    }
  }

  @Override
  public boolean equals(Object otherReview) {
    if (!(otherReview instanceof Review)) {
      return false;
    } else {
      Review myReview = (Review) otherReview;
      return this.getDescription().equals(myReview.getDescription());
    }
  }

}
