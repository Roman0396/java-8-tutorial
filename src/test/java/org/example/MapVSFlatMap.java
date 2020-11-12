package org.example;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Both map and flatMap can be applied to a Stream<T> and they both return a Stream<R>. The difference is that the map operation produces one output value for each input value, whereas the flatMap operation produces an arbitrary number (zero or more) values for each input value.
 * <p>
 * This is reflected in the arguments to each operation.
 * <p>
 * The map operation takes a Function, which is called for each value in the input stream and produces one result value, which is sent to the output stream.
 * <p>
 * The flatMap operation takes a function that conceptually wants to consume one value and produce an arbitrary number of values.
 */
public class MapVSFlatMap extends TestCase {
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
    // create 2 integers from the original one
    public void testSimpleFlatMap() {
        List<Integer> integers = Arrays.asList(6, 12, 18);
        List<Integer> devidedIntegers = integers.stream().flatMap(i -> Stream.of(i / 2, i / 3)).collect(Collectors.toList());
        devidedIntegers.forEach(LOGGER::debug);
    }

    @Test
    // device each value for 2
    public void testSimpleMap() {
        List<Integer> integers = Arrays.asList(6, 12, 18);
        List<Integer> devidedIntegers = integers.stream().map(i -> i / 2).collect(Collectors.toList());
        devidedIntegers.forEach(LOGGER::debug);
    }

    @Test
    public void test_flatmap() {
        Potus trump = potuses.get(0);
        LOGGER.debug("children list of lists==============================");
        List<List<Child>> kidsViaMap = trump.getWives().stream().map(wife -> wife.getChildren()).collect(Collectors.toList()); // not convenient list of lists
        kidsViaMap.forEach(LOGGER::debug);

        LOGGER.debug("children list via loop==============================");
        List<Child> kidsViaLoop = new ArrayList<>();
        trump.getWives().forEach(w -> kidsViaLoop.addAll(w.getChildren())); // working example but we need one more loop to store data
        kidsViaLoop.forEach(LOGGER::debug);

        LOGGER.debug("children list via flatmap==============================");
        List<Child> kidsViaFlatMap = trump.getWives().stream().flatMap(wife -> wife.getChildren().stream()).collect(Collectors.toList()); // good solution
        kidsViaFlatMap.forEach(LOGGER::debug);

        LOGGER.debug("president name -> children list==============================");
        Map<String, List<Child>> presidentChild = potuses.stream().distinct()
                .collect(Collectors.
                        toMap(Potus::getFirstName, potus -> potus.getWives().stream().flatMap(wife -> wife.getChildren().stream()).collect(Collectors.toList())));

        presidentChild.forEach((key, value) -> {
            LOGGER.debug(key + " " + value);
        });

        LOGGER.debug("president name -> child names==============================");
        Map<String, List<String>> presidentChildName = potuses.stream().distinct()
                .collect(Collectors.
                        toMap(
                                Potus::getFirstName,
                                potus -> potus.getWives().stream() // stream of wives
                                        .flatMap(wife -> wife.getChildren().stream().map(child -> child.getName())).collect(Collectors.toList())));

        presidentChildName.forEach((key, value) -> {
            LOGGER.debug(key + " " + value);
        });


    }

}
