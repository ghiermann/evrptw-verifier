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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class EVRPTWRouteVerifierTest {
    @Test
    @DisplayName("Load r205_21 and verify the corresponding solution (valid)")
    void testLoadFileR205_21AndVerifySolution() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File instanceFile = new File(classLoader.getResource("r205_21.txt").getFile());
            File solutionFile = new File(classLoader.getResource("r205.sol.txt").getFile());

            EVRPTWInstance instance = (new SchneiderLoader()).load(instanceFile);
            EVRPTWRouteVerifier verifier = EVRPTWRouteVerifier.create(instance);

            RoutesLoader.CostRoutesPair res = RoutesLoader.create(instance).load(solutionFile);
            assertTrue(verifier.verify(res.routes, res.cost, true));

        } catch(Exception e) {
            fail("file should be readable");
        }
    }
}
