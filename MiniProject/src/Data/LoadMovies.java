package Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import Model.Cast;
import Model.Crew;
import Model.Movies;
import Service.MovieOperations;

public class LoadMovies {
	
	public static void loadMovies(MovieOperations moviesop) {
		File movieFile = LoadData.getFile("movielist.csv");
		if (movieFile.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(movieFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.startsWith("\uFEFF")) {
						line = line.substring(1).trim();
					}
					if (line.isEmpty() || line.startsWith("#")) {
						continue;
					}
					String[] movieData = line.split(",");
					if (movieData.length < 3) {
						continue;
					}
					try {
						int id = Integer.parseInt(movieData[0].trim());
						String name = movieData[1].trim();
						String genre = movieData[2].trim();
						moviesop.addMovie(new Movies(id, name, genre));
					} catch (NumberFormatException e) {
						continue;
					}
				}
			} catch (IOException e) {
				System.err.println("Error loading movies: " + e.getMessage());
			}
		} else {
			System.err.println("Warning: movielist.csv not found at " + movieFile.getAbsolutePath());
		}
		
		File crewFile = LoadData.getFile("movie_crew.csv");
		if (crewFile.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(crewFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.startsWith("\uFEFF")) {
						line = line.substring(1).trim();
					}
					if (line.isEmpty() || line.startsWith("#")) {
						continue;
					}
					String[] crewData = line.split(",");
					if (crewData.length < 3) {
						continue;
					}
					try {
						int movieId = Integer.parseInt(crewData[0].trim());
						String name = crewData[1].trim();
						String role = crewData[2].trim();
						Movies movie = moviesop.getMoviebyId(movieId);
						if (movie != null) {
							movie.addCrew(new Crew(movieId, name, role));
						}
					} catch (NumberFormatException e) {
						continue;
					}
				}
			} catch (IOException e) {
				System.err.println("Error loading movie crew: " + e.getMessage());
			}
		}
		
		File castFile = LoadData.getFile("movie_cast.csv");
		if (castFile.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(castFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.startsWith("\uFEFF")) {
						line = line.substring(1).trim();
					}
					if (line.isEmpty() || line.startsWith("#")) {
						continue;
					}
					String[] castData = line.split(",");
					if (castData.length < 4) {
						continue;
					}
					try {
						int movieId = Integer.parseInt(castData[0].trim());
						String name = castData[1].trim();
						String character = castData[2].trim();
						String role = castData[3].trim();
						Movies movie = moviesop.getMoviebyId(movieId);
						if (movie != null) {
							movie.addCast(new Cast(movieId, name, character, role));
						}
					} catch (NumberFormatException e) {
						continue;
					}
				}
			} catch (IOException e) {
				System.err.println("Error loading movie cast: " + e.getMessage());
			}
		}
	}
}




