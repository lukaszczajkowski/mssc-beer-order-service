package guru.sfg.beer.order.service.services;

import guru.sfg.brewery.model.BeerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@ConfigurationProperties(prefix = "sfg.brewery", ignoreUnknownFields = false)
@Service
public class BeerServiceImpl implements BeerService {

    public static final String BEER_SERVICE_ENDPOINT = "/api/v1/";
    public static final String GET_BEER_BY_ID_PATH = "beer/";
    public static final String BEER_PATH_V1 = "/api/v1/beer/";
    public static final String BEER_UPC_PATH_V1 = "/api/v1/beerUpc/";
    public static final String GET_BEER_BY_UPC_PATH = "beerUpc/";

    private final RestTemplate restTemplate;

    private String beerServiceHost;

    public BeerServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Optional<BeerDto> getBeerById(UUID beerId) throws ChangeSetPersister.NotFoundException {
        return Optional.of(restTemplate
                .getForObject(beerServiceHost + BEER_SERVICE_ENDPOINT + GET_BEER_BY_ID_PATH + beerId.toString(),
                        BeerDto.class));
    }

    @Override
    public Optional<BeerDto> getBeerByUpc(String upc) {
        return Optional.of(restTemplate
                .getForObject(beerServiceHost + BEER_SERVICE_ENDPOINT + GET_BEER_BY_UPC_PATH + upc,
                        BeerDto.class));
    }

    public void setBeerServiceHost(String beerServiceHost) {
        this.beerServiceHost = beerServiceHost;
    }
}
