/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @param divisiones
     * @return  Blacklists numbers where the given host's IP address was found.
     * @throws java.lang.InterruptedException
     */
    public List<Integer> checkHost(String ipaddress, int divisiones) throws InterruptedException{
        List<ThreadSeeker> threadlist = new LinkedList<>();
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        int ocurrencesCount=0;
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        int checkedListsCount=0;
        int totServers = skds.getRegisteredServersCount();
        int range = totServers/divisiones;
        
        for(int i=0;i<divisiones;i++){
            if(i==divisiones-1){
                threadlist.add(new ThreadSeeker(ipaddress,i*range,range+(totServers % divisiones)));
            }else{
                threadlist.add(new ThreadSeeker(ipaddress,i*range,range));
            }
        }
        
        for(ThreadSeeker i:threadlist){
            i.start();
        }
        for(ThreadSeeker i:threadlist){
            i.join();
        }
        for(ThreadSeeker i:threadlist){
            ocurrencesCount += i.getOcurrencesCount();
            checkedListsCount += i.getCheckedListsCount();
            blackListOcurrences.addAll(i.getBlackListOcurrences());
        }
        
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        return blackListOcurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    
    
}
