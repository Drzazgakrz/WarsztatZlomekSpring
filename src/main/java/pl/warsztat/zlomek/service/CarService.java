package pl.warsztat.zlomek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.warsztat.zlomek.data.CarRepository;
import pl.warsztat.zlomek.data.CarsHasOwnersRepository;
import pl.warsztat.zlomek.data.ClientRepository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.Car;
import pl.warsztat.zlomek.model.db.CarsHasOwners;
import pl.warsztat.zlomek.model.db.Client;
import pl.warsztat.zlomek.model.db.OwnershipStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private CarRepository carRepository;
    private CarsHasOwnersRepository carsHasOwnersRepository;
    private ClientRepository clientRepository;

    @Autowired
    public CarService(CarRepository carRepository, CarsHasOwnersRepository carsHasOwnersRepository,
                      ClientRepository clientRepository) {
        this.carsHasOwnersRepository = carsHasOwnersRepository;
        this.clientRepository = clientRepository;
        this.carRepository = carRepository;
    }

    public CarsHasOwners getClientCar(Client client, long id) {
        CarsHasOwners ownership = client.getCars().stream().filter(cho -> {
            OwnershipStatus os = cho.getStatus();
            if (os.equals(OwnershipStatus.CURRENT_OWNER) || os.equals(OwnershipStatus.COOWNER))
                return cho.getOwner().equals(client) && cho.getCar().getId() == id;
            return false;
        }).findAny().orElse(null);
        if (ownership == null)
            throw new ResourcesNotFoundException("Samochód nie należy do tego klienta");
        return ownership;
    }

    public void addOwnership(Car car, Client client, OwnershipStatus status, String registrationNumber){
        CarsHasOwners carsHasOwners = car.addCarOwner(client, status, registrationNumber);
        this.carsHasOwnersRepository.insertOwnership(carsHasOwners);
        this.clientRepository.update(client);
    }
}
