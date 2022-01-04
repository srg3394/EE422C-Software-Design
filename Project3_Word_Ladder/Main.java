/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Michael Fortanely>
 * <Student1 mjf2894>
 * <Student1 76000>
 * <Student2 Sathvik Gujja>
 * <Student2 srg3394>
 * <Student2 76000>
 * Slip days used: <0>
 * Git URL:
 * Summer 2021
 */


package assignment3;
import java.lang.reflect.Array;
import java.util.*;
import java.io.*;

public class Main {

    // static variables and constants only here.
    static Set<String> dictionary;
    public static void main(String[] args) throws Exception {

        Scanner kb;	// input Scanner for commands
        PrintStream ps;	// output file, for student testing and grading only
        // If arguments are specified, read/write from/to files instead of Std IO.
        if (args.length != 0) {
            kb = new Scanner(new File(args[0]));
            ps = new PrintStream(new File(args[1]));
            System.setOut(ps);			// redirect output to ps
        } else {
            kb = new Scanner(System.in);// default input from Stdin
            ps = System.out;			// default output to Stdout
        }
        initialize();
        ArrayList<String> words = parse(kb);
        if(words != null){
            String word1 = words.get(0).toUpperCase();
            String word2 = words.get(1).toUpperCase();
            ArrayList<String> answer = getWordLadderDFS(word1, word2);
           printLadder(answer);
        }
        // TODO methods to read in words, output ladder
    }

    public static void initialize() {
        // initialize your static variables or constants here.
        // We will call this method before running our JUNIT tests.  So call it
        // only once at the start of main.
        dictionary = makeDictionary();
    }

    /**
     * @param keyboard Scanner connected to System.in
     * @return ArrayList of Strings containing start word and end word.
     * If command is /quit, return empty ArrayList.
     */
    public static ArrayList<String> parse(Scanner keyboard) {
        String word1 = keyboard.next();
        ArrayList<String> words = new ArrayList<>();
        if(word1.equals("/quit")){
            return null;
        }
        String word2 = keyboard.next();
        words.add(word1);
        words.add(word2);
        // TO DO
        return words;
    }

    public static ArrayList<String> getWordLadderDFS(String start, String end) {

        // Returned list should be ordered start to end.  Include start and end.
        // If ladder is empty, return list with just start and end.
        // TODO some code
        start = start.toUpperCase();
        end = end.toUpperCase();

        char[] ending = end.toUpperCase().toCharArray();
        Set<String> wordsVisited = new HashSet<>();
        ArrayList<String> dfs = new ArrayList<>();


        wordsVisited.add(start);
        recursiveDFS(start, end, dfs, wordsVisited, ending);
        if(dfs.size() == 0){
            dfs.add(end);
        }
        dfs.add(0, start);
        return dfs;
        }

     public static boolean recursiveDFS(String start, String end, ArrayList<String> dfs, Set<String> wordsVisited, char[] ending ){
        if(start.equals(end)){
            return true;
        }
         ArrayList<String> allVariationsOfWordBy1;
         allVariationsOfWordBy1 = generateTargetedVariations(start, ending);
         for (String s : allVariationsOfWordBy1) {
             if (!wordsVisited.contains(s)) {
                 wordsVisited.add(s);
                 boolean isPredecessor = recursiveDFS(s, end, dfs, wordsVisited, ending);
                 if (isPredecessor) {
                     dfs.add(0, s);
                     return true;
                 }
             }
         }
         return false;
     }

     public static ArrayList<String> generateTargetedVariations(String string, char[] ending){
         ArrayList<String> list = new ArrayList<>();
         StringBuilder sb = new StringBuilder(string);
         HashSet<String> targetString = new HashSet<>();
         for(int i = 0; i < 5; i++){
             sb = new StringBuilder(string);
             //for each letter
             sb.replace(i,i + 1, String.valueOf(ending[i]));
             if(dictionary.contains(sb.toString()) && !string.equals(sb.toString()) && !targetString.contains(sb.toString())){
                 targetString.add(sb.toString());
                 list.add(0, sb.toString());
             }
                 sb = new StringBuilder(string);
             for(int j = 0; j < 26; j++){
                 sb.replace(i,i + 1, String.valueOf((char) ('A' + j)));
                 if(dictionary.contains(sb.toString()) && !string.equals(sb.toString()) && !targetString.contains(sb.toString())){
                     targetString.add(sb.toString());
                     list.add(sb.toString());
                 }
             }
         }
         return list;
     }
    public static ArrayList<String> getWordLadderBFS (String start, String end) {

        start = start.toLowerCase();
        end = end.toLowerCase();

        Queue<String> words = new LinkedList<>();					// queue for words to be checked
        Set<String> checked = new HashSet<>();					// words that already have been checked
        HashMap<String, String> parent = new HashMap<>();	// holds the parent (key,child value,parent)

        Iterator<String> iter;
        String current;		                          // holds the current word
        String checkWord;						      // holds the dictionary word being compared to current
        words.add(start.toUpperCase());
        checked.add(start.toUpperCase());

        while ( !( checked.contains(end.toUpperCase()) || words.isEmpty() ) ) {

            iter = dictionary.iterator();
            current = words.poll();
            while (iter.hasNext()) {
                checkWord = (String) iter.next();
                if (oneLetter(current, checkWord) &&  !(checked.contains(checkWord)) ) { // compares the current and checkWord
                    checked.add(checkWord);
                    words.add(checkWord);
                    parent.put(checkWord, current);
                }
            }
        }

        ArrayList<String> result = new ArrayList<>();

        if (!(checked.contains(end.toUpperCase()))) { // if ending word is not in the queue, returns a result arraylist with just the start and end word
            result.add(start);
            result.add(end);
            return result;
        }

        result.add(start);
        result.add(end);

        while ( !(start.equals(parent.get(result.get(1).toUpperCase()).toLowerCase())) ) { // works backwords and adds parents to the result arraylist
            result.add(1, parent.get(result.get(1).toUpperCase()).toLowerCase());
        }

        return result;
    }


    private static boolean oneLetter(String word1, String word2) {
        word1 = word1.toUpperCase();
        word2 = word2.toUpperCase();
        if(word1.length() != word2.length()) {
            return false;
        }
        int check = 0;
        for(int i = 0; i < word1.length(); i++) {
            if(word1.charAt(i) != word2.charAt(i)) {
                check++;
            }
        }

        return check != 0 && check <= 1;
    }





    public static void printLadder(ArrayList<String> ladder) {
        String word1 = ladder.get(0).toLowerCase();
        String word2 = ladder.get(ladder.size() - 1).toLowerCase();
        if(ladder.size() == 2){
            System.out.println("no word ladder can be found between " + word1 + " and " + word2 + ".");
        } else {
            System.out.println("a " + ladder.size() + "-rung word ladder exists between " + word1 + " and " + word2 + ".");
            for (String s : ladder) {
                System.out.println(s.toLowerCase());
            }
        }
    }
    // TODO
    // Other private static methods here


    /* Do not modify makeDictionary */
    public static Set<String>  makeDictionary () {
        Set<String> words = new HashSet<>();
        Scanner infile = null;
        try {
            infile = new Scanner (new File("five_letter_words.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Dictionary File not Found!");
            e.printStackTrace();
            System.exit(1);
        }
        while (infile.hasNext()) {
            words.add(infile.next().toUpperCase());
        }
        return words;
    }
}
