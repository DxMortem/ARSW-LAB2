/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 *
 * @author 2116177
 */
public class ThreadSeeker extends Thread {
    private final String ipAddress;
    private final int min;
    private final int tot;
    private int ocurrencesCount;
    private int checkedListsCount;
    private final LinkedList<Integer> blackListOcurrences;
    private final HostBlacklistsDataSourceFacade skds;
    
    
    @Override
    public void run() {
        for (int i=min;i<min+tot;i++){
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipAddress)){
                blackListOcurrences.add(i);
                ocurrencesCount++;
            }
        }
    }

    public ThreadSeeker(String ipAddress, int min, int tot) {
        this.checkedListsCount = 0;
        this.ocurrencesCount = 0;
        this.skds = HostBlacklistsDataSourceFacade.getInstance();
        this.blackListOcurrences = new LinkedList<>();
        this.ipAddress = ipAddress;
        this.min = min;
        this.tot = tot;
    }

    public int getOcurrencesCount() {
        return ocurrencesCount;
    }

    public int getCheckedListsCount() {
        return checkedListsCount;
    }

    public LinkedList<Integer> getBlackListOcurrences() {
        return blackListOcurrences;
    }
}
