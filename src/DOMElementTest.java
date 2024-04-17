import java.util.*;

import static org.junit.Assert.*;

// Create your classes here:
abstract class HtmlTag {
    public String getTagOpen() {
        return "<" + this.getClass().getSimpleName().toLowerCase() + ">";
    }

    public String getTagClose() {
        return "</" + this.getClass().getSimpleName().toLowerCase() + ">";
    }
}
class Html extends HtmlTag implements DOMElement {

    private Body body = null;
    private DOMElement element;

    public Html (Body body) {
        this.body = body;
        body.setParent(this);
    }

    @Override
    public String toString() {
        return getTagOpen() + body.toString() + getTagClose();
    }

    @Override
    public DOMElement getParent() {
        return null;
    }

    @Override
    public void setParent(DOMElement parent) {

    }
}

class Body extends HtmlTag  implements DOMElement {
    private DOMElement header;
    private DOMElement list;
    private DOMElement parent;

    public Body (H1 header, Ul list) {
        this.header = header;
        this.header.setParent(this);
        this.list = list;
        this.list.setParent(this);
    }

    public DOMElement getParent() {
        return parent;
    }

    public void setParent(DOMElement parent) {
        this.parent = parent;
    }

    public String toString() {
        return getTagOpen() + header.toString() + list.toString() + getTagClose();
    }
}

class H1 extends HtmlTag implements DOMElement {
    private DOMElement parent;
    private String text;
    public H1(String text) {
        this.text = text;
    }

    public DOMElement getParent() {
        return parent;
    }

    public void setParent(DOMElement parent) {
        this.parent = parent;
    }

    public String toString() {
        return getTagOpen() + text + getTagClose();
    }
}

class Ul extends HtmlTag implements DOMElement {
    private DOMElement parent;
    private Set<Li> list = new TreeSet<Li>(Comparator.comparing(Li::getText));

    private String text;
    public Ul(Li...list) {
        for(Li element : list) {
            element.setParent(this);
            this.list.add(element);
        }
    }

    public DOMElement getParent() {
        return parent;
    }

    public void setParent(DOMElement parent) {
        this.parent = parent;
    }

    public String toString() {
        text = getTagOpen();
        list.forEach(element -> text += element);
        text += getTagClose();
        return text;
    }
}

class Li extends HtmlTag implements DOMElement{
    private DOMElement parent;
    public String text;
    public Li(String text){
        this.text = text;
    }
    public DOMElement getParent() {
        return parent;
    }

    public void setParent(DOMElement parent) {
        this.parent = parent;
    }

    public String toString() {
        return getTagOpen() + text + getTagClose();
    }

    public String getText() {
        return this.text;
    }
}


// DO NOT CHANGE THIS INTERFACE
interface DOMElement {

    void setParent(DOMElement parent);

    DOMElement getParent();

}

/**
 * DO NOT CHANGE THIS CLASS
 */
public class DOMElementTest {

    public static void main(String[] args) {

        Li lastLi = new Li("Option 4");

        Html domElement = new Html(
                new Body(
                        new H1("Hello world!"),
                        new Ul(
                                new Li("Option 1"),
                                new Li("Option 2"),
                                new Li("Option 2"),
                                new Li("Option 3"),
                                lastLi)));
        assertEquals(
                "<html><body><h1>Hello world!</h1><ul><li>Option 1</li><li>Option 2</li><li>Option 3</li><li>Option 4</li></ul></body></html>",
                domElement.toString());

        assertEquals("Ul",   lastLi.getParent().getClass().getSimpleName());
        assertEquals("Body", lastLi.getParent().getParent().getClass().getSimpleName());
        assertEquals("Html", lastLi.getParent().getParent().getParent().getClass().getSimpleName());

        System.out.println(domElement);

        System.out.println("Success");
    }
}