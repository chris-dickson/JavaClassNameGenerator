package oculus.JCN;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import oculus.JCN.parser.JavaClassNameParser;
import oculus.JCN.parser.TransitionObject;

public class JavaClassNamer {
	
	public static <T> T getRandomElementFromProbabilityMap(Map<T,Float> objectProbabilityMap, Random r) {
		float f = r.nextFloat();
		float sum = 0.f;
		for (T obj : objectProbabilityMap.keySet()) {
			sum += objectProbabilityMap.get(obj);
			if (f < sum) {
				return obj;
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		JavaClassNameParser jcnp = new JavaClassNameParser();
		
		// Parse the resource file that contains all our class names and create a transition object that we use to generate random names
		TransitionObject to = null;
		try {
			Set<String> classNames = jcnp.getClassNames();
			to = jcnp.getTransitionInfo(classNames);
			to.classNames = classNames;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (to != null) {
			Random r = new Random();
			int i = 0;
			while (i < 1000) {
				// Get a maximum number of camel case pieces for the random class name
				int maxNumPieces = Math.max(3, getRandomElementFromProbabilityMap(to.lengthProbabilities, r));
				
				// Grab a random first piece to start with
				String currentPiece = to.firstWords.get(Math.abs(r.nextInt()) % to.firstWords.size());
				String randomName = currentPiece;
				for (int j = 1; j < maxNumPieces; j++) {
					
					// Get a random next word based on probability of occurance in the java library
					List<String> transitionList = to.transitions.get(currentPiece);
					if (transitionList != null) {
						String nextPiece = transitionList.get( Math.abs(r.nextInt()) % transitionList.size() );
						randomName += nextPiece;
						currentPiece = nextPiece;
					} else {
						break;
					}
				}
				
				// If this is actually a class (this happens a lot by chance) try again
				if (!to.classNames.contains(randomName)) {
					i++;
				}
				
				System.out.println(randomName);
			}
		}
	}
}
