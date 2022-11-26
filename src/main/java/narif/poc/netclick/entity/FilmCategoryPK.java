package narif.poc.netclick.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the film_category database table.
 * 
 */
@Embeddable
public class FilmCategoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="film_id", insertable=false, updatable=false)
	private Integer filmId;

	@Column(name="category_id", insertable=false, updatable=false)
	private Integer categoryId;

	public FilmCategoryPK() {
	}
	public Integer getFilmId() {
		return this.filmId;
	}
	public void setFilmId(Integer filmId) {
		this.filmId = filmId;
	}
	public Integer getCategoryId() {
		return this.categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FilmCategoryPK)) {
			return false;
		}
		FilmCategoryPK castOther = (FilmCategoryPK)other;
		return 
			this.filmId.equals(castOther.filmId)
			&& this.categoryId.equals(castOther.categoryId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.filmId.hashCode();
		hash = hash * prime + this.categoryId.hashCode();
		
		return hash;
	}
}