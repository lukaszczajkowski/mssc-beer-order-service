package guru.sfg.beer.order.service.services;

import guru.sfg.beer.order.service.web.model.BeerDto;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Optional<BeerDto> getBeerById(UUID uuid) throws ChangeSetPersister.NotFoundException;

    Optional<BeerDto> getBeerByUpc(String upc);
}
