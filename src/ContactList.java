import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactList implements Serializable {
    private List<Contact> contacts;
    private static final String FILE_NAME = "contacts.dat";

    public ContactList() {
        contacts = new ArrayList<>();
        loadContacts();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
        saveContacts();
    }

    public void deleteContact(String name) {
        contacts.removeIf(contact -> contact.getName().equalsIgnoreCase(name));
        saveContacts();
    }

    public void updateContact(String oldName, Contact newContact) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getName().equalsIgnoreCase(oldName)) {
                contacts.set(i, newContact);
                break;
            }
        }
        saveContacts();
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    private void saveContacts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(contacts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContacts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            contacts = (List<Contact>) ois.readObject();
        } catch (FileNotFoundException e) {
            // No existing file, ignore
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
