package fi.muni.pv168;

/**
 * <p>Entity class representing Knights participation in Discipline.</p>
 * <p>If results are unknown (tourney is not over yet), points should be null.</p>
 */
public class Match {

	private Long id;
	private Knight knight;
	private Discipline discipline;
	private int startNumber;
	private Integer points;

	public Match() {
		this(null, null, null, 0, null);
	}

	public Match(Long id, Knight knight, Discipline discipline, int number, Integer points) {
		this.id = id;
        this.knight = knight;
        this.discipline = discipline;
        this.startNumber = number;
        this.points = points;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(int startNumber) {
        this.startNumber = startNumber;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match)) return false;

        Match match = (Match) o;

        return id != null && id.equals(match.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", knight=" + knight +
                ", discipline=" + discipline +
                ", startNumber=" + startNumber +
                ", points=" + points +
                '}';
    }
}