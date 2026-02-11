package main;

/**
 * Represents a person's name consisting of first and last names.
 */
public class Name {

    private String firstName;
    private String lastName;

    /**
     * Constructs a Name object.
     *
     * @param firstName the first name
     * @param lastName  the last name
     */
    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Returns the first name.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     * 
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name.
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     * 
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the initials of the name.
     *
     * @return initials in upperCase
     */
    public String getInitials() {
        return ("" + firstName.charAt(0) + lastName.charAt(0)).toUpperCase();
    }

    /**
     * Returns the full name as a string.
     *
     * @return full name
     */
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
