package org.example;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MergeTwoMaps extends TestCase {
    private static Logger LOGGER = Logger.getLogger(PotusTest.class.getName());
    private static final String REPUBLICAN = "Republican";
    private static final String DEMOCRATIC = "Democratic";
    private static Map<String, Potus> map1 = new HashMap<>();
    private static Map<String, Potus> map2 = new HashMap<>();

    static {
        Potus p1 = new Potus("Donald", "Trump", 2016, REPUBLICAN);
        Potus p2 = new Potus("BaracK", "Obama", 2008, DEMOCRATIC);
        Potus p3 = new Potus("Gerge W.1", "Bush", 2004, REPUBLICAN);
        Potus p4 = new Potus("Gerge W.", "Bush", 2000, REPUBLICAN);
        Potus p5 = new Potus("Bill1", "Clinton", 1996, DEMOCRATIC);

        map1.put(p1.getFirstName(), p1);
        map1.put(p2.getFirstName(), p2);
        map1.put(p3.getFirstName(), p3);
        map1.put(p4.getFirstName(), p4);

        map2.put(p4.getFirstName(), new Potus(p4.getFirstName(), "newLast", 1111, REPUBLICAN));
        map2.put(p5.getFirstName(), p5);
    }

    @Test
    public void test_map_merge() {
        Map<String, Potus> map1Copy = new HashMap<>(map1);
        map2.forEach(
                (key, value) -> map1Copy.merge(
                        key, // key from map2
                        value, // value from map2
                        //Entries with duplicate keys will be merged according to the logic below (an old value (from map1) will be replaced by new value from map2)
                        (existedValueFromMap1, newValueFromMap2) -> newValueFromMap2)); // the function to resolve the case with the same keys, in our case value from map2 will be used

        map1Copy.forEach((key, value) -> LOGGER.debug("key = " + key + " value = " + value));
    }

    // the same action as MAP merge method
    @Test
    public void test_stream_contact_merge() {
        Stream<Map.Entry<String, Potus>> combined = Stream.concat(map1.entrySet().stream(), map2.entrySet().stream()); // it contains all elements (with duplicates) from map1 and map2
        Map<String, Potus> result = combined.collect(
                Collectors.toMap(
                        entry -> entry.getKey(), // key that should be used for map creation
                        entry -> entry.getValue(), // value that should be used for map creation
                        (value1FromMap1, value2FromMap2) -> value1FromMap1)); // the function to resolve duplicates
        result.forEach((key, value) -> LOGGER.debug("key = " + key + " value = " + value));
    }

    @Test
    public void test_stream_of() {
        Map<String, Potus> result = Stream.of(map1, map2) // create stream from map1 and map2
                .flatMap(map -> map.entrySet().stream())// stream of Map.Entry objects from both maps
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // key
                        Map.Entry::getValue, // value
                        (v1, v2) -> v1)); // resolve duplicates
        result.forEach((key, value) -> LOGGER.debug("key = " + key + " value = " + value));
    }

    // Combine two lists
    @Test
    public void test_stream_contact_lists() {
        // Stream.concat(Stream.concat(stream1, stream2), stream3); can be used to combine several streams
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(4, 5, 6, 7);
        Stream<Integer> combined = Stream.concat(list1.stream(), list2.stream());
        List<Integer> result = combined.distinct().collect(Collectors.toList());
        result.forEach(LOGGER::debug);
    }
}
