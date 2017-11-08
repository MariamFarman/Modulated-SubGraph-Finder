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
import uk.ac.roslin.ensembl.config.DBConnection.DataSource;
import uk.ac.roslin.ensembl.dao.database.DBRegistry;
import uk.ac.roslin.ensembl.model.core.Gene;
import uk.ac.roslin.ensembl.model.core.Species;
import uk.ac.roslin.ensembl.model.database.Registry;
import uk.ac.roslin.ensembl.exception.ConfigurationException;
import uk.ac.roslin.ensembl.exception.DAOException;
import uk.ac.roslin.ensembl.exception.NonUniqueException;




public class DataStore {
	
	public static PSheet pSheet = new PSheet();
	public static List<PSheet> pSheetList = new ArrayList<PSheet>();
	public static Map<String, Double> psheetKeyValyePair = new HashMap<String, Double>();
	public static List<String> edgeListSet = new ArrayList<String>();
	public static List<EdgeClass> edgeClassListComplete = new ArrayList<EdgeClass>();
	static Map<String, List<String>> edgeListHashMap = new HashMap<String, List<String>>();
	static Map<String, List<String>> EnsemblData = new HashMap<String, List<String>>();
	static Hashtable<String, String> geneInfoHashTable = new Hashtable<>();
	static String outputPath;
	private static DataStore instance = null;
	static Registry reg;
	static Species hsa;
	
	
	
	
	
	public static String getOutputPath() {
		return outputPath;
	}

	public static void setOutputPath(String outputPath) {
		DataStore.outputPath = outputPath;
	}

	public  static void DataStor (String psheet, String interaction, String outputPathX) /*throws ConfigurationException, DAOException, NonUniqueException */
	{
		outputPath =outputPathX;
		/*reg =  DBRegistry.createRegistryForDataSourceCurrentRelease(DataSource.ENSEMBLDB);
		hsa = reg.getSpeciesByAlias("human");*/
		readPValuesFromTextFile(psheet);
		readInteractionsFromTextFile(interaction);
		createHashListFromEdegeClassList();	
		System.out.println("Finding Initial Paths");
	}
		
	public static void main(String[] args) throws Exception {
		{			
			
		}}
	
	  public static DataStore getInstance(){
	    if(instance==null){
	       instance = new DataStore();
	      }
	      return instance;
	  }
	  
	
	protected static PSheet getpSheet( ) {
	      return pSheet;
	   }
	  protected static List<PSheet> getpSheetList( ) {
	      return pSheetList;
	   }

	  protected static Map<String, Double> getpsheetKeyValyePair( ) {
	      return psheetKeyValyePair;
	   }
	  protected static List<String> getedgeListSet( ) {
	      return edgeListSet;
	   }

	  protected static List<EdgeClass> getedgeClassListComplete( ) {
	      return edgeClassListComplete;
	   }
	  protected static Map<String, List<String>> getedgeListHashMap ( ) {
	      return edgeListHashMap;
	   }
	  protected static Hashtable<String, String> getgeneInfoHashTable ( ) {
	      return geneInfoHashTable;
	   }
	  protected static Map<String, List<String>> getEnsemblData ( ) {
	      return EnsemblData;
	   }

		public static void readPValuesFromTextFile(String fileName) /*throws DAOException*/   {

			
			List<String> list = new ArrayList<>();
			try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
				list = stream.map(String::toUpperCase).collect(Collectors.toList());

			} catch (IOException e) {
				e.printStackTrace();
			}
			int count = 0;
			for (String string : list) {
				count = count + 1;
				pSheet = new PSheet();
				String[] splitArray = string.split("\\s+"); 
				String geneName = splitArray[0].replace("\"", ""); 
				String pvalue = splitArray[5];
				pSheet.setGeneName(geneName.toLowerCase());
				if (pvalue.equalsIgnoreCase("NA"))
					continue;
				double tempasd = (Double.parseDouble(pvalue));
				if (tempasd == 2D)
					tempasd = 1;
				pSheet.setpValue(tempasd);
				/*Gene s = hsa.getGeneByStableID(geneName);*/
		   		 List<String> valuesList = new ArrayList<String>();
		   		/*valuesList.add(s.getBiotype());
		   		valuesList.add(s.getDescription());*/
		   		 /*EnsemblData.put(geneName.toLowerCase(), valuesList);*/
				if (pSheet.getpValue() > 0 && pSheet.getpValue() < 1)
					pSheetList.add(pSheet);
				psheetKeyValyePair.put(pSheet.getGeneName().toLowerCase(), pSheet.getpValue());
				geneInfoHashTable.put(geneName.toLowerCase(), String.valueOf(splitArray[2]));
		
			}
		}
	
		public static void readInteractionsFromTextFile(String fileName ) {

			List<String> list = new ArrayList<>();
			try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
				list = stream.map(String::toUpperCase).collect(Collectors.toList());

			} catch (IOException e) {
				e.printStackTrace();
			}
			int count = 0;
			//System.out.println("list size" + list.size());
			for (String string : list) {
				count = count + 1;
				EdgeClass edgeClass = new EdgeClass();
				String[] splitArray = string.split("\\s+"); 
				//System.out.println("string "+ string);
				//System.out.println("splitArray"+splitArray);
				String from = splitArray[0].toLowerCase();
				String to = splitArray[1].toLowerCase();
				Boolean pvalueFound = true;
				try {
					Double pvalue = psheetKeyValyePair.get(from);
					Double pvalue2= psheetKeyValyePair.get(to);
				}
				catch (Exception e)
				{
					pvalueFound =false;
					
				}
				if (!pvalueFound)
					continue;
				if (!edgeListSet.contains(from))
					edgeListSet.add(from);
				if (!edgeListSet.contains(to))
					edgeListSet.add(to);
				edgeClass.setColumn1(from);
				edgeClass.setColmn2(to);
				edgeClassListComplete.add(edgeClass);

			}
	}
		
		private static void createHashListFromEdegeClassList() {
			for (String uniqueKey : edgeListSet) {
				List<String> valuesList = new ArrayList<String>();
				for (EdgeClass edgeClass : edgeClassListComplete) {
					if (edgeClass.getColumn1().equalsIgnoreCase(uniqueKey)) {
						valuesList.add(edgeClass.getColmn2());
					}
				}
				for (EdgeClass edgeClass : edgeClassListComplete) {
					if (edgeClass.getColmn2().equalsIgnoreCase(uniqueKey)) {
						valuesList.add(edgeClass.getColumn1());
					}
				}
				edgeListHashMap.put(uniqueKey, valuesList);
			}
			//System.out.println("map size" + edgeListHashMap.size());

		}

}