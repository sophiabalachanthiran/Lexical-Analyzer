
//Sophia Balachanthiran
//CSCI316 Summer 2023 8am-10:40am
//A lexical Analyzer for a subset of the Java Programming Language

import java.io.*;
import java.util.*;

public class LexicalAnalyzer {
    private static List<String> reservedWords;
    private static List<String> operators;
    private static Set<Character> acceptCharacters;
    private static int currentIndex;

    //Initializing reserved words, operators, and valid characters
    static {
        reservedWords = Arrays.asList("if", "else");
        operators = Arrays.asList("=", "==", "<", ">", "<=", ">=", "+", "-", "*", "/");
        acceptCharacters = new HashSet<>();
        for (char c = 'a'; c <= 'z'; c++) {
        	acceptCharacters.add(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
        	acceptCharacters.add(c);
        }
        for (char c = '0'; c <= '9'; c++) {
        	acceptCharacters.add(c);
        }
        acceptCharacters.add('_');
    }
    

    public static void main(String[] args) {
        try {
            File inputFile = new File("input.txt");
            Scanner scanner = new Scanner(inputFile);

            while (scanner.hasNextLine()) {
                String statement = scanner.nextLine();
                analyzeStatement(statement);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void analyzeStatement(String statement) {
        currentIndex = 0;

        while (currentIndex < statement.length()) {
            char currentChar = getChar(statement);
            
            // Check to see if it starts with a comment (//)
            if (currentChar == '/' && currentIndex < statement.length() && statement.charAt(currentIndex) == '/') {
                // Skips the rest of the line and continues to next statement
                break;
            }

            // Check to see if it starts with a comment (/*)
            if (currentChar == '/' && currentIndex < statement.length() && statement.charAt(currentIndex) == '*') {
            	skipComment(statement);
                continue;
            }
            
            if (Character.isWhitespace(currentChar)) {
                //Ignore whitespaces
                continue;
            }

            if (Character.isLetter(currentChar) || currentChar == '_') {
                String identifier = addChar(statement, currentChar);
                if (isReservedWord(identifier)) {
                    System.out.println("Next Token is: RESERVED WORD\tNext Lexeme is: " + identifier);
                } else {
                    System.out.println("Next Token is: IDENTIFIER\tNext Lexeme is: " + identifier);
                }
            } else if (Character.isDigit(currentChar)) {
                String constant = addChar(statement, currentChar);
                System.out.println("Next Token is: CONSTANT\t\tNext Lexeme is: " + constant);
            } else if (isOperator(String.valueOf(currentChar))) {
                String operator = addChar(statement, currentChar);
                System.out.println("Next Token is: OPERATOR\t\tNext Lexeme is: " + operator);
            } else {
                System.out.println("Next Token is: UNKNOWN\t\tNext Lexeme is: " + currentChar);
            }
        }
    }
    
    private static void skipComment(String statement) {
        //Case where the current character is '/' and the next character is '*'
        currentIndex += 2;

        while (currentIndex < statement.length()) {
            char currentChar = statement.charAt(currentIndex);
            char nextChar = currentIndex < statement.length() - 1 ? statement.charAt(currentIndex + 1) : '\0';

            // Check to see if it is the end of the comment (*/)
            if (currentChar == '*' && nextChar == '/') {
                currentIndex += 2;
                break;
            }

            currentIndex++;
        }
    }

    //Gets the next character of the input
    private static char getChar(String statement) {
        return statement.charAt(currentIndex++);
    }


    private static String addChar(String statement, char currentChar) {
        StringBuilder lexeme = new StringBuilder();
        lexeme.append(currentChar);

        while (currentIndex < statement.length()) {
            char nextChar = statement.charAt(currentIndex);
            if (acceptCharacters.contains(nextChar)) {
                lexeme.append(nextChar);
                currentIndex++;
            } else {
                break;
            }
        }

        return lexeme.toString();
    }

    private static boolean isReservedWord(String token) {
        return reservedWords.contains(token);
    }

    private static boolean isOperator(String token) {
        return operators.contains(token);
    }
}