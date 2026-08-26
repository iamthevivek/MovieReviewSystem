package Model;

import java.util.Objects;

public class Cast {
	
	private int movieId;
	private String name;
	private String character;
	private String role;
	
	public Cast() {
		
	}

	public Cast(int movieId, String name, String character, String role) {
		this.movieId = movieId;
		this.name = name;
		this.character = character;
		this.role = role;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Cast [movieId=" + movieId + ", name=" + name + ", character=" + character + ", role=" + role + "]\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(movieId, name, character, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Cast other = (Cast) obj;
		return movieId == other.movieId &&
				Objects.equals(name, other.name) &&
				Objects.equals(character, other.character) &&
				Objects.equals(role, other.role);
	}
}
