package com.timeworx.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 唯一Id生成类
 * 本id生成器可以生成30年的数据，单机每秒生成16000个不重复，时间从2015-01-01开始到2045-01-01止，数字开始重复
 * 本类为了保证生成的id精度在52位，减少了每毫秒生成的个数为16，主要是方便js等语言使用
 * 为了区分每台机器，会自动选取ip地址的后8个bit来最为标示，你可以通过在java命令行中传入 -Dserver.id=xx 来设置一个不同的值
 *
 * 	serverBinaryString 左对齐补0
 * 	sequenceBinaryString 增加至8位（注意：当 1ms 内生成次数 大于 256 次时，会产生同样的 id）
 *  sequenceBinaryString 超过40位时去除低位（之前去除的是高位）
 *  总长度一共56位，生成的ID最大值为 72057594037927935
 * @Author: ryzhang
 * @Date 2023/3/7 9:57 PM
 */
public final class UniqueIDUtil {

    private static Logger logger = LoggerFactory.getLogger(UniqueIDUtil.class);

    /**
     * 开始时间，在项目开始前可根据需要进行修改，但当数据产生以后，为保证id的连续性和唯一性，请不要修改
     */
    public static String beginTime = "2015-01-01 00:00:00";
    private static long beginTimeMillis = 0;
    private static short sequence = 0;
    private static Lock seqLock = new ReentrantLock();
    private static String serverBinaryString = "";

    private static int timeBits = 40;
    private static int seqBits = 8;
    private static int serverBits = 8;

    private static int seqMax = (int) Math.pow(2, seqBits);

    private static long preId;

    static {
        try {
            beginTimeMillis = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beginTime).getTime();
            int maxServerId = (int) Math.pow(2, serverBits);
            String serverId = System.getProperty("server.id");
            int server = 0;
            if (serverId != null){
                server = Integer.parseInt(serverId);
            }else {
                String ip = getIpAddress();
                if (ip == null || ip.equals("")) {
                    //获取不到的用随机数代替
                    server = new Random().nextInt(maxServerId - 1);
                } else {
                    server = Integer.parseInt(ip.substring(ip.lastIndexOf(".") + 1));
                }
            }
            if (server >= maxServerId){
                System.err.println("server.id is max than "+maxServerId);
            }
            System.out.println("WARN: 此台服务器的Id为 server = " + server + ", 必须与其他负载机器不一致，否则id可能会重复");
            System.out.println("WARN: 你可以通过在java命令行中传入 -Dserver.id=xx 来设置一个不同的值");
            serverBinaryString = StringUtils.leftPad(Integer.toBinaryString(server), serverBits,'0');
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取当前服务器ip地址
     * @return
     * @throws SocketException
     */
    public static String getIpAddress() throws SocketException {
        Enumeration<NetworkInterface> allNetInterfaces  = NetworkInterface.getNetworkInterfaces();
        if (allNetInterfaces != null) {
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        //去除localhost 127.0.0.1
                        String ipString = ip.getHostAddress();
                        if (!"127.0.0.1".equals(ipString)) {
                            System.out.println("this server ip is:" + ipString);
                            return ipString;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 生成唯一标识Id
     * 40时间戳(2015年1月1日0时0分0秒到现在的毫秒数)+4位顺序数（每台业务机器自己内部自增的顺序数，到达16时归0）+8位机器码（每台接口服务器配置的机器唯一标识）
     * 本id生成器可以生成30年的数据，单机每秒生成16000个不重复
     * 且Id满足一下特性：
     * 1、保持基本的按时间增长特性
     * 2、每一次生成的Id都是唯一的，且同一时间不同机器生成的Id不同
     * @return
     */
    public static long generateId() {
        Long id = getId();

        try {
            seqLock.lock();
            if(id <= preId){
                //休眠1ms,保证id自增长 add 2019.10.22
                Thread.sleep(1L);
                id = getId();
            }
            preId = id;

        } catch (Exception e) {
            logger.error("生成Id异常：", e);
        } finally {
            seqLock.unlock();
        }

        return id;
    }

    private static Long getId() {
        short thisSequence;
        seqLock.lock();
        sequence = ++sequence;
        if (sequence >= seqMax){
            sequence = 0;
        }
        thisSequence = sequence;
        seqLock.unlock();

        long timestamp = System.currentTimeMillis() - beginTimeMillis;
        String timestampBinaryString = Long.toBinaryString(timestamp);
        if (timestampBinaryString.length() >= timeBits) {
            //timestampBinaryString = timestampBinaryString.substring(timestampBinaryString.length() - timeBits + 1);
            timestampBinaryString = timestampBinaryString.substring(0, timeBits);
        }
        timestampBinaryString = StringUtils.leftPad(timestampBinaryString, timeBits, '0');
        String sequenceBinaryString = Integer.toBinaryString(thisSequence);
        sequenceBinaryString = StringUtils.leftPad(sequenceBinaryString, seqBits, '0');

        String idBinaryString = MessageFormat.format("{0}{1}{2}", timestampBinaryString, serverBinaryString, sequenceBinaryString);

        return Long.parseLong(idBinaryString, 2);
    }
}
