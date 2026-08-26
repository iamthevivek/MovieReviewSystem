
# 🎬 Movie Review System

A console-based Java application that allows users to analyze movies, ratings, users, and calculate average ratings and statistics. This project demonstrates key Java concepts such as OOP, file handling, and collection frameworks, following a layered architecture with services and models.

---

## 📁 Project Structure

- **src/Application.java** – Entry point of the application
- **src/ApplicationOperations.java** – High-level console reporting and display operations
- **src/Data/**
  - `LoadData.java` – Loads user data from CSV
  - `LoadMovies.java` – Loads movie, cast, and crew data from CSV
  - `LoadRatings.java` – Loads rating data from CSV
- **src/Model/**
  - `User.java`, `Movies.java`, `Rating.java` – Core entity classes
  - `Cast.java`, `Crew.java` – Metadata classes
- **src/Service/**
  - `UserOperations.java`, `MovieOperations.java`, `RatingsOperations.java` – Business logic interfaces
- **src/ServiceImpl/**
  - `UserOperationsImpl.java`, `MovieOperationsImpl.java`, `RatingsOperationsImpl.java` – Service implementations
- **data/**
  - `users.csv`, `movielist.csv`, `movie_crew.csv`, `movie_cast.csv`, `user_ratings.csv` – Sample data files

---

## 🧠 Features

- Dynamic CSV data loading with automated path resolution
- User activity analysis (user with most ratings, rating counts, users with no ratings)
- Movie performance statistics (average ratings, most rated movie, unrated movies)
- Individual movie rating drill-down by movie ID
- Clean and robust console UI with error handling for invalid input

---

## 🛠️ Technologies Used

- **Language:** Java 8+
- **Concepts:** Layered Architecture, OOP, Java Collections Framework (`List`, `Map`, `Set`, `Stream API`), File I/O (`BufferedReader`, try-with-resources)
- **Tools:** VS Code / Eclipse / CLI, Git

---

## 🚀 How to Run

### Command Line (PowerShell / Bash)
1. Navigate to the `MiniProject` folder:
   ```bash
   cd MiniProject
   ```
2. Compile the project:
   ```bash
   javac -d bin src/*.java src/*/*.java
   ```
3. Run the application:
   ```bash
   java -cp bin Application
   ```

---