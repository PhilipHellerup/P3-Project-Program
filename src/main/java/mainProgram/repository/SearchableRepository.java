package mainProgram.repository;

import java.util.List;

public interface SearchableRepository<T> {
  List<T> findByTitleContainingIgnoreCase(String title);
}
