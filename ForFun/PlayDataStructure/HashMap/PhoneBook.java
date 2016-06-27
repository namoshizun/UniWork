package HashMap;

public class PhoneBook {
	/**
	 * Creates a new phonebook
	 */

	SimpleHashMap phoneBook;
	LinearHashMapEntry linearBook;

	public PhoneBook(int size) {
		
			this.phoneBook = new SimpleHashMap(size);
			
	}

	/**
	 * Saves the given person's name and phone number
	 */
	public void addNumber(String name, Integer phoneNumber) {

		if (phoneBook.get(name) == null) {

			phoneBook.put(name, phoneNumber);

		} else {

			phoneBook.remove(name);
			phoneBook.put(name, phoneNumber);
		}
	}

	/**
	 * Gets the phone number for the person with the given name Returns null if
	 * no person exists with that name
	 */
	public Integer getNumber(String name) {

		return (Integer) phoneBook.get(name);
	}

	/**
	 * Deletes the contact with the given name from the book
	 */
	public void deleteContact(String name) {

		phoneBook.remove(name);

	}

	public static void main(String[] args) {

		PhoneBook tester = new PhoneBook(2);
		tester.addNumber("Yo", 123);
		tester.addNumber("yay", 231);
		tester.addNumber("why", 321);

		System.out.println(tester.getNumber("why"));
		System.out.println(tester.phoneBook.getItems().length);

	}
}