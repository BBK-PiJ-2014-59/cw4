import org.junit.*;
import static org.junit.Assert.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;


public class ContactManagerTests { 

  private final String textfile = "contacts.txt";
  private final int firstContactId = 100;

  private final String name1 = "name1";
  private final String name2 = "name2";
  private final String name3 = "name2x";
  private String notes;
  private Contact myContact1;
  private Contact myContact2;
  private ContactManager myCm;

  @Before 
  public void buildUp() {
    notes = "notes";
    myContact1 = new ContactImpl(name1, notes, 100);
    int id1 = myContact1.getId();
    System.out.println("id1: " + id1);
    myContact2 = new ContactImpl(name2, notes, 101);
    myCm = new ContactManagerImpl();
  }

  // Contact tests

  // BAD TEST: Since moving control of contact ID uniqueness inside ContactManagerImpl, so then hardcoding id values into myContact1 and myContact2, TEST 1 now tests nothing!
  @Test 
  public void contact_GetId_ReturnsUniqueIDs() {
    System.out.println("TEST 1");
    assertTrue(myContact1.getId() != myContact2.getId());
  }

  @Test
  public void contact_GetName_ReturnsName() {
    System.out.println("TEST 2");
    assertEquals(name1, myContact1.getName());
  }

  @Test
  public void contact_GetNotes_ReturnsNotes() {
    System.out.println("TEST 3");
    myContact1.addNotes(notes);
    assertEquals(notes, myContact1.getNotes());
  }

  // ContactManager tests related to Contact

  @Test
  public void addOneContact_searchByName_foundContact() {
    System.out.println("TEST 4");
    //ContactManager myCm = new ContactManagerImpl();
    myCm.addNewContact(name1, notes); 
    Set<Contact> s = myCm.getContacts(name1);   
    Iterator<Contact> i = s.iterator();
    Contact c = null;
    if (i.hasNext())
      c = i.next(); 
    assertEquals(name1, c.getName()); 
    assertEquals(firstContactId, c.getId()); 
  }
  
  @Test
  public void addOneContact_searchByName_contactNotFound() {
    System.out.println("TEST 4.1");
    myCm.addNewContact(name1, notes); 
    Set<Contact> s = myCm.getContacts(name2);   
    assertEquals(1, myCm.getContacts("").size()); 
    assertTrue(s.isEmpty());
  }

  @Test
  public void addThreeContacts_searchByName_getTwoBack() {
    System.out.println("TEST 4.2");
    myCm.addNewContact(name1, notes); 
    myCm.addNewContact(name2, notes); 
    myCm.addNewContact(name3, notes); 
    Set<Contact> s = myCm.getContacts(name2);   
    Iterator<Contact> i = s.iterator();
    Contact c = null;
    int count = 0;
    while (i.hasNext()) { 
      c = i.next(); 
      if (c.getName().contains(name2)) 
        ++count;
    }
    assertEquals(3, myCm.getContacts("").size()); 
    assertEquals(2, count); 
  }

  @Test
  public void addThreeContacts_searchByName_getAllBack() {
    System.out.println("TEST 4.3");
    myCm.addNewContact(name1, notes); 
    myCm.addNewContact(name2, notes); 
    myCm.addNewContact(name3, notes); 
    assertEquals(3, myCm.getContacts("").size()); 
  }

  @Test (expected=NullPointerException.class)
  public void addNewContact_nullName() {
    System.out.println("TEST 5");
    myCm.addNewContact(null, notes); 
  }

  @Test (expected=NullPointerException.class)
  public void addNewContact_nullNotes() {
    System.out.println("TEST 6");
    myCm.addNewContact(notes, null); 
  }

  @Test
  public void getSameContactsBackAfterFlush() {
    System.out.println("TEST 7");
    myCm.addNewContact(name1, notes); 
    myCm.addNewContact(name2, notes); 
    myCm.addNewContact(name3, notes); 
    Set<String> namesBefore = new HashSet<String>(Arrays.asList(name1,name2,name3));
    Set<Integer> idsBefore = new HashSet<Integer>(Arrays.asList(firstContactId,firstContactId+1,firstContactId+2));
    myCm.flush();
    ContactManager myCm2 = new ContactManagerImpl(textfile);
    // check names, notes, IDs
    Set<Contact> s = myCm.getContacts("");   
    Iterator<Contact> i = s.iterator();
    Set<String> namesAfter = new HashSet<String>();
    Set<Integer> idsAfter = new HashSet<Integer>();
    Contact c = null;
    while (i.hasNext()) { 
      c = i.next();
      namesAfter.add(c.getName()); 
      idsAfter.add(c.getId()); 
    }
    assertTrue(namesAfter.equals(namesBefore)); 
    assertTrue(idsAfter.equals(idsBefore)); 
  }
}
