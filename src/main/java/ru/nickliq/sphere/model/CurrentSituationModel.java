package ru.nickliq.sphere.model;

import lombok.Data;

@Data
public class CurrentSituationModel {

    Integer vacanciesFound;

    Tendency tendency;

    Situation situation;
}
