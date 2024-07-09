public class dist
{
    //distance value received from user
    double distance;

    //conversion functions
    public double milesToKilo()
    {
        double mil = getDist();
        return (mil * 1.609);
    }

    public double kiloToMiles()
    {
        double kilo = getDist();
        return (kilo / 1.609);
    }

    //function to get dist value
    public double getDist()
    {
        return distance;
    }
}
