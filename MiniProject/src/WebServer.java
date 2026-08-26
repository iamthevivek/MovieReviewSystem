import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;

import Data.LoadData;
import Data.LoadMovies;
import Data.LoadRatings;
import Model.Cast;
import Model.Crew;
import Model.Movies;
import Model.Rating;
import Model.User;
import Service.MovieOperations;
import Service.RatingsOperations;
import Service.UserOperations;
import ServiceImpl.MovieOperationsImpl;
import ServiceImpl.RatingsOperationsImpl;
import ServiceImpl.UserOperationsImpl;

public class WebServer {

    private static UserOperations userop;
    private static MovieOperations moviesop;
    private static RatingsOperations ratingsop;

    public static void main(String[] args) throws IOException {
        userop = new UserOperationsImpl();
        moviesop = new MovieOperationsImpl();
        ratingsop = new RatingsOperationsImpl();

        LoadMovies.loadMovies(moviesop);
        LoadData.loadUsersData(userop);
        LoadRatings.loadRatings(userop, moviesop, ratingsop);

        int port = 8080;
        String portEnv = System.getenv("PORT");
        if (portEnv != null && !portEnv.trim().isEmpty()) {
            try {
                port = Integer.parseInt(portEnv.trim());
            } catch (NumberFormatException ignored) {}
        }

        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);

        server.createContext("/", new StaticFileHandler());
        server.createContext("/api/movies", new MoviesApiHandler());
        server.createContext("/api/users", new UsersApiHandler());
        server.createContext("/api/ratings", new RatingsApiHandler());
        server.createContext("/api/stats", new StatsApiHandler());
        server.createContext("/api/add-rating", new AddRatingApiHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("==================================================");
        System.out.println(" Movie Review System Web Server is LIVE!");
        System.out.println(" Listening on: http://0.0.0.0:" + port);
        System.out.println("==================================================");
    }

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    static class MoviesApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            List<Movies> list = moviesop.getAllMovies();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                Movies m = list.get(i);
                double avg = ratingsop.getAverageRating(m);
                sb.append("{");
                sb.append("\"id\":").append(m.getId()).append(",");
                sb.append("\"name\":\"").append(escapeJson(m.getName())).append("\",");
                sb.append("\"genre\":\"").append(escapeJson(m.getJonour())).append("\",");
                sb.append("\"averageRating\":").append(String.format(Locale.US, "%.2f", avg)).append(",");
                
                sb.append("\"cast\":[");
                List<Cast> casts = m.getCast();
                for (int j = 0; j < casts.size(); j++) {
                    Cast c = casts.get(j);
                    sb.append("{\"name\":\"").append(escapeJson(c.getName()))
                      .append("\",\"character\":\"").append(escapeJson(c.getCharacter()))
                      .append("\",\"role\":\"").append(escapeJson(c.getRole())).append("\"}");
                    if (j < casts.size() - 1) sb.append(",");
                }
                sb.append("],");

                sb.append("\"crew\":[");
                List<Crew> crews = m.getCrew();
                for (int j = 0; j < crews.size(); j++) {
                    Crew cr = crews.get(j);
                    sb.append("{\"name\":\"").append(escapeJson(cr.getName()))
                      .append("\",\"role\":\"").append(escapeJson(cr.getRole())).append("\"}");
                    if (j < crews.size() - 1) sb.append(",");
                }
                sb.append("]");

                sb.append("}");
                if (i < list.size() - 1) sb.append(",");
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    static class UsersApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            List<User> list = userop.getAllUsers();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                User u = list.get(i);
                int count = (u.getRating() != null) ? u.getRating().size() : 0;
                sb.append("{");
                sb.append("\"id\":").append(u.getId()).append(",");
                sb.append("\"name\":\"").append(escapeJson(u.getName())).append("\",");
                sb.append("\"gender\":\"").append(escapeJson(u.getGender())).append("\",");
                sb.append("\"age\":").append(u.getAge()).append(",");
                sb.append("\"ratingCount\":").append(count);
                sb.append("}");
                if (i < list.size() - 1) sb.append(",");
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    static class RatingsApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            List<Rating> list = ratingsop.getAllRatings();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                Rating r = list.get(i);
                String uName = (r.getUserId() != null) ? r.getUserId().getName() : "Unknown";
                String mName = (r.getMovieId() != null) ? r.getMovieId().getName() : "Unknown";
                int mId = (r.getMovieId() != null) ? r.getMovieId().getId() : 0;
                int uId = (r.getUserId() != null) ? r.getUserId().getId() : 0;
                sb.append("{");
                sb.append("\"userId\":").append(uId).append(",");
                sb.append("\"userName\":\"").append(escapeJson(uName)).append("\",");
                sb.append("\"movieId\":").append(mId).append(",");
                sb.append("\"movieName\":\"").append(escapeJson(mName)).append("\",");
                sb.append("\"rating\":").append(r.getRating());
                sb.append("}");
                if (i < list.size() - 1) sb.append(",");
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    static class StatsApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            List<User> users = userop.getAllUsers();
            List<Movies> movies = moviesop.getAllMovies();
            List<Rating> ratings = ratingsop.getAllRatings();

            int maxReviews = 0;
            String topUser = "None";
            for (User u : users) {
                int count = (u.getRating() != null) ? u.getRating().size() : 0;
                if (count > maxReviews) {
                    maxReviews = count;
                    topUser = u.getName();
                }
            }

            Map<Integer, Integer> movieReviewCount = new HashMap<>();
            for (Rating r : ratings) {
                if (r.getMovieId() != null) {
                    int mId = r.getMovieId().getId();
                    movieReviewCount.put(mId, movieReviewCount.getOrDefault(mId, 0) + 1);
                }
            }
            int maxMovieReviews = 0;
            String mostRatedMovieName = "None";
            for (Map.Entry<Integer, Integer> e : movieReviewCount.entrySet()) {
                if (e.getValue() > maxMovieReviews) {
                    maxMovieReviews = e.getValue();
                    Movies m = moviesop.getMoviebyId(e.getKey());
                    if (m != null) mostRatedMovieName = m.getName();
                }
            }

            Set<Integer> ratedMovieIds = new HashSet<>();
            Set<Integer> ratedUserIds = new HashSet<>();
            for (Rating r : ratings) {
                if (r.getMovieId() != null) ratedMovieIds.add(r.getMovieId().getId());
                if (r.getUserId() != null) ratedUserIds.add(r.getUserId().getId());
            }

            List<String> unratedMovies = new ArrayList<>();
            for (Movies m : movies) {
                if (!ratedMovieIds.contains(m.getId())) unratedMovies.add(m.getName());
            }

            List<String> unratedUsers = new ArrayList<>();
            for (User u : users) {
                if (!ratedUserIds.contains(u.getId())) unratedUsers.add(u.getName());
            }

            StringBuilder sb = new StringBuilder("{");
            sb.append("\"totalMovies\":").append(movies.size()).append(",");
            sb.append("\"totalUsers\":").append(users.size()).append(",");
            sb.append("\"totalRatings\":").append(ratings.size()).append(",");
            sb.append("\"topUser\":{\"name\":\"").append(escapeJson(topUser)).append("\",\"count\":").append(maxReviews).append("},");
            sb.append("\"mostRatedMovie\":{\"name\":\"").append(escapeJson(mostRatedMovieName)).append("\",\"count\":").append(maxMovieReviews).append("},");

            sb.append("\"unratedMovies\":[");
            for (int i = 0; i < unratedMovies.size(); i++) {
                sb.append("\"").append(escapeJson(unratedMovies.get(i))).append("\"");
                if (i < unratedMovies.size() - 1) sb.append(",");
            }
            sb.append("],");

            sb.append("\"unratedUsers\":[");
            for (int i = 0; i < unratedUsers.size(); i++) {
                sb.append("\"").append(escapeJson(unratedUsers.get(i))).append("\"");
                if (i < unratedUsers.size() - 1) sb.append(",");
            }
            sb.append("]");

            sb.append("}");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    static class AddRatingApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 204, "");
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJsonResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
                return;
            }

            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String body = sb.toString();

            try {
                int userId = parseJsonInt(body, "userId");
                int movieId = parseJsonInt(body, "movieId");
                int ratingVal = parseJsonInt(body, "rating");

                if (ratingVal < 1 || ratingVal > 5) {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Rating must be between 1 and 5\"}");
                    return;
                }

                User u = userop.getUserbyId(userId);
                Movies m = moviesop.getMoviebyId(movieId);

                if (u == null || m == null) {
                    sendJsonResponse(exchange, 404, "{\"success\":false,\"message\":\"User or Movie not found\"}");
                    return;
                }

                Rating r = new Rating(u, m, ratingVal);
                boolean added = ratingsop.addRatings(r);
                if (added) {
                    u.addRating(r);
                    sendJsonResponse(exchange, 200, "{\"success\":true,\"message\":\"Rating submitted successfully!\"}");
                } else {
                    sendJsonResponse(exchange, 409, "{\"success\":false,\"message\":\"User already rated this movie\"}");
                }
            } catch (Exception e) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"message\":\"Invalid request format\"}");
            }
        }

        private int parseJsonInt(String json, String key) {
            String pattern = "\"" + key + "\":";
            int idx = json.indexOf(pattern);
            if (idx == -1) throw new IllegalArgumentException("Key not found");
            int start = idx + pattern.length();
            while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
            int end = start;
            while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
            return Integer.parseInt(json.substring(start, end).trim());
        }
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path == null || path.equals("/") || path.isEmpty()) {
                path = "/index.html";
            }

            File file = resolveWebFile(path);
            if (!file.exists() || file.isDirectory()) {
                file = resolveWebFile("/index.html");
            }

            if (!file.exists()) {
                String notFound = "<h1>404 Not Found</h1>";
                exchange.sendResponseHeaders(404, notFound.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(notFound.getBytes(StandardCharsets.UTF_8));
                }
                return;
            }

            String contentType = "text/html; charset=UTF-8";
            if (path.endsWith(".css")) contentType = "text/css; charset=UTF-8";
            else if (path.endsWith(".js")) contentType = "application/javascript; charset=UTF-8";
            else if (path.endsWith(".json")) contentType = "application/json; charset=UTF-8";
            else if (path.endsWith(".png")) contentType = "image/png";
            else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) contentType = "image/jpeg";
            else if (path.endsWith(".svg")) contentType = "image/svg+xml";

            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, file.length());
            try (OutputStream os = exchange.getResponseBody(); FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        }

        private File resolveWebFile(String path) {
            String clean = path.startsWith("/") ? path.substring(1) : path;
            String[] possible = {
                "web/" + clean,
                "MiniProject/web/" + clean,
                "../web/" + clean,
                clean
            };
            for (String p : possible) {
                File f = new File(p);
                if (f.exists() && f.isFile()) return f;
            }
            return new File("web/" + clean);
        }
    }
}
