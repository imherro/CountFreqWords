package com.haojianuo.eng;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static HashSet hs= new HashSet();
    public static HashMap<String,Integer> hm = new HashMap();


    public static int j=0;
    public static void main(String[] args) {
	// write your code here

        count(System.getProperty("user.dir")+"/db/srt");
        removeTransWords();
        printHM();
    }



        private static void count(String fileDir) {
            File file = new File(fileDir);
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files.length == 0) {
                    System.out.println("文件夹是空的!");
                    return;
                } else {
                    for (File file2 : files) {
                        if (file2.isDirectory()) {
                            System.out.println("文件夹:" + file2.getAbsolutePath());

                            for (File file3 : new File(file2.getAbsolutePath()).listFiles()) {
                                if (file3.isDirectory()) {
                                    System.out.println("文件夹:" + file3.getAbsolutePath());

                                } else {
                                    System.out.println("文件:" + file3.getAbsolutePath());
                                    countWords(file3.getAbsolutePath());
                                }
                            }
                        } else {
                            System.out.println("文件:" + file2.getAbsolutePath());
                            countWords(file2.getAbsolutePath());
                        }
                    }
                }
            } else {
                System.out.println("文件不存在!");
            }
        }
    private static void countWords(String file){
        try {
            File filename = new File(file); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            line = br.readLine();
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                if(line==null || line.length()==0){continue;}

                String[] al = line.split(" ");
                if(al.length>0){
                    for (int i = 0; i < al.length; i++) {
                        j++;
                        al[i]=al[i].toLowerCase();
                        al[i]=al[i].replace("--","");
                        if(al[i].indexOf("-")==0){al[i]=al[i].replace("-","");}
                        if(al[i].indexOf("'")==0){al[i]=al[i].replace("'","");}
                        if(al[i].indexOf("'")==al[i].length()-1){al[i]=al[i].replace("'","");}
                        if(al[i].indexOf("-")==al[i].length()-1){al[i]=al[i].replace("-","");}
                        if(al[i].indexOf("'s")==al[i].length()-2){al[i]=al[i].replace("'s","");}
                        String  t=al[i].replace("'","");
                        t=t.replace("-","");
                        if(!t.matches("[a-z]+")) continue;
                        if(hs.add(al[i])){
                            hm.put(al[i],new Integer(1));
                        }else{
                            hm.put(al[i],new Integer(Integer.valueOf(""+hm.get(al[i]))+1));
                        }
                    }
                }
            }
            System.out.println("count:"+j+" hs:"+hs.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void removeTransWords(){

        System.out.println("before remove Transform:"+hs.size());
        try {
            File filename = new File(System.getProperty("user.dir")+"/db/EnglishWordsTransform.txt"); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            line = br.readLine();

            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                if(line==null || line.length()==0){continue;}
                line =  line.replace("\t"," ");
                String[] al = line.split(" ");
                if(al.length>1){
                    for (int i = 1; i < al.length; i++) {
                        if(hs.contains(al[i]) && hm.get(al[0])!=null && hm.get(al[i])!=null) {
                            int a = Integer.parseInt("" + hm.get(al[0]));
                            int b = Integer.parseInt("" + hm.get(al[i]));
                            hm.put(al[0], new Integer(b + a));

                            hs.remove(al[i]);
                            hm.remove(al[i]);
                        }
                    }
                }
            }

            System.out.println("After remove Transform:"+hs.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void printHM(){

        List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(hm.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
            //升序排序
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return 0-o1.getValue().compareTo(o2.getValue());
            }

        });
        int i=0;
        for (Map.Entry<String, Integer> mapping : list) {

            if(i%10==0){System.out.println(":" + mapping.getValue());System.out.print(""+i+":  ");}
            System.out.print(mapping.getKey() +"  ");

            i=i+1;
        }
    }

}
