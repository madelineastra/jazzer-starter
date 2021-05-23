import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.api.FuzzerSecurityIssueMedium;
import org.junit.Assume;
import org.junit.runner.RunWith;
import java.io.*;
import java.util.Base64;
import java.util.ArrayList;

public class FuzzDriver {
    public static void fuzzerTestOneInput(FuzzedDataProvider data) {
	try {
	    byte[] raw = Base64.getDecoder().decode(data.consumeString(9999));
	    InputStream input = new ByteArrayInputStream(raw);
	    ObjectInputStream ois = new SafeObjectInputStream(input);
	    Object o = ois.readObject();
	    if(!o.getClass().toString().contains("java.lang")) {
		throw new FuzzerSecurityIssueMedium("Invalid class created" + o.getClass().toString());
	    }
	} catch (InvalidClassException ice) {
	} catch (IllegalArgumentException ice) {
	} catch (IOException ioe) {
	} catch (ClassNotFoundException cnfe) {
	} catch (Exception e) {
	}
    }
}


class SafeObjectInputStream extends ObjectInputStream {

    ArrayList<String> allowedTypes;

    public SafeObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
        allowedTypes = new ArrayList<>();
        allowedTypes.add(String.class.getName());
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {

        if (!allowedTypes.contains(desc.getName()))
            throw new InvalidClassException("Unauthorized deserialization attempt", desc.getName());

        return super.resolveClass(desc);
    }
}
