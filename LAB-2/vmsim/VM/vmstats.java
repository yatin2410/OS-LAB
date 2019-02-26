/**
 * @author YatinPatel
 *
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

public class vmstats {

	public static void main(String[] args) throws IOException {
		int minframes = 0;
		int maxframes = 0;
		int frames =0;
		try {
			minframes = new Integer(args[0]);
			maxframes = new Integer(args[1]);
			frames = new Integer(args[2]);
		}catch (Exception e) {
			System.out.println("Please enter by min page frame, max page frame, diff page frame numbers accordingly");
			return;
		}
		vm vms = new vm();
		int frm[] = new int[((maxframes-minframes)/frames)+1];
		String lru[] = new String[((maxframes-minframes)/frames)+1];
		String opt[] = new String[((maxframes-minframes)/frames)+1];
		String fifo[] = new String[((maxframes-minframes)/frames)+1];
		int j = 0;
		for(Integer i = minframes;i<=maxframes;i+=frames) {
			lru[j] = vms.run(false,i.toString(),args[3], "lru");
			j++;
		}
		j =0;
		for(Integer i = minframes;i<=maxframes;i+=frames) {
			opt[j] = vms.run(false,i.toString(),args[3], "opt");
			j++;
		}
		j=0;
		for(Integer i = minframes;i<=maxframes;i+=frames) {
			fifo[j] = vms.run(false,i.toString(),args[3], "fifo");
			j++;
		}
		String filename = "vmrates.dat";
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
		} catch (IOException e) {
			System.out.println("can't write rates to file");
		}
		for(Integer i=minframes;i<=maxframes;i+=frames) {
			writer.write(i.toString());
			if(i+frames>maxframes) {
				writer.write("\n");
			}
			else {
				writer.write(" ");
			}
		}
		
		for(int i=0;i<j;i++) {
			writer.write(opt[i]);
			if(i!=j-1) {
				writer.write(" ");
			}
			else {
				writer.write("\n");
			}
		}
		
		for(int i=0;i<j;i++) {
			writer.write(lru[i]);
			if(i!=j-1) {
				writer.write(" ");
			}
			else {
				writer.write("\n");
			}
		}
		
		for(int i=0;i<j;i++) {
			writer.write(fifo[i]);
			if(i!=j-1) {
				writer.write(" ");
			}
			else {
				writer.write("\n");
			}
		}
		
		writer.close();
	}
	
}