import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataPuller {

	public static List<Integer> getTeamsInEvent(String eventKey, String apiKey) {
		List<Integer> numbers = new ArrayList<>();
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://theorangealliance.org/api/event/" + eventKey + "/teams"))
					.header("Content-Type", "application/json")
					.header("X-TOA-Key", apiKey)
					.header("X-Application-Origin", "TOA Auto Pre-scout")
					.GET()
					.build();
			HttpResponse<String> response = null;
			response = client.send(request, HttpResponse.BodyHandlers.ofString());

			Object obj = new JsonParser().parse(response.body());
			JsonArray ja = (JsonArray) obj;
			for (int i = 0; i < ja.size(); i++) {
				JsonObject jo = ja.get(i).getAsJsonObject();
				numbers.add(Integer.parseInt(jo.get("team_key").getAsString()));
			}
			Collections.sort(numbers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return numbers;
	}

	public static Team getTeamInfo(int teamNumber, String apiKey) {
		Team team = new Team();
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://theorangealliance.org/api/team/" + teamNumber + "/results/2122"))
					.header("Content-Type", "application/json")
					.header("X-TOA-Key", apiKey)
					.header("X-Application-Origin", "TOA Auto Pre-scout")
					.GET()
					.build();
			HttpResponse<String> response = null;
			response = client.send(request, HttpResponse.BodyHandlers.ofString());

//			System.out.println(response.body());

			// Setup JSON stuff
			Object obj = new JsonParser().parse(response.body());
			JsonArray ja = (JsonArray) obj;
//			System.out.println("ja.get(0): " + ja.get(0));

			// Setup variables
//			System.out.println("ja.length: " + ja.size());
			team.teamNumber = teamNumber;
			JsonObject jo = new JsonObject();
			try {
				jo = ja.get(0).getAsJsonObject();
			} catch (Exception e) {
				return team;
			}
			jo = jo.get("team").getAsJsonObject();
//			jo = teamArray.get(0).getAsJsonObject();
			team.teamName = jo.get("team_name_short").getAsString();

			team.wins = 0;
			team.losses = 0;
			team.ties = 0;

			List<Double> OPRs = new ArrayList<Double>();
			List<Integer> RPs = new ArrayList<Integer>();
			List<Integer> ranks = new ArrayList<Integer>();
			// Parse the JSON
			for (int i = 0; i < ja.size(); i++) {
				jo = ja.get(i).getAsJsonObject();
				team.wins += jo.get("wins").getAsInt();
				team.losses += jo.get("losses").getAsInt();
				team.ties += jo.get("ties").getAsInt();
				OPRs.add(jo.get("opr").getAsDouble());
				RPs.add(jo.get("ranking_points").getAsInt());
				ranks.add(jo.get("rank").getAsInt());

				if (jo.get("league_key") != null) {
					team.teamName = jo.get("league_key").getAsString();
				}
			}
			team.topOPR = 0.0;
			team.averageOPR = 0.0;
			for (int i = 0; i < OPRs.size(); i++) {
				team.averageOPR += OPRs.get(i);
				if (OPRs.get(i) > team.topOPR) {
					team.topOPR = OPRs.get(i);
				}
			}
			team.averageOPR /= OPRs.size();
			team.topRP = 0;
			team.averageRP = 0.0;
			for (int i = 0; i < RPs.size(); i++) {
				team.averageRP += RPs.get(i);
				if (RPs.get(i) > team.topRP) {
					team.topRP = RPs.get(i);
				}
			}
			team.averageRP /= RPs.size();
			team.topRank = Integer.MAX_VALUE;
			team.averageRank = 0.0;
			for (int i = 0; i < ranks.size(); i++) {
				team.averageRank += ranks.get(i);
				if (ranks.get(i) < team.topRank) {
					team.topRank = ranks.get(i);
				}
			}
			team.averageRank /= ranks.size();

//			System.out.println("Team Number: " + team.teamNumber);
//			System.out.println("Team Name: " + team.teamName);
//			System.out.println("Wins: " + team.wins);
//			System.out.println("Losses: " + team.losses);
//			System.out.println("Ties: " + team.ties);
//			System.out.println("Average OPR: " + team.averageOPR);
//			System.out.println("Top OPR: " + team.topOPR);
//			System.out.println("Average RP: " + team.averageRP);
//			System.out.println("Top RP: " + team.topRP);
//			System.out.println("Average Rank: " + team.averageRank);
//			System.out.println("Top Rank: " + team.topRank);
			System.out.println("Team String: " + team.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return team;
	}

}
