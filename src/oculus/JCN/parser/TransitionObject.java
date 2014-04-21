package oculus.JCN.parser;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransitionObject {
	public List<String> firstWords;							// A list of all the first words that start classes in java
	public Map<String,List<String>> transitions;			// For each string in the map, a list of every word that follow it
	public Map<Integer,Float> lengthProbabilities;			// A map from length (representing camel case length) to the probability at which they occur in the java library
	public Set<String> classNames;							// A set of all class names in java
}
