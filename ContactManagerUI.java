/**
 * A class to run user interface to ContactManager
 */
public interface ContactManagerUI { 
  /**
   * Starts user interface by creating a ContactManager object (based on a textfile if one is available). 
   * 
   * @param textfile filename for persistent storage of contacts and meetings 
   * @returns contact manager object
   * @throws NullPointerException if textfile is null.
   * @throws IOException if file referred to by textfile is unreadable or unwritable.
   * @throws IllegalArgumentException if textfile isn't a String or file referred to by it isn't in JSON (GSON) format.
   */
  ContactManager launch(String textfile);
  /**
   * Lets user manage meetings and contacts by manipulating ContactManager object.
   * @param cm contains the contact/meeting content to manipulate 
   * @throws NullPointerException if cm is null.
   * @throws IllegalArgumentException if cm is not a ContactManager object.
   */
  void display(ContactManager cm);
}
