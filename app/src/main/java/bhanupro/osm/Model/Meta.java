package bhanupro.osm.Model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class Meta
{
    @Attribute
    private String osm_base;

    public String getOsm_base ()
    {
        return osm_base;
    }

    public void setOsm_base (String osm_base)
    {
        this.osm_base = osm_base;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [osm_base = "+osm_base+"]";
    }
}
