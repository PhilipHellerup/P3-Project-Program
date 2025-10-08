package mainProgram.model;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private String title;        // e.g., "Order #1234"
    private String status;       // e.g., "PENDING", "SHIPPED"
    private LocalDateTime start; // start datetime for calendar
    private LocalDateTime end;   // optional end datetime

    public Order() {}

    public Order(Long id, String title, String status, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.start = start;
        this.end = end;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }
    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }
}
