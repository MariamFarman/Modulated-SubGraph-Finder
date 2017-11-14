import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;



public class FinalThreads {

	
	static Map<String, List<String>> genesKeyInteractionPair = new HashMap<String, List<String>>();
	static Map<String, List<String>> interactingGeneFrom = new HashMap<String, List<String>>();
	static Map<String, List<String>> interactingGeneTo = new HashMap<String, List<String>>();
	static List<GenesInteractions> genesInteractionsListComplete = new ArrayList<GenesInteractions>();
	static List<String> uniqueGenesInInteraction = new ArrayList<String>();
	//static List<String> Genes = new ArrayList<>();
	static List<GenesInfo> genesInfoList = new ArrayList<GenesInfo>();
	static Map<String, Double> genesKeyPValuesPair = new HashMap<String, Double>();
	static int rowCounter = 0;
	static int sourceGreaterCOunter = 0;
	static int originalSourceCOunter = 3;
	static Hashtable<String, String> geneInfoHashTable = new Hashtable<>();
	static List<String> genesList= new ArrayList<>();
	static String fileoutPathText;

	public static void main (List<ArrayList<String>> AllMergedpaths) throws Exception {	
		System.out.println("Finding Sources and Sinks");
		fileoutPathText = DataStore.getOutputPath()+ "FinalOutput.text";
		genesInfoList = DataStore.getpSheetList();
		genesInteractionsListComplete = DataStore.getedgeClassListComplete();
		uniqueGenesInInteraction = DataStore.getedgeListSet();
		genesKeyPValuesPair = DataStore.getpsheetKeyValyePair();
		genesKeyInteractionPair = DataStore.getedgeListHashMap();
		geneInfoHashTable = DataStore.getgeneInfoHashTable();
		//EnsemblData = DataStore.getEnsemblData();
		createHashListFromEdegeClassList(0);
		int listNumer =1;
		for (ArrayList<String> arrayList : AllMergedpaths) {
			step4(arrayList, (listNumer));
			listNumer++;
		}
		
		System.out.println("Sources and Sinks found....Creating Final output file");
	}
	
	
	
	private static synchronized void step4(List<String> genesList, int pathNumber)  {

		List<String> prepareList = new ArrayList<String>();
		prepareList.add("Path " + pathNumber);
		writeInFIle(prepareList);
		prepareList = new ArrayList<String>();
		List<Double> pavlueListString = new ArrayList<>();
		for (String string : genesList) {
			prepareList.add(string);
			pavlueListString.add(genesKeyPValuesPair.get(string));
		}

		double calculationResult = GenericFunctions.hartungFunction(pavlueListString);
		prepareList.add(String.valueOf(calculationResult));
		writeInFIle(prepareList);
		for (String string : genesList) {
			prepareList = new ArrayList<String>();
			prepareList.add(string);
			String interactingGenes = "";
			List<String> getALLInteraction = genesKeyInteractionPair.get(string);
			for (String stringX : getALLInteraction) {
				if (genesList.contains(stringX))
					interactingGenes = interactingGenes.concat("-" + stringX);
			}
			prepareList.add(interactingGenes);
			pavlueListString.clear();
			prepareList.add(geneInfoHashTable.get(string));
			prepareList.add(String.valueOf(genesKeyPValuesPair.get(string)));
			prepareList.add(checkGeneSourceSink(genesList, string));
			/*prepareList.add(EnsemblData.get(string).get(0));
		    prepareList.add(EnsemblData.get(string).get(1));*/
			writeInFIle(prepareList);
		}
	}

	private static synchronized void writeInFIle(List<String> output) {
		
		Charset charset = StandardCharsets.UTF_8;
		try {
			Files.write(Paths.get(fileoutPathText), (output.toString() + "\n").getBytes(charset),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String checkGeneSourceSink(List<String> genesList, String gene) {
		GenesInteractions edgeClass = new GenesInteractions();
		edgeClass.getColmn2();
		List<String> sinkInteraction = new ArrayList<>();
		sinkInteraction = interactingGeneFrom.get(gene);
		if (sinkInteraction.contains(gene))
			sinkInteraction.remove(gene);
		List<String> sourceIneraction = new ArrayList<>();
		sourceIneraction = interactingGeneTo.get(gene);
		if (sourceIneraction.contains(gene))
			sourceIneraction.remove(gene);
		Boolean sink = false, source = false;
		String sinkGeneName = null, sourceGeneName = null;
		for (String sinkGene : sinkInteraction) {
			if (genesList.contains(sinkGene)) {
				sink = true;
				sinkGeneName = sinkGene;
				break;
			}
		}
		for (String sourceGene : sourceIneraction) {
			if (genesList.contains(sourceGene)) {
				source = true;
				sourceGeneName = sourceGene;
				break;
			}
		}
		if ((!(sink && source)) && (sinkGeneName != null || sourceGeneName != null)) {
			if (sink) {
				return "Sink";
			}
			if (source) {
				//System.out.println("sourceGeneName " + gene);
				return "Source";
			}
		}
		return "-";

	}

	private static void createHashListFromEdegeClassList(int column) {
		genesKeyInteractionPair = new HashMap<String, List<String>>();
		interactingGeneFrom = new HashMap<String, List<String>>();
		interactingGeneTo = new HashMap<String, List<String>>();
		genesKeyInteractionPair = new HashMap<String, List<String>>();
		int column1 = 0;
		int column2 = 0;

		for (String uniqueKey : uniqueGenesInInteraction) {
			List<String> valuesList = new ArrayList<String>();
			List<String> valuesList1 = new ArrayList<String>();
			List<String> valuesList2 = new ArrayList<String>();
			if (column == 0 || column == 2) {
				column1++;
				for (GenesInteractions edgeClass : genesInteractionsListComplete) {
					if (valuesList.size() > 1000)
						break;
					if (edgeClass.getColumn1().equalsIgnoreCase(uniqueKey)) {
						try {
							String column2Value = edgeClass.getColmn2().toLowerCase();
							Double pvalueTemp = genesKeyPValuesPair.get(column2Value);
							if (!pvalueTemp.isNaN())
								if (pvalueTemp > 0 && pvalueTemp < 1) {
									valuesList.add(column2Value);
									valuesList1.add(column2Value);
								}
						} catch (Exception e) {
							//System.out.println("");
						}
					}
				}
				interactingGeneTo.put(uniqueKey.toLowerCase(), valuesList1);
			}
			if (column == 0 || column == 1) {
				column2++;
				for (GenesInteractions edgeClass : genesInteractionsListComplete) {
					if (valuesList.size() > 1000)
						break;
					if (edgeClass.getColmn2().equalsIgnoreCase(uniqueKey)) {
						try {
							String column1Value = edgeClass.getColumn1().toLowerCase();
							Double pvalueTemp = genesKeyPValuesPair.get(column1Value);
							if (!pvalueTemp.isNaN())
								if (pvalueTemp > 0 && pvalueTemp < 1) {
									valuesList.add(column1Value);
									valuesList2.add(column1Value);
								}
						} catch (Exception e) {
							//System.out.println("");
						}
					}
				}
				interactingGeneFrom.put(uniqueKey.toLowerCase(), valuesList2);
			}
			genesKeyInteractionPair.put(uniqueKey.toLowerCase(), valuesList);
		}
	}

}
