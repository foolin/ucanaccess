package net.ucanaccess.test.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.Parameterized;

public abstract class AccessVersion2007Test extends UcanaccessTestBase {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Iterable<Object[]> getAccessVersion2007() {
        List<Object[]> list = new ArrayList<Object[]>();
        list.add(new Object[] { AccessVersion.V2007 });
        return list;
    }

    public AccessVersion2007Test(AccessVersion _fileFormat) {
        super(_fileFormat.getFileFormat());
    }
}
