package my.project.repos;

import my.project.domain.Good;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


public interface GoodRepository extends JpaRepository<Good, Long> {

    Page<Good> findAll(Pageable pageable);

    @Query(value = "select * from good g where lower(g.title) like %:keyword%", nativeQuery = true)
    Page<Good> findByKeyword(@Param("keyword") String keyword, Pageable pageable);


    Page<Good> findByPriceBetween(Integer startingPrice, Integer endingPrice, Pageable pageable);


    Page<Good> findByProducer(String producer, Pageable pageable);


    Page<Good> findByCountry(String goodCountry, Pageable pageable);


    Page<Good> findByCountryIn(List<String> goodCountry, Pageable pageable);


    Page<Good> findByProducerOrTitle(String producer, String title, Pageable pageable);


    Page<Good> findByProducerInAndBreedIn(List<String> producer, List<String> breed, Pageable pageable);


    Page<Good> findByProducerInOrBreedIn(List<String> producer, List<String> breed, Pageable pageable);


    Page<Good> findByProducerIn(List<String> producer, Pageable pageable);

    Page<Good> findByBreed(String breed, Pageable pageable);

    Page<Good> findByBreedIn(List<String> breed, Pageable pageable);

    Page<Good> findByTypeIn(List<String> type, Pageable pageable);

    Page<Good> findByTypeInAndBreedIn(List<String> type, List<String> breed, Pageable pageable);

    Page<Good> findByProducerInAndTypeIn(List<String> producer, List<String> type, Pageable pageable);

    Page<Good> findByProducerInAndBreedInAndTypeIn(List<String> producer, List<String> breed, List<String> type, Pageable pageable);

    @Query(value = "SELECT min(price) FROM Good ")
    BigDecimal minGoodPrice();


    @Query(value = "SELECT max(price) FROM Good ")
    BigDecimal maxGoodPrice();


    @Modifying
    @Transactional
    @Query("update Good g set g.title = ?1, g.producer = ?2, g.country = ?3, " +
            "g.stash = ?4, g.breed = ?5, " +
            "g.description = ?6, g.filename = ?7, g.price = ?8, g.volume = ?9, g.type = ?10  where g.id = ?11")
    void saveProductInfoById(String title, String producer, String country, String stash,
                             String breed, String description, String filename, Integer price,
                             String volume, String type, Long id);
}