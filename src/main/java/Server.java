import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import login.loginHandler;
import util.Config;

public class Server {

    private static ServerBootstrap serverBootstrap = new ServerBootstrap();


    private static void run(){
        EventLoopGroup bossGroup  = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            serverBootstrap.group(bossGroup,workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(Config.SOCKET_SERVER_PORT);
            serverBootstrap.option(ChannelOption.SO_KEEPALIVE,true);
            serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    System.out.println("练到了");
                    socketChannel.pipeline().addLast(loginHandler.instance);
                }
            });
            System.out.println("来了?");
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println("服务器启动成功");
            ChannelFuture closeCahnnel = channelFuture.channel().closeFuture();
            closeCahnnel.sync();




        }catch (Exception e){

        }finally {

        }
        return ;
    }

    public static void main(String[] args) {
        Server server = new Server();

        server.run();



    }
}
