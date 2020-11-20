package org.example;

import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class AdvancedCollectors extends TestCase {
    private static Logger LOGGER = Logger.getLogger(AdvancedCollectors.class.getName());

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

    public void test_collectors() {
        // 1 Collectors.joining() will insert the delimiter between the two String elements of the stream.
        String names = potuses.stream().map(Potus::getFirstName).collect(Collectors.joining(", "));
        LOGGER.debug("Collectors.joining()===========\n" + names);

        // 2  toSet() to get a set out of stream elements
        Set<String> lastNames = potuses.stream().map(Potus::getLastName).collect(Collectors.toSet());
        LOGGER.debug("Collectors.toSet()================\n" + lastNames);

        // 3 We can use Collectors.toCollection() to extract the elements into any other collection
        Set<Child> children = potuses.stream().flatMap(potus -> potus.getWives().stream()).flatMap(wife -> wife.getChildren().stream()).collect(Collectors.toCollection(HashSet::new));
        LOGGER.debug("Collectors.toCollection=================\n");
        children.forEach(LOGGER::debug);

        // We can partition a stream into two – based on whether the elements satisfy certain criteria or not.
        Map<Boolean, List<Potus>> oldNewPotuses = potuses.stream().collect(Collectors.partitioningBy(potus -> potus.getElectionYear() > 2000));
        LOGGER.debug("Collectors.partitioningBy=====================\n");
        oldNewPotuses.forEach((key, value) -> LOGGER.debug("key = " + key + " value = " + value));

        // groupingBy() offers advanced partitioning – where we can partition the stream into more than just two groups.
        Map<String, List<Potus>> partyPotusMapping = potuses.stream().collect(Collectors.groupingBy(p -> p.getParty()));
        LOGGER.debug("Collectors.groupingBy=================\n");
        // there are to keys REPUBLICAN and DEMOCRATIC, all potuses are grouped according to this value
        partyPotusMapping.forEach((key, value) -> LOGGER.debug("key = " + key + " value = " + value));

        //we can use mapping() which can actually adapt the collector to a different type – using a mapping function:
        //Here mapping() maps the stream element Potus into just the potus name
        LOGGER.debug("Collectors.groupingBy with Collectors.mapping =================\n");
        Map<String, List<String>> partyPotusToPotusName = potuses.stream().collect(Collectors.groupingBy(p -> p.getParty(), Collectors.mapping(potus -> potus.getFirstName(), Collectors.toList())));
        partyPotusToPotusName.forEach((key, value) -> LOGGER.debug("key = " + key + " value = " + value));
    }
}
