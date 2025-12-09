package mainProgram;

import mainProgram.repository.JobRepository;
import mainProgram.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;


// Spring Boot test class that ensures the application context loads correctly and that critical repository beans are available.
// @SpringBootTest runs the main application (controller, service, repositories etc.) so that it can be tested
@SpringBootTest
class MainApplicationTests {

    // @Autowired inject the applicationContext bean into the variable, instead of having to create it here
    @Autowired
    private ApplicationContext applicationContext;

    // Verify that the Spring application context starts up without errors. If the context fails to load, this test will fail.
    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    // Ensures that the required repository beans have been created by Spring.
    // If either bean is missing from the context (e.g., due to component-scan issues, wrong package structure,
    // or missing @Repository annotation), this test will fail and immediately reveal the configuration problem.
    @Test
    void testRepositoryBeansAreLoaded() {
        assertNotNull(applicationContext.getBean(JobRepository.class));
        assertNotNull(applicationContext.getBean(ProductRepository.class));
    }
}

