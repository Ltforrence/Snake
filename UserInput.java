public class UserInput {
    


    public int getSpeed()
    {
        keyboard = new Scanner(System.in);
        println("Hello and Welcom to my snake game!\nFrom 1-10 what speed would you like your snake to travel at?\nSpeed: ");
        try{
            keyboard.nextInt();
        }
        catch(exception e)
        {
            System.out.println("Hey, you didn't enter a number!");
        }

    }
}
