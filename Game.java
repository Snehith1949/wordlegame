
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Game
{
  
  private final String CYAN = "\u001B[36m";
  private final String GREEN = "\u001B[32m";
  private final String RESET = "\u001B[0m";
  private final String CLEARCONSOLE = "\033[H\033[2J";

  private Scanner reader = new Scanner(System.in);

  public Game()
  {
    game();
  }

  private void game()
  {
    // Take a random word from the word list to be guessed by the user
    String chosenWord = getWord(getWordList());

    // Create a gameboard that stores all of the user's guesses and alerts them if they are correct or wrong
    String[][] gameBoard = new String[6][5];

    // Fill the gameboard with temporary placeholders
    for(int i = 0; i < gameBoard.length; i++)
    {
      for(int j = 0; j < gameBoard[i].length; j++)
        gameBoard[i][j] = "~";
    }

    // Declare variable userWon that will store if a user has won the game
    boolean userWon = false;

    // Declare the variable attemptNum that will store what attempt the user is on
    int attemptNum = 0;

    // If the user has not won and hasn't used all of thier six attempts, allow them to retry
    while(!userWon && attemptNum < 6)
    {
      // Give the user another attempt by calling the method attempt, and store whether they won or not to the variable userWon
      userWon = attempt(gameBoard, chosenWord, attemptNum);

      // Increase the user's move by one
      attemptNum++;
    }

    // If the user did not win after six attempts, end the game
    if(!userWon)
    {
      // End the game
      gameEnd(gameBoard, chosenWord, false);
    }
  }

  // Declare the method getWordList to get the word list from the file "words.txt"
  private String[] getWordList()
  {
    // Try to get the word list from the file "words.txt"
    try
    {
      // If the file does exist, create the variable words, which is an array, to store all of the words in the word list in
      String[] words = new String(Files.readAllBytes(Paths.get("words.txt"))).split(" ");

      // Return the word list
      return words;
    }
    catch(Exception e)
    {
      // If the file does not exist, give the user a 404 error alerting them that the file does not exist
      System.out.println("Error 404: File \"words.txt\" not found");

      // Exit the program with error code 404
      System.exit(404);

      // Return null to satisfy java syntax
      return null;
    }
  }

  // Declare the method getWord to select a random word from the word list it is given
  private String getWord(String[] words)
  {
    // Declare the variable wordIndex to store a random index in the array declared as words that is not out of bounds
    int wordIndex = (int)(Math.random()*words.length);

    // Retrieve the word stored in the array declared as words at the random index stored in the variable wordIndex
    String word = words[wordIndex];

    // Return the word stored in the variable word
    return word;
  }


  // Declare the method attempt to allow the user to attempt a new word
  private boolean attempt(String[][] gameBoard, String chosenWord, int attempt)
  {
    // Clear Console
    System.out.print(CLEARCONSOLE);

    // Print the game title in terminal
    System.out.println("Wordle\n");

    // Call printGameBoard to print the gameboard in terminal
    printGameBoard(gameBoard);

    // Print a line break in terminal
    System.out.println();

    // Declare the variable loop to loop certain code
    boolean loop = true;

    // Loop a piece of code while the variable loop is set to true
    while(loop)
    {
      // Ask the user to input a five letter word
      System.out.println("Please enter a 5-letter word");
      System.out.print("> ");

      // Declare the variable answer to store the user's input to the variable
      String answer = reader.nextLine();

      // Print a line break in terminal
      System.out.println();

      // Redirect code based on the variable answer's length
      if(answer.length() > 5)
      {
        // If the variable answer's length is bigger than five, alert the user that they cannot enter a word bigger than five letters
        System.out.println("Your word is longer than five letters");
        System.out.println("Please enter a word that has five letters");

        // Print a line break in terminal
        System.out.println();
      }
      else if (answer.length() < 5)
      {
        // If the variable answer's length is smaller than five, alert the user that they cannot enter a word smaller than five letters
        System.out.println("Your word is shorter than five letters");
        System.out.println("Please enter a word that has five letters");

        // Print a line break in terminal
        System.out.println();
      }
      else if(answer.equals(chosenWord))
      {
        // If the variable answer is the chosen word, color green and capatalize all letters in the variable answer and store the variable answer to the gameboard
        for(int i = 0; i < 5; i++)
          gameBoard[attempt][i] = GREEN + Character.toString(answer.charAt(i)).toUpperCase() + RESET;

        // End the game
        gameEnd(gameBoard, chosenWord, true);

        // Return true to alert the program that the user did win
        return true;
      }
      else
      {
        // If the variable answer's length is five letters and is not the chosen word, check it against the word list to see if it is a valid word 
        for(String word : getWordList())
        {
          // If the variable answer is in the word list, check all letters to see if they are either in the word, and if they are in the correct spot if in the word.
          if(answer.equalsIgnoreCase(word))
          {
            // Check all letters using a for loop to see if they are either in the word, and if they are in the correct spot if in the word.
            for(int i = 0; i < 5; i++)
            {
              // Declare the variable letter to store the letter at variable i
              String letter = Character.toString(answer.charAt(i));

              // Check letter to see if they are either in the word, and if they are in the correct spot if in the word.
              if(letter.equals(chosenWord.substring(i, i + 1)))
              {
                // If the variable letter is in the chosen word and is in the same spot compared to the chosen word, color green and capatalize the variable letter
                letter = GREEN + letter.toUpperCase() + RESET;
              }
              else if(chosenWord.contains(letter))
              {
                // If the variable letter is in the chosen word but is not in the same spot compared to the chosen word, color yellow and capatalize the variable letter
                letter = CYAN + letter.toUpperCase() + RESET;
              }
              else
              {
                // If the variable letter is not in the chosen word and is not in the same spot compared to the chosen word, only capatalize the variable letter
                letter = letter.toUpperCase();
              }

              // Store the variable letter to the correct spot in the gameboard
              gameBoard[attempt][i] = letter;
            }

            // Return false to alert the program that the user did not win
            return false;
          }
        }

        // If the variable answer was not in the word list, alert the user that their input is invalid
        System.out.println("Your word was not in the word list!");
        System.out.println("Please try entering a new 5 letter word");

        // Print a line break in terminal
        System.out.println();
      }
    }
    
    // Return false to satisfy java syntax
    return false;
  }

  // Declare the method printGameBoard to print the gameboard in the terminal
  private void printGameBoard(String[][] gameBoard)
  {
    // Print "⌈ " in the terminal 
    System.out.print("⌈ ");

    // Print the first row of the gameboard
    for(int i = 0; i < gameBoard[0].length; i++)
      System.out.print(gameBoard[0][i] + " ");

    // Print "⌉" in the terminal
    System.out.println("⌉");

    // Print rows 2-5 of the gameboard, being surronded by "|", in terminal
    for(int i = 1; i < gameBoard.length - 1; i++)
    {
      // Print "| " in terminal
      System.out.print("| ");

      // Print row i of the gameboard in terminal
      for(int j = 0; j < gameBoard[i].length; j++)
        System.out.print(gameBoard[i][j] + " ");

      // Print "|" in terminal
      System.out.println("|");
    }

    // Print "⌊ " in terminal
    System.out.print("⌊ ");

    // Print row 5 of the gameboard in terminal
    for(int i = 0; i < gameBoard[5].length; i++)
      System.out.print(gameBoard[5][i] + " ");

    // Print "⌋" in terminal
    System.out.println("⌋");
  }

  // Declare the method gameEnd to allow the program to end the game
  private void gameEnd(String[][] gameBoard, String chosenWord, boolean userWon)
  {
    // Clear console
    System.out.print(CLEARCONSOLE);

    // Print the game title in terminal
    System.out.println("Wordle\n");

    // Call printGameBoard to print the gameboard in terminal
    printGameBoard(gameBoard);

    // Print a line break in terminal
    System.out.println();

    // Redirect code based on if user won or not
    if(userWon)
    {
      // If the user won, alert the user that they won
      System.out.println("YOU WON!");

      // Print a line break in terminal
      System.out.println();
    }
    else
    {
      // If the user lost, alert the user that they loat
      System.out.println("You Lost ;-;");
      System.out.println("Better luck next time!");

      // Print a line break in terminal
      System.out.println();

      // Print the chosen word in the terminal
      System.out.println("The word was: " + chosenWord.toUpperCase());

      // Print a line break in terminal
      System.out.println();
    }

    // Declare the variable loop to loop certain code
    boolean loop = true;

    // Loop a piece of code while the variable loop is set to true
    while(loop)
    {
      // Ask user whether or not they want to replay
      System.out.println("Do you want to play again? (Y / N)");
      System.out.print("> ");

      // Declare variable answer to store user input to the variable
      String answer = reader.nextLine();

      // Print a line break to terminal
      System.out.println();

      // Redirect code based on what is stored in variable answer
      if(answer.equalsIgnoreCase("Y") || answer.equalsIgnoreCase("Yes"))
      {
        
        game();

       
        loop = false;
      }
      else if(answer.equalsIgnoreCase("N") || answer.equalsIgnoreCase("No"))
      {
        
        System.out.print(CLEARCONSOLE);

       
        reader.close();

        
        System.out.println("Wordle\n");

        
        System.out.println("Oh okay ;-;");
        System.out.println("Thanks for playing!");

        
        loop = false;
      }
      else
      {
       
        System.out.println("Your input is not valid");
        System.out.println("Please enter \"Y\" or \"N\"");

        
        System.out.println();
      }
    }
  }

  
  public static void main(String[] args)
  {
    
    Game app = new Game();
  }
}