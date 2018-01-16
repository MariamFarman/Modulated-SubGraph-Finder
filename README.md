# SubPathFinder

The tool is used to find the significantly dis-regulated sub-modules or cluster of genes from the host cell signaling network, giving these sub-paths an overall significance of modulation by combining the individual p-values of the genes derived from differential genes expression analysis. 

# Installation

## Prerequisites

* Java version 8
* Jdk 1.8

## Execute following commands to compile and run ModulatedSubPathFinder Project.

### Copy the whole project of ModulatedSubpathFiner from github into a new directory

git clone https://github.com/MariamFarman/Modulated-SubPath-Finder.git

### Naviagate to src folder of project
### Execute following command to compile ModulatedSubPathFinder Project

javac -cp ../lib/dom4j-1.6.1.jar:../lib/ooxml-schemas-1.0.jar:../lib/poi-3.9.jar:../lib/poi-ooxml-3.7-20101029.jar:../lib/xmlbeans-2.5.0.jar -d ../bin/ Bernoulli.java BigIntegerMath.java BigSurdVec.java Euler.java BigComplex.java BigIntegerPoly.java EulerPhi.java Harmonic.java PartitionsP.java Rational.java Wigner3jGUI.java BigDecimalMath.java BigSurd.java Factorial.java Ifactor.java Prime.java RatPoly.java Wigner3j.java GenesInfo.java GenesInteractions.java GenericFunctions.java DataStore.java InitialPaths.java ExtensionMerging.java FinalThreads.java StringListSorting.java MergingPojo.java

## Package ModulatedSubPathFinder Project as a jar file

### Navigate to bin folder of ModulatedSubPathFinder
### Run following command

    echo Main-Class: InitialPaths  >manifest.txt
    jar cvfm ModulatedSubPathFinder.jar  manifest.txt *.class



## Getting Started

To find the Modulated sub-paths you need 2 files. One file is tab-seperated text file containg the output of DESeq2 analysis (Column names should be removed) and Pvalue of genes should be in column number 5. Second file is the intearction file with 3 columns, first two columns with interacting gene and the third column direction of the interaction. The gene identifiers should be same in both the files. Example files are included in Docs folder

* Text file of DEG analysis 
* Text file containing interactions 

## Execute following commands from bin folder of ModulatedSubPathFinder Project

java -jar ModulatedSubPathfinder.jar inputDEGFilePath inputInteractionFilePath OutputFolderPath/

## Output Format

### Initial Paths 

This is a text file that contains the initial subpaths that are found by combining the individual P-values of the gene.

### Extended Paths

In this text file any initial subpath that could be further extended by incoporating insignificant and significant genes but with a smaller CombinePvalue.

### Merged Paths

This is the text file showing if any or all the (extended/unextended) subpaths merge with each other or not.

### Final Output

This output file gives details about the genes found in the sub-modules. It shows all the possible sources and sinks for each sub-modules indentified and the interaction genes of each gene in the sub-module and its corresponding P-value.


## Built With

Eclipse Neon

## Version

Version 1.0 of the tool.

## Authors

**Mariam Farman** 




