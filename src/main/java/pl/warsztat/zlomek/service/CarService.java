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
import pl.warsztat.zlomek.model.request.AddCoownerRequest;
import pl.warsztat.zlomek.model.request.ClientForm;
import pl.warsztat.zlomek.model.request.VerifyOwnershipRequest;

import java.time.LocalDate;
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
        }).findAny().or(()->{ throw new ResourcesNotFoundException("Samochód nie należy do tego klienta");}).get();
        return ownership;
    }

    public void addOwnership(Car car, Client client, OwnershipStatus status, String registrationNumber){
        CarsHasOwners carsHasOwners = car.addCarOwner(client, status, registrationNumber);
        this.carsHasOwnersRepository.insertOwnership(carsHasOwners);
        this.clientRepository.update(client);
    }

    public void verifyOwnership(VerifyOwnershipRequest request){
        List<ClientForm> verified = request.getVerified();
        long carId = request.getCarId();
        verifyUsers(verified, carId);
        List<ClientForm> notVerified = request.getNetVerified();
        endOwnership(notVerified, carId);
    }

    private void verifyUsers(List<ClientForm> verified, long carId){
        OwnershipStatus status = verified.size()>1?OwnershipStatus.COOWNER: OwnershipStatus.CURRENT_OWNER;
        verified.forEach(user->{
            Client client = this.clientRepository.findClientByUsername(user.getEmail());
            CarsHasOwners cho = this.getClientCar(client,carId);
            cho.setStatus(status);
            this.carsHasOwnersRepository.updateOwnership(cho);
        });
    }

    private void endOwnership(List<ClientForm> notVerified, long carId){
        notVerified.forEach(user->{
            Client client = this.clientRepository.findClientByUsername(user.getEmail());
            CarsHasOwners cho = this.getClientCar(client,carId);
            if(!cho.getStatus().equals(OwnershipStatus.NOT_VERIFIED_OWNER)) {
                cho.setStatus(OwnershipStatus.FORMER_OWNER);
                this.carsHasOwnersRepository.updateOwnership(cho);
            }
            else
                this.carsHasOwnersRepository.deleteOwnership(cho);
        });
    }

    public void setCoownersStatus(List<String> coowners, CarsHasOwners cho){
        System.out.println(coowners.size());
        cho.getCar().getOwners().forEach(currentCho->{
            Client current = currentCho.getOwner();
            System.out.println(current.getEmail());
            if(coowners.contains(current.getEmail())){
                currentCho.setStatus(OwnershipStatus.FORMER_OWNER);
                currentCho.setEndOwnershipDate(LocalDate.now());
                this.carsHasOwnersRepository.updateOwnership(currentCho);
            }
        });
    }
}
