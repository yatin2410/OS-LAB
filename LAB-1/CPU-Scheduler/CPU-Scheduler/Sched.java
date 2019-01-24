/* 
 * 
 * Author:- Yatin Patel
 * Id:- 201601454
 * Email:- yatinpatel.gt@gmail.com
 * 
 */

import java.util.*;
import java.io.*;
import Scheduler.Scheduler;

public class Sched {
	
	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler(args[2]);
		File file = new File(args[1]);
		try {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNext()) {
				int pid = scanner.nextInt();
				int at = scanner.nextInt();
				int cb = scanner.nextInt();
				scheduler.setTask(pid, at, cb);
			}
		}
		catch(Exception e) {
			System.out.println("Can't open input File or it is not in pid, arrival time and cpu burst time formate!");
			return;
		}
		if(args[2].equals("RR")) {
//			try {
				scheduler.run(args[3]);				
//			}
//			catch (Exception e) {
//				System.out.println("Please enter Timer time!");
//				return;
//			}
		}
		else {
			scheduler.run();				
		}
	}

}
