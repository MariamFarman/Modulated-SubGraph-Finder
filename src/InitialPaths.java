import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InitialPaths {
	static List<GenesInfo> genesInfoList = new ArrayList<GenesInfo>();
	static List<GenesInteractions> genesInteractionsListComplete = new ArrayList<GenesInteractions>();
	public static List<ArrayList<String>> Allpaths = new ArrayList<ArrayList<String>>();
	static List<String> uniqueGenesInInteraction = new ArrayList<String>();
	static String Step1Output;
	static Map<String, Double> genesKeyPValuesPair = new HashMap<String, Double>();
	static Map<String, List<String>> genesKeyInteractionPair = new HashMap<String, List<String>>();
	static GenesInfo geneInfo = new GenesInfo();
	static long threshold = 2L;
	static int rowCounter = 0;

	public static void main(String[] args) throws Exception {
		System.out.println("Looking for Initial Paths");
		Step1Output = args[2] + "InitialPaths.text";
			DataStore.DataStor(args[0], args[1], args[2]);
			geneInfo = DataStore.getpSheet();
			genesInfoList = DataStore.getpSheetList();
			genesInteractionsListComplete = DataStore.getedgeClassListComplete();
			uniqueGenesInInteraction = DataStore.getedgeListSet();
			genesKeyPValuesPair = DataStore.getpsheetKeyValyePair();
			genesKeyInteractionPair = DataStore.getedgeListHashMap();
			identifyingInitialPaths();
			System.out.println("Found Initial Paths");
			ExtensionMerging.main(Allpaths);
		}
	

	private static List<String> sortpSheetList(List<String> mainValueList) {
		if (mainValueList == null)
			return null;
		List<GenesInfo> psheetList = new ArrayList<GenesInfo>();
		List<String> returnList = new ArrayList<>();
		for (String item : mainValueList) {
			GenesInfo temp = new GenesInfo();
			temp.setGeneName(item);
			try {
				temp.setpValue(genesKeyPValuesPair.get(item));
			} catch (Exception e) {
			}
			if (temp.getpValue() > 0)
				psheetList.add(temp);
		}
		Collections.sort(psheetList);
		for (GenesInfo returnL : psheetList) {
			returnList.add(returnL.getGeneName());
		}
		return returnList;
	}

	static int returnCounter(int counter, int newCounter) {
		if (newCounter <= 0) {
			return 0;
		} else
			return counter++;
	}

	private static void identifyingInitialPaths() {
		Collections.sort(genesInfoList);
		Set<String> iterationSet = new HashSet<String>(); 
		for (GenesInfo psheet : genesInfoList) {
			String rootGeneName = "";
			double combinePValue = 0.0;
			ArrayList<String> selectedGenes = new ArrayList<String>(); 
			rootGeneName = psheet.getGeneName();
			if (iterationSet.contains(rootGeneName))
				continue;
			combinePValue = genesKeyPValuesPair.get(rootGeneName);
			iterationSet.add(rootGeneName);
			if (combinePValue >= 0.99)
				continue;
			List<String> interactingGenes = genesKeyInteractionPair.get(rootGeneName);
			interactingGenes = sortpSheetList(interactingGenes); 
			selectedGenes.add(rootGeneName);
			int counterCheck = 0;
			for (int counter = counterCheck; interactingGenes != null
					&& counter < interactingGenes.size(); counter = returnCounter(counter, counterCheck)) {
				String genekey = interactingGenes.get(counter);
				interactingGenes.remove(counter);
				counterCheck++;
				if (iterationSet.contains(genekey))
					continue;
				double genePValue = genesKeyPValuesPair.get(genekey);
				if (genePValue >= 1.0)
					continue;
				List<Double> pavlueListString = new ArrayList<>();
				pavlueListString.clear();
				for (String string : selectedGenes) {
					pavlueListString.add(genesKeyPValuesPair.get(string));
				}
				pavlueListString.add(genePValue);
				double calculationResult = GenericFunctions.hartungFunction(pavlueListString);
				if (calculationResult >= combinePValue)
					continue;
				iterationSet.add(genekey);
				combinePValue = calculationResult;
				selectedGenes.add(genekey);
				List<String> newStringListTemp = genesKeyInteractionPair.get(genekey);
				List<String> newStringList = new ArrayList<String>();
				for (String temp : newStringListTemp) {
					if (iterationSet.contains(temp))
						continue;
					newStringList.add(temp);
				}
				for (String interact : interactingGenes) {
					if (iterationSet.contains(interact))
						continue;
					newStringList.add(interact);
				}
				interactingGenes = sortpSheetList(newStringList);
				counterCheck = 0;
			}
			if (selectedGenes.size() > 2) {
				Allpaths.add(selectedGenes);
				writeInFIle(selectedGenes, combinePValue);
			}
		}
	}

	private static synchronized void writeInFIle(ArrayList<String> output, double combineValue) {
		Charset charset = StandardCharsets.UTF_8;
		try {
			Files.write(Paths.get(Step1Output), (output.toString() + " " + combineValue + "\n").getBytes(charset),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
