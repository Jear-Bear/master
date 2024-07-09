public class temper
{
    //temperature value received from user
    double temperature;

    //conversion functions
    public double farToCels()
    {
        final double far = getTemp();
        return ((far - 32.0) * 5.0 / 9.0);
    }

    public double celsToFar() {
        final double cel = getTemp();
        return ((cel * 9.0/5.0) + 32);
    }

    //function to get temp value
    public double getTemp()
    {
        return temperature;
    }
}

