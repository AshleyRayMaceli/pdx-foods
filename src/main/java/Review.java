
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

}
