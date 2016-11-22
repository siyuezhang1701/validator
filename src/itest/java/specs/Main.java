package specs;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String entryPointOfTestTarget = System.getenv("ENDPOINT");
        Main instance = new Main();
        if (entryPointOfTestTarget == null) {
            System.err.println("ENDPOINT NOT SET");
            instance.exit(1, false);
        }
        Result result = JUnitCore.runClasses(JunitTestSuite.class);
        if (!result.getFailures().isEmpty()) {
            instance.exit(1, true);
        }

        instance.exit(0, true);
    }

    private void exit(int exitCode, boolean submitResult) throws IOException {
        if (submitResult) {
            // hooks
        }
        System.exit(exitCode);
    }
}
