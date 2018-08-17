package ch06.bytebuffer._1_java;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.junit.Test;

public class ReadByteBufferTest {

    @Test
    public void readTest() {
        
        byte[] tempArray = { 1, 2, 3, 4, 5, 0, 0, 0, 0, 0, 0};
        ByteBuffer firstBuffer = ByteBuffer.wrap(tempArray);
        assertEquals(0, firstBuffer.position());
        assertEquals(11, firstBuffer.limit());
        
        assertEquals(1, firstBuffer.get());
        assertEquals(2, firstBuffer.get());
        assertEquals(3, firstBuffer.get());
        assertEquals(4, firstBuffer.get());
        assertEquals(4, firstBuffer.position());
        assertEquals(11, firstBuffer.limit());
        
        firstBuffer.flip();
        assertEquals(0, firstBuffer.position());
        assertEquals(4, firstBuffer.limit());
        
        firstBuffer.get(3);
        
        assertEquals(0, firstBuffer.position());
    }

}
