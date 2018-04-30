package assignment7;

import java.util.ArrayList;

public class Sequence {
    private String words;
    private String doc;
    private int hash;
    private char value[];

    public Sequence(String words, String doc){
        this.words = words;
        this.doc = doc;
        this.value = words.toCharArray();
    }

    public String getWords(){
        return words;
    }

    public String getDoc(){ return doc;}

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Sequence)) return false;
        Sequence s = (Sequence) o;
        if (words.equals(s.getWords())) return true;
        else return false;
    }

    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }
}
