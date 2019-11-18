package login;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import redis.clients.jedis.Jedis;
import util.JedisPoolBuildPool;

import java.nio.ByteBuffer;

public class LoginProcess {

    public static boolean loginVerified(ChannelHandlerContext ctx,Object msg){
        ByteBuf byteBuf = (ByteBuf) msg;
        int len = byteBuf.readableBytes();
        byte[] arr = new byte[len];
        byteBuf.getBytes(0,arr);
        String userPass = null;
        try{
            userPass = new String(arr,"UTF-8");
            System.out.println("server is:" + userPass);
        }catch (Exception e){
            System.out.println("用户名和密码转换错误");
            e.printStackTrace();
        }
        String[] splitArray = userPass.split(",");
        Jedis redis = JedisPoolBuildPool.getJedis();
        String passWord = redis.get(splitArray[0]);
        boolean login = false;
        if((!"nil".equals(passWord)) && splitArray[1].equals(passWord) ){
            login = true;
        }
        System.out.println("redis中数据存在的情况:"+login);
        byte[] bytes = ByteBuffer.allocate(4).putInt((login == false) ? 0 : 1).array();
        ByteBuf outbuf = ByteBufAllocator.DEFAULT.buffer(4);
        outbuf.writeBytes(bytes);
        ChannelFuture channelFuture = ctx.writeAndFlush(outbuf);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){
                    System.out.println("写回成功");
                }else{
                    System.out.println("写回失败");
                }
            }
        });
        return login;
    }
}
