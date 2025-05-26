package Game;

import java.util.Scanner;


public class Skeleton {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SkeletonHelper skeletonHelper = new SkeletonHelper();
    private static Boolean bool;

    /**
     * Main method that runs the program and presents the main menu.
     * Based on user input, it invokes different sets of tests like Entomologist, Mycologist, or Tecton tests.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            displayMainMenu();
            skeletonHelper.reset();
            int choice = getUserInput();

            switch (choice) {
                case 1:
                    runEntomologistTests();
                    break;
                case 2:
                    //              runMycologistTests();
                    break;
                case 3:
                    runTectonTests();
                    break;
                case 0:
                    running = false;
                    System.out.println("[i] Skeleton futása befejeződött.");
                    break;
                default:
                    System.out.println("[i] Érvénytelen választás, próbálja újra!");
            }
        }
    }

    /**
     * Displays the main menu for the user to choose a category.
     */
    private static void displayMainMenu() {
        System.out.println("\nVálasszon kategóriát:");
        System.out.println("1 - Rovarász tesztek");
        System.out.println("2 - Gombász tesztek");
        System.out.println("3 - Tekton tesztek");
        System.out.println("0 - Kilépés");
        System.out.print("Választás: ");
    }

    /**
     * Reads user input and returns the parsed integer.
     * If input is invalid, returns -1.
     *
     * @return the user input as an integer.
     */
    private static int getUserInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Runs the Entomologist tests by displaying the test menu and executing selected test cases.
     * Allows users to perform different insect-related tests and see the results.
     */
    private static void runEntomologistTests() {
        while (true) {
            skeletonHelper.reset();
            System.out.println("\nRovarász tesztek:");
            System.out.println("1 - " + getEntomologistTestDescription(1) + " - [7. Szekvencia diagram]");
            System.out.println("2 - " + getEntomologistTestDescription(2) + " - [8. Szekvencia diagram]");
            System.out.println("3 - " + getEntomologistTestDescription(3) + " - [9. Szekvencia diagram]");
            System.out.println("4 - " + getEntomologistTestDescription(4) + " - [10. Szekvencia diagram]");
            System.out.println("5 - " + getEntomologistTestDescription(5) + " - [11. Szekvencia diagram]");
            System.out.println("6 - " + getEntomologistTestDescription(6) + " - [12. Szekvencia diagram]");
            System.out.println("7 - " + getEntomologistTestDescription(7) + " - [20. Szekvencia diagram]");
            System.out.println("0 - Vissza a főmenübe");
            System.out.print("Választás: ");

            int choice = getUserInput();
            if (choice == 0) break;
            if (choice >= 1 && choice <= 7) {
                System.out.println("[i] Rovarász teszt: " + choice + ". -  " + getEntomologistTestDescription(choice));
            }
            switch (choice) {
                case 1: { //seq:7
                    skeletonHelper.getInsect().setSourceTecton(skeletonHelper.getTectons().get("t2"));
                    logMethodCall("Tester", "Insect", "eatSpore", "", 0);
                    skeletonHelper.getInsect().eatSpore();
                    logMethodReturn("", 0);
                    break;
                }
                case 2: { //seq:8
                    skeletonHelper.getInsect().setSourceTecton(skeletonHelper.getTectons().get("neighbourOft1"));

                    logMethodCall("Tester", "Insect", "eatSpore", "", 0);
                    skeletonHelper.getInsect().eatSpore();
                    logMethodReturn("", 0);
                    break;
                }
                case 3: { // seq:9
                    skeletonHelper.getInsect().setSourceTecton(skeletonHelper.getTectons().get("t1"));

                    logMethodCall("Tester", "Insect", "eatSpore", "", 0);
                    skeletonHelper.getInsect().eatSpore();
                    logMethodReturn("", 0);
                    break;
                }
                case 4: { // seq:10
                    skeletonHelper.getInsect().setSourceTecton(skeletonHelper.getTectons().get("neighbourboth"));

                    logMethodCall("Tester", "Insect", "eatSpore", "", 0);
                    skeletonHelper.getInsect().eatSpore();
                    logMethodReturn("", 0);
                    break;
                }
                case 5: { // seq:11
                    logMethodCall("Tester", "Insect", "moveToTecton", "neighbourboth", 0);
                    bool = skeletonHelper.getInsect().moveToTecton(skeletonHelper.getTectons().get("neighbourboth"));
                    logMethodReturn("" + bool, 0);
                    break;
                }
                case 6: { // seq:12
                    logMethodCall("Tester", "Insect", "cutYarn", "my1", 0);
                    bool = skeletonHelper.getInsect().cutYarn(skeletonHelper.getMushroomYarns().get("my1"));
                    logMethodReturn("" + bool, 0);
                    break;
                }
                case 7: { // seq:20
                    logMethodCall("Tester", "Insect", "moveToTecton", "t3", 0);
                    //System.out.println(""+SkeletonHelper.getTectonKey(skeletonHelper.getInsect().getSourceTecton()));
                    bool = skeletonHelper.getInsect().moveToTecton(skeletonHelper.getTectons().get("t3"));
                    break;
                }
                default: {
                    System.out.println("[i] Érvénytelen választás!");
                }
            }
        }
    }

    /**
     * Runs the Mycologist tests by displaying the test menu and executing selected test cases.
     * Allows users to perform different mycologist-related tests and see the results.
     */
   /* private static void runMycologistTests() {
        while (true) {
            skeletonHelper.reset();
            System.out.println("\nGombász tesztek:");
            System.out.println("1 - " + getMycologistTestDescription(1)+ " - [6. Szekvencia diagram]");
            System.out.println("2 - " + getMycologistTestDescription(2)+" - [13. Szekvencia diagram]");
            System.out.println("3 - " + getMycologistTestDescription(3)+" - [14. Szekvencia diagram]");
            System.out.println("4 - " + getMycologistTestDescription(4)+" - [15. Szekvencia diagram]");
            System.out.println("5 - " + getMycologistTestDescription(5)+" - [16. Szekvencia diagram]");
            System.out.println("6 - " + getMycologistTestDescription(6)+" - [18. Szekvencia diagram]");
            System.out.println("0 - Vissza a főmenübe");
            System.out.print("Választás: ");

            int choice = getUserInput();
            if (choice == 0) break;
            if (choice >= 1 && choice <= 6) {
                System.out.println("[i] Gombász teszt: " + choice +". - "+ getMycologistTestDescription(choice));
            }
            switch (choice) {
                case 1: {// seq:6
                    logMethodCall("Tester", "MushroomBody", "releaseSpore", "t4",0);
                    skeletonHelper.getMushroomBodies().get("mb1").releaseSpore(skeletonHelper.getTectons().get("t4"));
                    break;
                }
                case 2: { // seq: 13
                    logMethodCall("Tester", "MushroomBody", "releaseSpore", "neighbourboth",0);
                    skeletonHelper.getMushroomBodies().get("mb1").releaseSpore(skeletonHelper.getTectons().get("neighbourboth"));
                    break;
                }
                case 3: { // seq:14
                    logMethodCall("Tester", "MushroomBody", "releaseSpore", "t1",0);
                    skeletonHelper.getMushroomBodies().get("mb1").releaseSpore(skeletonHelper.getTectons().get("t1"));
                    break;
                }
                case 4: { // seq: 15
                    logMethodCall("Tester", "Insect", "cutYarn", "my2",0);
                    bool = skeletonHelper.getInsect().cutYarn(skeletonHelper.getMushroomYarns().get("my2"));
                    logMethodReturn(""+bool,0);
                    logMethodCall("\nTester", "MushroomYarn", "incLifeTime", "",0);
                    skeletonHelper.getMushroomYarns().get("my3").incLifeTime();
                    break;
                }
                case 5: { // seq: 16
                    logMethodCall("Tester", "MushroomBody", "initiateMushroomYarnGrowth", "neighbourboth", 0);
                    bool = skeletonHelper.getMushroomBodies().get("mb2").initiateMushroomYarnGrowth(skeletonHelper.getTectons().get("neighbourboth"));
                    logMethodReturn(""+bool,0);
                    break;
                }
                case 6: { // seq: 18
                    logMethodCall("Tester", "MushroomBody", "initiateMushroomBodyGrowth", "t1",0);
                    bool = skeletonHelper.getMushroomBodies().get("mb1").initiateMushroomBodyGrowth(skeletonHelper.getTectons().get("t1"), true);
                    //logMethodReturn(""+bool,0);
                    break;
                }
                default: {
                    System.out.println("[i] Érvénytelen választás!");
                }
            }
        }
    }
*/

    /**
     * Runs the Tecton tests by displaying the test menu and executing selected test cases.
     * Allows users to perform different tecton-related tests and see the results.
     */
    private static void runTectonTests() {

        while (true) {
            skeletonHelper.reset();
            System.out.println("\nTekton tesztek:");
            System.out.println("1 - " + getTectonTestDescription(1) + " - [2. Szekvencia diagram]");
            System.out.println("2 - " + getTectonTestDescription(2) + " - [4. Szekvencia diagram]");
            System.out.println("3 - " + getTectonTestDescription(3) + " - [1. Szekvencia diagram]");
            System.out.println("4 - " + getTectonTestDescription(4) + " - [3. Szekvencia diagram]");
            System.out.println("5 - " + getTectonTestDescription(5) + " - [5. Szekvencia diagram]");
            System.out.println("6 - " + getTectonTestDescription(6) + " - [17. Szekvencia diagram]");
            System.out.println("7 - " + getTectonTestDescription(7) + " - [19. Szekvencia diagram]");
            System.out.println("0 - Vissza a főmenübe");
            System.out.print("Választás: ");

            int choice = getUserInput();
            if (choice == 0) break;
            if (choice >= 1 && choice <= 7) {
                System.out.println("[i] Tekton teszt: " + choice + ". - " + getTectonTestDescription(choice));
            }
            switch (choice) {
                case 1: // seq:2
                {
                    logMethodCall("Tester", "MushroomBody", "initiateMushroomYarnGrowth", "neighbourboth", 0);
                    bool = skeletonHelper.getMushroomBodies().get("mb2").initiateMushroomYarnGrowth(skeletonHelper.getTectons().get("neighbourboth"));
                    if (bool) {
                        System.out.println(" The MushroomBody initiated growth on the target Tecton and succesfully grow a MushroomYarn althought the Tecton was full of yarns");

                    } else {
                        System.out.println(" The MushroomBody initiated growth on the target Tecton, but failed to grow a MushroomYarn because the tecton was full");
                    }
                    break;
                }
                case 2: // seq: 4
                {
                    logMethodCall("Tester", "Insect", "moveToTecton", "neighbourOft1", 0);

                    skeletonHelper.getInsect().moveToTecton(skeletonHelper.getTectons().get("neighbourOft1"));

                    break;
                }
                case 3: // seq: 1
                {

                    logMethodCall("Tester", "DegredableTecton", "checkYarnDegradation", "", 0);
                    skeletonHelper.getTectons().get("dt1").checkYarnDegradation();
                    break;
                }
                case 4: // seq: 3
                {
                    logMethodCall("Tester", "MushroomBody", "initiateMushroomBodyGrowth", "neighbourOft1", 0);
                    bool = skeletonHelper.getMushroomBodies().get("mb1").initiateMushroomBodyGrowth(skeletonHelper.getTectons().get("neighbourOft1"), true);
                    break;
                }
                case 5: // seq: 5
                {
                    logMethodCall("Tester", "MushroomBody", "initiateMushroomBodyGrowth", "dt1", 0);
                    bool = skeletonHelper.getMushroomBodies().get("mb1").initiateMushroomBodyGrowth(skeletonHelper.getTectons().get("dt1"), true);
                    //logMethodReturn(""+bool,0);
                    break;
                }
                case 6: // seq: 17
                {
                    logMethodCall("Tester", "MushroomBody", "releaseSpore", "t1", 0);
                    //      skeletonHelper.getMushroomBodies().get("mb1").releaseSpore(skeletonHelper.getTectons().get("t1"));
                    break;
                }
                case 7: // seq: 19
                {
                    logMethodCall("Tester", "MushroomBody", "releaseSpore", "t1", 0);
                    //       skeletonHelper.getMushroomBodies().get("mb1").releaseSpore(skeletonHelper.getTectons().get("t1"));
                    break;
                }
                default: {
                    System.out.println("[i] Érvénytelen választás!");
                }

            }
        }
    }


    /**
     * Provides the description of the Entomologist test based on the user's choice.
     *
     * @param choice the test number.
     * @return the description of the selected Entomologist test.
     */
    private static String getEntomologistTestDescription(int choice) {
        String[] descriptions = {
                "Gyorsító spóra hatása rovarokra és a rovarászra",
                "Lassító spóra hatása rovarokra és a rovarászra",
                "Bénító spóra hatása rovarokra és a rovarászra",
                "Gyengító spóra hatása rovarokra és a rovarászra",
                "Rovarok sikeres mozgása a gombafonalak mentén",
                "Rovarok gombafonal vágása",
                "Rovarok sikertelen mozgatása gombafonal mentén"
        };
        return descriptions[choice - 1];
    }

    /**
     * Provides the description of the Mycologist test based on the user's choice.
     *
     * @param choice the test number.
     * @return the description of the selected Mycologist test.
     */
    private static String getMycologistTestDescription(int choice) {
        String[] descriptions = {
                "Gombatest spórát szór",
                "Gombatestek fejlődése",
                "Gombatest maximális számú spóra szórása",
                "Tápláló gombatest nélküli fonal",
                "Gombatestek gombafonal növesztése",
                "Gombászok gombatestek fejlődését kezdeményezik"
        };
        return descriptions[choice - 1];
    }

    /**
     * Provides the description of the Tecton test based on the user's choice.
     *
     * @param choice the test number.
     * @return the description of the selected Tecton test.
     */
    private static String getTectonTestDescription(int choice) {
        String[] descriptions = {
                "Maximális számú gombafonal",
                "Tekton kettétörése",
                "Gombafonalak felszívódása",
                "Egy tektonon egy gombatest korlátozása",
                "Tekton, amely nem tud gombát növeszteni",
                "Egy tektonon egy típusú spóra lehet csak",
                "Egy tektonon azonos típusú spórából több"
        };
        return descriptions[choice - 1];
    }

    /**
     * Logs the method call with its parameters for debugging or tracing.
     *
     * @param callerClass    the name of the calling class.
     * @param targetClass    the name of the target class.
     * @param methodName     the name of the method being called.
     * @param parameters     the parameters passed to the method.
     * @param numberOfIndent the number of indentation levels for readability.
     */
    public static void logMethodCall(String callerClass, String targetClass, String methodName, String parameters, int numberOfIndent) {
        for (int i = 0; i < numberOfIndent; i++) {
            System.out.print("\t");
        }
        System.out.println(callerClass + "-->" + targetClass + "." + methodName + "(" + parameters + ")");
    }

    /**
     * Logs the return value of a method call.
     *
     * @param value          the return value of the method.
     * @param numberOfIndent the number of indentation levels for readability.
     */
    public static void logMethodReturn(String value, int numberOfIndent) {
        for (int i = 0; i < numberOfIndent; i++) {
            System.out.print("\t");

        }
        System.out.println("<--" + value);
    }


}