package util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;

public class Config {
    static public String  getValue(String key){
        FileInputStream fileInputStream = null;
        String value = null;
        try{
            fileInputStream = new FileInputStream(new File("NettyServer/src/main/resources/config.properties"));
            Properties properties = new Properties();
            properties.load(fileInputStream);
            Enumeration em = properties.propertyNames();
            while(em.hasMoreElements()){
                String emKey = (String) em.nextElement();
                if(emKey != null &&  emKey.equals(key)){
                    value = properties.getProperty(emKey);
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtil.close(fileInputStream);
        }
        return value;
    }
    public static String SOCKET_SERVER_IP = Config.getValue("SOCKET_SERVER_IP") ;
    public static int SOCKET_SERVER_PORT = Integer.parseInt(Config.getValue("SOCKET_SERVER_PORT")) ;
    public static int capacity = Integer.parseInt(Config.getValue("capacity"));
    public static String REDIS_SERVER_IP = Config.getValue("REDIS_SERVER_IP") ;
    public static int REDIS_SERVER_PORT = Integer.parseInt(Config.getValue("REDIS_SERVER_PORT")) ;
}
