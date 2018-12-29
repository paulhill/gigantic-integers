package giganticintegers;

import org.junit.Test;

import java.io.*;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class GiganticIntegersProcessorTest {

    @Test
    public void process() throws FileNotFoundException {
        /*
        Create a persistent GiganticIntegersProcessor
         */
        BigInteger threshold = BigInteger.ZERO;
        GiganticIntegersProcessor gip = new GiganticIntegersProcessor(threshold);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());
        Reader reader = new BufferedReader(new FileReader(file));
        /*
        Consume the file
        Nom nom nom
         */
        gip.process(reader);
        assertEquals(new BigInteger("50"), gip.getCount().toValue());
        assertEquals(new BigInteger("17413871698208807946414502079355074140520607469762275286803494859775709781235327348754395371255876692"), gip.getMax().toValue());
        assertEquals(new BigInteger("-17422706223666315951578128623638133250304176507958243202060218800600013326740021771693712386290771654"), gip.getMin().toValue());
    }
}