package Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import Model.Movies;
import Model.Rating;
import Model.User;
import Service.MovieOperations;
import Service.RatingsOperations;
import Service.UserOperations;

public class LoadRatings {
	
	public static void loadRatings(UserOperations userop, MovieOperations movieop, RatingsOperations ratingsop) {
		File file = LoadData.getFile("user_ratings.csv");
		if (!file.exists()) {
			System.err.println("Warning: user_ratings.csv not found at " + file.getAbsolutePath());
			return;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("\uFEFF")) {
					line = line.substring(1).trim();
				}
				if (line.isEmpty() || line.startsWith("#")) {
					continue;
				}
				String[] ratingData = line.split(",");
				if (ratingData.length < 3) {
					continue;
				}
				try {
					int userId = Integer.parseInt(ratingData[0].trim());
					int movieId = Integer.parseInt(ratingData[1].trim());
					int ratingVal = Integer.parseInt(ratingData[2].trim());

					User user = userop.getUserbyId(userId);
					Movies movie = movieop.getMoviebyId(movieId);

					if (user != null && movie != null) {
						Rating r = new Rating(user, movie, ratingVal);
						ratingsop.addRatings(r);
						user.addRating(r);
					}
				} catch (NumberFormatException e) {
					continue;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading ratings: " + e.getMessage());
		}
	}
}
