/**
 * Copyright 2015-2022, Francois Ritaly
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
package codingame.parse_sql_queries;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Solution {

    private static class Record {
        final List<String> fields;
        final List<String> values;

        private Record(List<String> fields, List<String> values) {
            this.fields = fields;
            this.values = values;
        }

        String getField(String name) {
            return values.get(fields.indexOf(name));
        }
    }

    private static final Pattern PATTERN = Pattern.compile("^SELECT (?<fields>.+) FROM (?<table>\\w+)( WHERE (?<whereColumn>\\w+)\\s*=\\s*(?<whereValue>\\w+))?( ORDER BY (?<orderByColumn>\\w+)( (?<order>DESC|ASC)?)?)?$");

    private static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d+(\\.\\d+)?");

    private static boolean isNumeric(String s) {
        return NUMERIC_PATTERN.matcher(s).matches();
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final String query = scanner.nextLine();
        final int rows = scanner.nextInt();

        System.err.println(query);
        System.err.println(rows);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        // Parse the SQL query
        final Matcher matcher = PATTERN.matcher(query);

        if (!matcher.matches()) {
            throw new IllegalStateException("Failed to parse query '" + query + "'");
        }

        final List<String> fields = Arrays.asList(matcher.group("fields").split(","))
                .stream()
                .map(s -> s.trim())
                .collect(Collectors.toList());

        final String tableHeader = scanner.nextLine();

        System.err.println(tableHeader);

        final List<String> tableHeaders = Arrays.asList(tableHeader.split(" "));

        List<Record> records = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            final String line = scanner.nextLine();

            System.err.println(line);

            records.add(new Record(tableHeaders, Arrays.asList(line.split(" "))));
        }

        filtering: {
            final String whereColumn = matcher.group("whereColumn"), whereValue = matcher.group("whereValue");

            if (whereColumn != null) {
                records = records.stream().filter(r -> r.getField(whereColumn).equals(whereValue)).collect(Collectors.toList());
            }
        }

        sort: {
            final String orderByColumn = matcher.group("orderByColumn"), order = matcher.group("order");

            if (orderByColumn != null) {
                Collections.sort(records, (a, b) -> {
                    final String value1 = a.getField(orderByColumn), value2 = b.getField(orderByColumn);

                    if (isNumeric(value1) && isNumeric(value2)) {
                        return Float.compare(Float.parseFloat(value1), Float.parseFloat(value2));
                    }

                    return value1.compareTo(value2);
                });

                if ("DESC".equals(order)) {
                    Collections.reverse(records);
                }
            }
        }

        final List<String> columns = fields.contains("*") ? tableHeaders : fields;

        System.out.println(String.join(" ", columns));

        for (Record record : records) {
            System.out.println(columns.stream().map(c -> record.getField(c)).collect(Collectors.joining(" ")));
        }
    }
}