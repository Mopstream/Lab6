package com.mopstream.common.data;

import java.time.LocalDate;

/**
 * Main character. Is stored in the collection.
 */
public class LabWork implements Comparable<LabWork> {
    private final Long id;
    private final String name;
    private final Coordinates coordinates;
    private LocalDate creationDate;
    private final long minimalPoint;
    private final Difficulty difficulty;
    private final Discipline discipline;


    public LabWork(Long id, String name, Coordinates coordinates, LocalDate creationDate,
                   long minimalPoint, Difficulty difficulty, Discipline discipline) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.creationDate = LocalDate.now();
        this.minimalPoint = minimalPoint;
        this.difficulty = difficulty;
        this.discipline = discipline;
    }

    /**
     * @return ID of the labwork.
     */
    public Long getId() {
        return id;
    }

    /**
     * @return Name of the labwork.
     */
    public String getName() {
        return name;
    }


    /**
     * @return Coordinates of the labwork.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return Minimal Point of the labwork.
     */
    public Long getMinimalPoint() {
        return minimalPoint;
    }

    /**
     * @return Difficulty of the labwork.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * @return Discipline of the labwork.
     */
    public Discipline getDiscipline() {
        return discipline;
    }


    /**
     * @return Creation date of labwork.
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    @Override
    public int compareTo(LabWork labWorkObj) {
        return name.compareTo(labWorkObj.getName());
    }

    @Override
    public String toString() {
        String info = "";
        info += "Лабораторная работа №" + id;
        info += " (добавлена " + creationDate + ")";
        info += "\n Название: " + name;
        info += "\n Координаты: " + coordinates;
        info += "\n Минимальный балл: " + minimalPoint;
        info += "\n Сложность: " + difficulty;
        info += "\n Предмет: " + discipline;
        return info;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + coordinates.hashCode() + difficulty.hashCode() + discipline.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof LabWork) {
            LabWork labWorkObj = (LabWork) obj;
            return name.equals(labWorkObj.getName()) && coordinates.equals(labWorkObj.getCoordinates())
                    && (minimalPoint == labWorkObj.getMinimalPoint()) && (difficulty == labWorkObj.getDifficulty())
                    && (discipline.equals(labWorkObj.getDiscipline()));
        }
        return false;
    }
}
