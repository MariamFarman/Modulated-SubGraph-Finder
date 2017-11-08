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


	
	static Map<String, List<String>> edgeListHashMap = new HashMap<String, List<String>>();
	static Map<String, List<String>> edgeListHashMapItIColumn1 = new HashMap<String, List<String>>();
	static Map<String, List<String>> edgeListHashMapItIColumn2 = new HashMap<String, List<String>>();
	static Map<String, List<String>> EnsemblData = new HashMap<String, List<String>>();
	static List<EdgeClass> edgeClassListComplete = new ArrayList<EdgeClass>();
	static List<String> edgeListSet = new ArrayList<String>();
	static List<String> Genes = new ArrayList<>();
	static List<PSheet> pSheetList = new ArrayList<PSheet>();
	static Map<String, Double> psheetKeyValyePair = new HashMap<String, Double>();
	static int rowCounter = 0;
	static int sourceGreaterCOunter = 0;
	static int originalSourceCOunter = 3;
	static Hashtable<String, String> geneInfoHashTable = new Hashtable<>();
	static List<String> genesList= new ArrayList<>();
	static String fileoutPathText;

	public static void main (List<ArrayList<String>> AllMergedpaths) throws Exception {	
	
		fileoutPathText = DataStore.getOutputPath()+ "FinalOutput.text";
		pSheetList = DataStore.getpSheetList();
		edgeClassListComplete = DataStore.getedgeClassListComplete();
		edgeListSet = DataStore.getedgeListSet();
		psheetKeyValyePair = DataStore.getpsheetKeyValyePair();
		edgeListHashMap = DataStore.getedgeListHashMap();
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
			pavlueListString.add(psheetKeyValyePair.get(string));
		}

		double calculationResult = GenericFunctions.hartungFunction(pavlueListString);
		prepareList.add(String.valueOf(calculationResult));
		writeInFIle(prepareList);
		for (String string : genesList) {
			prepareList = new ArrayList<String>();
			prepareList.add(string);
			String interactingGenes = "";
			List<String> getALLInteraction = edgeListHashMap.get(string);
			for (String stringX : getALLInteraction) {
				if (genesList.contains(stringX))
					interactingGenes = interactingGenes.concat("-" + stringX);
			}
			prepareList.add(interactingGenes);
			pavlueListString.clear();
			prepareList.add(geneInfoHashTable.get(string));
			prepareList.add(String.valueOf(psheetKeyValyePair.get(string)));
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
		EdgeClass edgeClass = new EdgeClass();
		edgeClass.getColmn2();
		List<String> sinkInteraction = new ArrayList<>();
		sinkInteraction = edgeListHashMapItIColumn1.get(gene);
		if (sinkInteraction.contains(gene))
			sinkInteraction.remove(gene);
		List<String> sourceIneraction = new ArrayList<>();
		sourceIneraction = edgeListHashMapItIColumn2.get(gene);
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
				//System.out.println("sinkGeneName " + gene);
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
		edgeListHashMap = new HashMap<String, List<String>>();
		edgeListHashMapItIColumn1 = new HashMap<String, List<String>>();
		edgeListHashMapItIColumn2 = new HashMap<String, List<String>>();
		edgeListHashMap = new HashMap<String, List<String>>();
		int column1 = 0;
		int column2 = 0;

		for (String uniqueKey : edgeListSet) {
			List<String> valuesList = new ArrayList<String>();
			List<String> valuesList1 = new ArrayList<String>();
			List<String> valuesList2 = new ArrayList<String>();
			if (column == 0 || column == 2) {
				column1++;
				for (EdgeClass edgeClass : edgeClassListComplete) {
					if (valuesList.size() > 1000)
						break;
					if (edgeClass.getColumn1().equalsIgnoreCase(uniqueKey)) {
						try {
							String column2Value = edgeClass.getColmn2().toLowerCase();
							Double pvalueTemp = psheetKeyValyePair.get(column2Value);
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
				edgeListHashMapItIColumn2.put(uniqueKey.toLowerCase(), valuesList1);
			}
			if (column == 0 || column == 1) {
				column2++;
				for (EdgeClass edgeClass : edgeClassListComplete) {
					if (valuesList.size() > 1000)
						break;
					if (edgeClass.getColmn2().equalsIgnoreCase(uniqueKey)) {
						try {
							String column1Value = edgeClass.getColumn1().toLowerCase();
							Double pvalueTemp = psheetKeyValyePair.get(column1Value);
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
				edgeListHashMapItIColumn1.put(uniqueKey.toLowerCase(), valuesList2);
			}
			edgeListHashMap.put(uniqueKey.toLowerCase(), valuesList);
		}
	}

}
