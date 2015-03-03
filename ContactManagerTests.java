import org.junit.*;
import static org.junit.Assert.*;


public class ContactManagerTests { 
  private final String name = "name";
  private Contact myContact1;
  private Contact myContact2;
  private String notes;

  @Before 
  public void buildUp() {
    myContact1 = new ContactImpl(name);
    myContact2 = new ContactImpl(name);
    notes = "notes";
  }

  @Test
  public void contactGetIdReturnsUniqueIDs() {
    System.out.println("TEST 1");
    assertTrue(myContact1.getId() != myContact2.getId());
  }

  @Test
  public void contactGetNameReturnsName() {
    System.out.println("TEST 2");
    assertEquals(name, myContact1.getName());
  }

  @Test
  public void contactGetNotesReturnsNotes() {
    System.out.println("TEST 3");
    myContact1.addNotes(notes);
    assertEquals(notes, myContact1.getNotes());
  }
}
