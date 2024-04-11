
/*
   TCSS 143
   @author: Jaskaran Toor
   @version: 11/18/2021
   Description: Covid Case class

*/


    public class CovidCase implements Comparable<CovidCase>  {
        private String country;
        private double totalCases;
        private double totalDeaths;
        private double deathToCaseRatio;

        //Constructor
        public CovidCase(String country, double totalCases, double totalDeaths) {
            this.country = country;
            this.totalCases = totalCases;
            this.totalDeaths = totalDeaths;
            setDeathToCaseRatio();
        }

        //Getter methods
        public String getCountry() {
            return country;
        }

        public double getTotalCases() {
            return totalCases;
        }

        public double getTotalDeaths() {
            return totalDeaths;
        }

        public double getDeathToCaseRatio() {
            return deathToCaseRatio;
        }


        //Setter methods
        public void setCountry(String country) {
            this.country = country;
        }
        public void setTotalCases(double totalCases) {
            this.totalCases = totalCases;
        }

        public void setTotalDeaths(double totalDeaths) {
            this.totalDeaths = totalDeaths;
        }

        private void setDeathToCaseRatio() {
            this.deathToCaseRatio = this.totalDeaths / this.totalCases; //sets the death to case ratio
        }


        //@override compareTo method
        public int compareTo(CovidCase o) {

            return this.country.compareTo(o.country);
        }

        //@override toString()
        public String toString() {
            return "Country: " + country + "\nTotal Cases: " + totalCases + "\nTotal Deaths:" + totalDeaths ;
        }





    }

