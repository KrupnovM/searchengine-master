package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Site;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
    Site findByUrl(String url);
    Site findById(long id);
    void deleteByUrl(String url);
}