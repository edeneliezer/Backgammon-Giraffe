package jUnit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CheckIfReadyToPlayTest.class, GameInfoTest.class, InfoPanelTest.class, OpenPdfFileTest.class,
		ResetTest.class })
public class AllTests {

}
