package mainProgram; // Project Organization

import java.util.ArrayList;
import java.util.List;

// View Mode (Enum)
enum viewMode{
    day,
    week,
    month
}

// Calendar Class
public class Calendar {
    // Attributes
    private List<Day> listOfDays = new ArrayList<>();
    private viewMode viewMode;

    // Constructor
    public Calendar(List<Day> listOfDays, viewMode viewMode) {
        this.listOfDays = new ArrayList<>(listOfDays);
        this.viewMode = viewMode;
    }

    // Getters
    public List<Day> getListOfDays() {
        return new ArrayList<>(listOfDays);
    }

    public viewMode getViewMode() {
        return viewMode;
    }

    // Setters
    public void setListOfDays(List<Day> listOfDays){
        this.listOfDays = new ArrayList<>(listOfDays);
    }

    public void setViewMode(viewMode viewMode){
        this.viewMode = viewMode;
    }
}
