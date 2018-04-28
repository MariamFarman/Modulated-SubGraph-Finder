# Modulated Sub-graph Finder

The tool is used to find the significantly dis-regulated sub-graphs or cluster of genes from the host cell signaling network, giving these sub-graphs an overall significance of modulation by combining the individual p-values of the genes derived from differential genes expression analysis. 

## Prerequisites

* Java version 8
* Jdk 1.8

## Download

### Linux

Copy the whole project of Modulated Sub-graph Finder from github into a new directory

`git clone https://github.com/MariamFarman/Modulated-SubGraph-Finder`

### Windows

## Installation
Navigate to src folder of project and run command

    `make`


## Input Data Preparation

To find the Modulated sub-graphs you need two files. One file is tab-seperated text file containg the output of DESeq2/EdgeR analysis. Second file is a directed adjacency list with 3 columns, first two columns with interacting genes and the third column direction of the interaction. The gene identifiers should be same in both the input files. Example files included in Docs folder.

## Tutorial

MSF has seven argument parameters 

* `-p`	The path to differential gene expression analysis file 
* `-i`	The path to network file (Interaction file)
* `-t`	Software used for differntial gene expression analysis (DEseq2 or EdgeR)
* `-e`	The extension limit (1 to 3 genes extension)
* `-m`	The merging limit (1 to 3 genes merging)
* `-k`	Output extra files (InitialGraphs, ExtendedGraphs and MergedGraphs)
* `-o`	The path to output folder

Navigate  to src folder of project and run command

java -jar ModulatedSubgraphfinder.jar -p ../Docs/

The default extension and merging limit is 2.

### Output Files

#### InitialGraphs

This is a text file that contains the initial sub-graphs that are found by combining the individual P-values of the gene.

#### ExtendedGraphs

In this text file any initial sub-graph that could be further extended beyond its immediate neighbourhood.

#### MergedGraphs

This is the text file showing if any or all the (extended/unextended) sub-graphs merge with each other or not.

#### SourcesAndSinks

This output file gives details about the genes found in the sub-graphs. It shows all the possible sources and sinks for each sub-module identified  and the interacting genes of each gene in the sub-graph and its corresponding P-value.

### NetworkFile
A text file with directed adjacency list for MSF identified modulated sub-graphs. This file could further be used to visualize the sub-graphs in other tools for example in Cytoscape.

### Tutorial MSF to StringApp


## Built With

Eclipse Neon

## Version

Version 1.0 of the tool.

## Authors

**Mariam Farman** 

## License

This project is licensed under the Creative Commons Attribution 4.0 International License.




