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
import java.io.File;
//import com.google.gson.Gson;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ContactManagerImpl implements ContactManager { 

  //private static final String textfile = "contacts.txt";
  private String textfile = "contacts.txt";
  private static final int FIRSTCONTACTID = 100;
  private int nextContactId = FIRSTCONTACTID;
  //private Set<Contact> contacts = new HashSet<Contact>();
  private Set<Contact> contacts;

  public ContactManagerImpl() {
    contacts = new HashSet<Contact>();
  }

/*
  public ContactManagerImpl() {
    //Set<Contact> contacts = new HashSet<Contact>();
    contacts = new HashSet<Contact>();
    Set<Contact> fileContacts = readInTextfile(textfile);
    if (fileContacts != null)
      contacts.addAll(fileContacts);
  }

/*
  public ContactManagerImpl(String filename) {
    textfile = filename;
    contacts = new HashSet<Contact>();
    Set<Contact> fileContacts = readInTextfile(textfile);
    if (fileContacts != null)
      contacts.addAll(fileContacts);
  }
*/

/*
  private Set<Contact> readInTextfile(String filename) { 
    System.out.println("READING IN " + filename);
    Gson gson = new Gson();
    Set<Contact> result = new HashSet<Contact>();;
    if (new File(filename).exists()) { 
      try {
        BufferedReader br = new BufferedReader(
        new FileReader(filename));
        result.addAll(gson.fromJson(br, HashSet.class));
      } catch (IOException e) {
        e.printStackTrace();
      } 
    }
    return result;
  }
*/

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
    contacts.add(new ContactImpl(name, notes, nextContactId++));
	}

  public Set<Contact> getContacts(int... ids) {
    //return null;
    Set<Contact> result = new HashSet<Contact>();
    for (int argId : ids) {
      boolean foundId = false;
      for (Contact c : contacts) { // TODO: O(n^2) ... need to redo.. memoize?
        if (c.getId() == argId) {
          result.add(c);
          foundId = true;
        }
      }
      if (foundId == false) {
        throw new IllegalArgumentException("Could not find requested ID " + argId);
      }
    }
    return result;

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
    // save the object to file
    FileOutputStream fos = null;
    ObjectOutputStream out = null;
    try {
      fos = new FileOutputStream(textfile);
      out = new ObjectOutputStream(fos);
      out.writeObject(this);

      out.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

/*
  public void flush() {
    Gson gson = new Gson();
    String jsonContacts = gson.toJson(contacts);
    try {
      //FileWriter writer = new FileWriter("contacts.txt");
      FileWriter writer = new FileWriter(textfile);
      writer.write(jsonContacts);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(jsonContacts);
	}
*/

}
