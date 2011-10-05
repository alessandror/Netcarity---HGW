/*
Copyright 2011 Alex Redaelli

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package net.hgw4.hal;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

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