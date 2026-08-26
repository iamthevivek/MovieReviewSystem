import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Model.Movies;
import Model.Rating;
import Model.User;
import Service.MovieOperations;
import Service.RatingsOperations;
import Service.UserOperations;

public class ApplicationOperations {

	private UserOperations userop;
	private MovieOperations moviesop;
	private RatingsOperations ratingsop;

	public ApplicationOperations() {
	}

	public ApplicationOperations(UserOperations userOp, MovieOperations movieOp, RatingsOperations ratingsOp) {
		this.userop = userOp;
		this.moviesop = movieOp;
		this.ratingsop = ratingsOp;
	}

	public void printRatingCountPerUser() {
		List<User> users = userop.getAllUsers();
		if (users.isEmpty()) {
			System.out.println("No users found.");
			return;
		}

		System.out.println("Rating count per user:");
		for (User user : users) {
			int count = (user.getRating() != null) ? user.getRating().size() : 0;
			System.out.println(user.getName() + " gave " + count + " ratings");
		}
	}

	public void printUserWithMostRatings() {
		List<User> users = userop.getAllUsers();
		if (users.isEmpty()) {
			System.out.println("No users found.");
			return;
		}

		int max = users.stream()
				.mapToInt(user -> (user.getRating() != null) ? user.getRating().size() : 0)
				.max()
				.orElse(0);

		if (max == 0) {
			System.out.println("No ratings submitted yet.");
			return;
		}

		for (User user : users) {
			int count = (user.getRating() != null) ? user.getRating().size() : 0;
			if (count == max) {
				System.out.println("User with most ratings: " + user.getName() + " (" + max + " ratings)");
			}
		}
	}

	public void printMoviesRatedByUser() {
		List<User> users = userop.getAllUsers();
		List<Rating> ratings = ratingsop.getAllRatings();

		if (users.isEmpty()) {
			System.out.println("No users found.");
			return;
		}

		for (User u : users) {
			System.out.println("Movies rated by " + u.getName() + ":");
			boolean rated = false;

			for (Rating r : ratings) {
				if (r.getUserId() != null && r.getUserId().getId() == u.getId()) {
					rated = true;
					String movieTitle = (r.getMovieId() != null) ? r.getMovieId().getName() : "Unknown Movie";
					System.out.println("- " + movieTitle + ": " + r.getRating() + " stars");
				}
			}
			if (!rated) {
				System.out.println("No movies rated");
			}
			System.out.println();
		}
	}

	public void printAverageRatingOfEachMovie() {
		List<Movies> movies = moviesop.getAllMovies();
		if (movies.isEmpty()) {
			System.out.println("No movies found.");
			return;
		}

		System.out.println("Average rating of each movie:");
		for (Movies movie : movies) {
			double avg = ratingsop.getAverageRating(movie);
			if (avg > 0) {
				System.out.printf("%s - Average Rating: %.2f%n", movie.getName(), avg);
			} else {
				System.out.println(movie.getName() + " - No ratings yet");
			}
		}
	}

	public void printTotalNumberOfRatings() {
		List<Rating> ratings = ratingsop.getAllRatings();
		System.out.println("Total no of ratings are: " + ratings.size());
	}

	public void printMoviesWithNoRatings() {
		List<Movies> movies = moviesop.getAllMovies();
		List<Rating> ratings = ratingsop.getAllRatings();

		Set<Integer> ratedMovies = new HashSet<>();
		for (Rating r : ratings) {
			if (r.getMovieId() != null) {
				ratedMovies.add(r.getMovieId().getId());
			}
		}

		System.out.println("Movies with no ratings:");
		boolean found = false;
		for (Movies m : movies) {
			if (!ratedMovies.contains(m.getId())) {
				System.out.println("- " + m.getName());
				found = true;
			}
		}

		if (!found) {
			System.out.println("All movies have at least one rating");
		}
	}

	public void printUsersWithNoRatings() {
		List<User> users = userop.getAllUsers();
		List<Rating> ratings = ratingsop.getAllRatings();
		Set<Integer> ratedUsers = new HashSet<>();

		for (Rating r : ratings) {
			if (r.getUserId() != null) {
				ratedUsers.add(r.getUserId().getId());
			}
		}

		System.out.println("Users with no ratings:");
		boolean found = false;
		for (User u : users) {
			if (!ratedUsers.contains(u.getId())) {
				System.out.println("- " + u.getName());
				found = true;
			}
		}

		if (!found) {
			System.out.println("All users have given at least one rating");
		}
	}

	public void printMostRatedMovie() {
		List<Movies> movies = moviesop.getAllMovies();
		List<Rating> ratings = ratingsop.getAllRatings();

		if (ratings.isEmpty()) {
			System.out.println("No ratings available");
			return;
		}

		Map<Integer, Integer> ratingCount = new HashMap<>();
		for (Rating r : ratings) {
			if (r.getMovieId() != null) {
				int movieId = r.getMovieId().getId();
				ratingCount.put(movieId, ratingCount.getOrDefault(movieId, 0) + 1);
			}
		}

		int maxCount = 0;
		for (int count : ratingCount.values()) {
			if (count > maxCount) {
				maxCount = count;
			}
		}

		if (maxCount == 0) {
			System.out.println("No ratings available");
			return;
		}

		for (Map.Entry<Integer, Integer> entry : ratingCount.entrySet()) {
			if (entry.getValue() == maxCount) {
				Movies m = moviesop.getMoviebyId(entry.getKey());
				String movieName = (m != null) ? m.getName() : "Movie ID #" + entry.getKey();
				System.out.println("Most rated movie is " + movieName + " -> " + maxCount + " ratings");
			}
		}
	}

	public List<Rating> getAllRatingsOfMovie(int movieId) {
		List<Rating> ratings = ratingsop.getAllRatings();
		List<Rating> ratingslist = new ArrayList<>();

		for (Rating r : ratings) {
			if (r.getMovieId() != null && r.getMovieId().getId() == movieId) {
				ratingslist.add(r);
			}
		}
		return ratingslist;
	}

	public void printMovieDetails(int movieId) {
		Movies m = moviesop.getMoviebyId(movieId);
		if (m == null) {
			System.out.println("Movie with ID " + movieId + " does not exist.");
			return;
		}

		System.out.println("----------------------------------------------");
		System.out.println("Title: " + m.getName() + " | Genre: " + m.getJonour());
		System.out.println("----------------------------------------------");

		System.out.println("Cast Members:");
		List<Model.Cast> castList = m.getCast();
		if (castList == null || castList.isEmpty()) {
			System.out.println("  No cast details available.");
		} else {
			for (Model.Cast c : castList) {
				System.out.println("  - " + c.getName() + " as '" + c.getCharacter() + "' (" + c.getRole() + ")");
			}
		}

		System.out.println("\nCrew Members:");
		List<Model.Crew> crewList = m.getCrew();
		if (crewList == null || crewList.isEmpty()) {
			System.out.println("  No crew details available.");
		} else {
			for (Model.Crew cr : crewList) {
				System.out.println("  - " + cr.getName() + " (" + cr.getRole() + ")");
			}
		}

		double avg = ratingsop.getAverageRating(m);
		if (avg > 0) {
			System.out.printf("\nAverage Rating: %.2f / 5.0%n", avg);
		} else {
			System.out.println("\nAverage Rating: No ratings yet");
		}
	}
}


