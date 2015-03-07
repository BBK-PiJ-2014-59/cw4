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
  /**
   * Prompts user for a String at console and returns user's response stripped of unnecessary whitespace.
   * 
   * @param prompt user prompt.   
   * @throws NullPointerException if prompt is null.
   * @return user's response trimmed of leading and trailing whitespace; any tabs or
   * multiple spaces inside the string are also replaced by a single space.
   */
  String prompt(String prompt);

}
