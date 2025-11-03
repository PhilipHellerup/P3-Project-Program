package mainProgram.services;

import java.util.List;

///  Each service where we want to be able to search implements this base searchService.

public interface BaseSearchService<T> {
  List<T> search(String keyword);
}
