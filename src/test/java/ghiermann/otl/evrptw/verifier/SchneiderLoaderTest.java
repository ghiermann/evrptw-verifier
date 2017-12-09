/*
 * Copyright 2017 Gerhard Hiermann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ghiermann.otl.evrptw.verifier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SchneiderLoaderTest {

    @Test
    void testLoadFileC103_21() throws IOException {
        assertTrue(testLoadFile("c103_21.txt"));
    }

    @Test
    void testLoadFileR205_21() throws IOException {
        assertTrue(testLoadFile("r205_21.txt"));
    }

    private boolean testLoadFile(String path) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(path).getFile());
            (new SchneiderLoader()).load(file);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Test
    @DisplayName("Load C101C10 and check that everything is correct")
    void testLoadFilec101C10AndCheck() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("c101C10.txt").getFile());
            EVRPTWInstance instance = (new SchneiderLoader()).load(file);

            assertEquals(10, instance.getNumCustomers());
            assertEquals(5, instance.getRechargingStations().size());
            assertEquals(16, instance.getNumNodes());

            double tolerance = 0.001;
            assertEquals(77.75, instance.getVehicleEnergyCapacity(), tolerance);
            assertEquals(200.00, instance.getVehicleCapacity(), tolerance);
            assertEquals(1.0, instance.getVehicleEnergyConsumption(), tolerance);
            assertAll(instance.getRechargingStations().stream().map((it) ->
                () -> assertEquals(3.47, it.rechargingRate, tolerance)));

        } catch(Exception e) {
            fail("file should be readable");
        }
    }
}
