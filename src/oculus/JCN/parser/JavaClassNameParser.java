package oculus.JCN.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class JavaClassNameParser {
	public Set<String> getClassNames() throws IOException { 
		Set<String> names = new HashSet<String>();
		InputStream is = this.getClass().getResourceAsStream("../resources/javaclasses.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = br.readLine()) != null) {
			boolean allUpper = true;
			for (int i = 0; i < line.length(); i++) {
				char ch = line.charAt(i);
				if (Character.isUpperCase(ch)) {
					allUpper = false;
				}
			}
			if (allUpper || line.contains("_")) continue;
			
			
			String []subClasses = line.split("\\.");
			for (String name : subClasses) {
				names.add(name);
			}
		}
		return names;
	}
	
	public TransitionObject getTransitionInfo(Set<String> classNames) {
		TransitionObject to = new TransitionObject();
		to.firstWords = new ArrayList<String>();
		to.transitions = new HashMap<String,List<String>>();
		to.lengthProbabilities = new HashMap<Integer, Float>();
		
		Map<Integer,Integer> lengthHistogram = new HashMap<Integer, Integer>();
		
		Set<String> firstWordSet = new HashSet<String>();
		
		for (String name : classNames) {
			
			// Split each class name on CamelCase
			String []camelCasePieces = name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
			for (int i = 0; i < camelCasePieces.length-1; i++) {		
				String current = camelCasePieces[i];
				if (i==0) {
					firstWordSet.add(current);
				}
			
				Integer lengthCount = lengthHistogram.get(camelCasePieces.length);
				if (lengthCount == null) {
					lengthCount = 0;
				}
				lengthCount++;
				lengthHistogram.put(camelCasePieces.length,lengthCount);
				
				String next = camelCasePieces[i+1];
				List<String> nextList = to.transitions.get(current);
				if (nextList == null) {
					nextList = new ArrayList<String>();
				}
				nextList.add(next);
				to.transitions.put(current,nextList);
			}
		}
		
		for (String firstWord : firstWordSet) {
			to.firstWords.add(firstWord);
		}
		
		int totalPieces = 0;
		for (Integer classNamePiecesLength : lengthHistogram.keySet()) {
			totalPieces += lengthHistogram.get(classNamePiecesLength);
		}
		for (Integer classNamePiecesLength : lengthHistogram.keySet()) {
			to.lengthProbabilities.put(classNamePiecesLength, lengthHistogram.get(classNamePiecesLength) / (float)totalPieces);
		}
		
		return to;
	}
}
