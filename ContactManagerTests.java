import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ContactManagerTests { 

  private final String textfile = "contacts.txt";

  // First IDs are from ContactImpl and MeetingImpl

  private final static int firstContactId = 100;
  private final static int badContactId = firstContactId-1;
  private final static int firstMeetingId = 100;
  private final static int badMtgId = firstMeetingId-1;

  private Calendar futureDate1;
  private Calendar futureDate2;
  private Calendar futureDate3;
  private Calendar pastDate1;
  private Calendar pastDate2;
  private Calendar pastDate3;
  private Calendar futureDate1Plus1Hr;
  private Calendar futureDate1Plus2Hr;

  // Some constants for Contacts and Meetings:

  private final String name1 = "name1";
  private final String name2 = "name2";
  private final String name3 = "name2x";
  private final String notes = "notes";

  // Test Contacts:

  private Contact testContact1;
  private Contact testContact2;
  private Contact badContact; 

  // myCm is a ContactManager that isn't pre-populated.
  // testCm is a ContactManager that gets pre-populated with a meeting with one contact.

  private ContactManager myCm;
  private Contact testCmContact1;
  private Set<Contact> testCmSetOfName1;   
  private int testCmMtgId1;
  private Meeting testCmMtg1;
  private ContactManager testCm;

  private ContactManagerUtil util;

  private static void deleteFile(String filename) { 
    try { 
      File file = new File(filename); 
      if (file.exists())
        file.delete();
    } catch (Exception e) { 
      e.printStackTrace();
    }
  }

  @Before
  public void buildUp() {

    ContactManagerTests.deleteFile(textfile);

    // Objects for testing

    badContact = new ContactImpl("nameOfBadContact", notes); 
    testContact1 = new ContactImpl(name1, notes);
    testContact2 = new ContactImpl(name2, notes);

    myCm = new ContactManagerImpl();

    // Dates:

    int noon = 12; 

    futureDate1 = Calendar.getInstance();
    futureDate1.add(Calendar.MONTH, 1);
    futureDate1.set(Calendar.HOUR_OF_DAY, noon); // in case tests are run at midnight

    futureDate1Plus1Hr = (Calendar) futureDate1.clone();
    futureDate1Plus1Hr.add(Calendar.HOUR, 1);
    futureDate1Plus2Hr = (Calendar) futureDate1.clone();
    futureDate1Plus2Hr.add(Calendar.HOUR, 2);

    futureDate2 = Calendar.getInstance();
    futureDate2.add(Calendar.MONTH, 2);
    futureDate2.set(Calendar.HOUR_OF_DAY, noon); 

    futureDate3 = Calendar.getInstance();
    futureDate3.add(Calendar.MONTH, 3);
    futureDate3.set(Calendar.HOUR_OF_DAY, noon); 

    pastDate1 = Calendar.getInstance();
    pastDate1.add(Calendar.MONTH, -3);
    pastDate1.set(Calendar.HOUR_OF_DAY, noon); 

    pastDate2 = Calendar.getInstance();
    pastDate2.add(Calendar.MONTH, -2);
    pastDate2.set(Calendar.HOUR_OF_DAY, noon); 

    pastDate3 = Calendar.getInstance();
    pastDate3.add(Calendar.MONTH, -1);
    pastDate3.set(Calendar.HOUR_OF_DAY, noon); 

    util = new ContactManagerUtilImpl();

    // Populate testCm:

    testCm = new ContactManagerImpl();
    testCm.addNewContact(name1, notes); 
    testCmSetOfName1 = testCm.getContacts(name1);   
    testCmContact1 = (Contact) testCmSetOfName1.toArray()[0];
    testCmMtgId1 = testCm.addFutureMeeting(testCmSetOfName1, futureDate1);
    testCmMtg1 = testCm.getFutureMeeting(testCmMtgId1);

  }

  @Test
  public void contact_GetName() {
    String label = "TEST_2";
		System.out.println(label);
    assertEquals(name1, testContact1.getName());
  }

  @Test
  public void contact_GetNotes() {
    String label = "TEST_3";
		System.out.println(label);
    testContact1.addNotes("newNotes");
    assertEquals("newNotes", testContact1.getNotes());
  }

  @Test
  public void cm_addOneContactSearchByName() {
    String label = "TEST_4";
		System.out.println(label);
    assertEquals(name1, testCmContact1.getName()); 
  }
  
  @Test
  public void cm_getContactByNameNotFound() {
    String label = "TEST_4.1";
		System.out.println(label);
    assertTrue(testCm.getContacts(name2).isEmpty());
  }

  @Test
  public void cm_getContactsByPartOfName() {
    String label = "TEST_4.2";
		System.out.println(label);
    testCm.addNewContact("abcd", notes); 
    testCm.addNewContact("abcde", notes); 
    assertEquals(2, testCm.getContacts("abcd").size()); 
  }

  @Test (expected=NullPointerException.class)
  public void cm_addNewContactWithNullName() {
    String label = "TEST_5";
		System.out.println(label);
    testCm.addNewContact(null, notes); 
  }

  @Test (expected=NullPointerException.class)
  public void cm_addNewContactWithNullNotes() {
    String label = "TEST_6";
		System.out.println(label);
    testCm.addNewContact(name1, null); 
  }

  @Test
  public void cm_sameContactsAfterFlushAndNextContactIdIsIncremented() {
    String label = "TEST_7";
		System.out.println(label);
    ContactManagerTests.deleteFile(textfile);

    myCm.addNewContact("name1", notes); 
    myCm.addNewContact("name2", notes); 
    Contact c1Before = (Contact) myCm.getContacts("name1").toArray()[0];
    Contact c2Before = (Contact) myCm.getContacts("name2").toArray()[0];

    myCm.flush();

    ContactManager cmAfter = new ContactManagerImpl();
    Contact c1After = (Contact) cmAfter.getContacts("name1").toArray()[0];
    Contact c2After = (Contact) cmAfter.getContacts("name2").toArray()[0];

    // IDs same after flush?

    assertEquals(c1Before.getId(), c1After.getId());
    assertEquals(c2Before.getId(), c2After.getId());

    cmAfter.addNewContact("name3", notes); 
    Contact c3 = (Contact) cmAfter.getContacts("name3").toArray()[0];

    // IDs getting incremented after flush?

    assertEquals(c2Before.getId()+1, c3.getId());
    

  }

  @Test
  public void cm_getContactsById() {
    String label = "TEST_16";
		System.out.println(label);

    // One ID:

    myCm.addNewContact(name1, notes); 
    Contact c1 = (Contact) myCm.getContacts(name1).toArray()[0];
    int id1 = c1.getId(); 
    Contact c1ById = (Contact) myCm.getContacts(id1).toArray()[0];

    assertEquals(id1, c1ById.getId()); 

    // Two IDs:

    myCm.addNewContact(name2, notes); 
    Contact c2 = (Contact) myCm.getContacts(name2).toArray()[0];
    int id2 = c2.getId(); 
    Set<Integer> testIds = new HashSet<Integer>(Arrays.asList(new Integer[]{id1, id2}));  
    Set<Contact> resultContacts = myCm.getContacts(id1, id2);
    Set<Integer> resultIds = new HashSet<Integer>();  

    for (Contact c : resultContacts) 
      resultIds.add(c.getId());

    assertEquals(testIds, resultIds);
  }

  @Test (expected=IllegalArgumentException.class) 
  public void cm_getContactsByIdUsingBadId() {
    String label = "TEST_17";
		System.out.println(label);

    testCm.getContacts(firstContactId,badContactId);
  }

  @Test (expected=NullPointerException.class) 
  public void cm_getContactsByNameUsingNull() {
    String label = "TEST_17.4";
		System.out.println(label);

    String n = null;
    testCm.getContacts(n);
  }

  @Test
  public void cm_addFutureMeetingCreatesMeetingsWithUniqueIds() {
    String label = "TEST_17.5";
		System.out.println(label);

    int mtgId2 = testCm.addFutureMeeting(testCmSetOfName1, futureDate2);
    Meeting mtg2 = testCm.getMeeting(mtgId2);
    assertEquals(testCmMtgId1, testCmMtg1.getId());
    assertEquals(mtgId2, mtg2.getId());
  }

  @Test
  public void cm_getMeetingByNameUsingPartialName() {
    String label = "TEST_17.51";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    myCm.addNewContact(name2, notes);
    myCm.addNewContact(name3, notes);
    myCm.addNewContact("unmatched-N-a-M-e", notes);
    Set<Contact> set1 = myCm.getContacts("name");
    int expectedMatches = 3;
    int mtgId = myCm.addFutureMeeting(set1, futureDate1);
    System.out.println("mtgId: " + mtgId);
    Meeting myMtg = myCm.getMeeting(mtgId);
    Set<Contact> set2 = myMtg.getContacts();
    assertEquals(expectedMatches, set1.size());
    assertEquals(set1, set2);
    assertEquals(futureDate1, myMtg.getDate());
  }

  @Test
  public void cm_getFutureMeetingById() {
    String label = "TEST_17.55";
		System.out.println(label);
    assertEquals(testCmMtgId1, testCmMtg1.getId());
  }

  @Test
  public void util_isFuture() {
    String label = "TEST_17.56";
		System.out.println(label);
    assertFalse(util.isFuture(Calendar.getInstance()));
    assertTrue(util.isFuture(futureDate1));
  }

  @Test
  public void util_isPast() {
    String label = "TEST_17.57";
		System.out.println(label);
    assertTrue(util.isPast(pastDate1));
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.SECOND,-1);
    assertTrue(util.isPast(cal));
    assertFalse(util.isPast(futureDate1));
  }

  @Test (expected=IllegalArgumentException.class) 
  public void cm_retrieveFutureMeetingWithGetPastMeetingById() {
    String label = "TEST_17.58";
		System.out.println(label);
    testCm.getPastMeeting(testCmMtgId1);
  }

  @Test (expected=IllegalArgumentException.class) 
  public void cm_retrievePastMeetingWithGetFutureMeetingById() {
    String label = "TEST_17.585";
		System.out.println(label);
    testCm.addNewPastMeeting(testCmSetOfName1, pastDate1, notes);
    List<PastMeeting> list = testCm.getPastMeetingList(testCmContact1);
    assertEquals(1, list.size());
    testCm.getFutureMeeting(list.get(0).getId());
  }

  @Test (expected=IllegalArgumentException.class) 
  public void cm_addFutureMeetingWithPastDate() {
    String label = "TEST_17.59";
		System.out.println(label);
    testCm.addFutureMeeting(testCmSetOfName1, pastDate1);
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
    String label = "TEST_19.55";
		System.out.println(label);
    Set<Contact> set = new HashSet<Contact>();
    Set<Contact> set2 = new HashSet<Contact>();
    set.add(testContact1);
    set.add(testContact2);
    set2.add(testContact1);
    Meeting m1 = new FutureMeetingImpl(futureDate1, set); 
    Meeting m2 = new FutureMeetingImpl(futureDate1, set); 
    Meeting m3 = new FutureMeetingImpl(futureDate1, set2); 
    assertFalse(util.meetingsAreDuplicate(m1, m2));
    assertFalse(util.meetingsAreDuplicate(m1, m3));
  }
  
  public void util_dedupeMeetingList() {
    String label = "TEST_19.55";
		System.out.println(label);
    Set<Contact> set = new HashSet<Contact>();
    Set<Contact> set2 = new HashSet<Contact>();
    set.add(testContact1);
    set.add(testContact2);
    set2.add(testContact1);
    Meeting m1 = new FutureMeetingImpl(futureDate1, set);
    Meeting m2 = new FutureMeetingImpl(futureDate1, set); 
    Meeting m3 = new FutureMeetingImpl(futureDate1, set2); 
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
    Set<Contact> set = new HashSet<Contact>();
    set.add(testContact1);
    Meeting m1 = new FutureMeetingImpl(futureDate1, set);
    Meeting m2 = new FutureMeetingImpl(futureDate2, set);
    List<Meeting> list = new ArrayList<Meeting>();
    list.add(m2);
    list.add(m1);
    util.sortMeetingList(list);
    assertEquals(m1, list.get(0));

    Meeting m3 = new FutureMeetingImpl(futureDate2, set);
    List<Meeting> list2 = new ArrayList<Meeting>();
    list2.add(m2);
    list2.add(m3);
    list2.add(m1);
    util.sortMeetingList(list2);
    assertEquals(m1, list2.get(0));
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
    assertTrue(list.get(0) != list.get(1));
  }

  @Test
  public void cm_getFutureMeetingListByDateReturnsEmptyListIfNoMatches() {
    String label = "TEST_19.9";
		System.out.println(label);
    assertEquals(0, myCm.getFutureMeetingList(futureDate1).size());
  }

  @Test
  public void cm_getFutureMeetingListByContactReturnsEmptyListIfNoMatches() {
    String label = "TEST_19.91";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> name1Set = myCm.getContacts(name1);
    Contact contact1 = (Contact) name1Set.toArray()[0];
    assertEquals(0, myCm.getFutureMeetingList(contact1).size());
  }

  @Test
  public void cm_getPastMeetingListByContactReturnsEmptyListIfNoMatches() {
    String label = "TEST_19.92";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> name1Set = myCm.getContacts(name1);
    Contact contact1 = (Contact) name1Set.toArray()[0];
    assertEquals(0, myCm.getPastMeetingList(contact1).size());
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
  public void cm_getFutureMeetingListByDateReturnsOnlyMeeting() {
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
    assertTrue(list.get(0) != list.get(1));
  }

  @Test
  public void cm_getFutureMeetingListByDateReturnsOneOfTwoMeetings() {
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
  public void cm_getFutureMeetingListByDateReturnsNoMeetings() {
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

    myCm.addNewContact("name1", notes);
    myCm.addNewContact("name2", notes);

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
    assertTrue(list.get(0) != list.get(1));
  }

  @Test
  public void cm_getFutureMeetingForNonexistentIdReturnsNull() {
    String label = "TEST_28.4";
		System.out.println(label);
    assertNull(myCm.getFutureMeeting(badContactId));
  }

  @Test
  public void cm_getPastMeetingForNonexistentIdReturnsNull() {
    String label = "TEST_28.5";
		System.out.println(label);
    assertNull(myCm.getPastMeeting(badContactId));
  }

  @Test
  public void cm_getMeetingForNonexistentIdReturnsNull() {
    String label = "TEST_28.6";
		System.out.println(label);
    assertNull(myCm.getMeeting(badContactId));
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
    Set<Contact> name1Set = myCm.getContacts("name1");
    myCm.addNewPastMeeting(name1Set, pastDate1, label);
    Contact c = (Contact) name1Set.toArray()[0];
    List<PastMeeting> list = myCm.getPastMeetingList(c);
    PastMeeting pm = list.get(0); 
    assertEquals(pastDate1, pm.getDate());
    assertEquals(label, pm.getNotes());
    assertEquals(name1Set, pm.getContacts());
  }

  @Test
  public void cm_addMeetingNotesToPastMeetingOverwritesExistingNotes() {
    String label = "TEST_32";
		System.out.println(label);
    myCm.addNewContact("name1", notes);
    Set<Contact> name1Set = myCm.getContacts("name1");
    myCm.addNewPastMeeting(name1Set, pastDate1, notes);
    String notes2 = "notes2";
    Contact c = (Contact) name1Set.toArray()[0];
    List<PastMeeting> list = myCm.getPastMeetingList(c);
    PastMeeting pm = list.get(0);
    int id = pm.getId();
    myCm.addMeetingNotes(id, notes2);
    assertEquals(notes2, myCm.getPastMeeting(id).getNotes());
  }

  @Test (expected=IllegalArgumentException.class) 
  public void cm_addMeetingNotesToNonexistentMeeting() {
    String label = "TEST_33";
		System.out.println(label);
    myCm.addMeetingNotes(badMtgId, notes);
  }

  @Test (expected=IllegalStateException.class) 
  public void cm_addMeetingNotesToFutureMeetingThatHasNotHappenedYet() {
    String label = "TEST_34";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> nameSet = myCm.getContacts("name");
    int mtgId = myCm.addFutureMeeting(nameSet, futureDate1);
    myCm.addMeetingNotes(mtgId, notes);
  }

  @Test
  public void cm_addMeetingNotesToPastFutureMeeting() {
    String label = "TEST_34.5";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> nameSet = myCm.getContacts("name");
    Calendar soon = Calendar.getInstance();
    soon.add(Calendar.SECOND, 1);
    int mtgId = myCm.addFutureMeeting(nameSet, soon);
    try {
      Thread.sleep(2000);
    } catch(InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
    myCm.addMeetingNotes(mtgId, notes);
    assertEquals(notes, myCm.getPastMeeting(mtgId).getNotes());
  }

  @Test (expected=NullPointerException.class) 
  public void cm_addMeetingNotesWithNullNotes() {
    String label = "TEST_35";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> name1Set = myCm.getContacts("name1");
    myCm.addNewPastMeeting(name1Set, pastDate1, notes);
    Contact c = (Contact) name1Set.toArray()[0];
    List<PastMeeting> list = myCm.getPastMeetingList(c); 
    myCm.addNewPastMeeting(name1Set, pastDate1, notes);
    myCm.addMeetingNotes(list.get(0).getId(), null);
  }

  @Test
  public void meetingsHaveUniqueIdsWithinContactManager() {
    String label = "TEST_36";
		System.out.println(label);
    myCm.addNewContact(name1, notes);
    Set<Contact> name1Set = myCm.getContacts(name1);
    Contact contact1 = (Contact) name1Set.toArray()[0];
    int id1 = myCm.addFutureMeeting(name1Set, futureDate1);
    int id2 = myCm.addFutureMeeting(name1Set, futureDate2);
    assertTrue(id1 != id2);
  }

  @Test
  public void futureMeetingsHaveUniqueIdsWithinClass() {
    String label = "TEST_37";
		System.out.println(label);
    Set<Contact> sc = new HashSet<Contact>();
    sc.add(testContact1);
    Meeting m1 = new FutureMeetingImpl(futureDate1, sc); 
    Meeting m2 = new FutureMeetingImpl(futureDate2, sc); 
    System.out.println("m1.getId(): " + m1.getId());
    System.out.println("m2.getId(): " + m2.getId());
    assertTrue(m1.getId() != m2.getId());
  }

  @Test
  public void pastMeetingsHaveUniqueIdsWithinClass() {
    String label = "TEST_37.5";
		System.out.println(label);
    Set<Contact> sc = new HashSet<Contact>();
    sc.add(testContact1);
    Meeting m1 = new PastMeetingImpl(futureDate1, sc, notes); 
    Meeting m2 = new PastMeetingImpl(futureDate2, sc, notes); 
    assertTrue(m1.getId() != m2.getId());
  }

  @Test
  public void contactsHaveUniqueIdsWithinContact() {
    String label = "TEST_38";
		System.out.println(label);
    assertTrue(testContact1.getId() != testContact2.getId());
  }

  @Test
  public void getNotesOnContactWithNoNotesReturnsEmptyString() {
    String label = "TEST_39";
		System.out.println(label);
    Contact c = new ContactImpl("name", "");
    assertEquals("", c.getNotes()); 
  }

  @Test
  public void getNotesOnPastMeetingWithNoNotesReturnsEmptyString() {
    String label = "TEST_40";
		System.out.println(label);
    Set<Contact> sc = new HashSet<Contact>();
    sc.add(testContact1);
    PastMeeting pm = new PastMeetingImpl(pastDate1, sc, "");
    assertEquals("", pm.getNotes()); 
  }
}
