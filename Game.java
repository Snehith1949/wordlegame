
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
   
    String chosenWord = getWord(getWordList());

   
    String[][] gameBoard = new String[6][5];

    
    for(int i = 0; i < gameBoard.length; i++)
    {
      for(int j = 0; j < gameBoard[i].length; j++)
        gameBoard[i][j] = "~";
    }

  
    boolean userWon = false;

    
    int attemptNum = 0;

    
    while(!userWon && attemptNum < 6)
    {
     
      userWon = attempt(gameBoard, chosenWord, attemptNum);

     
      attemptNum++;
    }

    
    if(!userWon)
    {
      
      gameEnd(gameBoard, chosenWord, false);
    }
  }


  private String[] getWordList()
  {
    
    try
    {
      
      String[] words = new String(Files.readAllBytes(Paths.get("words.txt"))).split(" ");

    
      return words;
    }
    catch(Exception e)
    {
     
      System.out.println("Error 404: File \"words.txt\" not found");

     
      System.exit(404);

      
      return null;
    }
  }
  private String getWord(String[] words)
  {
    
    int wordIndex = (int)(Math.random()*words.length);
    String word = words[wordIndex];
    return word;
  }
  private boolean attempt(String[][] gameBoard, String chosenWord, int attempt)
  {
    
    System.out.print(CLEARCONSOLE);
    System.out.println("Wordle\n");
    printGameBoard(gameBoard);
    System.out.println();
    boolean loop = true;
    while(loop)
    {
      System.out.println("Please enter a 5-letter word");
      System.out.print("> ");
      String answer = reader.nextLine();
      System.out.println();
      if(answer.length() > 5)
      {
       
        System.out.println("Your word is longer than five letters");
        System.out.println("Please enter a word that has five letters");

        System.out.println();
      }
      else if (answer.length() < 5)
      {
        
        System.out.println("Your word is shorter than five letters");
        System.out.println("Please enter a word that has five letters");
        System.out.println();
      }
      else if(answer.equals(chosenWord))
      {
        for(int i = 0; i < 5; i++)
          gameBoard[attempt][i] = GREEN + Character.toString(answer.charAt(i)).toUpperCase() + RESET;

        // End the game
        gameEnd(gameBoard, chosenWord, true);
        return true;
      }
      else
      {
        for(String word : getWordList())
        {
          if(answer.equalsIgnoreCase(word))
          {
           
            for(int i = 0; i < 5; i++)
            {
             
              String letter = Character.toString(answer.charAt(i));

             
              if(letter.equals(chosenWord.substring(i, i + 1)))
              {
                
                letter = GREEN + letter.toUpperCase() + RESET;
              }
              else if(chosenWord.contains(letter))
              {
                
                letter = CYAN + letter.toUpperCase() + RESET;
              }
              else
              {
                letter = letter.toUpperCase();
              }
              gameBoard[attempt][i] = letter;
            }

            return false;
          }
        }

        
        System.out.println("Your word was not in the word list!");
        System.out.println("Please try entering a new 5 letter word");

        
        System.out.println();
      }
    }
    
    
    return false;
  }

 
  private void printGameBoard(String[][] gameBoard)
  {
   
    System.out.print("⌈ ");

  
    for(int i = 0; i < gameBoard[0].length; i++)
      System.out.print(gameBoard[0][i] + " ");

    
    System.out.println("⌉");

    
    for(int i = 1; i < gameBoard.length - 1; i++)
    {
      
      System.out.print("| ");

     
      for(int j = 0; j < gameBoard[i].length; j++)
        System.out.print(gameBoard[i][j] + " ");

      
      System.out.println("|");
    }

    // Print "⌊ " in terminal
    System.out.print("⌊ ");

   
    for(int i = 0; i < gameBoard[5].length; i++)
      System.out.print(gameBoard[5][i] + " ");

   
    System.out.println("⌋");
  }


  private void gameEnd(String[][] gameBoard, String chosenWord, boolean userWon)
  {
   
    System.out.print(CLEARCONSOLE);

   
    System.out.println("Wordle\n");

    
    printGameBoard(gameBoard);

   
    System.out.println();

  
    if(userWon)
    {
     
      System.out.println("YOU WON!");
      System.out.println();
    }
    else
    {
     
      System.out.println("You Lost ;-;");
      System.out.println("Better luck next time!");

      System.out.println();


      System.out.println("The word was: " + chosenWord.toUpperCase());

      System.out.println();
    }

   
    boolean loop = true;

   
    while(loop)
    {
      
      System.out.println("Do you want to play again? (Y / N)");
      System.out.print("> ");

     
      String answer = reader.nextLine();

   
      System.out.println();

     
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
