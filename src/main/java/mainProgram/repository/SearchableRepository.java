package mainProgram.repository; // Project Organization

/* --- Imports --- */
import java.util.List;

/* --- SearchableRepository Interface --- */
// Generic repository interface that defines a search operation based on title.
// Intended to be implemented by entities that support text-based searching.
public interface SearchableRepository<T> {
    /// Methods

    // Search by Title - READ
    /** @param title part or full title string to search for (case-insensitive) **/
    /** @return list of matching entities where the title contains the given string **/
    List<T> findByTitleContainingIgnoreCase(String title);
}
