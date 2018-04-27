JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) *.java

CLASSES = Bernoulli.java \
	BigIntegerMath.java \
	BigSurdVec.java \
	Euler.java \
	BigComplex.java \
	BigIntegerPoly.java \
	EulerPhi.java \
	Harmonic.java \
	PartitionsP.java \
	Rational.java \
	Wigner3jGUI.java \
	BigDecimalMath.java \
	BigSurd.java \
	Factorial.java \
	Ifactor.java \
	Prime.java \
 	RatPoly.java \
	Wigner3j.java \
	GenesInfo.java \
	GenesInteractions.java \
	GenericFunctions.java \
	DataStore.java \
	InitialPaths.java \
	ExtensionMerging.java \
	FinalThreads.java \
	StringListSorting.java \
	MergingPojo.java \
	TempModel.java \
	CustomEnum.java \

default: classes

classes: $(CLASSES:.java=.class)
	jar cvfm ModulatedSubPathFinder.jar manifest.txt *.class 

clean:
	$(RM) *.class
	$(RM) ModulatedSubPathFinder.jar
