import java.util.List;
import java.util.Scanner;
import Data.LoadData;
import Data.LoadMovies;
import Data.LoadRatings;
import Model.Movies;
import Model.Rating;
import Service.MovieOperations;
import Service.RatingsOperations;
import Service.UserOperations;
import ServiceImpl.MovieOperationsImpl;
import ServiceImpl.RatingsOperationsImpl;
import ServiceImpl.UserOperationsImpl;

public class Application {
	
	public static void main(String[] args) {
		
		UserOperations userop = new UserOperationsImpl();
		MovieOperations moviesop = new MovieOperationsImpl();
		RatingsOperations ratingsop = new RatingsOperationsImpl();
		
		LoadMovies.loadMovies(moviesop);
		LoadData.loadUsersData(userop);
		LoadRatings.loadRatings(userop, moviesop, ratingsop);
		
		ApplicationOperations appOps = new ApplicationOperations(userop, moviesop, ratingsop);
		Scanner sc = new Scanner(System.in);
		int choice = 0;
		
		do {
			System.out.println("\n----- Movie Review System -----");
			System.out.println("1.  Print User with most ratings");
			System.out.println("2.  Print rating count per user");
			System.out.println("3.  Print movies rated by user");
			System.out.println("4.  Print average rating of each movie");
			System.out.println("5.  Print total number of ratings");
			System.out.println("6.  Print movies with no ratings");
			System.out.println("7.  Print users with no ratings");
			System.out.println("8.  Print most rated movie");
			System.out.println("9.  Get all ratings of a movie");
			System.out.println("10. View movie details (Cast & Crew)");
			System.out.println("11. Exit");
			
			System.out.print("Enter your choice: ");
			
			if (!sc.hasNextInt()) {
				String invalid = sc.next();
				System.out.println("Invalid input: '" + invalid + "'. Please enter a number between 1 and 11.");
				continue;
			}
			
			choice = sc.nextInt();
			System.out.println();
			
			switch (choice) {
				case 1:
					appOps.printUserWithMostRatings();
					break;
				case 2:
					appOps.printRatingCountPerUser();
					break;
				case 3:
					appOps.printMoviesRatedByUser();
					break;
				case 4:
					appOps.printAverageRatingOfEachMovie();
					break;
				case 5:
					appOps.printTotalNumberOfRatings();
					break;
				case 6:
					appOps.printMoviesWithNoRatings();
					break;
				case 7:
					appOps.printUsersWithNoRatings();
					break;
				case 8:
					appOps.printMostRatedMovie();
					break;
				case 9:
					List<Movies> allMovies = moviesop.getAllMovies();
					if (allMovies.isEmpty()) {
						System.out.println("No movies available.");
						break;
					}
					System.out.println("Available Movies:");
					for (Movies m : allMovies) {
						System.out.println("ID: " + m.getId() + " | Title: " + m.getName() + " | Genre: " + m.getJonour());
					}
					System.out.print("\nPlease enter movie id from above list to get all ratings of the movie: ");
					if (!sc.hasNextInt()) {
						String inv = sc.next();
						System.out.println("Invalid input: '" + inv + "'. Expected a movie ID number.");
						break;
					}
					int movieId = sc.nextInt();
					Movies selectedMovie = moviesop.getMoviebyId(movieId);
					if (selectedMovie == null) {
						System.out.println("Movie with ID " + movieId + " does not exist.");
						break;
					}
					List<Rating> ratingsList = appOps.getAllRatingsOfMovie(movieId);
					if (ratingsList.isEmpty()) {
						System.out.println("No ratings found for movie: " + selectedMovie.getName());
					} else {
						System.out.println("Ratings for " + selectedMovie.getName() + ":");
						for (Rating r : ratingsList) {
							String userName = (r.getUserId() != null) ? r.getUserId().getName() : "Unknown User";
							System.out.println("- " + userName + ": " + r.getRating() + " stars");
						}
					}
					break;
				case 10:
					List<Movies> moviesForDetails = moviesop.getAllMovies();
					if (moviesForDetails.isEmpty()) {
						System.out.println("No movies available.");
						break;
					}
					System.out.println("Available Movies:");
					for (Movies m : moviesForDetails) {
						System.out.println("ID: " + m.getId() + " | Title: " + m.getName() + " | Genre: " + m.getJonour());
					}
					System.out.print("\nPlease enter movie id to view Cast, Crew & Details: ");
					if (!sc.hasNextInt()) {
						String inv = sc.next();
						System.out.println("Invalid input: '" + inv + "'. Expected a movie ID number.");
						break;
					}
					int mId = sc.nextInt();
					appOps.printMovieDetails(mId);
					break;
				case 11:
					System.out.println("Thank you for using Movie Review System. Goodbye!");
					break;
				default:
					System.out.println("Invalid choice. Please enter a number between 1 and 11.");
					break;
			}
		
		} while (choice != 11);
		
		sc.close();
	}
}
