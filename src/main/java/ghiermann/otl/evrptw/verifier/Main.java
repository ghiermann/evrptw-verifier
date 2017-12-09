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

import ghiermann.otl.evrptw.verifier.EVRPTWInstance.Customer;
import ghiermann.otl.evrptw.verifier.EVRPTWInstance.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;


public class Main {
    private static boolean detailedMode = false;

    public static void main(String[] args) {
        if(args.length < 2) {
            printUsage();
            return;
        }
        detailedMode = args[0].equals("-d");
        int offset = detailedMode ? 1 : 0;
        if(args.length < 2 + offset) {
            printUsage();
            return;
        }

        File instanceFile = Paths.get(args[offset]).toFile();
        File[] solutionFiles = IntStream.range(offset + 1, args.length)
            .mapToObj(i -> Paths.get(args[i]).toFile())
            .toArray(File[]::new);

        if(instanceFile.isDirectory() || Arrays.stream(solutionFiles).anyMatch(File::isDirectory)) {
            System.err.println("Error: instance path and solution paths should be files (not directories ..)");
            return;
        }
        verify(instanceFile, solutionFiles);
    }

    private static void printUsage() {
        System.err.println("Error: Wrong number of arguments\n"
            + "Usage: java -jar EVRPTWVerifier (-d) instancePath solutionPath(s)");
    }

    private static void verify(File instanceFile, File[] solutionFile) {
        EVRPTWInstance instance;
        try {
            instance = new SchneiderLoader().load(instanceFile);
        } catch(FileNotFoundException e) {
            System.err.println("Error: couldn't open instance file " + instanceFile.getPath());
            return;
        } catch(IOException e) {
            System.err.println("Error: error while parsing the instance file (" + instanceFile.getPath() + ")\n"
                + "is this an actual E-VRPTW instance file?");
            return;
        }

        RoutesLoader routesLoader = RoutesLoader.create(instance);

        for(int i = 0; i < solutionFile.length; i++) {

            System.out.print("Solution " + (i + 1) + " (" + solutionFile[i].getName() + "): ");
            RoutesLoader.CostRoutesPair res = routesLoader.load(solutionFile[i]);
            if(res == null) {
                // error while loading
                return;
            }

            double cost = res.cost;
            List<List<Node>> routes = res.routes;

            Set<Node> nodesMissing = allNodesInRoutes(instance, routes);
            boolean valid = false;
            if(nodesMissing.isEmpty()) {
                if(detailedMode) System.out.println("All customers have been assigned to a route.");
                EVRPTWRouteVerifier verifier = EVRPTWRouteVerifier.create(instance);
                valid = verifier.verify(routes, cost, detailedMode);
            } else {
                System.out.println("Not all customers have been assigned to a route!");

                StringBuilder missing = new StringBuilder();
                boolean first = true;
                for(Node node : nodesMissing) {
                    if(!first) missing.append(", ");
                    else first = false;
                    Customer c = instance.getCustomer(node);
                    missing.append(c.name);
                }

                System.out.println("customers missing: " + missing);
            }
            System.out.println(String.format("[_%s_]",
                (valid) ? "valid" : "INVALID"));
            if(detailedMode) System.out.println();
        }
    }

    private static Set<Node> allNodesInRoutes(EVRPTWInstance instance, List<List<Node>> routes) {
        Set<Node> nodes = new HashSet<>(instance.getCustomers());
        for(List<Node> route : routes)
            for(Node n : route) {
                nodes.remove(n);
            }
        return nodes;
    }
}
