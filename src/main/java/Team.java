public class Team {

	public int teamNumber;
	public String teamName;
	public int wins;
	public int losses;
	public int ties;
	public double averageOPR;
	public double topOPR;
	public double averageRP;
	public int topRP;
	public double averageRank;
	public int topRank;

	public String toString() {
		String splitChar = ",";
		String csvString = new String();
		csvString += teamNumber + splitChar;
		csvString += teamName + splitChar;
		csvString += wins + "-" + losses + "-" + ties + splitChar;
		csvString += averageOPR + splitChar;
		csvString += topOPR + splitChar;
		csvString += averageRP + splitChar;
		csvString += topRP + splitChar;
		csvString += averageRank + splitChar;
		csvString += topRank;
		return csvString;
	}

}
