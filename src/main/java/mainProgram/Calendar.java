package mainProgram; // Project Organization

// View Mode (Enum)
enum viewMode{
    day,
    week,
    month
}

// Calendar Class
public class Calendar {
    // Attributes
    private Day[] listOfDays;
    private viewMode viewMode;

    // Constructor
    public Calendar(Day[] listOfDays, viewMode viewMode) {
        this.listOfDays = listOfDays;
        this.viewMode = viewMode;
    }

    // Getters
    public Day[] getListOfDays() {
        return listOfDays;
    }

    public viewMode getViewMode() {
        return viewMode;
    }

    // Setters
    public void setListOfDays(Day[] listOfDays){
        this.listOfDays = listOfDays;
    }

    public void setViewMode(viewMode viewMode){
        this.viewMode = viewMode;
    }
}
