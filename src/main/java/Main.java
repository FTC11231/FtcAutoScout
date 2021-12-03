import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// Get info
		String apiKey = "";
		String event;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter the event you would like to scout.");
		event = scanner.next();
		System.out.println("Do you want to load your API key from a file? (Y/N)");
		String response = scanner.next();
		if (response.toCharArray()[0] == 'Y' || response.toCharArray()[0] == 'y') {
			// TODO: Load api key
			File file = new File("api_key.txt");
			try {
				Scanner fr = new Scanner(file);
				apiKey = fr.nextLine();
				System.out.println("API key loaded from " + file.getAbsolutePath() + ".");
			} catch (FileNotFoundException e) {
				System.out.println("Error loading API key: FileNotFoundException");
				e.printStackTrace();
			}
		} else {
			System.out.println("Please enter an API key. (Check README to get one)");
			apiKey = scanner.next();
			System.out.println("Do you want to save your API key? (Y/N)");
			response = scanner.next();
			if (response.toCharArray()[0] == 'Y' || response.toCharArray()[0] == 'y') {
				// TODO: Save API key
				File file = new File("api_key.txt");
				try {
					FileWriter fw = new FileWriter(file);
					fw.write(apiKey);
					fw.close();
					System.out.println("API key saved to " + file.getAbsolutePath() + ".");
				} catch (IOException e) {
					System.out.println("Error saving API key: IOException");
					e.printStackTrace();
				}
			} else {
				System.out.println("API key not saved.");
			}
		}

		// Gather info on all of the teams
		int requestsLeft = 30;
		List<Integer> numbers = DataPuller.getTeamsInEvent(event, apiKey);
		requestsLeft--;
		List<Team> teams = new ArrayList<Team>();
		Timer timer = new Timer();
		timer.start();
		for (int i = 0; i < numbers.size(); i++) {
			teams.add(DataPuller.getTeamInfo(numbers.get(i), apiKey));
			requestsLeft--;
			if (requestsLeft == 0) {
				System.out.println("Pausing to avoid request limiting (30 requests / min)");
				System.out.println("Time: " + timer.get());
				requestsLeft = 30;
				while (!timer.hasElapsed(60)) {
					if (timer.get() <= 54) {
						Timer.delay(5);
						System.out.println("Time: " + timer.get() + "s");
					}
				}
				timer.stop();
				timer.start();
			}
		}

		// Sort the teams by OPR
		System.out.println("Sorting by OPR");
		for (int i = 0; i < teams.size(); i++) {
			for (int k = i; k < teams.size(); k++) {
				if (teams.get(k).topOPR > teams.get(i).topOPR) {
					Team temp = teams.get(i);
					teams.set(i, teams.get(k));
					teams.set(k, temp);
				}
			}
		}

		// Create the CSV
		String csv = "Team Number,Team Name,WLT Ratio,Average OPR,Top OPR,Average RP,Top RP,Average Rank,Top Rank\n";
		for (int i = 0; i < teams.size(); i++) {
			if (i != 0) {
				csv += "\n";
			}
			csv += teams.get(i).toString();
		}

		// Export the CSV
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("data.csv"));
			bw.write(csv);
			bw.close();
			System.out.println("The file has been saved.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
