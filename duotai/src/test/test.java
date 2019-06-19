package test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class test {

	public static void main(String[] args) throws IOException {
				//File dir =new File("d:\\fw.txt");
		
//				FileWriter fw = new FileWriter("d:\\fw1.txt");
//				fw.write("你好谢谢再见");//这些文字都要先编码。都写入到了流的缓冲区中。
//				fw.flush();
//				fw.close();
			FileReader fr =new FileReader("d:\\fw1.txt");
			int a=0;
			char[] buf=new char[1024];
			while((a=fr.read(buf))!=-1){
				System.out.println(buf);
			}
			
			fr.close();
	}

}
