package Model;

import java.util.Objects;

public class Crew {
	
	private int movieId;
	private String name;
	private String role;
	
	public Crew() {
		
	}
	
	public Crew(int movieId, String name, String role) {
		this.movieId = movieId;
		this.name = name;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Crew [movieId=" + movieId + ", name=" + name + ", role=" + role + "]\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(movieId, name, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Crew other = (Crew) obj;
		return movieId == other.movieId &&
				Objects.equals(name, other.name) &&
				Objects.equals(role, other.role);
	}
}
