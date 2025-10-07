package mainProgram; // Project Organization

// Day Class
public class Day {
    // Attributes
    private int id;
    private Job[] listOfJobs;

    // Constructor
    public Day(int id, Job[] listOfJobs) {
        this.id = id;
        this.listOfJobs = listOfJobs;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Job[] getListOfJobs() {
        return listOfJobs;
    }

    // Setters
    public void setId(int id){
        this.id = id;
    }

    public void setListOfJobs(Job[] listOfJobs){
        this.listOfJobs = listOfJobs;
    }
}

