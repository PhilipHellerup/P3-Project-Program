package mainProgram.services; // Project Organization

/* --- Imports */
import java.util.List;

/* --- BaseSearchService Interface --- */
// Each service where we want to be able to search, implements this base searchService
// (Contract = Must Implement `search` Method).
public interface BaseSearchService<T> {
    List<T> search(String keyword);
}
