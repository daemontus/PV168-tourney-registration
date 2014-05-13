package fi.muni.pv168.ui.table;

import fi.muni.pv168.Discipline;
import fi.muni.pv168.Knight;
import fi.muni.pv168.Match;

public class Result {
    private Knight knight;
    private Discipline discipline;
    private Match match;

    public Result() {}

    public Result(Knight knight, Discipline discipline, Match match) {
        this.knight = knight;
        this.discipline = discipline;
        this.match = match;
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

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
