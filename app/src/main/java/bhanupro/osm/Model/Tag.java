package bhanupro.osm.Model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class Tag
{
    @Attribute
    private String v;

    @Attribute
    private String k;

    public String getV ()
    {
        return v;
    }

    public void setV (String v)
    {
        this.v = v;
    }

    public String getK ()
    {
        return k;
    }

    public void setK (String k)
    {
        this.k = k;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [v = "+v+", k = "+k+"]";
    }
}
