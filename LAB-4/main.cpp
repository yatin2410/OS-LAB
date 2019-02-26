#include <bits/stdc++.h>
#include <Windows.h>
#include "sfs.h"

using namespace std;

void cat(char *str){
    char itype;
	int blocks[3];
	_directory_entry _directory_entries[4];

	int i,j;
	int e_inode;

	// read inode entry for current directory
	// in SFS, an inode can point to three blocks at the most
	itype = _inode_table[CD_INODE_ENTRY].TT[0];
	blocks[0] = stoi(_inode_table[CD_INODE_ENTRY].XX,2);
	blocks[1] = stoi(_inode_table[CD_INODE_ENTRY].YY,2);
	blocks[2] = stoi(_inode_table[CD_INODE_ENTRY].ZZ,2);

	// its a directory; so the following should never happen
	if (itype=='F') {
		printf("Fatal Error! Aborting.\n");
		exit(1);
	}
    bool flag = false;
	// lets traverse the directory entries in all three blocks
	for (i=0; i<3; i++) {
		if (blocks[i]==0) continue;	// 0 means pointing at nothing

		readSFS(blocks[i],(char *)_directory_entries);	// lets read a directory entry; notice the cast

		// so, we got four possible directory entries now
		for (j=0; j<4; j++) {
			if (_directory_entries[j].F=='0') continue;	// means unused entry

			e_inode = stoi(_directory_entries[j].MMM,3);	// this is the inode that has more info about this entry

            if(strcmp(_directory_entries[j].fname,str)==0){
                if (_inode_table[e_inode].TT[0]=='F')  { // entry is for a file
                    _inode_entry node = _inode_table[e_inode];
                    int b1,b2,b3;
                    b1 = stoi(node.XX,2);
                    b2 = stoi(node.YY,2);
                    b3 = stoi(node.ZZ,2);
                    if(b1!=0){
                        char buff[1024];
                        readSFS(b1,buff);
                        printf("%s\n",buff);
                    }
                    if(b2!=0){
                        char buff[1024];
                        readSFS(b2,buff);
                        printf("%s\n",buff);
                    }
                    if(b3!=0){
                        char buff[1024];
                        readSFS(b3,buff);
                        printf("%s\n",buff);
                    }
                }
                else if (_inode_table[e_inode].TT[0]=='D')  { // entry is for a directory; print it in BRED
                    cout<<str<<" is directory name."<<endl;
                    return;
                }
                flag = true;
            }
		}
	}
	if(!flag){
        cout<<"can't find file name "<<str<<endl;
	}
}

void create(char *str,char *data){
	char itype;
	int blocks[3];
	_directory_entry _directory_entries[4];

	int i,j;

	int empty_dblock=-1,empty_dentry=-1;
	int empty_ientry;

	// do we have free inodes
	if (free_inode_entries == 0) {
		printf("Error: Inode table is full.\n");
		return;
	}

	// read inode entry for current directory
	// in SFS, an inode can point to three blocks at the most
	itype = _inode_table[CD_INODE_ENTRY].TT[0];
	blocks[0] = stoi(_inode_table[CD_INODE_ENTRY].XX,2);
	blocks[1] = stoi(_inode_table[CD_INODE_ENTRY].YY,2);
	blocks[2] = stoi(_inode_table[CD_INODE_ENTRY].ZZ,2);

	// its a directory; so the following should never happen
	if (itype=='F') {
		printf("Fatal Error! Aborting.\n");
		exit(1);
	}

	// now lets try to see if the name already exists
	for (i=0; i<3; i++) {
		if (blocks[i]==0) { 	// 0 means pointing at nothing
			if (empty_dblock==-1) empty_dblock=i; // we can later add a block if needed
			continue;
		}

		readSFS(blocks[i],(char *)_directory_entries); // lets read a directory entry; notice the cast

		// so, we got four possible directory entries now
		for (j=0; j<4; j++) {
			if (_directory_entries[j].F=='0') { // means unused entry
				if (empty_dentry==-1) { empty_dentry=j; empty_dblock=i; } // AAHA! lets keep a note of it, just in case we have to create the new directory
				continue;
			}
		}

	}

	// if we did not find an empty directory entry and all three blocks are in use; then no new directory can be made
	if (empty_dentry==-1 && empty_dblock==-1) {
		printf("Error: Maximum directory entries reached.\n");
		return;
	}
	else { // otherwise
		if (empty_dentry == -1) { // Great! didn't find an empty entry but not all three blocks have been used
			empty_dentry=0;

			if ((blocks[empty_dblock] = getBlock())==-1) {  // first get a new block using the block bitmap
				printf("Error: Disk is full.\n");
				return;
			}

			writeSFS(blocks[empty_dblock],NULL);	// write all zeros to the block (there may be junk from the past!)

			switch(empty_dblock) {	// update the inode entry of current dir to reflect that we are using a new block
				case 0: itos(_inode_table[CD_INODE_ENTRY].XX,blocks[empty_dblock],2); break;
				case 1: itos(_inode_table[CD_INODE_ENTRY].YY,blocks[empty_dblock],2); break;
				case 2: itos(_inode_table[CD_INODE_ENTRY].ZZ,blocks[empty_dblock],2); break;
			}
		}


		// NOTE: all error checkings have already been done at this point!!
		// time to put everything together

		empty_ientry = getInode();	// get an empty place in the inode table which will store info about blocks for this new directory

		readSFS(blocks[empty_dblock],(char *)_directory_entries);	// read block of current directory where info on this new directory will be written
		_directory_entries[empty_dentry].F = '1';			// remember we found which directory entry is unused; well, set it to used now
		strncpy(_directory_entries[empty_dentry].fname,str,252);	// put the name in there
		itos(_directory_entries[empty_dentry].MMM,empty_ientry,3);	// and the index of the inode that will hold info inside this directory
		writeSFS(blocks[empty_dblock],(char *)_directory_entries);	// now write this block back to the disk

		strncpy(_inode_table[empty_ientry].TT,"FI",2);		// create the inode entry...first, its a directory, so DI
        int len = strlen(data);
        int cnt = 0;
        while(len>0){
            int blkid;
            if(cnt==0){
                if ((blkid = getBlock())==-1) {  // first get a new block using the block bitmap
                    printf("Error: Disk is full.\n");
                    return;
                }
                char dta[1024];
                int ii;
                for(ii=1024*cnt;ii<min(1024*(cnt+1),len);ii++){
                    dta[ii-(1024*cnt)] = data[ii];
                }
                cnt++;
                len -= 1024;
                writeSFS(blkid,dta);
                itos(_inode_table[empty_ientry].XX,blkid,2);		// directory is just created; so no blocks assigned to it yet
            }
            else if(cnt==1){
                if ((blkid = getBlock())==-1) {  // first get a new block using the block bitmap
                    printf("Error: Disk is full.\n");
                    return;
                }
                char dta[1024];
                int ii;
                for(ii=1024*cnt;ii<min(1024*(cnt+1),len);ii++){
                    dta[ii-(1024*cnt)] = data[ii];
                }
                cnt++;
                len -= 1024;
                writeSFS(blkid,dta);
                itos(_inode_table[empty_ientry].YY,blkid,2);		// directory is just created; so no blocks assigned to it yet
            }
            else if(cnt==2){
                if ((blkid = getBlock())==-1) {  // first get a new block using the block bitmap
                    printf("Error: Disk is full.\n");
                    return;
                }
                char dta[1024];
                int ii;
                for(ii=1024*cnt;ii<min(1024*(cnt+1),len);ii++){
                    dta[ii-(1024*cnt)] = data[ii];
                }
                cnt++;
                len -= 1024;
                writeSFS(blkid,dta);
                itos(_inode_table[empty_ientry].ZZ,blkid,2);		// directory is just created; so no blocks assigned to it yet
            }
        }
		writeSFS(BLOCK_INODE_TABLE, (char *)_inode_table);	// phew!! write the inode table back to the disk
	}

}

bool findFILE(char *str){
    char itype;
	int blocks[3];
	_directory_entry _directory_entries[4];

	int i,j;
	int e_inode;

	// read inode entry for current directory
	// in SFS, an inode can point to three blocks at the most
	itype = _inode_table[CD_INODE_ENTRY].TT[0];
	blocks[0] = stoi(_inode_table[CD_INODE_ENTRY].XX,2);
	blocks[1] = stoi(_inode_table[CD_INODE_ENTRY].YY,2);
	blocks[2] = stoi(_inode_table[CD_INODE_ENTRY].ZZ,2);

	// its a directory; so the following should never happen
	if (itype=='F') {
            return false;
	}
    bool flag = false;
	// lets traverse the directory entries in all three blocks
	for (i=0; i<3; i++) {
		if (blocks[i]==0) continue;	// 0 means pointing at nothing

		readSFS(blocks[i],(char *)_directory_entries);	// lets read a directory entry; notice the cast

		// so, we got four possible directory entries now
		for (j=0; j<4; j++) {
			if (_directory_entries[j].F=='0') continue;	// means unused entry

			e_inode = stoi(_directory_entries[j].MMM,3);	// this is the inode that has more info about this entry

            if(strcmp(_directory_entries[j].fname,str)==0){
                return true;
            }
		}
	}
	return false;
}

void rm(char* fname,int curr_inode,int flag) {

	char itype;
	int blocks[3];
	_directory_entry _directory_entries[4];

	int i,j;
	int e_inode;
	int exist=0;

	itype = _inode_table[curr_inode].TT[0];
	blocks[0] = stoi(_inode_table[curr_inode].XX,2);
	blocks[1] = stoi(_inode_table[curr_inode].YY,2);
	blocks[2] = stoi(_inode_table[curr_inode].ZZ,2);

	if (itype=='F') {
		printf("Fatal Error! Aborting.\n");
		exit(1);
	}

	for (i=0; i<3; i++) {
		if (blocks[i]==0) continue;

		readSFS(blocks[i],(char *)_directory_entries);

		for (j=0; j<4; j++) {
			if (_directory_entries[j].F=='0') continue;

			e_inode = stoi(_directory_entries[j].MMM,3);

			if(flag==1 || strcmp(fname,_directory_entries[j].fname)==0)
			{
				exist=1;
				if (_inode_table[e_inode].TT[0]=='F')  {
					if(stoi(_inode_table[e_inode].XX,2)!=0)
					{
						writeSFS(stoi(_inode_table[e_inode].XX,2),NULL);
						returnBlock(stoi(_inode_table[e_inode].XX,2));
					}
					if(stoi(_inode_table[e_inode].YY,2)!=0)
					{
						writeSFS(stoi(_inode_table[e_inode].YY,2),NULL);
						returnBlock(stoi(_inode_table[e_inode].YY,2));
					}
					if(stoi(_inode_table[e_inode].ZZ,2)!=0)
					{
						writeSFS(stoi(_inode_table[e_inode].ZZ,2),NULL);
						returnBlock(stoi(_inode_table[e_inode].ZZ,2));
					}
				}
				else if (_inode_table[e_inode].TT[0]=='D')  {
					rm("",e_inode,1);
				}

				strncpy(_inode_table[e_inode].XX,"00",2);
				strncpy(_inode_table[e_inode].YY,"00",2);
				strncpy(_inode_table[e_inode].ZZ,"00",2);
				returnInode(e_inode);

				strncpy(_directory_entries[j].MMM,"",0);
				strncpy(_directory_entries[j].fname,"",0);
				_directory_entries[j].F='0';
			}
		}
		writeSFS(blocks[i],(char *)_directory_entries);

		int k,cnt=0;
		for(k=0;k<4;k++)
		{
			if(_directory_entries[k].F=='0') cnt++;
		}
		if(cnt==4 && blocks[i]!=0)
		{
			returnBlock(blocks[i]);
			blocks[i]=0;

			if(i==0) strncpy(_inode_table[curr_inode].XX,"00",2);
			else if(i==1) strncpy(_inode_table[curr_inode].YY,"00",2);
			else if(i==2) strncpy(_inode_table[curr_inode].ZZ,"00",2);
		}
	}
	writeSFS(BLOCK_INODE_TABLE, (char *)_inode_table);

	if(flag==1) return;
	if(exist==0) printf("ERROR : No such file named '%s'\n",fname);
}

int main(){

    mountSFS();
    cout<<"Hola amigos\n";
    while(1){
        printPrompt();
        string cmd;
        cin>>cmd;
        if(cmd=="ls"){
            ls();
        }
        else if(cmd=="cd"){
            char str[100];
            scanf("%s",str);
            cd(str);
        }
        else if(cmd=="md"){
            char str[100];
            scanf("%s",str);
            md(str);
        }
        else if(cmd=="rd"){
            rd();
        }
        else if(cmd=="stats"){
            stats();
        }
        else if(cmd == "display"){
            char str[100];
            scanf("%s",str);
            cat(str);
        }
        else if(cmd == "create"){
            char str[100];
            scanf("%s",str);
            if(findFILE(str)){
                cout<<"File or Directory with name "<<str<<" is already exist\n";
            }
            else{
                char data[3072];
                char ch;
                int in = 0;
                scanf("%s",data);
                /*while (true) {
                    if(GetAsyncKeyState(VK_ESCAPE))
                    {
                        cout << "ESCAPE-PRESSED" << endl;
                        break;
                    }
                    cin>>ch;
                    data[in] = ch;
                    in++;
                }*/
                if(strlen(data)>3072){
                    cout<<"Please enter data less than 3 KB\n";
                    continue;
                }
                create(str,data);
            }
        }
        else if(cmd=="rm"){
            char str[100];
            scanf("%s",str);
            if(!findFILE(str)){
                cout<<"File or Directory with name "<<str<<" is not exist in current Directory\n";
            }
            else{
                rm(str,CD_INODE_ENTRY,0);
            }
        }
        else if(cmd=="exit"){
            cout<<"adios amigos\n";
            exit(0);
        }
        else if(cmd == "printBLK"){
            int bn;
            char buff[1024];
            cin>>bn;
            readSFS(bn,buff);
            cout<<buff<<endl;
        }
        else{
            cout<<"Please enter proper command\n";
            cout<<endl;
            continue;
        }
    }

    return 0;
}
