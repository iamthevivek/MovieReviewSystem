package Model;

import java.util.Objects;

public class Rating {
      
	private User user;
	private Movies movies;
	private int rating;
	
	public Rating() {
		
	}
	
	public Rating(User user, Movies movies, int rating) {
		this.user = user;
		this.movies = movies;
		this.rating = rating;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

	public User getUserId() {
		return user;
	}
	
	public void setMovie(Movies movies) {
		this.movies = movies;
	}

	public Movies getMovie() {
		return movies;
	}
	
	public Movies getMovieId() {
		return movies;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public int getRating() {
		return rating;
	}

	@Override
	public String toString() {
		String userName = (user != null) ? user.getName() : "Unknown";
		String movieName = (movies != null) ? movies.getName() : "Unknown";
		return "[username=" + userName + ", moviename=" + movieName + ", rating=" + rating + "]\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(movies, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rating other = (Rating) obj;
		return Objects.equals(movies, other.movies) && Objects.equals(user, other.user);
	}
}
