package ru.nickliq.sphere.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.nickliq.sphere.domain.SituationStored;
import ru.nickliq.sphere.model.CurrentSituationModel;
import ru.nickliq.sphere.repo.SituationStoredRepository;
import ru.nickliq.sphere.service.CurrentSituationAndTendencyService;

import java.util.List;

@Controller
public class SphereController {

    private final SituationStoredRepository situationStoredRepository;

    private final CurrentSituationAndTendencyService service;

    public SphereController(SituationStoredRepository situationStoredRepository,
                            CurrentSituationAndTendencyService service) {
        this.situationStoredRepository = situationStoredRepository;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<CurrentSituationModel> situation(){

        return ResponseEntity.ok(service.computeCurrentSituation());
    }

    @GetMapping("get")
    public ResponseEntity<List<SituationStored>> get() {
        List<SituationStored> storedList = situationStoredRepository.findAll();

        return ResponseEntity.ok(storedList);
    }
}
