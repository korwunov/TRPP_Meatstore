package my.project.service;

import my.project.domain.Good;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;


public interface GoodService {

    List<Good> findAll();

    Page<Good> findAll(Pageable pageable);

    Page<Good> findByPriceBetween(Integer startingPrice, Integer endingPrice, Pageable pageable);


    Page<Good> findByProducer(String producer, Pageable pageable);


    Page<Good> findByGoodCountry(String goodCountry, Pageable pageable);


    Page<Good> findByBreedIn(List<String> breed, Pageable pageable);


    Page<Good> findByProducerOrTitle(String producer, String title, Pageable pageable);


    Page<Good> findByProducerInAndBreedIn(List<String> producer, List<String> breed, Pageable pageable);

    Page<Good> findByProducerInOrBreedIn(List<String> producer, List<String> breed, Pageable pageable);


    Page<Good> findByProducerIn(List<String> producer, Pageable pageable);

    Page<Good> findByBreed(String breed, Pageable pageable);


    Page<Good> findByTypeIn(List<String> type, Pageable pageable);

    Page<Good> findByTypeInAndBreedIn(List<String> type, List<String> breed, Pageable pageable);

    Page<Good> findByProducerInAndTypeIn(List<String> producer, List<String> type, Pageable pageable);

    Page<Good> findByProducerInAndBreedInAndTypeIn(List<String> producer, List<String> breed, List<String> type, Pageable pageable);
    BigDecimal minGoodPrice();

    BigDecimal maxGoodPrice();

    Page<Good> findByKeyword(String keyword, Pageable pageable);

    void saveProductInfoById(String title, String producer, String country, String stash,
                             String breed,  String description, String filename, Integer price,
                             String volume, String type, Long id);


    Good save(Good good);
}