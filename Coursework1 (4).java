/**
 * Tarek Bessalah
 * STUDENT ID: 201344887
 *
 * Networks - Coursework 1
 */

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Main class that checks if
 * the filter and the hostname
 * are valid and match from a command
 * line argument
 */
public class Coursework1 {
    //Main function that calls other methods
    public static void main(String[] args) {
        String filter = args[0]; //stores the filter
        String hostname = args[1]; //stores the value of hostname

        if(filter.contains(":")){
            System.out.println("Error! Filter must be IPv4 and not IPv6");
            System.exit(0);
        }

        //splits the filter into individual components
        String[] filter_type = filter.split("[.]");
        isValidWildIP(filter);
        isValidHostName(hostname);

        //checks if the filter entered is a wildcard
        boolean wild_card = false;
        for(int i = 0; i < filter_type.length; i++){
            if(filter_type[i].equals("*")){
                wild_card = true;
            } else if(!filter_type[i].equals("*")){
                wild_card = false;
            }
        }

        if(wild_card){
            matchingWildCard(filter, hostname, filter_type);
        } else if(!wild_card){
            //isReachableIP(filter);
            match(filter, hostname);
        }
    }

    /**
     * If the filter entered has a wildcard
     * this method checks if the filter matches
     * the hostname by putting the filter components
     * into an array and comparing the respective components
     * @param filter
     * @param hostname
     * @param filter_address
     */
    public static void matchingWildCard(String filter, String hostname, String[] filter_address){
        String[] hostaddress;
        boolean match = false;

        try {
            InetAddress host = InetAddress.getByName(hostname);
            hostaddress = host.getHostAddress().split("[.]");
            for(int j = 0; j < hostaddress.length; j++) {
                if (hostaddress[j].equals(filter_address[j]) || filter_address[j].equals("*")) {
                    match = true;
                } else if (!hostaddress[j].equals(filter_address[j]) && !filter_address[j].equals("*")) {
                    match = false;
                    break;
                }
            }

            if(match){
                System.out.println("true");
                System.exit(0);
            } else if (!match){
                System.out.println("false");
                System.exit(0);
            }
        } catch (UnknownHostException e){

        }
    }

    /**
     * if filter contains no wildcards, this method is called
     * to check if the hostname that is converted to an instance
     * of InetAddress matches the IP
     *
     * @param filter
     * @param hostname
    */
    public static void match(String filter, String hostname){
        try{
            InetAddress host = InetAddress.getByName(hostname);
            if(host.getHostAddress().equals(filter)){
                System.out.println("true");
            } else {
                System.out.println("false");
            }
        } catch (UnknownHostException ex){
            System.err.println("Could not lookup filter");
        }
    }

    /**
     * this method checks the hostname entered is valid
     * by checking that it is an IPv4 address and that it
     * can be reached
     *
     * @param hostname
     */
    public static void isValidHostName(String hostname){
        try{
            //converts the hostname to an instance of InetAddress
            InetAddress valid_host = InetAddress.getByName(hostname);
            valid_host.getHostAddress();
            if(valid_host.getHostAddress().contains(":")){
                System.out.println("Error! Host name is not an IPv4 address!");
                System.exit(0);
            }
        } catch (UnknownHostException ex){
            System.out.println("Error! The " + hostname + " entered is an invalid hostname!");
        }
    }

    /**
     * Checks if the filter is a valid IPv4 address,
     * will return an error if it is invalid because
     * it is either wildcards have not been entered correctly,
     * out of range, or is not 4-byte string of 4 integers
     *
     * @param filter
     */
    public static void isValidWildIP(String filter){
        String[] wild_string = filter.split("[.]");
        int x = wild_string.length-1;
        int filter_size = wild_string.length;
        String last_string = wild_string[x];

        //checks if the IPv4 has 4 integers
        if(filter_size > 4){
            System.out.println("Error! Invalid filter must be a 4-byte string of integers");
            System.exit(0);
        }
        if(wild_string[0].equals("*")){
            System.out.println("Error! An IP address cannot start with a '*'");
            System.exit(0);
        }

        for(int i = 0; i < wild_string.length; i++) {
            if (wild_string[i].equals("*") && !last_string.equals("*") && i != wild_string.length - 1) {
                boolean isTrue = isNum(wild_string, (i + 1), filter);
                if (isTrue) {
                    System.out.println("Error! " + filter + " is invalid because there is a value after '*'");
                    System.exit(0);
                }

            } else if (wild_string[i].equals("*") && last_string.equals("*")  && i != 0  && i != wild_string.length - 1){
                //check if the next value is a number
                boolean isTrue2 = isNum2(wild_string[i+1]);
                if(isTrue2){
                    System.out.println("Error! " + filter + " is invalid because there is a value after '*'");
                    System.exit(0);
                }
            }
        }

        for(int j = 0; j < wild_string.length; j++){
            if(!wild_string[j].equals("*")){
                if(!(Integer.parseInt(wild_string[j]) >=0 && Integer.parseInt(wild_string[j]) <= 255)){
                    System.out.println("Error! " + filter + " is invalid because it is out of range!");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * This function checks if there is a number
     * after the wildcard
     *
     * @param wild_string
     * @param i
     * @param filter
     * @return
     */
    public static boolean isNum(String[] wild_string, int i, String filter){
        for(int j = i; j < wild_string[i].length(); j++){
            for(char c : wild_string[j].toCharArray()){
                if(!Character.isDigit(c)){
                    return false;
                }
            }
        }

        return true;
    }

    //This method returns false if there is no number in array
    public static boolean isNum2( String filter) {
        for (int j = 0; j < filter.length(); j++) {
            for (char c : filter.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
        }

        return true;
    }
}

