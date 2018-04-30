package assignment7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("In which directory are the files you would like to compare?");
        String directory = sc.next();
        System.out.println("How many words per sequence?");
        int n = Integer.valueOf(sc.next());
        Map<String, Map<String,Integer>> common = new HashMap<String, Map<String, Integer>>();

        File folder = null;
        File infile = null;
        try{
            folder = new File(directory);
        }
        catch (Exception e){
            System.out.println(e);
        }
        String[] essays = folder.list();
        commonInit(essays, common);
        ArrayList<HashSet<Sequence>> allData = new ArrayList<HashSet<Sequence>>();
        //ArrayList<Sequence> allSeqs = new ArrayList<Sequence>();
        for(String i : essays){
            HashSet<Sequence> DocSeq = new HashSet<Sequence>();
            try{
                infile = new File(directory + "\\" + i);
                BufferedReader br = new BufferedReader(new FileReader(infile));
                ArrayList<String> docWords = new ArrayList<String>();
                String line = br.readLine();
                while(line != null){
                    line = line.toLowerCase();
                    Pattern p = Pattern.compile("[\\w':\\-]+");
                    Matcher m = p.matcher(line);
                    while(m.find()){
                        docWords.add(m.group());
                    }
                    line = br.readLine();
                }
                int j=0;
                while(j+n <= docWords.size()){
                    String newStr = "";
                    for(int k=0; k<n; k++){
                        newStr += docWords.get(j+k);
                    }
                    Sequence newSeq = new Sequence(newStr, i);
                    DocSeq.add(newSeq);
                    //allSeqs.add(newSeq);
                    j++;
                }
                allData.add(DocSeq);
            }
            catch(Exception e){
                System.out.println(e);
            }
        }

        for(int i=0; i<allData.size(); i++){
            for(int j=i+1; j<allData.size(); j++){
                for(Sequence a: allData.get(i)){
                    if(allData.get(j).contains(a)){
                        Iterator<Sequence> it = allData.get(j).iterator();
                        Sequence temp = it.next();
                        if(chooseWhichDoc(temp.getDoc(), a.getDoc())){    //if temp lexicographically follows a
                            Map<String, Integer> newVal = common.get(a.getDoc());
                            newVal.replace(temp.getDoc(), newVal.get(temp.getDoc())+1);
                            common.replace(a.getDoc(), newVal);
                        }
                    }
                }
            }
        }

        int biggest = 0;
        String big = "";
        ArrayList<String> half = new ArrayList<String>();
        ArrayList<String> quarter = new ArrayList<String>();
        ArrayList<String> eighth = new ArrayList<String>();
        for (String key : common.keySet()){
            for(String k : common.get(key).keySet()){
                if (common.get(key).get(k) > biggest) {
                    big = (key + " & " + k);
                    biggest = common.get(key).get(k);
                }
            }
        }

        for (String key : common.keySet()){
            for(String k : common.get(key).keySet()){
                if ((common.get(key).get(k) > biggest/2)&&(common.get(key).get(k)<biggest)) {
                    half.add(key + " & " + k);
                }
                else if ((common.get(key).get(k) > biggest/4)&&(common.get(key).get(k)<biggest)) {
                    quarter.add(key + " & " + k);
                }
                else if ((common.get(key).get(k) > biggest/8)&&(common.get(key).get(k)<biggest)) {
                    eighth.add(key + " & " + k);
                }
            }
        }

        System.out.println(biggest + ": " + big );
        if(half.size() > 0) {
            System.out.println(">= " + biggest/2 + ": ");
            for(String i : half){
                System.out.println("\t"+i);
            }
        }
        if(quarter.size() > 0){
            System.out.println(">= " + biggest / 4 + ": ");
            for(String i : quarter){
                System.out.println("\t"+i);
            }
        }
        if(eighth.size() > 0) {
            System.out.println(">= " + biggest / 8 + ": ");
            for(String i : eighth){
                System.out.println("\t"+i);
            }
        }



        /*for (int i=0; i<allSeqs.size(); i++){
            System.out.println(allSeqs.get(i).getWords());
        }*/


    }

    public static boolean chooseWhichDoc(String a, String b){
        if(a.compareTo(b) < 0) return false; //returns false if a proceeds b
        else return true; //returns true if b proceeds a
    }

    public static void commonInit(String[] docs, Map<String, Map<String, Integer>> common){
        for(String d : docs){
            Map<String, Integer> newM = new HashMap<String, Integer>();
            for(String e : docs){
                if(!(e.equals(d))){
                    if(!(chooseWhichDoc(d,e))) newM.put(e, 0);      //inner map should be alphabetically inferior
                }
            }
            common.put(d, newM);
        }
    }

    /*public static boolean isInside(HashSet<String[]> outer, String[] seq){
        for(String[] a : outer){
            int counter = 0;
            for(int i = 0; i<a.length; i++){
                if(a[i].equals(seq[i])){
                    counter++;
                }
            }
            if (counter == a.length) return true;
        }
        return false;
    }*/
}
