package ru.nickliq.sphere.service;

import feign.FeignException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.nickliq.sphere.domain.SituationStored;
import ru.nickliq.sphere.feign.HeadHunterFeignClient;
import ru.nickliq.sphere.model.CurrentSituationModel;
import ru.nickliq.sphere.model.Situation;
import ru.nickliq.sphere.model.Tendency;
import ru.nickliq.sphere.model.VacanciesInfo;
import ru.nickliq.sphere.repo.SituationStoredRepository;

import static org.springframework.data.domain.Sort.Direction.DESC;

import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class CurrentSituationAndTendencyServiceImpl implements CurrentSituationAndTendencyService {

    private static final int PERIOD = 3;
    private static final int MIN_VACANCIES_FOR_GOOD = 2000;

    private final HeadHunterFeignClient headHunterFeignClient;

    private final SituationStoredRepository situationStoredRepository;

    public CurrentSituationAndTendencyServiceImpl(HeadHunterFeignClient headHunterFeignClient,
                                                  SituationStoredRepository situationStoredRepository) {
        this.headHunterFeignClient = headHunterFeignClient;
        this.situationStoredRepository = situationStoredRepository;
    }

    @Override
    public CurrentSituationModel computeCurrentSituation() {
        Pageable topTen = PageRequest.of(0, 10, DESC, "createdDate");
        List<SituationStored> storedSituations = situationStoredRepository.findAll(topTen).getContent();

        CurrentSituationModel currentSituationModel = new CurrentSituationModel();
        // Берем последнюю запись и вакансии из нее, не дергаем снова хх
        currentSituationModel.setVacanciesFound(storedSituations.get(0).getFound());
        currentSituationModel.setTendency(determineTendency(storedSituations.stream().map(SituationStored::getFound).collect(Collectors.toList())));
        currentSituationModel.setSituation(determineSituation(storedSituations));

        return currentSituationModel;
    }

    @Scheduled(fixedDelay = 28800000) // 8 часов
    private void getVacanciesAndSave() {
        VacanciesInfo vacanciesInfo;

        try {
            vacanciesInfo = headHunterFeignClient.getVacancies("java разработчик").getBody();
            //todo передавать keyword и формировать список "задач"
        } catch (FeignException fex) {
            return;
        }

        situationStoredRepository.save(SituationStored.of(vacanciesInfo));
    }

    private Tendency determineTendency(List<Integer> vacancies) {
        if (vacancies.size() < PERIOD * 2) {
            return Tendency.NOT_ENOUGH_DATA;
        }

        if (vacancies.subList(0, PERIOD).stream().mapToInt(e -> e).sum() >= vacancies.subList(PERIOD, PERIOD+PERIOD).stream().mapToInt(e -> e).sum()) {
            return Tendency.UP;
        } else {
            return Tendency.DOWN;
        }
        //todo FLAT
    }

    //todo взвешивать коэф. учитывать проценты
    private Situation determineSituation(List<SituationStored> storedSituations) {
        if (storedSituations.stream().anyMatch(e -> e.getFound() < MIN_VACANCIES_FOR_GOOD)) {
            return Situation.NOT_GOOD;
        } else {
            return Situation.ALL_GOOD;
        }
    }
}
