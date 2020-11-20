package org.example;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ForeachVSPeek extends TestCase {

    private static Logger LOGGER = Logger.getLogger(ForeachVSPeek.class.getName());


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
}
