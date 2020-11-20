package org.example;

import java.util.Collections;
import java.util.List;

public class Potus {
    private String firstName;
    private String lastName;
    private int electionYear;
    private String party;
    private List<Wife> wives;

    public Potus() {

    }

    public Potus(String firstName, String lastName, int electionYear, String party, List<Wife> wives) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.electionYear = electionYear;
        this.party = party;
        this.wives = wives;
    }

    public Potus(String firstName, String lastName, int electionYear, String party) {
        this(firstName, lastName, electionYear, party, Collections.EMPTY_LIST);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getElectionYear() {
        return electionYear;
    }

    public void setElectionYear(int electionYear) {
        this.electionYear = electionYear;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public List<Wife> getWives() {
        return wives;
    }

    public void setWives(List<Wife> wives) {
        this.wives = wives;
    }

    @Override
    public String toString() {
        return "Potus{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", electionYear=" + electionYear +
                ", party='" + party + '\'' +
                ", wives=" + wives +
                '}';
    }
}
