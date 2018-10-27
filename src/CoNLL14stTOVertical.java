import java.io.*;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.util.zip.GZIPInputStream;

public class CoNLL14stTOVertical {
    static int x;

    public static String conversion(String tok){
       if(tok.contains("Vt")){
           return ("Verb Tense (Vt)");
       }
        else if(tok.contains("Vm")){
            return ("Verb Model (Vm)");
        }
       else if(tok.contains("Vform")){
           return ("Verb Form (Vform)");
       }
       else if(tok.contains("V0")){
           return ("Verb missing (V0)");
       }
       else if(tok.contains("SVA")){
           return ("Subject-verb agreement (SVA)");
       }
       else if(tok.contains("ArtOrDet")){
           return ("Article or determiner (ArtOrDet)");
       }
       else if(tok.contains("Nn")){
           return ("Noun number (Nn)");
       }
       else if(tok.contains("Npos")){
           return ("Noun possessive (Npos)");
       }
       else if(tok.contains("Pform")){
           return ("Pronoun form (Pform)");
       }
       else if(tok.contains("Pref")){
           return ("Pronoun reference (Pref)");
       }
       else if(tok.contains("Pre")){
           return ("Preposition (Pre)");
       }
       else if(tok.contains("Wci")){
           return ("Wrong collocation/idiom (Wci)");
       }
       else if(tok.contains("Wa")){
           return ("Acronyms (Wa)");
       }
       else if(tok.contains("Wform")){
           return ("Word form (Wfrom)");
       }
       else if(tok.contains("Wtone")){
           return ("Tone (formal/informal) (Wtone)");
       }
       else if(tok.contains("Srun")){
           return ("Run-on sentences, comma splices (Srun)");
       }
       else if(tok.contains("Smod")){
           return ("Dangling modifiers (Smod)");
       }
       else if(tok.contains("Spar")){
           return ("Parallelism (Spar)");
       }
       else if(tok.contains("Sfrag")){
           return ("Sentence fragment (Sfrag)");
       }
       else if(tok.contains("Ssub")){
           return ("Subordinate clause (Ssub)");
       }
       else if(tok.contains("WOinc")){
           return ("Incorrect word order (WOinc)");
       }
       else if(tok.contains("WOadv")){
           return ("Incorrect adjective or adverb order (WOadv)");
       }
       else if(tok.contains("Trans")){
           return ("Linking words/phrases (Trans)");
       }
       else if(tok.contains("Mec")){
           return ("Spelling, punctuation or capitalization, etc. (Mec)");
       }
       else if(tok.contains("Rloc")){
           return ("Redundancy (Rloc)");
       }
       else if(tok.contains("Cit")){
           return ("Citation (Cit)");
       }
       else if(tok.contains("Um")){
           return ("Unclear meaning (Um)");
       }
       else {
           return ("Other errors");
       }

    }

    public static void main (String[] args) throws IOException {

        String fileName = "/Users/jasmeetkaurchawla/IdeaProjects/CSE467/src/conll14st-preprocessedextra.m2";
        //	BufferedReader d = new BufferedReader(new InputStreamReader(new FileInputStream(new File (fileName))));
        //                OutputStreamWriter out = new OutputStreamWriter (new FileOutputStream(fileName+".1"), "UTF-8");
        BufferedReader d;
        if (fileName.endsWith(".gz")) {
            d = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(new File(fileName)))));
        }
        else {
            d = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
        }
        String str = new String();
        str = d.readLine();

        String[][] ar = new String[1024][2];
        int id = 0;

        boolean flag = true;

        while (str != null) {
            str = str.trim();

            if (str.startsWith("S ")) {
                System.out.println("## " + str);
                String delim = " ";
                String[] entry;
                entry = str.split(delim);

                for (int i=0; i<entry.length; i++) {
                    String tok = entry[i];
                    ar[id++][0] = tok;
                }
            }
            else if (str.startsWith("A")) {
                System.out.println("## " + str);
//				System.out.println(str);
                String label = new String();
                String correct = new String();

                String delim = "|";
                StringTokenizer st = new StringTokenizer(str, delim);
                int addr = 0;
                int start = -1;
                int end = -1;


                //System.out.println(id);

                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
					/*
					   if (addr == 1)  {
					   start = Integer.parseInt(tok);
					   }
					   else if (addr == 2) {
					   end = Integer.parseInt(tok);
					//System.out.println(ar[end][0]);
					}
					else if (addr == 3) {
					label = tok;
					ar[end][1] = tok + "\t";
					}
					else if (addr == 4) {
					correct = tok;
					ar[end][1] += tok;
					}
					 */
                    addr++;
                    //System.out.println("### " + addr + "\t" + tok);

                    if (addr == 1) {
                        start = -1;
                        end = -1;
                        String temp = tok.substring(tok.indexOf(" "), tok.length()).trim();
                        start = Integer.parseInt(temp.substring(0, temp.indexOf(" ")).trim());
                        end = Integer.parseInt(temp.substring(temp.indexOf(" ")+1, temp.length()).trim());
                        if (start==end){
                            x=1;
                        }
                        else{
                            x=0;
                        }

                    }
                    else if (addr == 2) {
                        label = tok;
                        if (x == 1){
                                ar[end][1] = "add ("+ conversion(tok) + ")"+ "\t";
                        }

                        else{
                            ar[end][1] = conversion(tok) + "\t";
                        }

                    }
                    else if (addr == 3) {
                        correct = tok;

                        if(tok.contains("REQUIRED")){
                            tok="delete";
                            ar[end][1] += tok;
                        }
                        else{
                            ar[end][1] += tok;
                        }
                    }


                }
                flag = false;
            }
            else if (str.length()==0) {

                if (id>0) {
                    for (int i=1; i<id;i++) { // 0 -> print "S" ; 1 -> not print "S";
                        if (ar[i][1]!=null) {
                            System.out.println(ar[i][0] + "\t" + ar[i][1]);
                        }
                        else {
                            System.out.println(ar[i][0] + "\tO\tO" );
                        }
                    }
                    System.out.println();
                }
                id = 0;
                ar = new String[1024][2];
            }

            str = d.readLine();
        }
        d.close();
    }
}
