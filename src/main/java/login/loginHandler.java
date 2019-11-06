package login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class loginHandler extends ChannelInboundHandlerAdapter {
    public static final loginHandler instance = new loginHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int len = byteBuf.readableBytes();
        byte[] arr = new byte[len];
        byteBuf.getBytes(0,arr);
        System.out.println("server is:" + new String(arr,"UTF-8"));

        ChannelFuture channelFuture = ctx.writeAndFlush(msg);
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
    }
}
