package giganticintegers;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.io.*;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility I used to make a file full of gigantic integers
 */
public class MakeIntegers {

    private static final AtomicLong counter = new AtomicLong(0L);
    private static final long ITERATIONS = 100;

    public static void main(String[] args) {
        try {
            File file = new File("./input.txt");
            System.out.println("file = " + file.getPath());
            BufferedWriter fos = new BufferedWriter(new FileWriter(file.getPath()));

            Flowable<BigInteger> giganticIntegers =
                    Flowable.generate(Random::new, (random, emitter) -> {
                        /*
                        Here I generate a Flowable of <ITERATIONS> BigIntegers
                         */
                        long count = counter.getAndIncrement();
                        if (count >= ITERATIONS) {
                            emitter.onComplete();
                        }
                        BigInteger bi = new BigInteger(333, random);
                        if (count % 2 != 0) {
                            bi = bi.negate();
                        }
                        emitter.onNext(bi);
                    });

            giganticIntegers
                    .parallel()
                    .runOn(Schedulers.io())
                    .sequential()
                    .blockingSubscribe(
                            giganticInteger -> {
                                /*
                                I write the integers to the file here
                                 */
                                fos.append(giganticInteger.toString(10)).append('\n');
                            },
                            throwable -> {
                                throwable.printStackTrace();
                            },
                            () -> {
                                fos.close();
                            }
                    );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
