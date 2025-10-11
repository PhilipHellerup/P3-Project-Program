package mainProgram; // Project Organization

import java.util.ArrayList;
import java.util.List;

// Day Class
public class Day {

  // Attributes
  private int id;
  private List<Job> listOfJobs = new ArrayList<>();

  // Constructor
  public Day(int id, List<Job> listOfJobs) {
    this.id = id;
    this.listOfJobs = new ArrayList<>(listOfJobs);
  }

  // Getters
  public int getId() {
    return id;
  }

  public List<Job> getListOfJobs() {
    return new ArrayList<>(listOfJobs);
  }

  // Setters
  public void setId(int id) {
    this.id = id;
  }

  public void setListOfJobs(List<Job> listOfJobs) {
    this.listOfJobs = new ArrayList<>(listOfJobs);
  }
}
