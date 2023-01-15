package ru.nickliq.sphere.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nickliq.sphere.model.VacanciesInfo;

public interface HeadHunterApi {

    @GetMapping("vacancies")
    ResponseEntity<VacanciesInfo> getVacancies(@RequestParam(value = "text", defaultValue = "java") String text);
}
