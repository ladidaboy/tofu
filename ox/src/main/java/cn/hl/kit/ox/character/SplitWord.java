package cn.hl.kit.ox.character;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

public class SplitWord {
    private HashMap<String, String> mapEtymon = new HashMap<String, String>();

    private void readFileAndInit() throws Exception {
        println(" ~ Loading Resource and Init...");
        File file = new File(SplitWord.class.getResource("etymon.dat").getFile());

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String data = null;
        int count = 0;
        while ((data = br.readLine()) != null) {
            data = data.trim();
            char type = data.charAt(0);
            if (type == '#') {
                String[] index = data.split(" : ");
                if (index != null) {
                    String value = index[1];
                    String keys = index[0].substring(1);
                    String[] key = keys.split(",");

                    for (int i = 0; i < key.length; i++) {
                        mapEtymon.put(key[i], value);
                        count++;
                    }
                }
            }
        }
        fr.close();

        println(" ~ Loaded " + count + " Etymons!");

    }

    private void welcome() {
        println("+--------------------------------------------------|");
        println("|            SplitWord     2010.4.6                |");
        println("| Note: please input a word                        |");
        println("|       it will be split to a few english etymon   |");
        println("|---------------------------------------------------");
    }

    private static void print(Object o) {
        System.out.print(o);
    }

    private static void println(Object o) {
        System.out.println(o);
    }

    private void readWordToSplit() {
        Scanner sc = new Scanner(System.in);

        String word = "";
        while (word.equals("")) {
            print(" Inputing ~ : ");
            word = sc.nextLine();
        }
        println(" ~ Get Word : " + word);
    }

    public static void main(String[] args) {
        SplitWord sw = new SplitWord();
        sw.welcome();
        try {
            sw.readFileAndInit();
            sw.readWordToSplit();
        } catch (Exception e) {
            println(" ~ SplitWord Error");
        }

    }
}
