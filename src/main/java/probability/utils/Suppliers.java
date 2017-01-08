/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modifications copyright (C) 2016 Wolfgang Welz
 */

package probability.utils;

import java.util.Objects;
import java.util.function.Supplier;

public final class Suppliers {

  private Suppliers() {
    // do not initialize
  }

  /**
   * Returns a supplier which caches the instance retrieved during the first
   * call to {@code get()} and returns that value on subsequent calls to
   * {@code get()}. A return value of {@code null} is supported and cached.
   *
   * <p>The returned supplier is thread-safe. If {@code delegate} is an instance
   * created by an earlier call to {@code memoize}, it is returned directly.
   *
   * <p>This is a copy of {@link com.google.common.base.Suppliers#memoize}
   * modified to use the Java own {@link Supplier}, the serialization
   * support has been dropped.
   */
  public static <T> Supplier<T> memoize(Supplier<T> delegate) {

    if (delegate instanceof MemoizingSupplier) {
      return delegate;
    }
    return new MemoizingSupplier<>(Objects.requireNonNull(delegate));
  }

  private static class MemoizingSupplier<T> implements Supplier<T> {

    final Supplier<T> _delegate;

    volatile boolean _initialized;

    // "value" does not need to be volatile; visibility piggy-backs
    // on volatile read of "initialized".
    T _value;

    MemoizingSupplier(Supplier<T> delegate) {
      _delegate = delegate;
    }

    @Override public T get() {

      if (!_initialized) {
        // lock after check so that calls after init don't need to lock
        synchronized (this) {
          // check again, to always avoid multiple initializations
          if (!_initialized) {
            T t = _delegate.get();
            _value = t;
            _initialized = true;
            return t;
          }
        }
      }
      return _value;
    }
  }

}
