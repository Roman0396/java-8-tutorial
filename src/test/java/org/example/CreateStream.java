package org.example;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CreateStream extends TestCase {


    @Test
    public void test_create_stream() {
        // from an existing array
        Potus[] potuses = {
                new Potus("first", "last", 2000, "party")
        };
        Stream.of(potuses);

        //from an existing list:
        List<Potus> potusList = Arrays.asList(potuses);
        potusList.stream();

        //from individual objects
        Stream.of(potuses[0]);

        //using Stream.builder():
        Stream.Builder<Potus> potusStreamBuilder = Stream.builder();
        potusStreamBuilder.accept(potuses[0]);
        Stream<Potus> potusStream = potusStreamBuilder.build();
    }

}
