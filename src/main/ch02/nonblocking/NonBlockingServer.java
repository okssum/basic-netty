package ch02.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NonBlockingServer {
    
    private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
    private ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);
    
    private void startEchoServer() {
        
        try (
                // Selector: 자신에게 등록된 채널에 변경 사항이 발생했는지 검사하고 변경 사항이 발생한 채널에 대한 접근을 가능하게 해줌
                Selector selector = Selector.open();
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // 논블로킹 소켓의 서버 소켓 채널 생성
        ) {
            if ((serverSocketChannel.isOpen()) && (selector.isOpen())) {
                serverSocketChannel.configureBlocking(false); // 기본값은 true
                serverSocketChannel.bind(new InetSocketAddress(8888));
                
                // Selector 객체에 ServerSocketChannel 객체 등록
                // Selector가 감지할 이벤트는 연결 요청임(SelectionKey.OP_ACCEPT)
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("접속 대기중");
                
                while (true) {
                    // Selector 에 등록된 채널에서 변경 사항이 발생했는지 검사
                    // 아무런 I/O 이벤트가 발생하지 않으면 스레드는 이 부분에서 블로킹됨
                    // 블로킹을 피하고 싶으면 selectNow 메서드를 사용하면 됨
                    selector.select();
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    
                    while (keys.hasNext()) {
                        SelectionKey key = (SelectionKey) keys.next();
                        keys.remove();
                        
                        if (!key.isValid()) {
                            continue;
                        }
                        
                        // 조회된 I/O 이벤트가 연결 요청인지 확인
                        if (key.isAcceptable()) {
                            this.acceptOP(key, selector);
                        }
                        // 조회된 I/O 이벤트가 데이터 수신인지 확인
                        else if (key.isReadable()) {
                            this.readOP(key);
                        }
                        // 조회된 I/O 이벤트가 데이터 쓰기인지 확인
                        else if (key.isWritable()) {
                            this.writeOP(key);
                        }
                    }
                }
            }
            else {
                System.out.println("서버 소켓을 생성하지 못했습니다.");
            }
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    private void acceptOP(SelectionKey key, Selector selector) throws IOException {
        
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverChannel.accept();
        socketChannel.configureBlocking(false);
        
        System.out.println("클라이언트 연결됨 : " + socketChannel.getRemoteAddress());
        
        keepDataTrack.put(socketChannel, new ArrayList<byte[]>());
        socketChannel.register(selector, SelectionKey.OP_READ);
    }
    
    private void readOP(SelectionKey key) {
        
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            buffer.clear();
            int numRead = -1;
            try {
                numRead = socketChannel.read(buffer);
            } catch (IOException ex) {
                System.err.println("데이터 읽기 에러");
            }
            
            if (numRead == -1) {
                this.keepDataTrack.remove(socketChannel);
                System.out.println("클라이언트 연결 종료 : " + socketChannel.getRemoteAddress());
                socketChannel.close();
                key.channel();
                return;
            }
            
            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(), 0, data, 0, numRead);
            System.out.println(new String(data, "UTF-8") + " from " + socketChannel.getRemoteAddress());
            
            doEchoJob(key, data);
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    private void writeOP(SelectionKey key) throws IOException {
        
        SocketChannel socketChannel = (SocketChannel) key.channel();
        
        List<byte[]> channelData = keepDataTrack.get(socketChannel);
        Iterator<byte[]> its = channelData.iterator();
        
        while (its.hasNext()) {
            byte[] it = its.next();
            its.remove();
            socketChannel.write(ByteBuffer.wrap(it));
        }
        
        key.interestOps(SelectionKey.OP_READ);
    }
    
    private void doEchoJob(SelectionKey key, byte[] data) {
        
        SocketChannel socketChannel = (SocketChannel) key.channel();
        List<byte[]> channelData = keepDataTrack.get(socketChannel);
        channelData.add(data);
        
        key.interestOps(SelectionKey.OP_WRITE);
    }
    
    public static void main(String[] args) {
        
        NonBlockingServer main = new NonBlockingServer();
        main.startEchoServer();
    }
    
}
