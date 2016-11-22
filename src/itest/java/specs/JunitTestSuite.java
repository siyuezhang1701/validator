package specs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import specs.example.UserTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserTest.class
})
public class JunitTestSuite {
}
