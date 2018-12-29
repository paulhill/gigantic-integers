package giganticintegers;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicBigInteger {

    private final AtomicReference<BigInteger> valueHolder = new AtomicReference<>();

    public AtomicBigInteger(BigInteger bigInteger) {
        valueHolder.set(bigInteger);
    }

    public BigInteger incrementAndGet() {
        for (; ; ) {
            BigInteger current = valueHolder.get();
            BigInteger next = current.add(BigInteger.ONE);
            if (valueHolder.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    public BigInteger maxAndGet(BigInteger comparable) {
        for (; ; ) {
            BigInteger current = valueHolder.get();
            BigInteger next = current.max(comparable);
            if (valueHolder.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    public BigInteger minAndGet(BigInteger comparable) {
        for (; ; ) {
            BigInteger current = valueHolder.get();
            BigInteger next = current.min(comparable);
            if (valueHolder.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    @Override
    public String toString() {
        return valueHolder.get().toString();
    }

    public BigInteger toValue() {
        return valueHolder.get();
    }
}