import org.junit.*;
import static org.junit.Assert.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;


public class ContactManagerTests { 

  private final int firstContactId = 100;

  private final String name = "name";
  private Contact myContact1;
  private Contact myContact2;
  private String notes;
  private ContactManager myCm;

  @Before 
  public void buildUp() {
    notes = "notes";
    myContact1 = new ContactImpl(name, notes, 100);
    int id1 = myContact1.getId();
    System.out.println("id1: " + id1);
    myContact2 = new ContactImpl(name, notes, 101);
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
    assertEquals(name, myContact1.getName());
  }

  @Test
  public void contact_GetNotes_ReturnsNotes() {
    System.out.println("TEST 3");
    myContact1.addNotes(notes);
    assertEquals(notes, myContact1.getNotes());
  }

  // ContactManager tests related to Contact

  @Test
  public void contactManager_addNewContact_oneContactWithValidNameAndNotes_searchByName() {
    System.out.println("TEST 4");
    //ContactManager myCm = new ContactManagerImpl();
    myCm.addNewContact(name, notes); 
    Set<Contact> s = myCm.getContacts(name);   
    Iterator<Contact> i = s.iterator();
    Contact c = null;
    if (i.hasNext())
      c = i.next(); 
    assertEquals(name, c.getName()); 
    assertEquals(firstContactId, c.getId()); 
  }
  
  @Test (expected=NullPointerException.class)
  public void contactManager_addNewContact_nullName() {
    System.out.println("TEST 5");
    myCm.addNewContact(null, notes); 
  }

  @Test (expected=NullPointerException.class)
  public void contactManager_addNewContact_nullNotes() {
    System.out.println("TEST 6");
    myCm.addNewContact(notes, null); 
  }

}
