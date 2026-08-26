package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Movies {

	private int id;
	private String name;
	private String jonour;
	private List<Crew> crew;
	private List<Cast> cast;
	
	public Movies(int id, String name, String jonour, List<Crew> crew, List<Cast> cast) {
		this.id = id;
		this.name = name;
		this.jonour = jonour;
		this.crew = (crew != null) ? crew : new ArrayList<>();
		this.cast = (cast != null) ? cast : new ArrayList<>();
	}

	public Movies(int id, String name, String jonour) {
		this.id = id;
		this.name = name;
		this.jonour = jonour;
		this.cast = new ArrayList<>();
		this.crew = new ArrayList<>();
	}

	public void addCrew(Crew crew) {
		this.crew.add(crew);
	}
	
	public void addCast(Cast cast) {
		this.cast.add(cast);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJonour() {
		return jonour;
	}

	public void setJonour(String jonour) {
		this.jonour = jonour;
	}

	public String getGenre() {
		return jonour;
	}

	public void setGenre(String genre) {
		this.jonour = genre;
	}

	public List<Crew> getCrew() {
		return crew;
	}

	public void setCrew(List<Crew> crew) {
		this.crew = crew;
	}

	public List<Cast> getCast() {
		return cast;
	}

	public void setCast(List<Cast> cast) {
		this.cast = cast;
	}

	@Override
	public String toString() {
		return "Movies [id=" + id + ", name=" + name + ", genre=" + jonour + ", crew=" + crew + ", cast=" + cast + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Movies other = (Movies) obj;
		return id == other.id;
	}
}
