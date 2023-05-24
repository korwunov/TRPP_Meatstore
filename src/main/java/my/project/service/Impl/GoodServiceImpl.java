package my.project.service.Impl;

import my.project.domain.Good;
import my.project.repos.GoodRepository;
import my.project.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class GoodServiceImpl implements GoodService {

    private final GoodRepository goodRepository;


    @Autowired
    public GoodServiceImpl(GoodRepository goodRepository) {
        this.goodRepository = goodRepository;
    }


    @Override
    public List<Good> findAll() {
        return goodRepository.findAll();
    }


    @Override
    public Page<Good> findAll(Pageable pageable) {
        return goodRepository.findAll(pageable);
    }


    @Override
    public Page<Good> findByPriceBetween(Integer startingPrice, Integer endingPrice, Pageable pageable) {
        return goodRepository.findByPriceBetween(startingPrice, endingPrice, pageable);
    }


    @Override
    public Page<Good> findByProducer(String producer, Pageable pageable) {
        return goodRepository.findByProducer(producer, pageable);
    }


    @Override
    public Page<Good> findByGoodCountry(String goodCountry, Pageable pageable) {
        return goodRepository.findByCountry(goodCountry, pageable);
    }


    @Override
    public Page<Good> findByBreedIn(List<String> breed, Pageable pageable) {
        return goodRepository.findByBreedIn(breed, pageable);
    }

    @Override
    public Page<Good> findByKeyword(String keyword, Pageable pageable) {
        return goodRepository.findByKeyword(keyword, pageable);
    }

    @Override
    public Page<Good> findByProducerOrTitle(String producer, String title, Pageable pageable) {
        return goodRepository.findByProducerOrTitle(producer, title, pageable);
    }


    @Override
    public Page<Good> findByProducerInAndBreedIn(List<String> producer, List<String> breed, Pageable pageable) {
        return goodRepository.findByProducerInAndBreedIn(producer, breed, pageable);
    }


    @Override
    public Page<Good> findByProducerInOrBreedIn(List<String> producer, List<String> breed, Pageable pageable) {
        return goodRepository.findByProducerInOrBreedIn(producer, breed, pageable);
    }


    @Override
    public Page<Good> findByProducerIn(List<String> producer, Pageable pageable) {
        return goodRepository.findByProducerIn(producer, pageable);
    }

    @Override
    public Page<Good> findByBreed(String breed, Pageable pageable) {
        return  goodRepository.findByBreed(breed, pageable);
    }

    @Override
    public Page<Good> findByTypeIn(List<String> type, Pageable pageable) {
        return  goodRepository.findByTypeIn(type, pageable);
    }

    @Override
    public Page<Good> findByTypeInAndBreedIn(List<String> type, List<String> breed, Pageable pageable) {
        return goodRepository.findByTypeInAndBreedIn(type, breed, pageable);
    }

    @Override
    public Page<Good> findByProducerInAndTypeIn(List<String> producer, List<String> type, Pageable pageable) {
        return goodRepository.findByProducerInAndTypeIn(producer, type, pageable);
    }

    @Override
    public Page<Good> findByProducerInAndBreedInAndTypeIn(List<String> producer, List<String> breed, List<String> type, Pageable pageable) {
        return goodRepository.findByProducerInAndBreedInAndTypeIn(producer, breed, type, pageable);
    }

    @Override
    public BigDecimal minGoodPrice() {
        return goodRepository.minGoodPrice();
    }


    @Override
    public BigDecimal maxGoodPrice() {
        return goodRepository.maxGoodPrice();
    }


    @Override
    public void saveProductInfoById(String title, String producer, String country, String stash,
                                    String breed, String description, String filename, Integer price,
                                    String volume, String type, Long id)
    {
        goodRepository.saveProductInfoById(title, producer, country, stash, breed,
                description, filename, price, volume, type, id);
    }


    @Override
    public Good save(Good good) {
        return goodRepository.save(good);
    }
}
