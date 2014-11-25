import org.junit.Test

/**
 * Created by Александр on 10.11.2014.
 */

class GoogleServiceTest {
    GoogleService gs = new GoogleService()

    @org.junit.Test
    public void generateDistance() {
        assert gs.generateDistance("gvardeytsev shironintsev street 73", "gvardeytsev shironintsev street 73") == 9.9
    }

    @Test
    public void testGoThroughTheString() {
        gs.goThroughString("","Alex")
    }
}
