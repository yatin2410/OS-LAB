/**
 * @author YatinPatel
 *
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class vmgen {

	public static void main(String[] args) throws IOException {
		int mx = 0;
		int numbers = 0;
		try {
			mx = new Integer(args[0]);
			numbers = new Integer(args[1]);
		}
		catch(Exception e) {
			System.out.println("Please enter integer numbers");
			return;
		}
		String filename;
		try {
			filename = args[2];
		}
		catch(Exception e) {
			System.out.println("Please enter filename!");
			return;
		}
		int prev = -1;
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		for(int i=0;i<numbers;i++) {
			Random random = new Random();
			int lb = 0;
			int ub = mx-1;
			Integer res = random.nextInt(ub-lb)+lb;
			if(res==prev) {
				i--;
				continue;
			}
			else {
				writer.write(res.toString()+" ");
				prev = res;
			}
		}
	    writer.close();
	}
	
}
