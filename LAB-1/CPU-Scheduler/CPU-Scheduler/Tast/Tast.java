/* 
 * 
 * Author:- Yatin Patel
 * Id:- 201601454
 * Email:- yatinpatel.gt@gmail.com
 * 
 */

package Tast;
public class Tast {
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
