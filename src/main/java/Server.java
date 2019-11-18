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


    public void run(){
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
                    socketChannel.pipeline().addLast(loginHandler.instance);
                }
            });

            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println("服务器启动成功");
            ChannelFuture closeCahnnel = channelFuture.channel().closeFuture();
            closeCahnnel.sync();

        }catch (Exception e){
            System.out.println("服务器启动出现异常");
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

}
