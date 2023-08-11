package nuparu.sevendaystomine.util;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record BiOptional<T, S>(T left,  S right){
    public static <L, R> BiOptional<L, R> of(Optional<L> left, Optional<R> right){
        return new BiOptional<>(left.orElse(null), right.orElse(null));
    }

    public T getLeft() {
        if (left == null) {
            throw new NoSuchElementException("No value present");
        }
        return left;
    }

    public T leftOrElse(T other) {
        return left != null ? left : other;
    }

    public T leftOrElseGet(Supplier<? extends T> supplier) {
        return left != null ? left : supplier.get();
    }

    public S getRight() {
        if (left == null) {
            throw new NoSuchElementException("No value present");
        }
        return right;
    }

    public S rightOrElse(S other) {
        return right != null ? right : other;
    }

    public S rightOrElseGet(Supplier<? extends S> supplier) {
        return right != null ? right : supplier.get();
    }

    public boolean hasLeft(){
        return left != null;
    }

    public boolean hasRight(){
        return right != null;
    }

    public boolean emptyLeft(){
        return !hasLeft();
    }

    public boolean emptyRight(){
        return !hasRight();
    }

    public boolean isEmpty(){
        return emptyLeft() && emptyRight();
    }

    public boolean isFullyPresent(){
        return hasLeft() && hasRight();
    }

    public boolean isPresent(){
        return !isEmpty() && !isFullyPresent();
    }

    public void ifLeftPresent(Consumer<? super T> action) {
        if (left != null) {
            action.accept(left);
        }
    }

    public void ifRightPresent(Consumer<? super S> action) {
        if (right != null) {
            action.accept(right);
        }
    }

    public void ifPresent(BiConsumer<? super T, ? super S> action) {
        if (left != null && right != null) {
            action.accept(left, right);
        }
    }
}
