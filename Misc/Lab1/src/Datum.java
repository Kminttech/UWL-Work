/**
 * Simple interface for a data-storing object.
 * Can set/get data, and make a copy of the existing object.
 *
 * @author M. Allen
 */
public interface Datum<T>
    {
        /**
         * Returns a data element of parameterized type.
         *
         * @return Data element; calling get() after set( dat ) will return dat.
         *         Calling get() without previous set() call may return a default
         *         value, or null.
         */
        public T get();
        
        /**
         * Sets a data element of parameterized type.
         *
         * @param dat Data element; calling get() after set( dat ) will return dat.
         */
        public void set( T dat );
        
        /**
         * Returns a Datum<T> element with same data. Running get() on copy should
         * return identical data.
         *
         * @return Datum<T> element with same data.
         */
        public Datum<T> copyOf();
    }
