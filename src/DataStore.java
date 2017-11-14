import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStore {
	
	public static GenesInfo geneInfo = new GenesInfo();
	public static List<GenesInfo> geneInfoList = new ArrayList<GenesInfo>();
	public static Map<String, Double> genesKeyPValuesPair = new HashMap<String, Double>();
	public static List<String> uniqueGenesInInteraction = new ArrayList<String>();
	public static List<GenesInteractions> genesInteractionsListComplete = new ArrayList<GenesInteractions>();
	static Map<String, List<String>> genesKeyInteractionPair = new HashMap<String, List<String>>();
	static Hashtable<String, String> geneInfoHashTable = new Hashtable<>();
	
	static String outputPath;
	private static DataStore instance = null;
	public static String getOutputPath() {
		return outputPath;
	}

	public static void setOutputPath(String outputPath) {
		DataStore.outputPath = outputPath;
	}

	public static void DataStor(String psheet, String interaction, String outputPathX) {
		outputPath = outputPathX;
		readPValuesFromTextFile(psheet);
		readInteractionsFromTextFile(interaction);
		createHashListFromEdegeClassList();
		System.out.println("Started Finding Initial Paths");
	}

	public static void main(String[] args) throws Exception {
		{

		}
	}

	public static DataStore getInstance() {
		if (instance == null) {
			instance = new DataStore();
		}
		return instance;
	}

	protected static GenesInfo getpSheet() {
		return geneInfo;
	}

	protected static List<GenesInfo> getpSheetList() {
		return geneInfoList;
	}

	protected static Map<String, Double> getpsheetKeyValyePair() {
		return genesKeyPValuesPair;
	}

	protected static List<String> getedgeListSet() {
		return uniqueGenesInInteraction;
	}

	protected static List<GenesInteractions> getedgeClassListComplete() {
		return genesInteractionsListComplete;
	}

	protected static Map<String, List<String>> getedgeListHashMap() {
		return genesKeyInteractionPair;
	}

	protected static Hashtable<String, String> getgeneInfoHashTable() {
		return geneInfoHashTable;
	}

	public static void readPValuesFromTextFile(
			String fileName) {

		List<String> list = new ArrayList<>();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			list = stream.map(String::toUpperCase).collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}
		int count = 0;
		for (String string : list) {
			count = count + 1;
			geneInfo = new GenesInfo();
			String[] splitArray = string.split("\\s+");
			String geneName = splitArray[0].replace("\"", "");
			String pvalue = splitArray[5];
			geneInfo.setGeneName(geneName.toLowerCase());
			if (pvalue.equalsIgnoreCase("NA"))
				continue;
			double tempasd = (Double.parseDouble(pvalue));
			if (tempasd == 2D)
				tempasd = 1;
			geneInfo.setpValue(tempasd);
			if (geneInfo.getpValue() > 0 && geneInfo.getpValue() < 1)
				geneInfoList.add(geneInfo);
			genesKeyPValuesPair.put(geneInfo.getGeneName().toLowerCase(), geneInfo.getpValue());
			geneInfoHashTable.put(geneName.toLowerCase(), String.valueOf(splitArray[2]));
        }
	}

	public static void readInteractionsFromTextFile(String fileName) {

		List<String> list = new ArrayList<>();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			list = stream.map(String::toUpperCase).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		int count = 0;
		for (String string : list) {
			count = count + 1;
			GenesInteractions edgeClass = new GenesInteractions();
			String[] splitArray = string.split("\\s+");
			String from = splitArray[0].toLowerCase();
			String to = splitArray[1].toLowerCase();
			Boolean pvalueFound = true;
			try {
				Double pvalue = genesKeyPValuesPair.get(from);
				Double pvalue2 = genesKeyPValuesPair.get(to);
			} catch (Exception e) {
				pvalueFound = false;
			}
			if (!pvalueFound)
				continue;
			if (!uniqueGenesInInteraction.contains(from))
				uniqueGenesInInteraction.add(from);
			if (!uniqueGenesInInteraction.contains(to))
				uniqueGenesInInteraction.add(to);
			edgeClass.setColumn1(from);
			edgeClass.setColmn2(to);
			genesInteractionsListComplete.add(edgeClass);
		}
	}
	
	private static void createHashListFromEdegeClassList() {
		for (String uniqueKey : uniqueGenesInInteraction) {
			List<String> valuesList = new ArrayList<String>();
			for (GenesInteractions edgeClass : genesInteractionsListComplete) {
				if (edgeClass.getColumn1().equalsIgnoreCase(uniqueKey)) {
					valuesList.add(edgeClass.getColmn2());
				}
			}
			for (GenesInteractions edgeClass : genesInteractionsListComplete) {
				if (edgeClass.getColmn2().equalsIgnoreCase(uniqueKey)) {
					valuesList.add(edgeClass.getColumn1());
				}
			}
			genesKeyInteractionPair.put(uniqueKey, valuesList);
		}
	}
}