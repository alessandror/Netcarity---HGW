/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package HAL;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

/**
 *
 * @author alessandror
 */
public class CircularBuffer<T> {

  private T[] buffer;

  private int tail;

  private int head;

  @SuppressWarnings("unchecked")
  public CircularBuffer(int n) {
    buffer = (T[]) new Object[n];
    tail = 0;
    head = 0;
  }

  public void add(T toAdd) {
    if (head != (tail - 1)) {
        buffer[head++] = toAdd;
    } else {
        throw new BufferOverflowException();
    }
    head = head % buffer.length;
  }

  public T get() {
    T t = null;
    int adjTail = tail > head ? tail - buffer.length : tail;
    if (adjTail < head) {
        t = (T) buffer[tail++];
        tail = tail % buffer.length;
    } else {
        //throw new BufferUnderflowException();
        return null;
    }
    return t;
  }
}