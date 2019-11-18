package login;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import redis.clients.jedis.Jedis;
import task.CallBackTask;
import task.CallBackTaskThread;
import util.JedisPoolBuildPool;

import java.nio.ByteBuffer;

public class loginHandler extends ChannelInboundHandlerAdapter {
    public static final loginHandler instance = new loginHandler();


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(null == msg){
            super.channelRead(ctx,msg);
            return;
        }

        CallBackTaskThread.add(new CallBackTask<Boolean>() {
            @Override
            public Boolean execute() {
                System.out.println("发送登录信息");
                boolean login = LoginProcess.loginVerified(ctx,msg);
                return login;
            }

            @Override
            public void onBack(Boolean aBoolean) {
                if(aBoolean){
                    ctx.pipeline().remove(loginHandler.instance);
                    System.out.println("登录成功");
                }

            }

            @Override
            public void onException(Throwable t) {
                System.out.println("登录异常");
            }
        });




    }
}
