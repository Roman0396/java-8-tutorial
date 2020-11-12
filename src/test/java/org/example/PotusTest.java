package org.example;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PotusTest extends TestCase {
    private static Logger LOGGER = Logger.getLogger(PotusTest.class.getName());
    private static final String REPUBLICAN = "Republican";
    private static final String DEMOCRATIC = "Democratic";

    private List<Wife> trumpWifes = Arrays.asList(
            new Wife("Melania", Arrays.asList(new Child("Barron", 12))),
            new Wife("Marla", Arrays.asList(new Child("Tiffany", 24))),
            new Wife("Ivana", Arrays.asList(
                    new Child("Donald Jr.", 40),
                    new Child("Ivanka", 36),
                    new Child("Eric", 34))));

    private List<Potus> potuses = Arrays.asList(
            new Potus("Donald", "Trump", 2016, REPUBLICAN, trumpWifes),
            new Potus("BaracK1", "Obama", 2012, DEMOCRATIC),
            new Potus("BaracK", "Obama", 2008, DEMOCRATIC),
            new Potus("Gerge W.1", "Bush", 2004, REPUBLICAN),
            new Potus("Gerge W.", "Bush", 2000, REPUBLICAN),
            new Potus("Bill1", "Clinton", 1996, DEMOCRATIC),
            new Potus("Bill", "Clinton", 1992, DEMOCRATIC),
            new Potus("Gerge H. W.", "Bush", 1998, REPUBLICAN),
            new Potus("Ronald1", "Reagan", 1984, REPUBLICAN),
            new Potus("Ronald", "Reagan", 1970, DEMOCRATIC),
            new Potus("Jimmy", "Carter", 1976, DEMOCRATIC));



    @Test
    public void test_filter_map_distinct_limit_collect() {
        List<String> filteredNames = potuses.stream() // get stream
                .filter(potus -> potus.getElectionYear() < 2000) // filter by year
                .map(Potus::getLastName) // get stream with only names
                .distinct() // filter duplicates
                .limit(3) // get only 3 elements
                .collect(Collectors.toList()); // collect ot list of Strings
    }

    @Test
    public void test_flatmap() {
        //The difference is that the map operation produces one output value for each input value, whereas the flatMap operation produces an arbitrary number (zero or more) values for each input value.
        Potus trump = potuses.get(0);
        List<List<Child>> kidsViaMap = trump.getWives().stream().map(wife -> wife.getChildren()).collect(Collectors.toList()); // not convenient list of lists

        List<Child> kidsViaLoop = new ArrayList<>();
        trump.getWives().forEach(w -> kidsViaLoop.addAll(w.getChildren())); // working example but we need one more loop to store data

        List<Child> kidsViaFlatMap = trump.getWives().stream().flatMap(wife -> wife.getChildren().stream()).collect(Collectors.toList()); // good solution


        // one more flat map example
        List<Integer> integers = Arrays.asList(6, 12, 18);
        List<Integer> flatted = integers.stream().flatMap(i -> Stream.of(i / 2, i / 3)).collect(Collectors.toList());
        flatted.forEach(LOGGER::debug);

//        Map<String, List<Child>> presidentChild = potuses.stream().distinct()
//                .collect(Collectors.
//                        toMap(Potus::getFirstName, potus -> potus.getWives().stream().flatMap(wife -> wife.getChildren().stream()).collect(Collectors.toList())));

        Map<String, List<String>> presidentChild = potuses.stream().distinct()
                .collect(Collectors.
                        toMap(Potus::getFirstName, potus -> potus.getWives().stream().flatMap(wife -> wife.getChildren().stream().map(child -> child.getName())).collect(Collectors.toList())));

        presidentChild.forEach((key, value) -> {
            LOGGER.debug(key + " " + value);
        });

        // presidentChild.


    }


}