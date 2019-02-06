package pl.warsztat.zlomek.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class VisitModel {
    protected long carId;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    protected Date visitDate;

    @JsonProperty(value="isOverview")
    protected boolean isOverview;
}
