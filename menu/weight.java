public class weight
{
    //weight value received from user
    double w;

    //conversion functions
    public double poundsToKilo()
    {
        double pound = getWeight();
        return (pound / 2.20462);
    }

    public double kiloToPounds()
    {
        double kilo = getWeight();
        return (kilo * 2.20462);
    }

    //function to get weight value
    public double getWeight()
    {
        return w;
    }
}
