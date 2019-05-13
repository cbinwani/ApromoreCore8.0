import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.out.XesXmlSerializer;
import org.junit.Test;

/** Test suite. */
public class UnitTest {

    /** Dummy unit test. */
    @Test public void parse() throws Exception {
        try (InputStream in = UnitTest.class.getClassLoader().getResourceAsStream("bpLog.xes")) {
            XesXmlParser parser = new XesXmlParser();
            XLog xlog = parser.parse(in).get(0);
        }
    }

    @Test public void serialize() throws Exception {
        try (InputStream in = UnitTest.class.getClassLoader().getResourceAsStream("bpLog.xes")) {
            XesXmlParser parser = new XesXmlParser();
            XLog xlog = parser.parse(in).get(0);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                XesXmlSerializer serializer = new XesXmlSerializer();
                serializer.serialize(xlog, out);
            }
        }
    }
}
