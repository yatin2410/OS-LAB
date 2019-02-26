/* 
 * 
 * Author:- Yatin Patel
 * Id:- 201601454
 * Email:- yatinpatel.gt@gmail.com
 * 
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.management.Query;

class Tast {
	private int pid;
	private int arrival_time;
	private int cpu_burst;
	public void set(int pid,int arrival_time,int cpu_burst) {
		this.pid = pid;
		this.arrival_time  = arrival_time;
		this.cpu_burst = cpu_burst;
	}
	public int getPID() {
		return this.pid;
	}
	public int getAT() {
		return this.arrival_time;
	}
	public int getCB() {
		return this.cpu_burst;
	}
	public void dec(int timer) {
		this.cpu_burst -= timer;
	}
}

class SortBySJF implements Comparator<Tast>
{
	public int compare(Tast a,Tast b) {
		return a.getCB()-b.getCB();
	}
}

class SortBy implements Comparator<Tast>
{
	public int compare(Tast a,Tast b) {
		return a.getAT() - b.getAT();
	}
}

class SortBy1 implements Comparator<Tast>
{
	public int compare(Tast a,Tast b) {
		if(a.getAT()==b.getAT()) {
			return a.getCB()-b.getCB();
		}
		return a.getAT() - b.getAT();
	}
}

class Scheduler {
	private String algoType = "";
    ArrayList<Tast> tasks = new ArrayList<Tast>(); 
	public Scheduler(String string) {
		algoType = string;
	}
	public void setTask(int pid,int at,int cb) {
		Tast tast = new Tast();
		tast.set(pid, at, cb);
		this.tasks.add(tast);
	}
	public void run() {
		System.out.println("Scheduling Algorithm is : "+algoType);
		System.out.println("Total "+tasks.size()+" are read from file.");
		System.out.println("============================================================");
		if(algoType.equals("FCFS")) {
			Collections.sort(tasks,new SortBy());
			int time = 0;
			int tRes = 0;
			int tWait = 0;
			int tTur = 0;
			for(int i=0;i<tasks.size();i++)
			{
				while(time<tasks.get(i).getAT()) {
					System.out.println("<system time "+time+"> No process is runnig");
					time++;
				}
				tRes += (time-tasks.get(i).getAT());
				for(int j=1;j<tasks.get(i).getCB();j++) {
					time++;
					System.out.println("<system time "+time+"> process "+ tasks.get(i).getPID() + " is running");
				}
				time++;
				System.out.println("<system time "+time+"> process "+  tasks.get(i).getPID()+ " is finished.....");
				tWait += (time-(tasks.get(i).getAT()+tasks.get(i).getCB()));
				tTur += (time-tasks.get(i).getAT());
				
			}
			System.out.println("<system time "+time+"> All process are finished..........");
			System.out.println("============================================================");
			double ares = ((double)tRes)/tasks.size();
			double await = ((double)tWait)/tasks.size();
			double atur = ((double)tTur)/tasks.size();
			System.out.println("Average waiting time is : "+ await);
			System.out.println("Average response time is : "+ ares);
			System.out.println("Average turnaround time is : "+ atur);
			System.out.println("============================================================");
			
		}
		else if(algoType.equals("SJF")) {
			Collections.sort(tasks,new SortBy1());
		    ArrayList<Tast> newTasks = new ArrayList<Tast>();
		    newTasks.add(tasks.get(0));
			int time = 0;
			int tRes = 0;
			int tWait = 0;
			int tTur = 0;
			int len = tasks.size();
			for(int i=0;i<len;i++)
			{
				while(time<newTasks.get(0).getAT()) {
					System.out.println("<system time "+time+"> No process is runnig");
					time++;
				}
				tRes += (time-newTasks.get(0).getAT());
				for(int j=1;j<newTasks.get(0).getCB();j++) {
					time++;
					System.out.println("<system time "+time+"> process "+  newTasks.get(0).getPID() + " is running");
				}
				time++;
				System.out.println("<system time "+time+"> process "+ newTasks.get(0).getPID() + " is finished.....");
				tWait += (time-(newTasks.get(0).getAT()+newTasks.get(0).getCB()));
				tTur += (time-newTasks.get(0).getAT());
				tasks.remove(newTasks.get(0));
				newTasks.remove(newTasks.get(0));
				int ln = tasks.size();
				for(int k=0;k<ln && tasks.size()>0 && tasks.get(k).getAT()<=time;k++) {
					newTasks.add(tasks.get(k));
					tasks.remove(tasks.get(k));
					k--;
				}
				if(newTasks.size()==0 && tasks.size()!=0) {
					newTasks.add(tasks.get(0));
				}
				Collections.sort(newTasks,new SortBySJF());
			}
			System.out.println("<system time "+time+"> All process are finished..........");
			System.out.println("============================================================");
			double ares = ((double)tRes)/len;
			double await = ((double)tWait)/len;
			double atur = ((double)tTur)/len;
			System.out.println("Average waiting time is : "+ await);
			System.out.println("Average response time is : "+ ares);
			System.out.println("Average turnaround time is : "+ atur);
			System.out.println("============================================================");
			
		}
		else 
		{
			System.out.println("please enter valid type( FCFS, SJF, RR) ");
		}
		
	}
	public void run(String string) {
		System.out.println("Scheduling Algorithm is : "+algoType);
		System.out.println("Total "+tasks.size()+" are read from file.");
		System.out.println("============================================================");
		int timer = new Integer(string);
		int time =  0;
		int tRes = 0;
		int tWait = 0;
		int tTur = 0;
		Collections.sort(tasks,new SortBy());
		Queue<Tast> queue = new LinkedList<>();
		int len = tasks.size();
		queue.add(tasks.get(0));
		boolean fl[] = new boolean[200];
		boolean gl[] = new boolean[200];
		for(int i=0;i<len;i++) {
			fl[i] = false; 
		}
		for(int i=0;i<len;i++) {
			gl[i] = false; 
		}
		gl[tasks.get(0).getPID()] = true;
		int count = 0;
		int last = 0;
		while(count!=len) {
			if(queue.isEmpty()==true) {
				last++;
				queue.add(tasks.get(last));
				gl[tasks.get(last).getPID()] = true;
			}
			while(time<queue.peek().getAT()) {
				System.out.println("<system time "+time+"> No process is runnig");
				time++;
			}
			if(fl[queue.peek().getPID()]==false) {
				tRes += (time-queue.peek().getAT());
				fl[queue.peek().getPID()] = true;
			}
			if(queue.peek().getCB()<=timer) {
				for(int j=1;j<queue.peek().getCB();j++) {
					System.out.println("<system time "+time+"> process "+ queue.peek().getPID() + " is running");
					time++;
				}
				System.out.println("<system time "+time+"> process "+ queue.peek().getPID() + " is finished.....");
				count++;
				tWait += (time-(queue.peek().getAT()+queue.peek().getCB()));
				tTur += (time-queue.peek().getAT());
				queue.poll();
			}
			else {
				for(int j=0;j<timer;j++) {
					System.out.println("<system time "+time+"> process "+ queue.peek().getPID() + " is running");
					time++;
				}
				Tast tsTast = queue.poll();
				tsTast.dec(timer);
				for(int k=0;k<tasks.size();k++) {
					if(gl[tasks.get(k).getPID()]==false) {
						if(time>=tasks.get(k).getAT()) {
							queue.add(tasks.get(k));
							gl[tasks.get(k).getPID()] = true;
							last++;
						}
					}
				}
				queue.add(tsTast);
			}
		}
		System.out.println("<system time "+time+"> All process are finished..........");
		System.out.println("============================================================");
		double ares = ((double)tRes)/len;
		double await = ((double)tWait)/len;
		double atur = ((double)tTur)/len;
		System.out.println("Average waiting time is : "+ await);
		System.out.println("Average response time is : "+ ares);
		System.out.println("Average turnaround time is : "+ atur);
		System.out.println("============================================================");
	}
}


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
