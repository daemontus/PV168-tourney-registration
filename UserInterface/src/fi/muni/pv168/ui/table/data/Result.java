package fi.muni.pv168.ui.table.data;

import fi.muni.pv168.Discipline;
import fi.muni.pv168.Knight;

public class Result {
    private Knight knight;
    private Discipline discipline;
    private Integer points;

    public Result(Knight knight, Discipline discipline, Integer points) {
        this.knight = knight;
        this.discipline = discipline;
        this.points = points;
    }

    public Knight getKnight() {
        return knight;
    }

    public void setKnight(Knight knight) {
        this.knight = knight;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
