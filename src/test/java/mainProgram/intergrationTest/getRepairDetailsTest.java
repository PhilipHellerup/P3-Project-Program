package mainProgram.intergrationTest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class getRepairDetailsTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void jobTitleAppearsInRenderedHtml() throws Exception {
        MvcResult result = mockMvc.perform(get("/jobliste/20"))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        Document doc = Jsoup.parse(html);

        // Check the element where the title is rendered
        String title = doc.select("#job-title").text();

        assertEquals("bike1test", title);
    }

    @Test
    void jobCustomerNameAppearsInRenderedHtml() throws Exception {
        MvcResult result = mockMvc.perform(get("/jobliste/20"))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        Document doc = Jsoup.parse(html);

        // Check the element where the customer name is rendered
        String name = doc.select("#job-customer-name").text();

        assertEquals("test3", name);
    }

    @Test
    void jobCustomerPhoneAppearsInRenderedHtml() throws Exception {
        MvcResult result = mockMvc.perform(get("/jobliste/20"))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        Document doc = Jsoup.parse(html);

        // Check the element where the phone number is rendered
        String phone = doc.select("#job-customer-phone").text();

        assertEquals("44444444", phone);
    }
}
