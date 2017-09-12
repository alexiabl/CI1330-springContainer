package cr.ac.ucr.ecci.ci1330.SpringContainer;

import cr.ac.ucr.ecci.ci1330.SpringContainer.XMLBased.XMLBeanFactory;
import nu.xom.ParsingException;

import java.io.*;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws ParsingException, IOException {
        String path="./SpringContainer/src/main/resources/beanExample.xml";
        XMLBeanFactory beanFactory = new XMLBeanFactory(path);

}
}