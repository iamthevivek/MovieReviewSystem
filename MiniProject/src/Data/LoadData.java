package Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import Model.User;
import Service.UserOperations;

public class LoadData {
	
	public static File getFile(String fileName) {
		String[] possiblePaths = {
			"data/" + fileName,
			"MiniProject/data/" + fileName,
			"../data/" + fileName,
			fileName
		};
		for (String path : possiblePaths) {
			File f = new File(path);
			if (f.exists() && f.isFile()) {
				return f;
			}
		}
		return new File("data/" + fileName);
	}

	public static void loadUsersData(UserOperations userop) {
		File file = getFile("users.csv");
		if (!file.exists()) {
			System.err.println("Warning: users.csv not found at " + file.getAbsolutePath());
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
				String[] userData = line.split(",");
				if (userData.length < 4) {
					continue;
				}
				try {
					int id = Integer.parseInt(userData[0].trim());
					String name = userData[1].trim();
					String gender = userData[2].trim();
					int age = Integer.parseInt(userData[3].trim());
					userop.addUser(new User(id, name, gender, age));
				} catch (NumberFormatException e) {
					continue;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading users data: " + e.getMessage());
		}
	}
}



