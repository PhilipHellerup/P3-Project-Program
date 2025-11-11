package mainProgram;

import mainProgram.repository.JobRepository;
import mainProgram.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MainApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void testRepositoryBeansAreLoaded() {
        assertNotNull(applicationContext.getBean(JobRepository.class));
        assertNotNull(applicationContext.getBean(ProductRepository.class));
    }
}


