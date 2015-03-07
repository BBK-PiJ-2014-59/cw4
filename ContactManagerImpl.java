import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.Gson;

public class ContactManagerImpl implements ContactManager { 

  private static final String textfile = "contacts.txt";
  private static final int FIRSTCONTACTID = 100;
  private int nextContactId = FIRSTCONTACTID;
  private Set<Contact> contacts = new HashSet<Contact>();
  //private List<Contact> contacts = new ArrayList<Contact>();

  public ContactManagerImpl() {}

  public ContactManagerImpl(String textfile) {
    contacts = readInTextfile(textfile);
  }

  private Set<Contact> readInTextfile(String filename) { 
    Gson gson = new Gson();
    Set<Contact> result = null;
    try {
      BufferedReader br = new BufferedReader(
      new FileReader(filename));
      result = gson.fromJson(br, HashSet.class);
      //System.out.println("FUNKAYYYY" + result);
    } catch (IOException e) {
      e.printStackTrace();
    } 
    return result;
  }

  public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
    return 0;
	}

  public PastMeeting getPastMeeting(int id) {
    return null;
	}

  public FutureMeeting getFutureMeeting(int id) {
    return null;
	}

  public Meeting getMeeting(int id) {
    return null;
	}

  public List<Meeting> getFutureMeetingList(Contact contact) {
    return null;
	}

  public List<Meeting> getFutureMeetingList(Calendar date) {
    return null;
	}

  public List<PastMeeting> getPastMeetingList(Contact contact) {
    return null;
	}

  public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
	}

  public void addMeetingNotes(int id, String text) {
	}

  public void addNewContact(String name, String notes) {
    System.out.println("yoda");
    contacts.add(new ContactImpl(name, notes, nextContactId++));
	}

  public Set<Contact> getContacts(int... ids) {
    return null;
	}

  public Set<Contact> getContacts(String name) {
    //return null;
    //return contacts;
    Set<Contact> result = new HashSet<Contact>();
    Iterator<Contact> i = contacts.iterator();
    Contact c = null;
    while (i.hasNext()) { 
      c = i.next();
      if (c.getName().contains(name)) 
        result.add(c);
    }
    return result;
	}

  public void flush() {
    Gson gson = new Gson();
    String json = gson.toJson(contacts);
    try {
      FileWriter writer = new FileWriter("contacts.txt");
      writer.write(json);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(json);
	}
}
