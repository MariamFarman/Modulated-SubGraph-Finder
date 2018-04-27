# Modulated Sub-graph Finder

The tool is used to find the significantly dis-regulated sub-graphs or cluster of genes from the host cell signaling network, giving these sub-graphs an overall significance of modulation by combining the individual p-values of the genes derived from differential genes expression analysis. 

# Installation

## Prerequisites

* Java version 8
* Jdk 1.8

## Download

### Linux

Copy the whole project of ModulatedSubpathFiner from github into a new directory
git clone https://github.com/MariamFarman/Modulated-SubGraph-Finder

### Windows



### Copy the whole project of ModulatedSubpathFiner from github into a new directory

    git clone https://github.com/MariamFarman/Modulated-SubGraph-Finder`

## Installation
Naviagate to src folder of project and run

    `make`


## Getting Started

To find the Modulated sub-graphs you need 2 files. One file is tab-seperated text file containg the output of DESeq2 analysis (Column names should be removed) and Pvalue of genes should be in column number 5. Second file is the intearction file with 3 columns, first two columns with interacting gene and the third column direction of the interaction. The gene identifiers should be same in both the files. Example files are included in Docs folder

* Text file of DEG analysis 
* Text file containing interactions 
* Interactions file from Reactome (used by developers of MSF)

## Execute following commands from bin folder of ModulatedSubPathFinder Project

java -jar ModulatedSubPathfinder.jar inputDEGFilePath inputInteractionFilePath extensionLimit mergingLimit OutputFolderPath/

The default extension for the sub-graphs is 2 and merging is done by 1 gene by default. Either give no limits for extension or merging (default would be used) or give both limits.

## Output Format

### Initial Paths 

This is a text file that contains the initial sub-graphs that are found by combining the individual P-values of the gene.

### Extended Paths

In this text file any initial sub-graph that could be further extended beyond its immediate neighbourhood.

### Merged Paths

This is the text file showing if any or all the (extended/unextended) sub-graphs merge with each other or not.

### Final Output

This output file gives details about the genes found in the sub-graphs. It shows all the possible sources and sinks for each sub-module indentified and the interacting genes of each gene in the sub-graph and its corresponding P-value.

### Network File
A text file with adjacency list for MSF identified modulated sub-graphs. This file could further be used to visulaize the sub-graphs in other tools for example in Cytoscape.


## Built With

Eclipse Neon

## Version

Version 1.0 of the tool.

## Authors

**Mariam Farman** 




