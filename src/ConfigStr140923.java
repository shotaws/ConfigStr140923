import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;


public class ConfigStr140923 {

	public static void main(String[] args) {
		
		try {
			int files = 10; //files個できる（seedの値は123から122+filesまで）
			int start = 123;
			int check = 0;
			int thread = 1;
			int flag = 0;
			int i, j;
			
            String line;
            String[] strAry;
            String tmp = null;
			
			//コピー元
            FileReader[] in = new FileReader[2];
            in[0] =	new FileReader(args[0]);

            BufferedReader[] br = new BufferedReader[2];
            br[0] = new BufferedReader(in[0]);

            try{
            	in[1] =	new FileReader("Numbers.txt");
            	br[1] = new BufferedReader(in[1]);
            	i = 0;
            	while ((line = br[1].readLine()) != null){
            		if(i == 0)
            			start = Integer.valueOf(line);
            		else if(i == 1)
            			files = Integer.valueOf(line);
            		else if(i == 2)
            			check = Integer.valueOf(line);
            		else
            			thread = Integer.valueOf(line);
            		i++;
            	}
            	System.out.println("Numbers.txtがあるためseed = " + start + "から" + files + "個作成します。");
            }catch(FileNotFoundException e){
            	System.out.println("Numbers.txtが無いためとりあえずseed = " + start + "から" + files + "個作成します。");
            	flag = 1;
            }
            
            if(check == 0) System.out.println("マルチスレッドOFF");
            else System.out.println("マルチスレッドON");
            
            FileWriter[] out = new FileWriter[files];
            BufferedWriter[] bw = new BufferedWriter[files];
            
            //シェルスクリプト用　(MyBatch内のconfigを持ってきて実行)
            FileWriter out2 = new FileWriter("path.txt");
            BufferedWriter bw2 = new BufferedWriter(out2);
            
        	//MyBatchというファイルを作成
        	String dirtmp =  "./MyBatch" ;
        	File newfile = new File(dirtmp);
            if (newfile.mkdir()){
                //System.out.println("ディレクトリの作成に成功しました");
              }else{
                //System.out.println("ディレクトリの作成に失敗しました（もう作成されているかも）");
            }

            for(i = 0; i < files; i++){
                //seedの値のファイルを作成
                dirtmp =  "./MyBatch/" + (start + i);
            	newfile = new File(dirtmp);
                if (newfile.mkdir()){
                    //System.out.println("ディレクトリの作成に成功しました");
                  }else{
                    //System.out.println("ディレクトリの作成に失敗しました（もう作成されているかも）");
                }
            	
                //複製先のファイルを作成
            	String tmp2 = "./MyBatch/" + (start + i) + "/test" + (start + i) + ".config";
            	bw2.write(tmp2);
            	bw2.newLine();
            	out[i] = new FileWriter(tmp2);
            	bw[i] = new BufferedWriter(out[i]);
            }

            while ((line = br[0].readLine()) != null) {
            	//System.out.println(line);
            	strAry = line.split(" ");
                if(strAry[0].equals("seed")){
                	//strAry = line.split(" ");
                	for(i = 0; i < strAry.length - 1; i++){
                		if(i == 0)
                			tmp = strAry[i];
                		else
                			tmp += strAry[i];
                		
                		tmp += " ";
                		//System.out.println(strAry[i]);
                	}
                	//System.out.println(tmp);

                	for(j = 0; j < files; j++)
                		bw[j].write(tmp + (start + j));
                	
                	if(check == 0){
                		for(j = 0; j < files; j++)
                    	bw[j].newLine();
                	}
                	
                	else if(check != 0){
                    	for(j = 0; j < files; j++){
                        	bw[j].newLine();
                           	bw[j].write("parallelization-partition-index = " + thread);
                    		bw[j].newLine();
                    	}
                    }
                }
                
                else {
                	for(j = 0; j < files; j++)
                		bw[j].write(line);
                }
                
                for(j = 0; j < files; j++)
                	bw[j].newLine();
                
            }

            in[0].close();
            for(j = 0; j < files; j++){
            	bw[j].flush();
                out[j].close();
            }
            
            bw2.flush();
            out2.close();
            if(flag==0)
            	in[1].close();
        } catch (IOException e) {
            System.out.println(e);
        }

	}

}
