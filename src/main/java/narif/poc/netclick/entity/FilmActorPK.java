package narif.poc.netclick.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the film_actor database table.
 * 
 */
@Embeddable
public class FilmActorPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="actor_id", insertable=false, updatable=false)
	private Integer actorId;

	@Column(name="film_id", insertable=false, updatable=false)
	private Integer filmId;

	public FilmActorPK() {
	}
	public Integer getActorId() {
		return this.actorId;
	}
	public void setActorId(Integer actorId) {
		this.actorId = actorId;
	}
	public Integer getFilmId() {
		return this.filmId;
	}
	public void setFilmId(Integer filmId) {
		this.filmId = filmId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FilmActorPK)) {
			return false;
		}
		FilmActorPK castOther = (FilmActorPK)other;
		return 
			this.actorId.equals(castOther.actorId)
			&& this.filmId.equals(castOther.filmId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.actorId.hashCode();
		hash = hash * prime + this.filmId.hashCode();
		
		return hash;
	}
}