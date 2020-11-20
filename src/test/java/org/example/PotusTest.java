package org.example;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
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
    public void test_foreach() {
        // foreach is terminal operation, calling the supplied function on each element.\
        List<Potus> potuseslocal = new ArrayList<>(potuses);
        potuseslocal.stream().forEach(potus -> potus.setFirstName(potus.getFirstName() + "changed"));
        potuseslocal.forEach(LOGGER::debug);

        LOGGER.debug("Start wiht B letter ===============");
        List<Potus> list = new ArrayList<>();
        potuseslocal.stream().forEach(potus -> {
            if (potus.getFirstName().startsWith("B")) {
                list.add(potus);
            }
        });
        list.forEach(LOGGER::debug);

        LOGGER.debug("Correct implementation");
        List<Potus> list2 = new ArrayList<>();
        list2 = potuseslocal.stream().filter(potus -> potus.getFirstName().startsWith("B")).collect(Collectors.toList());
        list2.forEach(LOGGER::debug);
    }


    @Test
    public void test_foreach_VS_peek() {
        List<Potus> potusToTestForeach = new ArrayList<>(potuses);
        // foreach is terminal operation, calling the supplied function on each element.
        potusToTestForeach.stream().forEach(potus -> potus.setFirstName(potus.getFirstName() + "changed"));
        LOGGER.debug("original list after foreach");
        potusToTestForeach.forEach(LOGGER::debug);
        // RESULT potusToTestForeach objects were really changed in the original list

        // PEEK it performs the specified operation on each element of the stream and returns a new stream which can be used further. peek() is an intermediate operation:
        // This method exists mainly to support debugging, where you want to see the elements as they flow past a certain point in a pipeline
        //can be useful in another scenario: when we want to alter the inner state of an element
        List<Potus> potusToTestPeek = new ArrayList<>(potuses);
        List<Potus> potusPeekResult = potusToTestPeek.stream()
                .peek(potus -> potus.setFirstName(potus.getFirstName() + "peek")) // not terminate operation
                .filter(potus -> potus.getFirstName().startsWith("B")) // can do smth more
                .collect(Collectors.toList()); // terminate action

        LOGGER.debug("peek result");
        potusPeekResult.forEach(LOGGER::debug);
    }

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
    public void test_sort() {
        List<Child> children = trumpWifes.stream().flatMap(wife -> wife.getChildren().stream()).sorted(Comparator.comparing(Child::getAge).reversed()).collect(Collectors.toList());
        children.forEach(LOGGER::debug);
    }

    @Test
    public void test_list_to_map() {
        Map<Integer, Child> ageToChild = trumpWifes.stream() // stream of wives
                .flatMap(wife -> wife.getChildren().stream()) // stream of children
                .collect(Collectors.toMap(Child::getAge, Function.identity())); // Function.identity() - puts the child object as a value

        ageToChild.forEach((key, value) -> {
            LOGGER.debug(key + " " + value);
        });
    }


    @Test
    public void test_find_first_matching() {
        Predicate<Wife> predicate = wife -> wife.getChildren().size() > 2;
        Optional<Wife> wife = trumpWifes.stream().filter(predicate).findFirst();
        LOGGER.debug(wife);

        boolean isAllWivesHaveMoreThan2Children = trumpWifes.stream().allMatch(predicate);
        LOGGER.debug("All wives have more that 2 children =>" + isAllWivesHaveMoreThan2Children);

        boolean isAtLeastOneWifeWithMoreThan2Children = trumpWifes.stream().anyMatch(predicate);
        LOGGER.debug("At least one wife has more that 2 children =>" + isAtLeastOneWifeWithMoreThan2Children);
    }

    //reduction stream operations allow us to produce one single result from a sequence of elements, by applying repeatedly a combining operation to the elements in the sequence.
    // A reduction operation (also called as fold) takes a sequence of input elements and combines them into a single summary result by repeated application of a combining operation
    @Test
    public void test_reduce() {
        int totalNumberOfChildren = trumpWifes.stream()
                .mapToInt(w1 -> w1.getChildren().size()) // int stream
                .reduce(0, (sum, i) -> sum + i); // sum all children
        LOGGER.debug(totalNumberOfChildren);
    }

    public void test_my_reduce() {

        BinaryOperator<Potus> binaryOperator = (p1, p2)
                -> p1.getElectionYear() < p2.getElectionYear() ? p1 : p2;

        Optional<Potus> potus = potuses.stream().reduce(binaryOperator);
        LOGGER.debug(potus.get());

        // the same action via sort
        Optional<Potus> potus1 = potuses.stream().sorted((p1, p2) -> p1.getElectionYear() < p2.getElectionYear() ? -1 : 1).findFirst();
        LOGGER.debug(potus1.get());
    }

    private class Sum {
        private int sumValue;

        public int getSumValue() {
            return sumValue;
        }

        public void setSumValue(int sumValue) {
            this.sumValue = sumValue;
        }
    }
}