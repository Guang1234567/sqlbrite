package com.example.sqlbrite.todo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author Administrator
 * @date 2018/4/26 16:24
 */

public class ObjectMemorySizeFetcher {

    public static final int sizeOf(Object object) throws IOException {

        if (object == null)
            return -1;

        // Special output stream use to write the content
        // of an output stream to an internal byte array.
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();

        // Output stream that can write object
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(byteArrayOutputStream);

        // Write object and close the output stream
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();

        // Get the byte array
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        // TODO can the toByteArray() method return a
        // null array ?
        return byteArray == null ? 0 : byteArray.length;
    }
}
