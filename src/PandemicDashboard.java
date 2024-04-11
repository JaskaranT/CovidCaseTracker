/*
   TCSS 143
   @author: Jaskaran Toor
   @version: 11/18/2021
   Description: Driver file
*/

import java.util.*;
import java.io.*;

public class PandemicDashboard {
    /*
       Method: To read data from the file and create an instance of CovidCase and add to a List
       Parameter: String fileName
       Return: List<CovidCase> caseList
    */
    public static List<CovidCase> fillCovidCaseList(String fileName) throws FileNotFoundException {
        //List to store data from csv file
        List<CovidCase> covidCases = new ArrayList<>();
        //Pre-conditions
        String country = null, maxDate = null;
        String record = null;
        String[] data = null, lastData = null;
        int indexCountry = 2, indexDate = 3, indexTotal = 4, indexTotalDeath = 7; //Location of data
        boolean isHeading = true;
        File file = new File(fileName);
        CovidCase covidCase = null;
        Scanner reader = null;

        reader = new Scanner(file); //Scanner to read the file
        while (reader.hasNextLine()) {
            record = reader.nextLine(); //Reads through each line

            if (!record.isEmpty()) {   //Splits the line if it is empty
                data = record.split(",");

                if (isHeading) {

                    for (int i = 0; i < data.length; i++) {
                        //Adds required data and stores in array
                        if (data[i].equalsIgnoreCase("location"))
                            indexCountry = i;
                        else if (data[i].equalsIgnoreCase("date"))
                            indexDate = i;
                        else if (data[i].equalsIgnoreCase("total_cases"))
                            indexTotal = i;
                        else if (data[i].equalsIgnoreCase("total_deaths"))
                            indexTotalDeath = i;

                    }

                    isHeading = false;

                }

                else if (isValidLocation(data, indexCountry)) {

                    if (country == null && maxDate == null && lastData == null) {
                        country = data[indexCountry];
                        maxDate = data[indexDate];
                    } else if (lastData != null && country.equalsIgnoreCase(data[indexCountry]) && (maxDate.compareTo(data[indexDate]) < 0)) {
                        maxDate = data[indexDate];

                    } else if (lastData != null && !country.equalsIgnoreCase(data[indexCountry])
                            && (maxDate.compareTo(data[indexDate]) > 0)) {  //Checks if data is valid

                        covidCase = new CovidCase(lastData[indexCountry], Double.valueOf(lastData[indexTotal]),
                                Double.valueOf(lastData[indexTotalDeath])); //Creates an instance of covid case

                        if (covidCases.contains(covidCase))
                            covidCases.set(covidCases.indexOf(covidCase), covidCase);
                        else
                            covidCases.add(covidCase);

                        country = data[indexCountry];
                        maxDate = data[indexDate];
                    }
                    if (data.length > indexTotalDeath && !(data[indexCountry].isEmpty()
                            || data[indexTotal].isEmpty() || data[indexTotalDeath].isEmpty())) {
                        lastData = data;
                    }

                }

            }

        }




        reader.close(); //Closes the file reader
        return covidCases;

    }

    /*
       Method: To display the dashboard options to the user.
       Parameter: None
       Return: None
     */
    public static void displayDashboard() {
        System.out.println("Dashboard for reporting covid cases");
        System.out.println("-----------------------------------");
        System.out.println("Select one of the following options:");
        System.out.println("1. Report (Display) Covid Cases by Countries. \n"
                + "2. Report (Display) Covid Cases by Total Deaths (decreasing order). \n"
                + "3. Report (Display) Covid Cases by Total Cases (decreasing order). \n"
                + "4. Report Countries with minimum and maximum number of Total Deaths. \n"
                + "5. Report Countries with minimum and maximum number of Total Cases. \n"
                + "6. Search for a Country and report their Mortality Rate (death-to-case %). \n"
                + "7. Report (Display) top 10 countries by their Mortality Rate (decreasing order). \n"
                + "8. Exit from the program !\n");
    }

   /*
      Method: To check if FR is at valid spot
      Param: String[], int
      Return: boolean
   */

    public static boolean isValidLocation(String[] data, int indexCountry) {

        if (data.length < indexCountry || data[indexCountry].isEmpty() || data[indexCountry].equalsIgnoreCase("World")
                || data[indexCountry].equalsIgnoreCase("Upper middle income")
                || data[indexCountry].equalsIgnoreCase("Lower middle income")
                || data[indexCountry].equalsIgnoreCase("High middle income")
                || data[indexCountry].equalsIgnoreCase("High income") || data[indexCountry].equalsIgnoreCase("Europe")
                || data[indexCountry].equalsIgnoreCase("Asia") || data[indexCountry].equalsIgnoreCase("South America")
                || data[indexCountry].equalsIgnoreCase("North America")
                || data[indexCountry].equalsIgnoreCase("European Union")) {
            //Returns false for data that is not required
            return false;

        }
        //Returns true for required data
        return true;
    }
   /*
      Method: To display the Covid Cases in sorted order based on either – Total Cases, Total
      Deaths & Mortality Ratio. Display Country, Total Cases & Total Deaths
      Parameter: List<CovidCase> caseList (you are passing a sorted list)
      Return: void
   */

    public static void displayCovidCases(List<CovidCase> caseList) {

        System.out.printf("%-40s%-20s%-20s\n", "COUNTRY", "TOTAL CASES", "TOTAL DEATHS");
        System.out.println("--------------------------------------------------------------------------------");

        Collections.sort(caseList);
        //For each loop to search values in list
        for (CovidCase c : caseList) {
            System.out.printf("%-40s%-20.1f%-20.1f\n", c.getCountry(), c.getTotalCases(), c.getTotalDeaths()); //Displays all covid cases and deaths for every country

        }

    }
    /*
       Method: To display covid cases by total deaths in sorted order
       Parameter: List<CovidCase> caselist)
       Return: int
    */
    public static void displayCovidCasesByTotalDeath(List<CovidCase> caseList) {

        System.out.printf("%-40s%-20s%-20s\n", "COUNTRY", "TOTAL CASES", "TOTAL DEATHS");
        System.out.println("--------------------------------------------------------------------------------");
        //Comparator displays deaths in decreasing order
        Collections.sort(caseList, new Comparator<CovidCase>() {

            //@override compareTo()

            public int compare(CovidCase o1, CovidCase o2) {
                return (int) (o2.getTotalDeaths() - o1.getTotalDeaths()); //sorts the cases in decreasing order
            }

        });
        //For each loop to search values in list
        for (CovidCase c : caseList) {
            System.out.printf("%-40s%-20.1f%-20.1f\n", c.getCountry(), c.getTotalCases(), c.getTotalDeaths()); //Formats the output
        }
    }
    /*
      Method: To display covid cases by total cases in sorted order
      Parameter: List<CovidCase> caselist)
      Return: void
    */
    public static void displayCovidCasesByTotalCases(List<CovidCase> caseList) {

        System.out.printf("%-30s%-20s%-20s\n", "COUNTRY", "TOTAL CASES", "TOTAL DEATHS");
        System.out.println("--------------------------------------------------------------------------------");
        //Comparator dislplays cases in decreasing order
        Collections.sort(caseList, new Comparator<CovidCase>() {

            //@Override compareTo()
            public int compare(CovidCase o1, CovidCase o2) {
                return (int) (o2.getTotalCases() - o1.getTotalCases()); //Returns the cases in decreasing order
            }

        });
        //For each loop to search values in list
        for (CovidCase c : caseList) {
            System.out.printf("%-40s%-20.1f%-20.1f\n", c.getCountry(), c.getTotalCases(), c.getTotalDeaths());//Formats the output
        }

    }
   /*
      Method: To display the countries with the minumum and maximum deaths
      Param: List<CovidCase> caseList
      Return: Void
   */

    public static void displayMinMaxCountryByTotalDeath(List<CovidCase> caseList) {

        double minDeath = Double.MAX_VALUE, maxDeath = Double.MIN_VALUE;
        //For each loop to search values in list
        for (CovidCase c : caseList) {

            if (minDeath > c.getTotalDeaths()) //Gets the countries with min deaths
                minDeath = c.getTotalDeaths();

            if (maxDeath < c.getTotalDeaths()) //Gets the coutries with max deaths
                maxDeath = c.getTotalDeaths();

        }

        System.out.println("Countries with Minimum Deaths:");
        //For each loop to search values in list
        for (CovidCase c : caseList) {

            if (c.getTotalDeaths() == minDeath) {
                System.out.println(c);  //Displays countries with min deaths
                System.out.println();

            }

        }

        System.out.println("\nCountries with Maximum Deaths:\n");
        //For each loop to search values in list
        for (CovidCase c : caseList) {

            if (c.getTotalDeaths() == maxDeath) {
                System.out.println(c);  //displays countries with max deaths
                System.out.println();

            }

        }

    }
   /*
      Method: To display the countries with the minumum and maximum cases
      Param: List<CovidCase> caseList
      Return: Void
   */

    public static void displayMinMaxCountryByTotalCase(List<CovidCase> caseList) {

        double minCases = Double.MAX_VALUE, maxCases = Double.MIN_VALUE;
        //For each loop to search values in list
        for (CovidCase c : caseList) {

            if (minCases > c.getTotalCases())
                minCases = c.getTotalCases(); //displays countries with min cases

            if (maxCases < c.getTotalCases())
                maxCases = c.getTotalCases();  //displays countries wit max cases

        }

        System.out.println("Countries with Minimum Cases:");
        //For each loop to search values in list
        for (CovidCase c : caseList) {

            if (c.getTotalCases() == minCases) {
                System.out.println(c);
                System.out.println();

            }

        }

        System.out.println("\nCountries with Maximum Cases:\n");
        //For each loop to search values in list
        for (CovidCase c : caseList) {

            if (c.getTotalCases() == maxCases) {
                System.out.println(c);
                System.out.println();

            }

        }
    }

    /*
       Method: To search for country based on user input and display that specific countries statistics
       Param: List<CovidCase> caseList, Scanner keyboard
       Return: void
   */
    public static void searchCountryReport(List<CovidCase> caseList, Scanner keyboard) {

        System.out.println("Enter a country name:");
        String country = keyboard.nextLine();

        Collections.sort(caseList);

        int index = binarySearch(caseList, country);

        if (index != -1) {
            CovidCase c = caseList.get(index);
            System.out.println("Covid Case Data:");
            System.out.println(c);
            System.out.printf("\nMortality Rate: %.2f%%\n", c.getDeathToCaseRatio() * 100);
        } else
            System.out.println(country + " not found!");

    }

    /*
       Method:To display top 10 countries by mortality rates
       Param: List<CovidCase> caseList
       Return: void
    */
    public static void displayTopTenByMoratality(List<CovidCase> caseList) {

        System.out.printf("%-40s%-20s%-20s%-20s\n", "COUNTRY", "TOTAL CASES", "TOTAL DEATHS", "MORTALITY");
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println();
        Collections.sort(caseList, new Comparator<CovidCase>() { //sorts the values by mortality rate

            //@Override compare
            //Comparator to sort objects
            public int compare(CovidCase o1, CovidCase o2) {
                if(o2.getDeathToCaseRatio() - o1.getDeathToCaseRatio() > 0)
                    return 1; //returns positive if country has higher mortality rate
                else if(o2.getDeathToCaseRatio() - o1.getDeathToCaseRatio() < 0)
                    return -1; // returns negative if country has lower mortality rate
                return 0;
            }

        });
        //Returns the countries with top 10 mortality rate
        for (int i = 0; i < 10; i++) {
            System.out.printf("%-40s%-20.1f%-20.1f%-20s\n", caseList.get(i).getCountry(),
                    caseList.get(i).getTotalCases(), caseList.get(i).getTotalDeaths(),
                    String.format("%.2f%%", caseList.get(i).getDeathToCaseRatio() * 100) );
        }

    }
    /*
       Method: binary search to search for specific elements
       Param: List<CovidCase> caseList, String country
       return: int
    */
    public static int binarySearch(List<CovidCase> caseList, String country) {

        int low = 0;
        int high = caseList.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int diff = caseList.get(mid).getCountry().toLowerCase().compareTo(country.toLowerCase());
            if (diff == 0) //If element is present at the middle
                return mid;
            else if (diff < 0)
                low = mid + 1; //element can only be present at left subarray
            else
                high = mid - 1; //Element can only be present in right subarray
        }
        return -1; //element is not present in array
    }
    //Main
    public static void main(String[] args) throws FileNotFoundException {
        //Scanner to read user input
        Scanner input = new Scanner(System.in);
        String fileName = "owid-covid-data.csv";  //Data file
        List<CovidCase> covidCases = fillCovidCaseList(fileName); //Reads the data from the file

        //Quit for user to exit
        boolean quit = false;

        while(!quit) {
            //displays the dashboard with all options
            displayDashboard();
            //Prompts user for valid value
            System.out.println("Enter a number: ");
            int choice = input.nextInt(); //Takes user input
            //Displays the data for the option the user chose
            if(choice == 1) {
                displayCovidCases(covidCases);
            }
            else if(choice == 2) {
                displayCovidCasesByTotalDeath(covidCases);
            }
            else if(choice == 3) {
                displayCovidCasesByTotalCases(covidCases);
            }
            else if(choice == 4) {
                displayMinMaxCountryByTotalDeath(covidCases);
            }
            else if(choice == 5) {
                displayMinMaxCountryByTotalCase(covidCases);
            }
            else if(choice == 6) {
                input.nextLine(); //Asks user to enter country of choice
                searchCountryReport(covidCases, input); //Displays the countrys statistics
            }
            else if(choice == 7) {
                displayTopTenByMoratality(covidCases);
            }
            else if(choice == 8) {
                quit = true; // Exits the program
            }
            else {
                System.out.println("enter a valid number");
            }

        }



    }

}
