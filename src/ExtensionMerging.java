import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class ExtensionMerging {

	static Integer inerLoogCouterStep3 = 0;
	static Integer inerLoogSizeStep3 = 0;
	static List<Integer> alreadyIteratedPath = new ArrayList<>();
	static List<String> finalizingPath = new ArrayList<>();
	static List<String> step2GnesDone = new ArrayList<>();
	static List<String> genesDone = new ArrayList<>();
	static Set<String> aaa = new HashSet<String>();
	public static List<ArrayList<String>> AllMergedpaths = new ArrayList<ArrayList<String>>();
	//static String Step2Output = "/home/farman/ModulatesSubPaths/ExtendedPath.text";
	//static String Step3Output = "/home/farman/ModulatesSubPaths/MergedPath.text";
	
	
	static String Step2Output;// = "/home/farman/ModulatesSubPaths/ExtendedPath.text";
	static String Step3Output;//  = "/home/farman/ModulatesSubPaths/MergedPath.text";
	
	
	static List<PSheet> pSheetList = new ArrayList<PSheet>();
	static Map<String, Double> psheetKeyValyePair = new HashMap<String, Double>();
	static List<EdgeClass> edgeClassListComplete = new ArrayList<EdgeClass>();
	static List<String> edgeListSet = new ArrayList<String>();
	static PSheet pSheet = new PSheet();
	static Map<String, List<String>> edgeListHashMap = new HashMap<String, List<String>>();
	static long THRESHOLD = 2L;
	static int rowCounter = 0;
	static List<MergingPojo> mergedPath = new ArrayList<>();
	static List<Integer> mergedPathPathNumber = new ArrayList<>();
    static Hashtable<Integer, List<String> > hashTableList= new Hashtable<Integer, List<String>> ();
	static FileInputStream fsIPExcel;
	static List<StringListSorting> sortingList;
	static int arrayListSize = 0;

	
	public static void main (List<ArrayList<String>> Allpaths) throws Exception {
		DataStore datasource = DataStore.getInstance();
		Step2Output = DataStore.getOutputPath()+ "ExtendedPath.text";
		Step3Output = DataStore.getOutputPath()+ "MergedPath.text";		
		pSheet = DataStore.getpSheet();
		pSheetList = DataStore.getpSheetList();
		edgeClassListComplete = DataStore.getedgeClassListComplete();
		edgeListSet = DataStore.getedgeListSet();
		psheetKeyValyePair = DataStore.getpsheetKeyValyePair();
		edgeListHashMap = DataStore.getedgeListHashMap();
		arrayListSize = Allpaths.size();
		
		int listNumer =1;
		for (ArrayList<String> arrayList : Allpaths) {
			setGenesList(arrayList, (listNumer));
			listNumer++;
			for (String string : arrayList) {
				step2GnesDone.add(string);
			}
			
		}
		System.out.println("CLass-ExtensioMerging-extendPathAllPaths");
		extendPathAllPaths();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("CLass-ExtensioMerging-Return extendPathAllPaths");
		List<String> tempStringList = null;
		for (int i = 1; i <= arrayListSize; i++) {
			//System.gc();
			tempStringList = getGenesList(i);
			Integer currentNumber = i;
			if (alreadyIteratedPath.contains(currentNumber))
				continue;
			List<String> sending = tempStringList;
			try {
				inerLoogCouterStep3 = 0;
				inerLoogSizeStep3 = 0;
				inerLoogSizeStep3 = sending.size();
				for (inerLoogCouterStep3 = 0; inerLoogCouterStep3 < inerLoogSizeStep3; inerLoogCouterStep3++) {
					String gene = "";
					gene = sending.get(inerLoogCouterStep3);
					checkInteractionFunction(gene, i);
				}
			} catch (Exception e) {
				//System.out.println("");
			}
		}
		for (int x = 1; x <= arrayListSize; x++) 
		{
			System.out.println(x+" ..."+getGenesList(x).toString());
			if (getGenesList(x).size()>1)
				AllMergedpaths.add((ArrayList<String>) getGenesList(x));
		}

		System.out.println("CLass-ExtensioMerging-writeInFIleMerging");
		writeInFIleMerging();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//System.out.println(sdf.format(cal.getTime()));
		System.out.println("Extension and Merging Done .....Finding sources and sinks");
		System.out.println("arrayListSize"+arrayListSize);
		for (List<String> string : AllMergedpaths) {
			System.err.println(string.toString());
			
		}
	    FinalThreads.main(AllMergedpaths); 
	}
	
	private static synchronized void writeInFIleExtension() {
		Charset charset = StandardCharsets.UTF_8;
		try {
			for (int xc =1;xc<=arrayListSize;xc++) {
				List<String> tempOrg = getGenesList(xc);
				List<Double> pavlueListString = new ArrayList<>();
				pavlueListString.clear();
				for (String string : tempOrg) {
					pavlueListString.add(psheetKeyValyePair.get(string));
				}
			Files.write(Paths.get(Step2Output), (tempOrg.toString()+ GenericFunctions.hartungFunction(pavlueListString) +"\n").getBytes(charset),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void extendPathAllPaths() {
		Set<Integer> pathSet = new HashSet<Integer>();
		for (int pathNumber = 1; pathNumber <= arrayListSize; pathNumber++) {
			List<String> tempList = getGenesList(pathNumber);
			for (String string : tempList) {
				List<String> threeLengthPath = genesPathSize3(string);
				Boolean found = false;
				for (String stringX : threeLengthPath) {
					Double dp = psheetKeyValyePair.get(stringX);
					if (dp < .05) {
						found = true;
					}
				}
				if (!found)
					continue;
				List<Double> extendedPathCP = new ArrayList<>();
				List<Double> originalPathCP = new ArrayList<>();
				List<String> newExtendedPath = new ArrayList<>();
				// if (threeLengthPath.size() > 2) {
				List<String> tempOrg = getGenesList(pathNumber);
				for (String gene : tempOrg) {
					newExtendedPath.add(gene);
					originalPathCP.add(psheetKeyValyePair.get(gene));
					extendedPathCP.add(psheetKeyValyePair.get(gene));
				}
				for (String string1 : threeLengthPath) {
					newExtendedPath.add(string1);
					extendedPathCP.add(psheetKeyValyePair.get(string1));
				}
				Double originalPathHartung = GenericFunctions.hartungFunction(originalPathCP);
				if (originalPathHartung==0.0)
					originalPathHartung = 2E-16;
				Double extendedPathHartung = GenericFunctions.hartungFunction(extendedPathCP);
				if (originalPathHartung > extendedPathHartung) { 
					/*System.out.println("Genes " + string);
					System.err.println("3 Length Path" + threeLengthPath);
					System.out.println("Extented" + pathNumber);
					System.err.println(originalPathHartung + " > " + extendedPathHartung);
					System.out.println("Old Path" + tempOrg);
					System.err.println("Extended Path" + newExtendedPath);*/
					for (String as : threeLengthPath) {
						step2GnesDone.add(as);
					}
					pathSet.add(pathNumber);
					setGenesList(newExtendedPath, pathNumber);
				} else
					checkPathwithSig(threeLengthPath, pathNumber);
			}
			
		}
		for (Integer x : pathSet)
			System.err.println(x + " ");
		writeInFIleExtension();
	}
	private static void checkPathwithSig(List<String> ext, int pathNumber) {

		for (int qw = ext.size(); qw >= 1; qw--) {
			List<String> temp = new ArrayList<String>();
			for (int xc = 0; xc < qw - 1; xc++) {
				temp.add(ext.get(xc));
			}
			List<Double> originalPathCP = new ArrayList<>();
			List<Double> extendedPathCP = new ArrayList<>();
			List<String> newExtendedPath = new ArrayList<>();
			List<String> tempOrg = getGenesList(pathNumber);
			for (String gene : tempOrg) {
				newExtendedPath.add(gene);
				originalPathCP.add(psheetKeyValyePair.get(gene));
				extendedPathCP.add(psheetKeyValyePair.get(gene));
			}
			for (String string1 : temp) {
				newExtendedPath.add(string1);
				extendedPathCP.add(psheetKeyValyePair.get(string1));
			}
			Double originalPathHartung = GenericFunctions.hartungFunction(originalPathCP);
			if (originalPathHartung==0.0)
				originalPathHartung = 2E-16;
			Double extendedPathHartung = GenericFunctions.hartungFunction(extendedPathCP);
			if (originalPathHartung > extendedPathHartung) {
				/*System.err.println("3 Length Path----" + temp);
				System.out.println("Extented" + pathNumber);
				System.err.println(originalPathHartung + " > " + extendedPathHartung);
				System.out.println("Old Path" + tempOrg);
				System.err.println("Extended Path" + newExtendedPath);*/
				for (String as : ext) {
					step2GnesDone.add(as);
				}
				setGenesList(newExtendedPath, pathNumber);
				
			}
		} 

	}

	private static List<String> genesPathSize3(String gene) {
		List<String> returnString = new ArrayList<String>();
		String int1 = smallestPvalueInteractingGene(gene, returnString);
		if (int1 != null) {
			returnString.add(int1);
			String int2 = smallestPvalueInteractingGene(int1, returnString);
			if (int2 != null) {
				returnString.add(int2);
				String int3 = smallestPvalueInteractingGene(int2, returnString);
				if (int3 != null) {
					returnString.add(int3);
				}
			}
		}
		Boolean sig = false;
		for (String temp : returnString) {
			Double tDouble = psheetKeyValyePair.get(temp);
			if (.05 > tDouble) {
				sig = true;
				break;
			}
		}
		
		return returnString;
	}

	private static String smallestPvalueInteractingGene(String gene, List<String> temp) {
		List<String> interction = edgeListHashMap.get(gene);
		interction = sortpSheetList(interction);
		for (String intr1 : interction) {
			if ((!step2GnesDone.contains(intr1)) && (!temp.contains(intr1))) {
				try {
					Double pvalue = psheetKeyValyePair.get(intr1);
					if (pvalue < 1)
						return intr1;
				} catch (Exception e) {

				}
			}
		}
		return null;
	}

	
	private static void checkInteractionFunction(String gene, int listNumber) throws InterruptedException {
		Set<String> iterationSet = new HashSet<String>();
		iterationSet.add(gene);
		double keyPValue1 = psheetKeyValyePair.get(gene);
		List<String> interactingGenes1 = edgeListHashMap.get(gene);
		interactingGenes1 = sortpSheetList(interactingGenes1);
		List<String> parentGene = new ArrayList<>();
		parentGene.add(gene);
		checkIfIneract(gene, interactingGenes1, listNumber, 1, parentGene);
		if (interactingGenes1 != null)
			for (String item1 : interactingGenes1) {
				if (!step2GnesDone.contains(item1))
					if (!iterationSet.contains(item1)) {
						iterationSet.add(item1);
						parentGene = new ArrayList<>();
						List<String> interactingGenes2 = edgeListHashMap.get(item1);
						parentGene.add(gene);
						parentGene.add(item1);
						checkIfIneract(item1, interactingGenes2, listNumber, 2, parentGene);
						for (String item2 : interactingGenes2) {
							if (!step2GnesDone.contains(item2))
								if (!iterationSet.contains(item2)) {
									iterationSet.add(item2);
									parentGene = new ArrayList<>();
									List<String> interactingGenes3 = edgeListHashMap.get(item2);
									parentGene.add(gene);
									parentGene.add(item1);
									parentGene.add(item2);
									checkIfIneract(item1, interactingGenes3, listNumber, 3, parentGene);
									for (String item3 : interactingGenes3) {
										if (!step2GnesDone.contains(item3))
											if (!iterationSet.contains(item3)) {
												iterationSet.add(item3);
												parentGene = new ArrayList<>();
												List<String> interactingGenes4 = edgeListHashMap.get(item3);
												interactingGenes4 = sortpSheetList(interactingGenes4);
												parentGene.add(gene);
												parentGene.add(item1);
												parentGene.add(item2);
												parentGene.add(item3);
												checkIfIneract(item1, interactingGenes4, listNumber, 4, parentGene);
											}
									}
								}
						}
					}
			}
	}

	private static void checkIfIneract(String genedd, List<String> interactingGenes1, int listNumber, int level,
			List<String> parentsGenes) throws InterruptedException {
		if (parentsGenes.size() < 2)
			return;
		if (interactingGenes1 != null)
			for (String string : interactingGenes1) {
				List<String> inerationWith = null;
				for (int x = 1; x <= arrayListSize; x++) {
					if (x == listNumber)
						continue;
					inerationWith = getGenesList(x);
					if (inerationWith == null)
						continue;
					Integer asd = x;
					if (inerationWith.contains(string)) {
						boolean geneExist = false;
						List<String> checkList = new ArrayList<String>(parentsGenes);
						checkList.remove(0);
						for (String string2 : checkList) {
							if (inerationWith.contains(string2)) {
								geneExist = true;
							}
						}
						if (geneExist)
							return;
						List<String> originlParentList = new ArrayList<>();
						originlParentList = getGenesList(listNumber);
						if (originlParentList == null)
							continue;
						boolean geneExist1 = false;
						List<String> checkList1 = new ArrayList<String>(parentsGenes);
						checkList1.remove(0);
						for (String string2 : checkList1) {
							if (originlParentList.contains(string2)) {
								geneExist1 = true;
							}
						}
						if (geneExist1)
							return;
						List<Double> originalParentPValue = new ArrayList<>();
						for (String pGene : originlParentList) {
							originalParentPValue.add(psheetKeyValyePair.get(pGene));
						}
						Double OriginlHartungResults = GenericFunctions.hartungFunction(originalParentPValue);
						List<Double> originalInteractionPValue = new ArrayList<>();
						for (String pGene : inerationWith) {
							originalInteractionPValue.add(psheetKeyValyePair.get(pGene));
						}
						Double interactionHartungResults = GenericFunctions.hartungFunction(originalInteractionPValue);
						// added thse 4 lines
						/*
						if (Double.isNaN(interactionHartungResults))
							continue;
						if (interactionHartungResults==0d)
							interactionHartungResults = 2E-16;
							*/
						//System.out.println("New Path");
						List<String> newExtendedPath = new ArrayList<>();
						List<Double> newExtendedPathPValue = new ArrayList<>();
						Double hartungResultNew = 0.0;
						for (String s : originlParentList) {
							newExtendedPath.add(s);
							newExtendedPathPValue.add(psheetKeyValyePair.get(s));
						}
						for (String s : checkList) {
							newExtendedPath.add(s);
							newExtendedPathPValue.add(psheetKeyValyePair.get(s));
						}
						for (String s : inerationWith) {
							newExtendedPath.add(s);
							newExtendedPathPValue.add(psheetKeyValyePair.get(s));
						}
						
						hartungResultNew = GenericFunctions.hartungFunction(newExtendedPathPValue);
						//System.out.println(newExtendedPath.toString() + hartungResultNew);
						Boolean resultLessParent = (OriginlHartungResults > hartungResultNew);
						Boolean resultLessExtended = (interactionHartungResults > hartungResultNew);
						if (resultLessParent && resultLessExtended) {
							for (String sqw : checkList) {
								step2GnesDone.add(sqw);
							}
							System.out.println("/*********************************/");
							
							List<String> mostStupidRequest = new ArrayList<>();
							System.out.println("Original List");
							mostStupidRequest.add("Original List"+"\n");
							for (String double1 : originlParentList) {
								mostStupidRequest.add(double1);
								System.out.print(double1 + " " );
							}
							mostStupidRequest.add("\n"+OriginlHartungResults+ "\n") ;
							System.out.println(" "+ OriginlHartungResults);

							mostStupidRequest.add("Center path List"+"\n");
							System.out.println("Center path List");
							for (String double1 : checkList) {
								mostStupidRequest.add(double1);
								System.out.print(double1 + " ");
							}
							System.out.println(" ");
							mostStupidRequest.add("\n");
							mostStupidRequest.add("inerationWith"+"\n");
							System.out.println("inerationWith ");
							for (String doubleX : inerationWith) {
								mostStupidRequest.add(doubleX);
								System.out.print(doubleX + " ");
							} 
							System.out.println("  "+interactionHartungResults);

							mostStupidRequest.add("\n"+ interactionHartungResults + "\n");
							mostStupidRequest.add("final merged "+"\n");
							System.out.println("final merged ");
							for (String doubleX : newExtendedPath) {
								mostStupidRequest.add(doubleX);
								System.out.print(doubleX + " ");
							} 
							mostStupidRequest.add("\n"+ hartungResultNew +"\n");
							System.out.println("   "+hartungResultNew);
						
							Charset charset = StandardCharsets.UTF_8;
							try {
								
								Files.write(Paths.get("/home/farman/ModulatesSubPaths/dafahojao.text"), (
										mostStupidRequest.toString()+"\n").getBytes(charset),
										StandardOpenOption.CREATE, StandardOpenOption.APPEND);
							
							} catch (IOException e) {
								e.printStackTrace();
							}
									
					
							System.out.println(" closing *****");
							//System.err.println("Both less");
							aaa.add(Integer.toString(listNumber));
							aaa.add(Integer.toString(x));
							MergingPojo merged = new MergingPojo();
							merged.setMergedPath(newExtendedPath);
							merged.setPathNumber_1(listNumber);
							if (listNumber==2 || x==2 || listNumber==1 || x==1)
								System.out.println("check executed");
							setGenesList(newExtendedPath, listNumber);
							setGenesList(new ArrayList<String>(), x);
							inerLoogCouterStep3 = 0;
							inerLoogSizeStep3 = newExtendedPath.size();
							merged.setPathNumber_2(x);
							merged.setCombineCP(hartungResultNew);
							alreadyIteratedPath.add(listNumber);
							alreadyIteratedPath.add(x);
							for (String doubleString : newExtendedPath) {
								if (!finalizingPath.contains(doubleString))
									finalizingPath.add(doubleString);
							}
							Boolean found = false;
							for (int check = 0; check < mergedPath.size(); check++) {
								MergingPojo merged1 = mergedPath.get(check);
								Boolean matchedList = merged.compareMergingPojosObject(merged1);
								if (matchedList) {
									found = true;
									if (hartungResultNew < merged1.getCombineCP()) {
										mergedPath.remove(check);
										mergedPath.add(check, merged);
									}
									break;
								}

							} 
							if (!found) {
								mergedPathPathNumber.add(listNumber);
								mergedPathPathNumber.add(x);
								mergedPath.add(merged);
							}
						}
      
					}
				}
			}
	}
	private static synchronized void writeInFIleMerging() {
		Charset charset = StandardCharsets.UTF_8;
		try {
			List<Double> pavlueListString = new ArrayList<>();
			pavlueListString.clear();
			for (int sList = 0; sList <AllMergedpaths.size(); sList++) {
				Integer asd = sList;
				ArrayList<String> genesList = (ArrayList<String>) AllMergedpaths.get(sList);
				pavlueListString = new ArrayList<>();
				pavlueListString.clear();
				for (String string : genesList) {
					pavlueListString.add(psheetKeyValyePair.get(string));
				}
				Double calculationResult = GenericFunctions.hartungFunction(pavlueListString);
				Files.write(Paths.get(Step3Output), (genesList.toString() +" " +calculationResult+"\n").getBytes(charset),
						StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private static List<String> getGenesList(Integer index) {
		try {
		return hashTableList.get(index);
		} catch (Exception e)
		{

			return new ArrayList<>();
		}
		
}

	private static void setGenesList(List<String> list, Integer index) {
		hashTableList.put(index, list);
			}

	private static List<String> sortpSheetList(List<String> mainValueList) {
		if (mainValueList == null)
			return null;
		List<PSheet> tempList = new ArrayList<PSheet>();
		List<String> returnList = new ArrayList<>();
		for (String item : mainValueList) {
			PSheet temp = new PSheet();
			temp.setGeneName(item);
			try {
				temp.setpValue(psheetKeyValyePair.get(item));
			} catch (Exception e) {
				//System.out.println("error" + item);
				temp.setpValue(0.0);
			}
			if (temp.getpValue() > 0)
				tempList.add(temp);
		}

		Collections.sort(tempList);
		for (PSheet returnL : tempList) {
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
}