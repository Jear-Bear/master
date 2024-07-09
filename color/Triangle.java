public class Triangle extends GeometricObject implements Comparable<Triangle>
{
    //int array for storing sides of triangle
    double[] tri = new double[3];

    //for use in getArea()
    Double height;
    Double area;

    //constructor that takes args
    Triangle(double side1, double side2, double side3)
    {
        this.tri[0] = side1;
        this.tri[1] = side2;
        this.tri[2] = side3;
    }

    //constructor with no args
    Triangle()
    {
        this.tri[0] = 1;
        this.tri[1] = 1;
        this.tri[2] = 1;
        this.setColor(1);
        this.area = this.getArea();
    }
    //implementation of the getArea abstract method
    public Double getArea()
    {
        //use heron's formula
        height = (this.tri[0] + this.tri[1] + this.tri[2]) / 2.0;
        area = Math.sqrt(height*(height - this.tri[0])*(height - this.tri[1])*(height - this.tri[2]));

        return area;
    }

    //implementation of the getPerimeter abstract method
    public Double getPerimeter()
    {
        //return the sum of the sides
        return (this.tri[0] + this.tri[1] + this.tri[2]);
    }

    //implement Comparable interface to compare areas between two triangles
    @Override
    public int compareTo(Triangle t) 
    {
        if (this.getArea() > t.getArea()) return 1;
        else if (this.getArea() < t.getArea()) return -1;
        else return 0;
    }

    //get type of triangle
    String getType()
    {
        //check is all sides are the same length
        if ((this.tri[0] == this.tri[1]) && (this.tri[1] == this.tri[2])) return "equilateral";

        //check if the pythagorean theorem applies
        else if ((Math.pow(this.tri[0], 2) + Math.pow(this.tri[1], 2) == Math.pow(this.tri[2], 2))) return "right";

        //check if exactly two sides are the same length
        else if (((this.tri[0] == this.tri[1]) && (this.tri[1] != this.tri[2])) || ((this.tri[1] == this.tri[2]) && (this.tri[1] != this.tri[0])) || ((this.tri[0] == this.tri[2]) && (this.tri[2] != this.tri[1]))) return "isosceles";
        
        //otherwise it must be a scalene triangle
        else return "scalene";
    }
}

