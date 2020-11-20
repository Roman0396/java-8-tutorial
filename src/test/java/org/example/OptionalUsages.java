package org.example;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Advantages of Java 8 Optional:
 * <p>
 * Null checks are not required.
 * No more NullPointerException at run-time.
 * We can develop clean and neat APIs.
 * No more Boiler plate code
 *
 * USE OR NOT OPTIONAL
 * Optional is not Serializable. For that reason, it’s not intended to be used as a field in a class.
 * Another situation when it’s not very helpful to use the type is as a parameter for methods or constructors. This would lead to code that is unnecessarily complicated
 * The intended use of Optional is mainly as a return type. After obtaining an instance of this type, you can extract the value if it’s present or provide an alternate behavior if it’s not.
 */
public class OptionalUsages extends TestCase {
    private static Logger LOGGER = Logger.getLogger(OptionalUsages.class.getName());

    @Test //Creating Optional Instances
    public void test_optional_of_vs_ofNullable() {
        //To create an Optional object that can contain a value – you can use the of() and ofNullable() methods.
        // The difference between the two is that the of() method will throw a NullPointerException if you pass it a null value as an argument
        //you should only use of() when you are sure the object is not null.
        //If the object can be both null or not-null, then you should instead choose the ofNullable() method
        Optional<String> gender = Optional.ofNullable("Male");
        LOGGER.debug(gender);
        Optional<String> gender1 = Optional.of("Male");
        LOGGER.debug(gender1);
        Optional<String> gender3 = Optional.ofNullable(null);
        LOGGER.debug(gender3);
        Optional<String> gender2 = Optional.of(null);
    }

    @Test
    public void test_get_optional() {
        // One way to retrieve the actual object inside the Optional instance is to use the get() method:
        Optional<String> gender = Optional.ofNullable("male");
        if (gender.isPresent()) {
            String realGender = gender.get();
            LOGGER.debug(realGender);
        } else {
            LOGGER.warn("There is no value");
        }

        // the action value -> LOGGER.debug("The value present = " + value) will be executed only in case gender is not empty. It does nothing if the Optional is empty.
        gender.ifPresent(value -> LOGGER.debug("The value present = " + value));
    }


    @Test
    public void test_get_orElse_VS_orElseGet() {
        // orElse it returns the value if it’s present, or the argument it receives if not
        String value = "famale"; //  if it is null, mae value will be returned
        String gender = Optional.ofNullable(value).orElse("male");

        // one more example
        Optional<Potus> optionalPotus = Optional.ofNullable(new Potus("BaracK", "Obama", 2008, "democratic"));

        // in any case even if there is a value in optinalPotus the method 'orElse' will execute the function 'createPotus()'
        Potus realPotus = optionalPotus.orElse(createPotus());

        // the 'orElseGet' method will not execute the 'createPotus' method if the value presents
        Potus realPotus2 = optionalPotus.orElseGet(() -> createPotus());

    }


    @Test
    public void test_optional_return_exception() {
        Optional<Potus> optionalPotus = Optional.ofNullable(null);
        Potus potus = optionalPotus.orElseThrow(() -> new IllegalArgumentException("there is not potus"));
    }


    @Test
    public void test_optional_map() {
        List<Wife> trumpWifes = Arrays.asList(
                new Wife("Melania", Arrays.asList(new Child("Barron", 12))),
                new Wife("Marla", Arrays.asList(new Child("Tiffany", 24))),
                new Wife("Ivana", Arrays.asList(
                        new Child("Donald Jr.", 40),
                        new Child("Ivanka", 36),
                        new Child("Eric", 34))));


        //Transforming Values
        Potus original = new Potus("Donald", "Trump", 2016, "republican", trumpWifes);
        Optional<Potus> optionalPotus = Optional.ofNullable(original);

        //map() applies the Function argument to the value, then returns the result wrapped in an Optional
        String potusName = optionalPotus.map(Potus::getFirstName).orElse("emptyValue");
        LOGGER.debug(potusName);

        String potusWifeName = optionalPotus.map(potus -> potus.getWives()) // Optional<List<Wife>>
                .map(list -> list.get(0))
//                .flatMap(list -> getWifeByIndex(0, list)) // optional<wife> the same as line above
                .map(Wife::getName) // optional<String>
                .orElse("there is not name");
        LOGGER.debug(potusWifeName);

        String potusWifeNameOldStyle = null;
        if (original != null) {
            if (original.getWives() != null) {
                List<Wife> wives = original.getWives();
                if (wives.get(0) != null) {
                    Wife wife = wives.get(0);
                    if (wife.getName() != null) {
                        potusWifeNameOldStyle = wife.getName();
                    }
                }
            }
        }
        LOGGER.debug(potusWifeNameOldStyle.toString());

    }

    //Use map if the function returns the object you need or flatMap if the function returns an Optional.
    @Test
    public void test_optional_map_vs_flatmap() {
        // So, flatmap is used just to avoid Optional<Optional<String>>
        Optional<String> opt = Optional.ofNullable("String");
        Optional<Optional<String>> s1 = opt.map(this::toUpperCase);
        Optional<String> s2 = opt.flatMap(this::toUpperCase);
    }

    @Test
    public void test_optional_filter() {
        //The filter() method takes a Predicate as an argument and returns the value as it is if the test evaluates to true. Otherwise, if the test is false, the returned value is an empty Optional.
        Potus potus = new Potus("Donald", "Trump", 2016, "republican");
        Optional<Potus> optionalPotus = Optional.ofNullable(potus).filter(potus1 -> potus.getElectionYear() > 2020);
        LOGGER.debug(optionalPotus);
    }

    private Optional<Wife> getWifeByIndex(int i, List<Wife> wives) {
        return Optional.ofNullable(wives.get(i));
    }

    private Optional<String> toUpperCase(String str) {
        return Optional.ofNullable(str.toUpperCase());
    }

    private Potus createPotus() {
        LOGGER.debug("creating an object");
        return new Potus("Donald", "Trump", 2016, "REPUBLICAN");
    }
}
