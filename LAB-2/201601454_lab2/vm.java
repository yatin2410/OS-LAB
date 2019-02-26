

/**
 * @author YatinPatel
 *
 */

import java.io.File;
import java.text.DecimalFormat;
import java.util.Scanner;

public class vm {
	
	public String run(boolean isprint, String arg0,String arg1, String arg2 ) {
		int frames =0;
		try {
			frames = new Integer(arg0);
		}catch (Exception e) {
			System.out.println("Please enter page frame numbers");
			return null;
		}
		String type = arg2;
		Integer arr[] = new Integer[1000];
		File file = new File(arg1);
		int j = 0;
		try {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNext()) {
				int id = scanner.nextInt();
				arr[j]= id;
				j++;
			}
		}
		catch(Exception e) {
			System.out.println(e);
			System.out.println("Can't open input File or it is not in formate!"+file);
			return null;
		}
		if(type.equals("lru")) {
			int misses = 0;
			Integer fms[] = new Integer[frames];
			Integer cnt[] = new Integer[frames];
			for(int i=0;i<frames;i++) {
				cnt[i]= 0; 
				fms[i]= -1; 
			}
			for(int i=0;i<j;i++) {
				boolean isdone = false;
				for(int k=0;k<frames;k++) {
					if(fms[k]==-1) {
						fms[k]=arr[i];
						cnt[k]++;
						isdone = true;
						break;
					}
					if(fms[k]==arr[i]) {
						cnt[k]++;
						isdone = true;
						break;
					}
				}
				if(isdone==false) {
					int mn = 1000;
					int ind = -1;
					for(int k=0;k<frames;k++) {
						if(mn>cnt[k]) {
							mn = cnt[k];
							ind = k;
						}
					}
					cnt[ind]++;
					fms[ind]= arr[i]; 
				}
				if(isprint) {
					if(arr[i]<10){
						System.out.print(" ");
					}
					System.out.print(arr[i]+":[");
					for(int k=0;k<frames;k++) {
						if(k==frames-1) {
							if(fms[k]==-1) {
								System.out.print("  ]");
							}
							else if(fms[k]<10)
								System.out.print(" "+fms[k]+"]");
							else
								System.out.print(fms[k]+"]");
						}
						else {
							if(fms[k]==-1) {
								System.out.print("  |");
							}
							else if(fms[k]<10)
								System.out.print(" "+fms[k]+"|");
							else
								System.out.print(fms[k]+"|");
						}
					}
				}
				if(isdone==false) {
					if(isprint)
						System.out.println(" F");
					misses++;
				}
				else {
					if(isprint)
						System.out.print("\n");
				}
			}
			double d  = new Double(misses*1.0/(j*1.0))*100;
			DecimalFormat decimalFormat = new DecimalFormat("#00.00");
			if(isprint)
				System.out.println("Miss Rate = "+misses+"/"+j+" = "+decimalFormat.format(d)+"%");
			else 
				System.out.println(type+", "+frames+" frames: Miss rate = "+misses+"/"+j+" = "+decimalFormat.format(d)+"%");
			return decimalFormat.format(d).toString();
		}

		if(type.equals("opt")) {
			int misses = 0;
			Integer fms[] = new Integer[frames];
			for(int i=0;i<frames;i++) {
				fms[i]= -1; 
			}
			for(int i=0;i<j;i++) {
				boolean isdone = false;
				for(int k=0;k<frames;k++) {
					if(fms[k]==-1) {
						fms[k]=arr[i];
						isdone = true;
						break;
					}
					if(fms[k]==arr[i]) {
						isdone = true;
						break;
					}
				}
				if(isdone==false) {
					int mx = 0;
					int ind = 0;
					for(int k=0;k<frames;k++) {
						boolean bl = true;
						for(int kk=i+1;kk<j;kk++) {
							if(fms[k]==arr[kk]) {
								bl = false;
								if(mx<kk) {
									ind = k;
									mx = kk;
								}
								break;
							}
						}
						if(bl){
							ind = k;
							break;
						}
					}
					fms[ind]= arr[i]; 
				}
				if(isprint) {
					if(arr[i]<10){
						System.out.print(" ");
					}
					System.out.print(arr[i]+":[");
					for(int k=0;k<frames;k++) {
						if(k==frames-1) {
							if(fms[k]==-1) {
								System.out.print("  ]");
							}
							else if(fms[k]<10)
								System.out.print(" "+fms[k]+"]");
							else
								System.out.print(fms[k]+"]");
						}
						else {
							if(fms[k]==-1) {
								System.out.print("  |");
							}
							else if(fms[k]<10)
								System.out.print(" "+fms[k]+"|");
							else
								System.out.print(fms[k]+"|");
						}
					}
				}
				if(isdone==false) {
					if(isprint)
						System.out.println(" F");
					misses++;
				}
				else {
					if(isprint)
						System.out.print("\n");
				}
			}
			double d  = new Double(misses*1.0/(j*1.0))*100;
			DecimalFormat decimalFormat = new DecimalFormat("#00.00");
			if(isprint)
				System.out.println("Miss Rate = "+misses+"/"+j+" = "+decimalFormat.format(d)+"%");
			else 
				System.out.println(type+", "+frames+" frames: Miss rate = "+misses+"/"+j+" = "+decimalFormat.format(d)+"%");
			return decimalFormat.format(d).toString();
		}
		if(type.equals("fifo")) {
			int misses = 0;
			Integer fms[] = new Integer[frames];
			Integer time[] = new Integer[frames];
			for(int i=0;i<frames;i++) {
				fms[i]= -1; 
				time[i] = 0; 
			}
			for(int i=0;i<j;i++) {
				boolean isdone = false;
				for(int k=0;k<frames;k++) {
					if(fms[k]==-1) {
						fms[k]=arr[i];
						time[k]= i; 
						isdone = true;
						break;
					}
					if(fms[k]==arr[i]) {
						time[k]= i; 
						isdone = true;
						break;
					}
				}
				if(isdone==false) {
					int mn = 1000;
					int ind = -1;
					for(int k=0;k<frames;k++) {
						if(mn>time[k]) {
							mn = time[k];
							ind = k;
						}
					}
					fms[ind]= arr[i];
					time[ind]= i; 
				}
				if(isprint) {
					if(arr[i]<10){
						System.out.print(" ");
					}
					System.out.print(arr[i]+":[");
					for(int k=0;k<frames;k++) {
						if(k==frames-1) {
							if(fms[k]==-1) {
								System.out.print("  ]");
							}
							else if(fms[k]<10)
								System.out.print(" "+fms[k]+"]");
							else
								System.out.print(fms[k]+"]");
						}
						else {
							if(fms[k]==-1) {
								System.out.print("  |");
							}
							else if(fms[k]<10)
								System.out.print(" "+fms[k]+"|");
							else
								System.out.print(fms[k]+"|");
						}
					}
				}
				if(isdone==false) {
					if(isprint)
						System.out.println(" F");
					misses++;
				}
				else {
					if(isprint)
						System.out.print("\n");
				}
			}
			double d  = new Double(misses*1.0/(j*1.0))*100;
			DecimalFormat decimalFormat = new DecimalFormat("#00.00");
			if(isprint)
				System.out.println("Miss Rate = "+misses+"/"+j+" = "+decimalFormat.format(d)+"%");
			else 
				System.out.println(type+", "+frames+" frames: Miss rate = "+misses+"/"+j+" = "+decimalFormat.format(d)+"%");
			return decimalFormat.format(d).toString();
		}
		else{
			System.out.println("Please enter proper algo type. lru, opt or fifo");
		}
		return null;

	}
	
}
