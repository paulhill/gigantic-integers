package giganticintegers;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Processes streams of gigantic integers, maintaining the maximum, minimum, and a count of integers above a given threshold.
 */
public class GiganticIntegersProcessor {

    // gigantic 200 digit number for upper bound of comparison
    String upperBound = "999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999"
            + "9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999";
    // lower bound is inverse of upper bound
    String lowerBound = "-" + upperBound;
    // Given threshold
    private final BigInteger threshold;
    // Threadsafe max min and count variables
    private final AtomicBigInteger max = new AtomicBigInteger(new BigInteger(lowerBound));
    private final AtomicBigInteger min = new AtomicBigInteger(new BigInteger(upperBound));
    private final AtomicBigInteger count = new AtomicBigInteger(BigInteger.ZERO);

    /**
     * Processor of gigantic integers
     * @param threshold Maintains count of all integers above this threshold
     */
    public GiganticIntegersProcessor(BigInteger threshold) {
        this.threshold = threshold;
    }

    /**
     * Process the gigantic integers provided by the reader, updating max, min, and count.
     * @param reader Produces gigantic integer strings
     */
    public void process(Reader reader) {

        final AtomicBoolean firstComplete = new AtomicBoolean(true);

        Flowable<String> flow = Flowable.generate(
                () -> new BufferedReader(reader),
                (r, emitter) -> {
                    final String line = r.readLine();
                    if (line != null) {
                        emitter.onNext(line);
                    } else {
                        emitter.onComplete();
                    }
                },
                r -> r.close()
        );

        flow
                .parallel()
                .runOn(Schedulers.computation())
                .map(s -> new BigInteger(s))
                .map(b -> {

                    max.maxAndGet(b);
                    min.minAndGet(b);
                    if (b.compareTo(threshold) > 0) {
                        count.incrementAndGet();
                    }
                    return b;
                })
                .doOnComplete(() -> {
                    if (firstComplete.getAndSet(false)) {
                        System.out.println();
                    }
                    PrintUtils.println("complete");
                })
                .sequential()
                .doOnComplete(() -> {
                    PrintUtils.printlnStatus(this);
                })
                .blockingSubscribe();
    }

    public AtomicBigInteger getMax() {
        return max;
    }

    public AtomicBigInteger getMin() {
        return min;
    }

    public AtomicBigInteger getCount() {
        return count;
    }

}