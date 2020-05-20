package basic.anonymousClass;

public class Test3 {

	// The first method takes a number of iterations for a loop and a callback that is called on each iteration. The second method takes a number of iterations, creates a callback and passes them to the first method.

	static void performIterationsWithCallback(int numberOfIterations, LoopCallback callback) {
		for (int i = 0; i < numberOfIterations; i++) {
			callback.onNewIteration(i);
		}
	}

	static void startIterations(int numberOfIterations) {
		// invoke the method performIterationsWithCallback here
		performIterationsWithCallback(numberOfIterations, new LoopCallback() {
			@Override
			public void onNewIteration(int iteration) {
				System.out.println("iteration: " + iteration);
			}
		});
	}
}

interface LoopCallback {

	void onNewIteration(int iteration);
}