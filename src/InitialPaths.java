import java.io.BufferedWriter;
import java.io.FileWriter;
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
import java.util.stream.Collectors;


public class InitialPaths {
	static List<GenesInfo> genesInfoList = new ArrayList<GenesInfo>();
	static List<GenesInteractions> genesInteractionsListComplete = new ArrayList<GenesInteractions>();
	public static List<ArrayList<String>> Allpaths = new ArrayList<ArrayList<String>>();
	static List<String> uniqueGenesInInteraction = new ArrayList<String>();
	static String Step1Output;
	static String network_File;
	static Map<String, Double> genesKeyPValuesPair = new HashMap<String, Double>();
	static Map<String, List<String>> genesKeyInteractionPair = new HashMap<String, List<String>>();
	static GenesInfo geneInfo = new GenesInfo();
	static long threshold = 2L;
	static int rowCounter = 0;
	static int extensionLimit;
	static int mergeLimit;
	static String initialPathOutputFile, pvalueInput, interctionInput;

	public static void main(String[] args) throws Exception {
		if (args.length == 3 || args.length == 5) {
			if (args.length == 3) {
				extensionLimit = 2;
				mergeLimit = 1;
				pvalueInput = args[0];
				interctionInput = args[1];
				initialPathOutputFile = args[2];
				network_File = args[2];
			} else { // in case of 5
				int tempExtension = Integer.parseInt(args[2]);
				int tempMerging = Integer.parseInt(args[3]);
				if (tempExtension >= 3 || tempExtension < 1) {
					System.out.println("Extension Limit out of bound");
					return;
				}
				else if (tempMerging > 4 || tempMerging < 1) {
					System.out.println("Merging Limit out of bound");
					return;
				}
				
				else {
					extensionLimit = tempExtension;
					mergeLimit = tempMerging;
					pvalueInput = args[0];
					interctionInput = args[1];
					initialPathOutputFile = args[4];
					network_File = args[4];
				}
			}

		} else {
			System.out.println("Error");
			return;
		}
		System.out.println("MSF Runing");
		Step1Output = initialPathOutputFile + "InitialPaths.text";
		network_File = initialPathOutputFile + "NetworkFile.text";
		DataStore.DataStor(pvalueInput, interctionInput, initialPathOutputFile); 
		geneInfo = DataStore.getpSheet();
		genesInfoList = DataStore.getpSheetList();
		genesInteractionsListComplete = DataStore.getedgeClassListComplete();
		uniqueGenesInInteraction = DataStore.getedgeListSet();
		genesKeyPValuesPair = DataStore.getpsheetKeyValyePair();
		genesKeyInteractionPair = DataStore.getedgeListHashMap();
		identifyingInitialPaths();
		System.out.println("Found Initial Paths");
		ExtensionMerging.main(Allpaths, initialPathOutputFile,extensionLimit,mergeLimit);
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

		Map<String, List<TempModel>> interactingGeneFrom = new HashMap<String, List<TempModel>>();
		Map<String, List<TempModel>> interactingGeneTo = new HashMap<String, List<TempModel>>();
		HashMap<String, String> geneInteractingAncestor = new HashMap<>();
		DataStore.createHashListFromEdegeClassListTemp(0);
		interactingGeneFrom = DataStore.getInteractingGeneFrom();
		interactingGeneTo = DataStore.getInteractingGeneTo();
		Collections.sort(genesInfoList);
		Set<String> iterationSet = new HashSet<String>(); // all checked genes
		for (GenesInfo psheet : genesInfoList) {
			String rootGeneName = "";
			double combinePValue = 0.0;
			ArrayList<String> selectedGenes = new ArrayList<String>(); // parents
																		// genes
			rootGeneName = psheet.getGeneName();
			geneInteractingAncestor.put(rootGeneName, "");
			if (iterationSet.contains(rootGeneName))
				continue;
			combinePValue = genesKeyPValuesPair.get(rootGeneName);
			iterationSet.add(rootGeneName);
			if (combinePValue >= 0.99)
				continue;
			List<String> interactingGenes = genesKeyInteractionPair.get(rootGeneName);
			if (interactingGenes == null || interactingGenes.contains(null)) {
				int breakpsoint = 1;
				breakpsoint = 2;
				;
				continue;
			}
			for (String string : interactingGenes) {
				geneInteractingAncestor.put(string, rootGeneName);
			}
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
					if (!geneInteractingAncestor.containsKey(temp))
						geneInteractingAncestor.put(temp.trim(), genekey.trim());
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
				for (String stringX : selectedGenes) {
					try {

						String parentGeneOFChildGene = geneInteractingAncestor.get(stringX);
						List<TempModel> tempModelList = interactingGeneTo.get(parentGeneOFChildGene);
						if (tempModelList == null)
							continue;
						List<TempModel> xx = tempModelList.stream()
								.filter(x -> x.interactingGene.equalsIgnoreCase(stringX.trim()))
								.collect(Collectors.toList());
						TempModel tempp = new TempModel();
						ArrayList<String> printa = new ArrayList<>();
						String printx = "";
						if (xx.size() < 1) {
							tempModelList = interactingGeneFrom.get(parentGeneOFChildGene);
							xx = tempModelList.stream().filter(x -> x.interactingGene.equalsIgnoreCase(stringX.trim()))
									.collect(Collectors.toList());
							if (xx.size() > 0) {
								tempp = xx.get(0);
								printx = tempp.getInteractingGene() + " " + parentGeneOFChildGene + " "
										+ tempp.getSymbol();
							} else {
								System.out.println("");
							}

						} else {
							tempp = xx.get(0);
							printx = parentGeneOFChildGene + " " + tempp.getInteractingGene() + " " + tempp.getSymbol();
						}
						writeInFIle_NetworkFile(printx);
					} catch (Exception e) {
						System.out.println("");
					}

				}
				Allpaths.add(selectedGenes);
				writeInFIle(selectedGenes, combinePValue);
			}
		}
	}

	private static void writeInFIle_NetworkFile(String output) {

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(network_File, true));
			bw.write(output + "\n");
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally { // always close the file
			if (bw != null)
				try {
					bw.close();
				} catch (IOException ioe2) {
					// just ignore it
				}
		} // end try/c

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