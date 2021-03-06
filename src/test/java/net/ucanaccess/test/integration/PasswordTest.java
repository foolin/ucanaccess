/*
Copyright (c) 2012 Marco Amadei.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package net.ucanaccess.test.integration;

import java.io.File;
import java.sql.Connection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import net.ucanaccess.test.util.AccessVersion;
import net.ucanaccess.test.util.AccessVersionAllTest;

@RunWith(Parameterized.class)
public class PasswordTest extends AccessVersionAllTest {

    public PasswordTest(AccessVersion _accessVersion) {
        super(_accessVersion);
    }

    @Test
    public void testPassword() throws Exception {
        File dbFile = copyResourceToTempFile("testdbs/pwd.mdb");
        Connection ucanaccessConnection = null;
        try {
            ucanaccessConnection = getUcanaccessConnection(dbFile.getAbsolutePath());
        } catch (Exception _ex) {
            assertContains(_ex.getMessage(), "Password authentication failed");
        }
        assertNull(ucanaccessConnection);

        setPassword("ucanaccess");
        ucanaccessConnection = getUcanaccessConnection();
        ucanaccessConnection.close();
        assertNotNull(ucanaccessConnection);
    }
}
