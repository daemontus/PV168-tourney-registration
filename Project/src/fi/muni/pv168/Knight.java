package fi.muni.pv168;

import java.util.Date;

/**
 * <p>Entity class representing one Knight fighting in a tourney.</p>
 * <p>Knight is represented by synthetic id, name, his castle, date of birth and description of his heraldry.</p>
 * <p>One knight can compete in multiple disciplines.</p>
 *
 * @version 09/03/2014
 */
public class Knight {

	private Long id;
	private String name;
	private String castle;
	private Date born;
	private String heraldry;

	public Knight() {
		this(null,null,null,null,null);
	}

	public Knight(Long id, String name, String castle, Date born, String heraldry) {
	    this.id = id;
        this.name = name;
        this.castle = castle;
        this.born = born;
        this.heraldry = heraldry;
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

    public String getCastle() {
        return castle;
    }

    public void setCastle(String castle) {
        this.castle = castle;
    }

    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public String getHeraldry() {
        return heraldry;
    }

    public void setHeraldry(String heraldry) {
        this.heraldry = heraldry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Knight)) return false;

        Knight knight = (Knight) o;

        return id != null && id.equals(knight.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Knight{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", castle='" + castle + '\'' +
                ", born=" + born +
                ", heraldry='" + heraldry + '\'' +
                '}';
    }
}