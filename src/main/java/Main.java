import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// Get info
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter the event you would like to scout.");
		String event = scanner.next();
		System.out.println("Please enter an API key (Check README to get one)");
		String apiKey = scanner.next();

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
