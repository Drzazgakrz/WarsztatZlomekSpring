package pl.warsztat.zlomek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.warsztat.zlomek.data.*;
import pl.warsztat.zlomek.model.db.*;
import pl.warsztat.zlomek.model.request.AddInvoiceRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class InvoiceService {

    private CarServiceDataRepository carServiceDataRepository;
    private InvoicesRepository invoicesRepository;
    private CompaniesRepository companiesRepository;
    private VisitRepository visitRepository;
    private InvoicePositionRepository invoicePositionRepository;
    private CompanyDataRepository companyDataRepository;
    private InvoiceBufferRepository invoiceBufferRepository;
    private InvoiceBufferPositionRepository invoiceBufferPositionRepository;

    @Autowired
    public InvoiceService(CarServiceDataRepository carServiceDataRepository, InvoicesRepository invoicesRepository,
                          CompaniesRepository companiesRepository, VisitRepository visitRepository,
                          InvoicePositionRepository invoicePositionRepository,
                          CompanyDataRepository companyDataRepository, InvoiceBufferRepository invoiceBufferRepository,
                          InvoiceBufferPositionRepository invoiceBufferPositionRepository){
        this.carServiceDataRepository = carServiceDataRepository;
        this.invoicesRepository = invoicesRepository;
        this.companiesRepository = companiesRepository;
        this.visitRepository = visitRepository;
        this.invoicePositionRepository = invoicePositionRepository;
        this.companyDataRepository = companyDataRepository;
        this.invoiceBufferRepository = invoiceBufferRepository;
        this.invoiceBufferPositionRepository = invoiceBufferPositionRepository;
    }

    private MethodOfPayment createMethodOfPayment(String method) {
        switch (method) {
            case ("CASH"):
                return MethodOfPayment.CASH;
            case ("CARD"):
                return MethodOfPayment.CARD;
            case ("TRANSFER"):
                return MethodOfPayment.TRANSFER;
        }
        return null;
    }

    private void createInvoice(AddInvoiceRequest request, InvoicesModel invoice){
        CarServiceData carServiceData = carServiceDataRepository.getCurrentData();
        CompanyData companyData = getCompanyData(request.getCompanyName());
        this.companyDataRepository.persist(companyData);
        MethodOfPayment methodOfPayment = createMethodOfPayment(request.getMethodOfPayment());
        invoice.setCarServiceData(carServiceData);
        invoice.setCompanyData(companyData);
        invoice.setMethodOfPayment(methodOfPayment);
        invoice.setInvoiceNumber(this.invoicesRepository.generateInvoiceNumber());
        invoice.setPaymentDate(LocalDate.now().plusDays(30));
        invoice.setDiscount(invoice.getDiscount());
    }

    private CompanyData getCompanyData(String companyName){
        Company company = companiesRepository.getCompanyName(companyName);
        List<CompanyData> companies = this.companyDataRepository.getAllCompanies();
        System.out.println(companyName);
        return companies.stream().filter(data->
                data.compareCompanies(company)).findAny().orElse(new CompanyData(company));
    }

    public Invoice createVatInvoice(AddInvoiceRequest request){
        Invoice invoice = new Invoice();
        createInvoice(request, invoice);
        Visit visit = visitRepository.getVisitById(request.getVisitId());
        invoice.setVisitFinished(visit.getVisitFinished());
        this.invoicesRepository.persist(invoice);
        this.addPositions(invoice, visit);
        this.invoicesRepository.update(invoice);
        return invoice;
    }

    public void addPositions(Invoice invoice, Visit visit){
        visit.getParts().forEach(part->{
            CarPart carPart = part.getPart();
            String partName = carPart.getName()+", "+carPart.getProducer();
            InvoicePosition position = createPosition(part, "szt." ,partName, carPart.getTax(), invoice);
            this.invoicePositionRepository.persist(position);
        });
        visit.getServices().forEach(currentService->{
            pl.warsztat.zlomek.model.db.Service service = currentService.getService();
            InvoicePosition position = createPosition(currentService, "h" ,service.getName(), service.getTax(),
                    invoice);
            this.invoicePositionRepository.persist(position);
        });
    }
    private void addPositions(InvoiceBuffer invoice, Visit visit){
        visit.getParts().forEach(part->{
            CarPart carPart = part.getPart();
            String partName = carPart.getName()+", "+carPart.getProducer();
            InvoiceBufferPosition position = createPosition(part, "szt.", partName, carPart.getTax(), invoice);
            this.invoiceBufferPositionRepository.persist(position);
        });
        visit.getServices().forEach(currentService->{
            pl.warsztat.zlomek.model.db.Service service = currentService.getService();
            InvoiceBufferPosition position = createPosition(currentService, "h", service.getName(), service.getTax(),
                    invoice);
            this.invoiceBufferPositionRepository.persist(position);
        });
    }

    private InvoiceBufferPosition createPosition(VisitPosition position, String unitOfMeasure, String name, int tax, InvoiceBuffer invoice){
        InvoiceBufferPosition currentPosition = new InvoiceBufferPosition(position, unitOfMeasure, name, tax, invoice);
        addValues(invoice, currentPosition);
        invoice.add(currentPosition);
        return currentPosition;
    }
    private InvoicePosition createPosition(VisitPosition position, String unitOfMeasure, String name, int tax, Invoice invoice){
        InvoicePosition currentPosition = new InvoicePosition(position, name, tax, invoice, unitOfMeasure);
        addValues(invoice, currentPosition);
        invoice.add(currentPosition);
        return currentPosition;
    }

    private void addValues(InvoicesModel invoice, InvoicePositionModel position){
        invoice.setGrossValue(invoice.getGrossValue().add(position.getGrossPrice()));
        invoice.setNetValue(invoice.getNetValue().add(position.getNetPrice()));
    }

    public InvoiceBuffer createProFormaInvoice(AddInvoiceRequest request){
        InvoiceBuffer invoice = new InvoiceBuffer();
        createInvoice(request, invoice);
        Visit visit = visitRepository.getVisitById(request.getVisitId());
        invoice.setVisit(visit);
        this.invoiceBufferRepository.persist(invoice);
        this.addPositions(invoice, visit);
        this.invoiceBufferRepository.update(invoice);
        return invoice;
    }
}