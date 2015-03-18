import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;





public class ContactManagerTests { 

  private final String textfile = "contacts.txt";
  private final int firstMtgId = 100;
  private final int badMtgId = firstMtgId-1;
  private final int mtgId1 = firstMtgId;
  private final int mtgId2 = firstMtgId+1;
  private final int mtgId3 = firstMtgId+2;
  private final int mtgId4 = firstMtgId+3;

  private final int firstContactId = 100;
  private final int badContactId = firstContactId-1;
  private final int contactId1 = firstContactId;
  private final int contactId2 = firstContactId+1;
  private final int contactId3 = firstContactId+2;
  private final int contactId4 = firstContactId+3;

  private Calendar futureDate1;
  private Calendar futureDate2;
  private Calendar futureDate3;
  private Calendar pastDate1;
  private Calendar pastDate2;
  private Calendar pastDate3;
  private Calendar futureDate1Copy1;
  private Calendar futureDate1Copy2;
  private Calendar futureDate1Plus1Hr;
  private Calendar futureDate1Plus2Hr;

  private final String name1 = "name1";
  private final String name2 = "name2";
  private final String name3 = "name2x";
  private String notes;
  private Contact myContact1;
  private Contact myContact2;
  private Contact badContact; 
  private ContactManager myCm;
  private ContactManagerUI myCmui;
  private ContactManagerUtil util;

/*
  private static addContacts(int loId, int hiId) {
    Set <Contact> testContactSet = new HashSet<Contact>();
    for (int id=loId; id <= hiId; id++) {
      String name = "name" + id;
      String notes = "notes" + id;
      testContactSet.add(new ContactImpl(name,notes,id));
    }
    for (Contact ci: testContactSet) {
      System.out.println(ci.getName());
    }
    return testContactSet;
  }
*/

  private static Set<Contact> populateTestContactSet(int loId, int hiId) {
    Set <Contact> testContactSet = new HashSet<Contact>();
    for (int id=loId; id <= hiId; id++) {
      String name = "name" + id;
      String notes = "notes" + id;
      testContactSet.add(new ContactImpl(name,notes,id));
    }
    for (Contact ci: testContactSet) {
      System.out.println(ci.getName());
    }
    return testContactSet;
  }

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
        Files.copy(file1.toPath(),file2.toPath(),StandardCopyOption.REPLACE_EXISTING);
      } catch (Exception e) { 
        e.printStackTrace();
      }
  }

  @Before
  public void makeTestObjects() {

    badContact = new ContactImpl("name", "notes", firstContactId-1); 

    int noon = 12; 

    futureDate1 = Calendar.getInstance();
    futureDate1.add(Calendar.MONTH, 1);
    futureDate1.add(Calendar.HOUR_OF_DAY, noon); // in case tests are run at midnight

    futureDate1Plus1Hr = (Calendar) futureDate1.clone();
    futureDate1Plus1Hr.add(Calendar.HOUR, 1);
    futureDate1Plus2Hr = (Calendar) futureDate1.clone();
    futureDate1Plus2Hr.add(Calendar.HOUR, 2);

    futureDate2 = Calendar.getInstance();
    futureDate2.add(Calendar.MONTH, 2);
    futureDate2.add(Calendar.HOUR_OF_DAY, noon); 

    futureDate3 = Calendar.getInstance();
    futureDate3.add(Calendar.MONTH, 3);
    futureDate3.add(Calendar.HOUR_OF_DAY, noon); 


    pastDate1 = Calendar.getInstance();
    pastDate1.add(Calendar.MONTH, -3);
    pastDate1.add(Calendar.HOUR_OF_DAY, noon); // in case tests are run at midnight

    pastDate2 = Calendar.getInstance();
    pastDate2.add(Calendar.MONTH, -2);
    pastDate2.add(Calendar.HOUR_OF_DAY, noon); 

    pastDate3 = Calendar.getInstance();
    pastDate3.add(Calendar.MONTH, -1);
    pastDate3.add(Calendar.HOUR_OF_DAY, noon); 
  }

  @Before 
  public void buildUp() {
    util = new ContactManagerUtilImpl();
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
    myCm.addNewContact(name1, null); 
  }

  @Test
  public void getSameNumberOfContactsBackAfterFlush() {
    String label = "TEST_6.5";
		System.out.println(label);
    ContactManagerTests.deleteFile(textfile);
    //ContactManagerUI ui = new ContactManagerUIImpl();
    ContactManager cm = myCmui.launch(); 
    cm.addNewContact(name1, notes); 
    cm.addNewContact(name2, notes); 
    cm.addNewContact(name3, notes); 
    Set<Contact> cmContacts = cm.getContacts(""); 
    cm.flush();
    //ContactManager cm2 = new ContactManagerUIImpl(textfile).launch();
    ContactManager cm2 = new ContactManagerUIImpl().launch();
    Set<Contact> cm2Contacts = cm2.getContacts(""); 
    assertEquals(cmContacts.size(), cm2Contacts.size());
  }

  @Test
  public void getSameContactsBackAfterFlush() {
    String label = "TEST_7";
		System.out.println(label);
    //String testfile = "" + textfile + "." + label;
    //ContactManagerTests.deleteFile(testfile);
    ContactManagerTests.deleteFile(textfile);
    //ContactManagerUI ui = new ContactManagerUIImpl(testfile);
    ContactManagerUI ui = new ContactManagerUIImpl(textfile);
    ContactManager cm = ui.launch();
    cm.addNewContact(name1, notes); 
    cm.addNewContact(name2, notes); 
    cm.addNewContact(name3, notes); 
    Set<String> namesBefore = new HashSet<String>(Arrays.asList(name1,name2,name3));
    //Set<Integer> idsBefore = new HashSet<Integer>(Arrays.asList(firstContactId,firstContactId+1,firstContactId+2));
    Set<Integer> idsBefore = new HashSet<Integer>(Arrays.asList(contactId1,contactId2,contactId3));
    //ContactManagerTests.copyFile(testfile,"save");
    cm.flush();
    //ContactManager cm2 = new ContactManagerImpl(testfile);
    ContactManagerUI ui2 = new ContactManagerUIImpl();
    ContactManager cm2 = ui.launch();

    // check names and IDs
    Set<Contact> allCm2Contacts = cm2.getContacts("");   
    Iterator<Contact> i = allCm2Contacts.iterator();
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
  public void flushCreatesFileWithContactManagerObject() { 
    String label = "TEST_7.5";
		System.out.println(label);
    //String testfile = "" + textfile + "." + label;
    ContactManagerTests.deleteFile(textfile);
    ContactManager cm = new ContactManagerImpl();
    ContactManager cm2 = null;

    cm.flush();

    FileInputStream fis = null;
    ObjectInputStream in = null;
    try {
      fis = new FileInputStream(textfile);
      in = new ObjectInputStream(fis);
      cm2 = (ContactManager) in.readObject();
      in.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    assertTrue(cm2 instanceof ContactManager);
  }

  @Test
  public void launchAfterFlushStartingWithNoContactsTxtFileReturnsContactManager() { 
    String label = "TEST_7.55";
		System.out.println(label);

    ContactManagerTests.deleteFile(textfile);
    ContactManager cm = new ContactManagerImpl();
    cm.flush();
    ContactManager cm2 = new ContactManagerUIImpl().launch();
    assertTrue(cm2 instanceof ContactManager);
    //assertEquals(cm, cm2);
  }

/*
  @Test
  public void uiLaunchReturnsContactManager() { 
    String label = "TEST_7.6";
		System.out.println(label);
    String testfile = "" + textfile + "." + label;
    ContactManagerTests.deleteFile(testfile);
    assertTrue(new ContactManagerUIImpl(testfile).launch() instanceof ContactManager);
  }
*/

/*
  @Test
  public void flushAfterStartingWithMissingTestfileCreatesIt() { 
    String label = "TEST_7.7";
		System.out.println(label);
    String testfile = "" + textfile + "." + label;
    ContactManagerTests.deleteFile(testfile);
    ContactManagerUI ui = new ContactManagerUIImpl(testfile);
    ContactManager cm = ui.launch();
    //assertNull(cm);
    cm.addNewContact(name1, notes); 
    cm.flush();
    assertTrue(new File(testfile).exists());
  }
*/

  @Test
  public void nextContactIdIsIncrementedIfAddedAfterFlush() {
    String label = "TEST_8";
		System.out.println(label);
    ContactManagerTests.deleteFile(textfile);
    ContactManager cm = myCmui.launch();
    cm.addNewContact(name1, notes); 
    cm.addNewContact(name2, notes); 
    Set<String> namesBefore = new HashSet<String>(Arrays.asList(name1,name2));
    Set<Integer> idsBefore = new HashSet<Integer>(Arrays.asList(contactId1,contactId2));
    cm.flush();
    ContactManagerUI ui2 = new ContactManagerUIImpl();
    ContactManager cm2 = ui2.launch();
    cm2.addNewContact(name3, "BLAHNOTES"); 
    Set<Contact> name3Set = cm2.getContacts(name3);
    Contact contact3 = (Contact) name3Set.toArray()[0];
    assertEquals(contactId3, contact3.getId());
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

  @Test
  public void cm_getBackContactsById() {
    String label = "TEST_16";
		System.out.println(label);
    myCm.addNewContact(name1, notes); 
    myCm.addNewContact(name2, notes); 
    myCm.addNewContact(name3, notes); 
    Set<Integer> idsSearchedFor = new HashSet<Integer>(Arrays.asList(contactId1,contactId2));
    Set<Contact> s = myCm.getContacts(contactId1,contactId2);

    Iterator<Contact> i = s.iterator();
    Set<Integer> idsReturned = new HashSet<Integer>();
    Contact c = null;
    while (i.hasNext()) { 
      c = i.next();
      idsReturned.add(c.getId()); 
    }

    assertEquals(idsSearchedFor,idsReturned);
  }

  @Test (expected=IllegalArgumentException.class) 
  public void cm_badIdToGetContactsById() {
    String label = "TEST_17";
		System.out.println(label);
    myCm.addNewContact(name1, notes); 
    Set<Contact> s = myCm.getContacts(contactId1,badContactId);
  }

  @Test
  public void cm_addFutureMeetingWithOneExistingContact() {
    String label = "TEST_17.5";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> s = myCm.getContacts(name1);
    int mtgId = myCm.addFutureMeeting(s, futureDate1);
    assertEquals(firstMtgId, mtgId);
  }

  @Test
  public void cm_addOneFutureMeetingAndGetItBackWithGetMeeting() {
    String label = "TEST_17.51";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    myCm.addNewContact(name2, notes);
    myCm.addNewContact(name3, notes);
    myCm.addNewContact("unmatched-N-a-M-e", notes);
    Set<Contact> set1 = myCm.getContacts("name");
    int expectedMatches = 3;
    int mtgId = myCm.addFutureMeeting(set1, futureDate1);
    Meeting myMtg = myCm.getMeeting(firstMtgId);
    Set<Contact> set2 = myMtg.getContacts();
    assertEquals(expectedMatches, set1.size());
    assertEquals(set1, set2);
    assertEquals(firstMtgId, mtgId);
    assertEquals(futureDate1, myMtg.getDate());
  }

  @Test
  public void cm_addOneFutureMeetingAndGetItBackWithGetFutureMeeting() {
    String label = "TEST_17.55";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    myCm.addNewContact(name2, notes);
    myCm.addNewContact(name3, notes);
    myCm.addNewContact("unmatched-N-a-M-e", notes);
    Set<Contact> set1 = myCm.getContacts("name");
    int expectedMatches = 3;
    int mtgId = myCm.addFutureMeeting(set1, futureDate1);
    FutureMeeting myMtg = myCm.getFutureMeeting(firstMtgId);
    Set<Contact> set2 = myMtg.getContacts();
    assertEquals(expectedMatches, set1.size());
    assertEquals(set1, set2);
    assertEquals(firstMtgId, mtgId);
    assertEquals(futureDate1, myMtg.getDate());
  }

  @Test
  public void util_isFuture() {
    String label = "TEST_17.56";
		System.out.println(label);
    Calendar cal = Calendar.getInstance();
    assertFalse(util.isFuture(cal));
    cal.add(Calendar.YEAR,1);
    assertTrue(util.isFuture(cal));
  }

  @Test
  public void util_isPast() {
    String label = "TEST_17.57";
		System.out.println(label);
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.SECOND,-1);
    assertTrue(util.isPast(cal));
    cal.add(Calendar.YEAR,1);
    assertFalse(util.isPast(cal));
  }

  @Test (expected=IllegalArgumentException.class) 
  public void cm_addOneFutureMeetingAndRetrieveWithGetPastMeeting() {
    String label = "TEST_17.58";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> set = myCm.getContacts("name");
    int mtgId = myCm.addFutureMeeting(set, futureDate1);
    myCm.getPastMeeting(firstMtgId);
  }

  @Test (expected=IllegalArgumentException.class) 
  public void cm_addFutureMeetingWithOneInvalidContact() {
    String label = "TEST_17.6";
		System.out.println(label);
    Set<Contact> s = new HashSet<Contact>();
    s.add(badContact);
    myCm.addFutureMeeting(s, futureDate1);
  }

  @Test
  public void cm_getFutureMeetingListForContactReturnsOnlyMeeting() {
    String label = "TEST_18";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> set = myCm.getContacts("name");
    Contact c = (Contact) set.toArray()[0];
    int mtgId = myCm.addFutureMeeting(set, futureDate1);
    List<Meeting> list = myCm.getFutureMeetingList(c);
    Meeting m = list.get(0);
    Set<Contact> set2 = m.getContacts();
    Contact c2 = (Contact) set2.toArray()[0];
    assertEquals(name1, c2.getName());
  }

  @Test
  public void cm_getFutureMeetingListForContactReturnsNoMeeting() {
    String label = "TEST_19";
		System.out.println(label);
    // Create a meeting with contact "1" but search for contact "2"
    myCm.addNewContact(name1, notes);
    myCm.addNewContact(name2, notes);
    Set<Contact> name1Set = myCm.getContacts("name1");
    myCm.addFutureMeeting(name1Set, futureDate1);
    Set<Contact> name2Set = myCm.getContacts("name2");
    Contact contact2 = (Contact) name2Set.toArray()[0];
    List<Meeting> mtgListContact2 = myCm.getFutureMeetingList(contact2);
    assertEquals(1, name2Set.size());
    assertEquals(0, mtgListContact2.size());
  }


  @Test
  public void cm_getFutureMeetingListByContactReturnsChronologically() {
    String label = "TEST_19.5";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> nameSet = myCm.getContacts("name");
    myCm.addFutureMeeting(nameSet, futureDate2);
    myCm.addFutureMeeting(nameSet, futureDate1);
    myCm.addFutureMeeting(nameSet, futureDate3);
    Contact contact1 = (Contact) nameSet.toArray()[0];
    List<Meeting> list = myCm.getFutureMeetingList(contact1);
    assertTrue(list.get(0).getDate().before(list.get(1).getDate()));
    assertTrue(list.get(1).getDate().before(list.get(2).getDate()));
  }

  @Test 
  public void util_meetingsAreDuplicate() {
    // Two meetings are assumed here to be duplicate if they happen on the same date, at the same time and
    // involve the same contacts. 
    String label = "TEST_19.55";
		System.out.println(label);
    Contact c1 = new ContactImpl("name1", notes, 100); 
    Contact c2 = new ContactImpl("name2", notes, 101); 
    Set<Contact> set = new HashSet<Contact>();
    Set<Contact> set2 = new HashSet<Contact>();
    set.add(c1);
    set.add(c2);
    set2.add(c1);
    Meeting m1 = new FutureMeetingImpl(100, futureDate1, set); 
    Meeting m2 = new FutureMeetingImpl(101, futureDate1, set); 
    Meeting m3 = new FutureMeetingImpl(102, futureDate1, set2); 
    assertTrue(util.meetingsAreDuplicate(m1, m2));
    assertFalse(util.meetingsAreDuplicate(m1, m3));
  }
  
  public void util_dedupeMeetingList() {
    String label = "TEST_19.55";
		System.out.println(label);
    Contact c1 = new ContactImpl("name1", notes, 100); 
    Contact c2 = new ContactImpl("name2", notes, 101); 
    Set<Contact> set = new HashSet<Contact>();
    Set<Contact> set2 = new HashSet<Contact>();
    set.add(c1);
    set.add(c2);
    set2.add(c1);
    Meeting m1 = new FutureMeetingImpl(100, futureDate1, set);
    Meeting m2 = new FutureMeetingImpl(101, futureDate1, set); 
    Meeting m3 = new FutureMeetingImpl(102, futureDate1, set2); 
    List<Meeting> list = new ArrayList<Meeting>();
    list.add(m1);
    list.add(m2);
    list.add(m3);
    List<Meeting> dedupedList = new ArrayList<Meeting>();
    dedupedList.add(m1);
    dedupedList.add(m3);
    List<Meeting> newList = util.dedupeMeetingList(list);
    assertEquals(dedupedList, newList);
  }

  
  @Test (expected=IllegalArgumentException.class) 
  public void util_dedupeMeetingListPassedEmptyList() {
    String label = "TEST_19.6";
		System.out.println(label);
    util.dedupeMeetingList(new ArrayList<Meeting>());
  }

  @Test (expected=NullPointerException.class) 
  public void util_dedupeMeetingListPassedNull() {
    String label = "TEST_19.7";
		System.out.println(label);
    util.dedupeMeetingList(null);
  }

  @Test
  public void util_sortMeetingList() {
    String label = "TEST_19.75";
		System.out.println(label);
    Contact c1 = new ContactImpl("name1", notes, 100); 
    Set<Contact> set = new HashSet<Contact>();
    set.add(c1);
    Meeting m1 = new FutureMeetingImpl(100, futureDate1, set);
    Meeting m2 = new FutureMeetingImpl(101, futureDate2, set);
    List<Meeting> list = new ArrayList<Meeting>();
    list.add(m2);
    list.add(m1);
    list = util.sortMeetingList(list);
    assertEquals(m1, list.get(0));
  }

  @Test
  public void cm_getFutureMeetingListByContactReturnsNoDuplicates() {
    String label = "TEST_19.8";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    myCm.addNewContact(name2, notes);
    Set<Contact> nameSet = myCm.getContacts("name");
    Set<Contact> name1Set = myCm.getContacts(name1);
    myCm.addFutureMeeting(nameSet, futureDate1);
    myCm.addFutureMeeting(nameSet, futureDate1);
    Contact contact1 = (Contact) name1Set.toArray()[0];
    List<Meeting> list = myCm.getFutureMeetingList(contact1);
    assertEquals(1, list.size());
  }

  @Test
  public void cm_getFutureMeetingListForContactReturnsOneMeeting() {
    String label = "TEST_20";
		System.out.println(label);

    myCm.addNewContact(name1, notes);
    myCm.addNewContact(name2, notes);

    // add meeting with contact 1:

    Set<Contact> nameSet = myCm.getContacts("name");
    int contact1MtgId = myCm.addFutureMeeting(nameSet, futureDate1);

    // add meeting without contact 1:

    Set<Contact> name2Set = myCm.getContacts("name2");
    myCm.addFutureMeeting(name2Set, futureDate1);

    // search for meetings with contact 1:

    Set<Contact> name1Set = myCm.getContacts("name1");
    Contact contact1 = (Contact) name1Set.toArray()[0];
    List<Meeting> list = myCm.getFutureMeetingList(contact1);

    assertEquals(1, list.size());
    assertEquals(contact1MtgId, list.get(0).getId());
  }
 
  @Test (expected=IllegalArgumentException.class) 
  public void cm_getFutureMeetingListForInvalidContact() {
    String label = "TEST_21";
		System.out.println(label);
    myCm.getFutureMeetingList(badContact);
  }

  @Test
  public void cm_getFutureMeetingListForDateReturnsOnlyMeeting() {
    String label = "TEST_22";
		System.out.println(label);

    myCm.addNewContact(name1, notes);
    Set<Contact> nameSet = myCm.getContacts("name");
    int mtgId = myCm.addFutureMeeting(nameSet, futureDate1);

    List<Meeting> list = myCm.getFutureMeetingList(futureDate1);
    assertEquals(1, list.size());
    assertEquals(mtgId, list.get(0).getId());
  }

  @Test
  public void cm_getFutureMeetingListByDateReturnsChronologically() {
    String label = "TEST_22.5";
		System.out.println(label);

    myCm.addNewContact(name1, notes);
    myCm.addNewContact(name2, notes);

    Set<Contact> name1Set = myCm.getContacts(name1);
    Set<Contact> name2Set = myCm.getContacts(name2);

    myCm.addFutureMeeting(name2Set, futureDate1Plus1Hr);
    myCm.addFutureMeeting(name1Set, futureDate1);
    myCm.addFutureMeeting(name1Set, futureDate1Plus2Hr);

    List<Meeting> list = myCm.getFutureMeetingList(futureDate1);
    assertTrue(list.get(0).getDate().before(list.get(1).getDate()));
    assertTrue(list.get(1).getDate().before(list.get(2).getDate()));
  }

  @Test
  public void cm_getFutureMeetingListByDateReturnsNoDuplicates() {
    // Two meetings are assumed here to be duplicate if they happen on the same date, at the same time and
    // involve the same contacts. 
    String label = "TEST_22.7";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    myCm.addNewContact(name2, notes);
    Set<Contact> nameSet = myCm.getContacts("name");
    Set<Contact> name1Set = myCm.getContacts(name1);
    myCm.addFutureMeeting(nameSet, futureDate1);
    myCm.addFutureMeeting(nameSet, futureDate1);
    Contact contact1 = (Contact) name1Set.toArray()[0];
    List<Meeting> list = myCm.getFutureMeetingList(futureDate1);
    assertEquals(1, list.size());
  }

  @Test
  public void cm_getFutureMeetingListForDateReturnsOneOfTwoMeetings() {
    String label = "TEST_23";
		System.out.println(label);

    myCm.addNewContact(name1, notes);
    Set<Contact> nameSet = myCm.getContacts("name");

    myCm.addFutureMeeting(nameSet, futureDate1);
    myCm.addFutureMeeting(nameSet, futureDate2);

    List<Meeting> listDate1 = myCm.getFutureMeetingList(futureDate1);
    assertEquals(1, listDate1.size());
    assertEquals(futureDate1, listDate1.get(0).getDate());

    List<Meeting> listDate2 = myCm.getFutureMeetingList(futureDate2);
    assertEquals(1, listDate2.size());
    assertEquals(futureDate2, listDate2.get(0).getDate());

  }

  @Test
  public void util_areSameDay() {
    String label = "TEST_23.5";
		System.out.println(label);
    assertFalse(util.areSameDay(pastDate1, futureDate1));
    Calendar date = (Calendar) pastDate1.clone();
    assertTrue(util.areSameDay(pastDate1, date));
  }

  @Test
  public void cm_getFutureMeetingListForDateReturnsNoMeetings() {
    String label = "TEST_24";
		System.out.println(label);

    myCm.addNewContact(name1, notes);
    Set<Contact> nameSet = myCm.getContacts("name");

    myCm.addFutureMeeting(nameSet, futureDate1);
    myCm.addFutureMeeting(nameSet, futureDate2);

    List<Meeting> listDate3 = myCm.getFutureMeetingList(futureDate3);
    assertEquals(0, listDate3.size());
  }

  @Test
  public void cm_getPastMeetingListForContactReturnsOnlyMeeting() {
    String label = "TEST_25";
		System.out.println(label);

    myCm.addNewContact(name1, notes);
    Set<Contact> set = myCm.getContacts("name");
    Contact c = (Contact) set.toArray()[0];
    myCm.addNewPastMeeting(set, pastDate1, notes);
    List<PastMeeting> list = myCm.getPastMeetingList(c);
    Meeting m = list.get(0);
    Set<Contact> set2 = m.getContacts();
    Contact c2 = (Contact) set2.toArray()[0];
    assertEquals(name1, c2.getName());

  }

  @Test
  public void cm_getPastMeetingListForContactReturnsNoMeeting() {
    String label = "TEST_26";
		System.out.println(label);
    // Create a meeting with contact "1" but search for contact "2"
    myCm.addNewContact(name1, notes);
    myCm.addNewContact(name2, notes);
    Set<Contact> name1Set = myCm.getContacts("name1");
    myCm.addNewPastMeeting(name1Set, pastDate1, notes);
    Set<Contact> name2Set = myCm.getContacts("name2");
    Contact contact2 = (Contact) name2Set.toArray()[0];
    List<PastMeeting> list = myCm.getPastMeetingList(contact2);
    assertEquals(1, name2Set.size());
    assertEquals(0, list.size());
  }

  @Test
  public void cm_getPastMeetingListForContactReturnsOneOfTwoMeetings() {
    String label = "TEST_27";
		System.out.println(label);

    myCm.addNewContact(name1, notes);
    myCm.addNewContact(name2, notes);

    // add meeting with contacts 1 and 2:

    Set<Contact> nameSet = myCm.getContacts("name");
    myCm.addNewPastMeeting(nameSet, pastDate1, notes);

    // add meeting with just contact 2:

    Set<Contact> name2Set = myCm.getContacts("name2");
    myCm.addNewPastMeeting(name2Set, pastDate1, notes);

    // search for meetings with contact 1:

    Set<Contact> name1Set = myCm.getContacts("name1");
    Contact contact1 = (Contact) name1Set.toArray()[0];
    List<PastMeeting> list1 = myCm.getPastMeetingList(contact1);

    // search for meetings with contact 2:

    Contact contact2 = (Contact) name2Set.toArray()[0];
    List<PastMeeting> list2 = myCm.getPastMeetingList(contact2);

    assertEquals(1, list1.size());
    assertEquals(mtgId1, list1.get(0).getId());
    assertEquals(2, list2.size());
  }
 
  @Test (expected=IllegalArgumentException.class) 
  public void cm_getPastMeetingListForInvalidContact() {
    String label = "TEST_28";
		System.out.println(label);
    myCm.getPastMeetingList(badContact);
  }

  @Test
  public void cm_getPastMeetingListByContactReturnsNoDuplicates() {
    // Two past meetings are assumed here to be duplicate if they happen on the same date, at the same time and
    // involve the same contacts and have the same notes.
    String label = "TEST_28.3";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    myCm.addNewContact(name2, notes);
    Set<Contact> nameSet = myCm.getContacts("name");
    Set<Contact> name1Set = myCm.getContacts(name1);
    myCm.addNewPastMeeting(nameSet, pastDate1, notes);
    myCm.addNewPastMeeting(nameSet, pastDate1, notes);
    Contact contact1 = (Contact) name1Set.toArray()[0];
    List<PastMeeting> list = myCm.getPastMeetingList(contact1);
    assertEquals(1, list.size());
  }

  @Test
  public void cm_getPastMeetingForNonexistentIdReturnsNull() {
    String label = "TEST_28.5";
		System.out.println(label);
    assertNull(myCm.getPastMeeting(badMtgId));
  }

  @Test
  public void cm_getMeetingForNonexistentIdReturnsNull() {
    String label = "TEST_28.6";
		System.out.println(label);
    assertNull(myCm.getMeeting(badMtgId));
  }

  @Test
  public void cm_getPastMeetingListByContactReturnsChronologically() {
    String label = "TEST_28.7";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> nameSet = myCm.getContacts("name");
    myCm.addNewPastMeeting(nameSet, pastDate2, notes);
    myCm.addNewPastMeeting(nameSet, pastDate1, notes);
    myCm.addNewPastMeeting(nameSet, pastDate3, notes);
    Contact contact1 = (Contact) nameSet.toArray()[0];
    List<PastMeeting> list = myCm.getPastMeetingList(contact1);
    assertTrue(list.get(0).getDate().before(list.get(1).getDate()));
    assertTrue(list.get(1).getDate().before(list.get(2).getDate()));
  }

  @Test (expected=IllegalArgumentException.class) 
  public void cm_addNewPastMeetingNoContacts() {
    String label = "TEST_29";
		System.out.println(label);
    Set<Contact> emptySet = new HashSet<Contact>();
    myCm.addNewPastMeeting(emptySet, pastDate1, notes); 
  }

  @Test (expected=IllegalArgumentException.class) 
  public void cm_addNewPastMeetingInvalidContact() {
    String label = "TEST_29.5";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> badSet = new HashSet<Contact>();
    badSet.addAll(myCm.getContacts("name"));
    badSet.add(badContact);
    myCm.addNewPastMeeting(badSet, pastDate1, notes); 
  }

  @Test (expected=NullPointerException.class) 
  public void cm_addNewPastMeetingNullContacts() {
    String label = "TEST_30";
		System.out.println(label);
    myCm.addNewPastMeeting(null, pastDate1, notes);
  }

  @Test (expected=NullPointerException.class) 
  public void cm_addNewPastMeetingNullDate() {
    String label = "TEST_30.1";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> nameSet = myCm.getContacts("name");
    myCm.addNewPastMeeting(nameSet, null, notes);
  }

  @Test (expected=NullPointerException.class) 
  public void cm_addNewPastMeetingNullNotes() {
    String label = "TEST_30.2";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> nameSet = myCm.getContacts("name");
    myCm.addNewPastMeeting(nameSet, pastDate1, null);
  }

  @Test
  public void cm_addNewPastMeetingAddsPastMeeting() {
    String label = "TEST_31";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    String mtgNotes = "mtgNotes";
    Set<Contact> nameSet = myCm.getContacts("name");
    myCm.addNewPastMeeting(nameSet, pastDate1, mtgNotes);
    PastMeeting pm = myCm.getPastMeeting(mtgId1);
    assertEquals(mtgId1, pm.getId());
    assertEquals(pastDate1, pm.getDate());
    assertEquals(mtgNotes, pm.getNotes());
    assertEquals(nameSet, pm.getContacts());
  }

  @Test
  public void cm_addMeetingNotesAddsNotes() {
    String label = "TEST_32";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> nameSet = myCm.getContacts("name");
    myCm.addNewPastMeeting(nameSet, pastDate1, notes);
    String notes2 = "notes2";
    myCm.addMeetingNotes(mtgId1, notes2);
    assertEquals(notes2, myCm.getPastMeeting(mtgId1).getNotes());
  }

  @Test (expected=IllegalArgumentException.class) 
  public void cm_addMeetingNotesToNonexistentMeeting() {
    String label = "TEST_33";
		System.out.println(label);
    myCm.addMeetingNotes(badMtgId, notes);
  }

  @Test (expected=IllegalStateException.class) 
  public void cm_addMeetingNotesToFutureMeeting() {
    String label = "TEST_34";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> nameSet = myCm.getContacts("name");
    int mtgId = myCm.addFutureMeeting(nameSet, futureDate1);
    myCm.addMeetingNotes(mtgId, notes);
  }

  @Test (expected=NullPointerException.class) 
  public void cm_addMeetingNotesWithNullNotes() {
    String label = "TEST_35";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> nameSet = myCm.getContacts("name");
    myCm.addNewPastMeeting(nameSet, pastDate1, notes);
    myCm.addMeetingNotes(mtgId1, null);
  }
}
