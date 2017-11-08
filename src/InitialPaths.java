import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
	static List<PSheet> pSheetList = new ArrayList<PSheet>();
	static List<EdgeClass> edgeClassListComplete = new ArrayList<EdgeClass>();
	public static List<ArrayList<String>> Allpaths = new ArrayList<ArrayList<String>>();
	static List<String> edgeListSet = new ArrayList<String>();
	static String Step1Output;
	static Map<String, Double> psheetKeyValyePair = new HashMap<String, Double>();
	static Map<String, List<String>> edgeListHashMap = new HashMap<String, List<String>>();
	static PSheet pSheet = new PSheet();
	static long THRESHOLD = 2L;
	static int rowCounter = 0;
	static GenericFunctions b;

	public static void main(String[] args) throws Exception {
		 
		Step1Output = args[2]+"InitialPaths.text";
		DataStore.DataStor(args[0],args[1],args[2]);
		pSheet = DataStore.getpSheet();
		pSheetList = DataStore.getpSheetList();
		edgeClassListComplete = DataStore.getedgeClassListComplete();
		edgeListSet = DataStore.getedgeListSet();
		psheetKeyValyePair = DataStore.getpsheetKeyValyePair();
		edgeListHashMap = DataStore.getedgeListHashMap();
		newTreeImplement();
		System.out.println("Found Initial Paths....Now finding Extensions and Mergings");
		ExtensionMerging.main(Allpaths);
 
	}

	private static List<String> sortpSheetList(List<String> mainValueList) {
		if (mainValueList == null)
			return null;
		List<PSheet> psheetList = new ArrayList<PSheet>();
		List<String> returnList = new ArrayList<>();
		for (String item : mainValueList) {
			PSheet temp = new PSheet();
			temp.setGeneName(item);
			try {
				temp.setpValue(psheetKeyValyePair.get(item));
			} catch (Exception e) {
			//System.out.println("");
			}
			if (temp.getpValue() > 0)
				psheetList.add(temp);
		}
		Collections.sort(psheetList);
		for (PSheet returnL : psheetList) {
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

	private static void newTreeImplement() {
		Collections.sort(pSheetList);
		Set<String> iterationSet = new HashSet<String>(); // all checked genes
		int xcas = 1;
		xcas = 1;
		for (PSheet psheet : pSheetList) {
			//System.out.println("***************" + psheet.getGeneName() + " " + psheet.getpValue());
			String rootGeneName = "";
			xcas = 1;
			double combinePValue = 0.0;
			ArrayList<String> selectedGenes = new ArrayList<String>(); // parents genes
			rootGeneName = psheet.getGeneName();
			if (iterationSet.contains(rootGeneName))
				continue;
			combinePValue = psheetKeyValyePair.get(rootGeneName);
			iterationSet.add(rootGeneName);
			if (combinePValue >= 0.99)
				continue;
			List<String> interactingGenes = edgeListHashMap.get(rootGeneName);
			interactingGenes = sortpSheetList(interactingGenes); // sorting Interactions on Pvalues
			selectedGenes.add(rootGeneName);
			int counterCheck = 0;
			for (int counter = counterCheck; interactingGenes != null
					&& counter < interactingGenes.size(); counter = returnCounter(counter, counterCheck)) {
				String genekey = interactingGenes.get(counter);
				interactingGenes.remove(counter);
				counterCheck++;
				if (iterationSet.contains(genekey))
					continue;
				double genePValue = psheetKeyValyePair.get(genekey);
				if (genePValue >= 1.0)
					continue;
				List<Double> pavlueListString = new ArrayList<>();
				pavlueListString.clear();
				for (String string : selectedGenes) {
					pavlueListString.add(psheetKeyValyePair.get(string));
				}
				pavlueListString.add(genePValue);
				double calculationResult = b.hartungFunction(pavlueListString);
				if (calculationResult >= combinePValue)
					continue;
				xcas++;
				iterationSet.add(genekey);
				//System.err.println(genekey + " ^ " + "  " + genePValue + " ^ " + calculationResult);
				combinePValue = calculationResult;
				selectedGenes.add(genekey);
				List<String> newStringListTemp = edgeListHashMap.get(genekey);
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
			if (selectedGenes.size() > 2){
				Allpaths.add(selectedGenes);
				writeInFIle(selectedGenes, combinePValue);
			}
				
		}
	}

	private static synchronized void writeInFIle(ArrayList<String> output,double combineValue) {
		Charset charset = StandardCharsets.UTF_8;
		try {
			Files.write(Paths.get(Step1Output), (output.toString() + " "+ combineValue +"\n").getBytes(charset),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
