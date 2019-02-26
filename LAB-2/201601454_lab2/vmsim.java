/**
 * @author YatinPatel
 *
 */

import java.io.File;
import java.text.DecimalFormat;
import java.util.Scanner;

public class vmsim {

	public static void main(String[] args) {
		vm vms = new vm();
		try{
			vms.run(true, args[0], args[1], args[2]);	
		}
		catch(Exception e){
			System.out.println("Please enter in proper manner");
		}
		return;
	}

}