import org.junit.*;
import static org.junit.Assert.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;



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
  private ContactManagerUI myCmui;

  private static void deleteFile(String filename) { 
    try { 
      File file = new File(filename); 
      if (file.exists())
        file.delete();
    } catch (Exception e) { 
      e.printStackTrace();
    }
  }

  private static void renameFile(String oldName, String suffix) { 
    String newName="" + oldName + "." + suffix;
    File file1 = new File(oldName); 
    File file2 = new File(newName); 
    if (file1.exists())
      file1.renameTo(file2);
  }

  private static void copyFile(String oldName, String suffix) { 
    String newName="" + oldName + "." + suffix;
    File file1 = new File(oldName); 
    File file2 = new File(newName); 
    //if (file1.exists() && !file2.exists())
    if (file1.exists())
      try { 
        System.out.println("COPYING");
        Files.copy(file1.toPath(),file2.toPath());
      } catch (Exception e) { 
        e.printStackTrace();
      }
  }

  @Before 
  public void buildUp() {
    ContactManagerTests.deleteFile(textfile);
    notes = "notes";
    myContact1 = new ContactImpl(name1, notes, 100);
    int id1 = myContact1.getId();
    myContact2 = new ContactImpl(name2, notes, 101);
    myCm = new ContactManagerImpl();
    myCmui = new ContactManagerUIImpl();
  }

  // Contact tests

  // BAD TEST: Since moving control of contact ID uniqueness inside ContactManagerImpl, so then hardcoding id values into myContact1 and myContact2, TEST 1 now tests nothing!
  @Test 
  public void contact_GetId_ReturnsUniqueIDs() {
    String label = "TEST_1";
		System.out.println(label);
    assertTrue(myContact1.getId() != myContact2.getId());
  }

  @Test
  public void contact_GetName_ReturnsName() {
    String label = "TEST_2";
		System.out.println(label);
    assertEquals(name1, myContact1.getName());
  }

  @Test
  public void contact_GetNotes_ReturnsNotes() {
    String label = "TEST_3";
		System.out.println(label);
    myContact1.addNotes(notes);
    assertEquals(notes, myContact1.getNotes());
  }

  @Test
  public void addOneContact_searchByName_foundContact() {
    String label = "TEST_4";
		System.out.println(label);
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
    String label = "TEST_4.1";
		System.out.println(label);
    myCm.addNewContact(name1, notes); 
    Set<Contact> s = myCm.getContacts(name2);   
    assertEquals(1, myCm.getContacts("").size()); 
    assertTrue(s.isEmpty());
  }

  @Test
  public void addThreeContacts_searchByName_getTwoBack() {
    String label = "TEST_4.2";
		System.out.println(label);
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
    String label = "TEST_4.3";
		System.out.println(label);
    myCm.addNewContact(name1, notes); 
    myCm.addNewContact(name2, notes); 
    myCm.addNewContact(name3, notes); 
    assertEquals(3, myCm.getContacts("").size()); 
  }

  @Test (expected=NullPointerException.class)
  public void addNewContact_nullName() {
    String label = "TEST_5";
		System.out.println(label);
    myCm.addNewContact(null, notes); 
  }

  @Test (expected=NullPointerException.class)
  public void addNewContact_nullNotes() {
    String label = "TEST_6";
		System.out.println(label);
    myCm.addNewContact(notes, null); 
  }

  @Test
  public void getSameContactsBackAfterFlush() {
    String label = "TEST_7";
		System.out.println(label);
    String testfile = "" + textfile + "." + label;
    ContactManagerTests.deleteFile(testfile);
    ContactManagerUI ui = new ContactManagerUIImpl(testfile);
    ContactManager cm = ui.launch();
    cm.addNewContact(name1, notes); 
    cm.addNewContact(name2, notes); 
    cm.addNewContact(name3, notes); 
    Set<String> namesBefore = new HashSet<String>(Arrays.asList(name1,name2,name3));
    Set<Integer> idsBefore = new HashSet<Integer>(Arrays.asList(firstContactId,firstContactId+1,firstContactId+2));
    ContactManagerTests.copyFile(testfile,"save");
    cm.flush();
    ContactManager cm2 = new ContactManagerImpl(testfile);
    // check names, notes, IDs
    Set<Contact> s = cm.getContacts("");   
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

  @Test
  public void flushAfterStartingWithMissingTestfileCreatesIt() { 
    String label = "TEST_7.01";
		System.out.println(label);
    String testfile = "" + textfile + "." + label;
    ContactManagerTests.deleteFile(testfile);
    ContactManagerUI ui = new ContactManagerUIImpl(testfile);
    ContactManager cm = ui.launch();
    cm.addNewContact(name1, notes); 
    cm.flush();
    assertTrue(new File(testfile).exists());
  }

  @Test
  public void nextContactIdIsIncrementedIfAddedAfterFlush() {
    String label = "TEST_7.1";
		System.out.println(label);
    String testfile = "" + textfile + "." + label;
    ContactManagerTests.deleteFile(testfile);
    ContactManagerUI ui = new ContactManagerUIImpl(testfile);
    ContactManager cm = ui.launch();
    cm.addNewContact(name1, notes); 
    cm.addNewContact(name2, notes); 
    Set<String> namesBefore = new HashSet<String>(Arrays.asList(name1,name2));
    Set<Integer> idsBefore = new HashSet<Integer>(Arrays.asList(firstContactId,firstContactId+1));
    cm.flush();
    ContactManagerTests.copyFile(testfile,"save");
    ContactManager cm2 = new ContactManagerImpl(testfile);
    // check names, notes, IDs
    Set<Contact> s = cm.getContacts("");   
    Iterator<Contact> i = s.iterator();
    Set<Integer> idsAfter = new HashSet<Integer>();
    Contact c = null;
    while (i.hasNext()) { 
      c = i.next();
      idsAfter.add(c.getId()); 
    }
    cm2.flush();
    assertTrue(idsAfter.equals(idsBefore)); 
  }

  @Test (expected=NullPointerException.class) 
  public void ui_nullContactManagerToDisplay() {
    String label = "TEST_9";
		System.out.println(label);
    myCmui.display(null);
  }

  @Test (expected=NullPointerException.class) 
  public void ui_nullPromptToPrompt() {
    String label = "TEST_10";
		System.out.println(label);
    myCmui.promptString(null);
  }

  @Test 
  public void ui_promptStringStripsWhitespace() {
    String label = "TEST_11";
		System.out.println(label);
    String stripped = "abc def";
    String tooSpacey = "   abc     def        ";
    ByteArrayInputStream in = new ByteArrayInputStream(tooSpacey.getBytes());
    System.setIn(in);
    String out = myCmui.promptString("prompt");
    System.setIn(System.in);
    assertEquals(stripped,out);
  }

  @Test (expected=NullPointerException.class) 
  public void ui_nullPromptToPromptInt() {
    String label = "TEST_12";
		System.out.println(label);
    myCmui.promptInt(null);
  }

  @Test 
  public void ui_promptIntReturnsInt() {
    String label = "TEST_13";
		System.out.println(label);
    String fiftySix = "56";
    int myInt = 56;
    ByteArrayInputStream in = new ByteArrayInputStream(fiftySix.getBytes());
    System.setIn(in);
    int out = myCmui.promptInt("prompt");
    System.setIn(System.in);
    assertEquals(myInt,out);
  }

  @Test 
  public void ui_InvalidResponseToPromptInt() {
    String label = "TEST_15";
		System.out.println(label);
    String invalid = "invalid";
    ByteArrayInputStream in = new ByteArrayInputStream(invalid.getBytes());
    System.setIn(in);
    int out = myCmui.promptInt("prompt");
    System.setIn(System.in);
    assertEquals(-1,out);
  }

}
