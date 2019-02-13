package bhanupro.osm.Model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class Osm
{
    @Element(required = false)
    private String note;

    @ElementList(entry = "node",inline = true)
    private List<Node> node;

    @Element(required = false)
    private Meta meta;
    @Attribute
    private String generator;
    @Attribute
    private String version;

    public String getNote ()
    {
        return note;
    }

    public void setNote (String note)
    {
        this.note = note;
    }

    public List<Node> getNode() {
        return node;
    }

    public void setNode(List<Node> node) {
        this.node = node;
    }

    public Meta getMeta ()
    {
        return meta;
    }

    public void setMeta (Meta meta)
    {
        this.meta = meta;
    }

    public String getGenerator ()
    {
        return generator;
    }

    public void setGenerator (String generator)
    {
        this.generator = generator;
    }

    public String getVersion ()
    {
        return version;
    }

    public void setVersion (String version)
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [note = "+note+", node = "+", meta = "+meta+", generator = "+generator+", version = "+version+"]";
    }
}
