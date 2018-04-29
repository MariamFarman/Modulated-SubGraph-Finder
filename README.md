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

This is a text file that contains the initial sub-graphs that are found by combining the individual P-values of the gene. The snapshot shows there core sub-graphs were found, writen in a linw with the combined p-value of the sub-graph at the end.

`[ppp2r1a, tgfbr2, tgfb2, ppp2ca] 1.1103580381200124E-18`

`[smad2, skp1a, smad3] 1.6569054494446366E-7`
 
 `[nog, bmp2, bmp5, bmpr2, gdf7, bmp6] 3.482452728268697E-4`

#### ExtendedGraphs

This file shows if any sub-graphs were extended by adding genes beyond its immediate neighbourhood.

`[ppp2r1a, tgfbr2, tgfb2, ppp2ca] 1.1103580381200124E-18`

`[smad2, skp1a, smad3, smad7, ifng, acvr1, smad5] 1.9580322709452766E-10`
 
`[nog, bmp2, bmp5, bmpr2, gdf7, bmp6] 3.482452728268697E-4`

#### MergedGraphs

This is the text file showing if any or all the (extended/unextended) sub-graphs merge with each other or not.

#### SourcesAndSinks

This output file gives details about the genes found in the sub-graphs. It shows graph number followed by genes in the graph. Then each gene from the graph, its interactions in the graph, its fold chnage, individual p-value and in the last if it was identifed as a source, intermediate or sink.

`[Graph 1]`

`[ppp2r1a, tgfbr2, tgfb2, ppp2ca]`

`[ppp2r1a, -tgfbr2, -1.15250657211695, 1.4E-7, Sink]`

`[tgfbr2, -ppp2ca-ppp2r1a-tgfb2, 0.357347052992014, 0.03294361100258, Intermediate]`

`[tgfb2, -tgfbr2, -0.273710254909285, 4.0E-5, Source]`

`[ppp2ca, -tgfbr2, -0.225269184801659, 6.44660834994E-4, Sink]`

`[Graph 2]`

`[smad2, skp1a, smad3, smad7, ifng, acvr1, smad5]`

`[smad2, -smad7-acvr1-skp1a, 1.20822323638527, 1.51528460481E-4, Sink]

 [skp1a, -smad2-smad3, 0.47396118683819, 0.014525956708056, Source]`
 
`[smad3, -smad7-skp1a, -0.193102843027487, 0.146036813182214, Sink]

 [smad7, -smad2-smad3-smad5-ifng, 0.100940948274761, 0.562318600418486, Intermediate]`
 
`[ifng, -smad7, -0.411195572576727, 2.12566137193E-4, Source]`

`[acvr1, -smad5-smad2, -0.360351622623896, 0.017364084706018, Source]`

`[smad5, -smad7-acvr1, 0.43197314616271, 0.005281642821289, Sink]`

`[Graph 3]`

`[nog, bmp2, bmp5, bmpr2, gdf7, bmp6]`

`[nog, -bmp2-bmp5-bmp6-gdf7, -0.397877024648883, 0.005498079382534, Source]`

`[bmp2, -bmpr2-nog, -0.135915332971465, 0.237098851922717, Intermediate]`

`[bmp5, -bmpr2-nog, -0.21104227566159, 0.419057181261883, Intermediate]`

`[bmpr2, -bmp2-bmp5-bmp6-gdf7, 0.255224975603306, 0.017218841862483, Sink]`

`[gdf7, -bmpr2-nog, 0.334306828205764, 0.011845850200211, Intermediate]`

`[bmp6, -bmpr2-nog, 0.079033613236002, 0.403941986198972, Intermediate]`


### NetworkFile

A text file with directed adjacency list for MSF identified modulated sub-graphs. This file could further be used to visualize the sub-graphs in other tools for example in Cytoscape.

`ppp2r1a tgfbr2 |-`

`tgfbr2 tgfb2 <-`

`ppp2ca tgfbr2 |-`

`smad2 skp1a <-`

`smad3 skp1a <-`

`nog bmp2 ->`

`nog bmp5 ->`

`bmpr2 bmp2 <-`

`gdf7 nog <-`

`nog bmp6 ->`

`smad7 smad2 ->`

`smad7 ifng <-`

`acvr1 smad3 <->`

`acvr1 smad5 ->`

### Tutorial MSF to StringApp




## Built With

Eclipse Neon

## Version

Version 1.0 of the tool.

## Authors

**Mariam Farman** 

## License

This project is licensed under the Creative Commons Attribution 4.0 International License.




