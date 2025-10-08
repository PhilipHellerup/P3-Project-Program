package mainProgram.controller;

import mainProgram.model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final List<Order> orders = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final AtomicLong idCounter = new AtomicLong(1000);

    public OrderController() {
        // Seed with some example orders
        orders.add(new Order(idCounter.getAndIncrement(), "Order #1001 - ACME", "PENDING",
                LocalDateTime.now().withHour(9).withMinute(0).withSecond(0),
                LocalDateTime.now().withHour(10).withMinute(0).withSecond(0)));
        orders.add(new Order(idCounter.getAndIncrement(), "Order #1002 - Beta", "SHIPPED",
                LocalDateTime.now().plusDays(1).withHour(14).withMinute(0),
                null));
        orders.add(new Order(idCounter.getAndIncrement(), "Order #1003 - Gamma", "PENDING",
                LocalDateTime.now().plusDays(3).withHour(11).withMinute(30),
                LocalDateTime.now().plusDays(3).withHour(12).withMinute(0)));
    }

    // Returns Thymeleaf page (calendar)
    @GetMapping("calendar")
    public String calendarPage(Model model) throws JsonProcessingException {
        // Convert orders to a lightweight JSON for FullCalendar (start/end as ISO strings)
        List<Object> events = new ArrayList<>();
        for (Order o : orders) {
            var obj = new java.util.HashMap<String, Object>();
            obj.put("id", o.getId());
            obj.put("title", o.getTitle() + " (" + o.getStatus() + ")");
            obj.put("start", o.getStart() != null ? o.getStart().toString() : null);
            if (o.getEnd() != null) obj.put("end", o.getEnd().toString());
            // add custom properties
            obj.put("status", o.getStatus());
            events.add(obj);
        }
        String ordersJson = mapper.writeValueAsString(events);
        model.addAttribute("ordersJson", ordersJson);
        return "calendar";
    }

    // REST endpoint for orders as JSON (useful for client-side fetch)
    @GetMapping("api/orders")
    @ResponseBody
    public List<Order> getOrdersApi() {
        return orders;
    }

    // Create a new order (accepts JSON)
    @PostMapping("api/orders")
    @ResponseBody
    public ResponseEntity<Order> createOrder(@RequestBody Order newOrder) {
        // assign id if missing
        if (newOrder.getId() == null) {
            newOrder.setId(idCounter.getAndIncrement());
        }
        // basic validation: ensure start is present
        if (newOrder.getStart() == null) {
            return ResponseEntity.badRequest().build();
        }
        orders.add(newOrder);
        return ResponseEntity.ok(newOrder);
    }
}
