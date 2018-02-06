/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;
import java.lang.Runtime;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]) throws InterruptedException{
        HostBlackListsValidator hblv=new HostBlackListsValidator();
        int proc = Runtime.getRuntime().availableProcessors();
        List<Integer> blackListOcurrences=hblv.checkHost("202.24.34.55",4);
        System.out.println("The host was found in the following blacklists:"+blackListOcurrences);
        System.out.println("Number of processors is: "+ proc);
        
    }
    
}
