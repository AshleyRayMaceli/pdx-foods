import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("locations", Location.all());
      model.put("cuisines", Cuisine.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/restaurants", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("restaurants", Restaurant.all());
      model.put("template", "templates/restaurants.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/restaurants/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      model.put("restaurant", restaurant);
      model.put("template", "templates/restaurant.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/restaurants/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Restaurant restaurant = Restaurant.find(Integer.parseInt(request.params(":id")));
      String description = request.queryParams("review");
      Review newReview = new Review(description, restaurant.getId());
      newReview.save();
      model.put("restaurant", restaurant);
      model.put("template", "templates/restaurant.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/search-results", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      List<Location> locations = Location.all();
      List<Restaurant> restaurants = Restaurant.all(); 
      Integer dropDownResult = Integer.parseInt(request.queryParams("locationId"));
      Location location = Location.find(dropDownResult);
      model.put("location", location.getName());
      model.put("locations", locations);
      model.put("restaurants", restaurants);
      model.put("template", "templates/search-results.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
