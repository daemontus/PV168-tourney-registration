package fi.muni.pv168;

import java.util.Date;

/**
 * <p>This is an entity class representing one discipline of tourney.
 * Every discipline has synthetic id, name, starting and ending time and maximum number of participants.</p>
 *
 * @version 09/03/2014ß
 */
public class Discipline {

	private Long id;
	private String name;
	private Date start;
	private Date end;
	private int maxParticipants;

	public Discipline() {
		this(null,null,null,null,0);
	}

	public Discipline(Long id, String name, Date start, Date end, int maxParticipants) {
		this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.maxParticipants = maxParticipants;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "id=" + id +
                ", nameß='" + name + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", maxParticipants=" + maxParticipants +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Discipline)) return false;

        Discipline that = (Discipline) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}