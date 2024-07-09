import java.util.*;

public abstract class GeometricObject 
{
    int color;
    Date d1 = new Date(); 

    //abstract methods
    public abstract Double getArea();
    public abstract Double getPerimeter();

    //non-abstract methods
    public void setColor(int col)
    {
        color = col;
    }

    public int getColor()
    {
        return color;
    }
}