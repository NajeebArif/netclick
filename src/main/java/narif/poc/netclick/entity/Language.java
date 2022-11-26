package narif.poc.netclick.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * The persistent class for the language database table.
 * 
 */
@Entity
@NamedQuery(name="Language.findAll", query="SELECT l FROM Language l")
public class Language implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="language_id")
	private Integer languageId;

	@Column(name="last_update")
	private Timestamp lastUpdate;

	private String name;

	//bi-directional many-to-one association to Film
//	@OneToMany(mappedBy="language", fetch = FetchType.LAZY)
//	@JsonIgnore
//	private List<Film> films;

	public Language() {
	}

	public Integer getLanguageId() {
		return this.languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public Timestamp getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public List<Film> getFilms() {
//		return this.films;
//	}
//
//	public void setFilms(List<Film> films) {
//		this.films = films;
//	}
//
//	public Film addFilm(Film film) {
//		getFilms().add(film);
//		film.setLanguage(this);
//
//		return film;
//	}
//
//	public Film removeFilm(Film film) {
//		getFilms().remove(film);
//		film.setLanguage(null);
//
//		return film;
//	}

}