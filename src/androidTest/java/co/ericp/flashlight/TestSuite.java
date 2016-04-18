package co.ericp.flashlight;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        FlashlightImplTest.class,
        FlashlightProviderTest.class,
        LegacyFlashlightImplTest.class,
        MainActivityTest.class
})
public class TestSuite {}
