package pl.warsztat.zlomek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.warsztat.zlomek.data.OverviewRepository;
import pl.warsztat.zlomek.exceptions.FieldsNotCorrect;
import pl.warsztat.zlomek.model.db.Overview;
import pl.warsztat.zlomek.model.db.Visit;
import pl.warsztat.zlomek.model.db.VisitStatus;

import java.time.LocalDate;

@Service
public class OverviewsService {

    private OverviewRepository overviewRepository;

    @Autowired
    public OverviewsService(OverviewRepository overviewRepository){
        this.overviewRepository = overviewRepository;
    }

    public void setOverviewDate(Overview overview, int countYears, Visit visit){
        if(overview != null && visit.getStatus().equals(VisitStatus.FOR_PICKUP)) {
            if(countYears<1)
                throw new FieldsNotCorrect(new String[]{"countYears"});
            overview.setOverviewLastDay(visit.getVisitDate().toLocalDate().plusYears(countYears));
            this.overviewRepository.updateOverview(overview);
        }
    }
}
