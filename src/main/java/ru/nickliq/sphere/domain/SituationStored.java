package ru.nickliq.sphere.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;
import ru.nickliq.sphere.model.VacanciesInfo;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity(name = "situations")
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@ToString
public class SituationStored {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "found_vacancies")
    private Integer found;

    @CreatedDate
    @Column
    private ZonedDateTime createdDate;

    @LastModifiedDate
    @Column
    private ZonedDateTime lastModifiedDate;

    public static SituationStored of(VacanciesInfo vacanciesInfo) {
        Assert.notNull(vacanciesInfo, "vacanciesInfo must not be null");

        SituationStored currentSituation = new SituationStored();
        currentSituation.setFound(vacanciesInfo.getFound());

        return currentSituation;
    }
}
