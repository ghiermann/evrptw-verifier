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

import ghiermann.otl.evrptw.verifier.EVRPTWInstance.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

class RoutesLoader {
    private EVRPTWInstance instance;

    private RoutesLoader(EVRPTWInstance instance) {
        this.instance = instance;
    }

    public static class CostRoutesPair {
        final Double cost;
        final List<List<Node>> routes;

        public CostRoutesPair(Double cost, List<List<Node>> routes) {
            this.cost = cost;
            this.routes = routes;
        }
    }

    public CostRoutesPair load(File solutionFile) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(solutionFile));

            // if the first line starts with an #, then ignore it
            // first value is the objective value
            String line = nextLine(in);
            while(line.startsWith("#")) line = nextLine(in);

            double cost;
            try {
                cost = Double.parseDouble(line);
            } catch(NumberFormatException e) {
                System.err.println("Error: first line of the solution has to be the cost (number)");
                return null;
            }

            List<List<Node>> routes = new ArrayList<>();
            try {
                while((line = nextLine(in)) != null)
                    routes.add(parseLine(line, instance));
            } catch(RuntimeException e) {
                e.printStackTrace();
                System.err.println("Error: exception while parsing the nodes in the solution");
                return null;
            }
            return new CostRoutesPair(cost, routes);
        } catch(FileNotFoundException e) {
            System.err.println("Error: couldn't open solution file " + solutionFile.getPath());
            return null;
        } catch(IOException e) {
            System.err.println("Error while reading solution file (IOException)");
            return null;
        }
    }

    private static String nextLine(BufferedReader in) throws IOException {
        String line = in.readLine();
        while(line != null && (line = line.trim()).equals(""))
            line = in.readLine();
        return line;
    }

    private static List<Node> parseLine(String line, EVRPTWInstance instance) throws RuntimeException {
        List<Node> list;
        list = new ArrayList<>();
        StringTokenizer tok = new StringTokenizer(line, " ,");
        for(; tok.hasMoreTokens(); ) {
            String id = tok.nextToken();
            if(id.startsWith("D"))
                list.add(instance.getDepot());
            else if(id.startsWith("S"))
                list.add(instance.getRechargingStation(id));
            else if(id.startsWith("C"))
                list.add(instance.getCustomer(id));
            else
                throw new RuntimeException();
        }
        return list;
    }

    static RoutesLoader create(EVRPTWInstance instance) {
        return new RoutesLoader(instance);
    }
}
