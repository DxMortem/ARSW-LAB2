/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 *
 * @author 2116177
 */
public class ThreadSeeker extends Thread {
    private final String ipAddress;
    private final int min;
    private final int tot;
    private AtomicInteger ocurrencesCount;
    private int checkedListsCount;
    private LinkedBlockingQueue<Integer> queue = null;
    private final LinkedList<Integer> blackListOcurrences;
    private final HostBlacklistsDataSourceFacade skds;
    
    
    @Override
    public void run() {
        int i = min;
        while (i<min+tot && ocurrencesCount.get()<5){
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipAddress)){
                blackListOcurrences.add(i);
                ocurrencesCount.getAndIncrement();
            }
            i++;
        }
    }

    public ThreadSeeker(String ipAddress, int min, int tot, LinkedBlockingQueue queue, AtomicInteger ocurrencesCount) {
        this.checkedListsCount = 0;
        this.ocurrencesCount = ocurrencesCount;
        this.skds = HostBlacklistsDataSourceFacade.getInstance();
        this.blackListOcurrences = new LinkedList<>();
        this.ipAddress = ipAddress;
        this.min = min;
        this.tot = tot;
        this.queue = queue;
    }

    public int getCheckedListsCount() {
        return checkedListsCount;
    }

    public LinkedList<Integer> getBlackListOcurrences() {
        return blackListOcurrences;
    }
}
