package com.dzt.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class ImageCode {

    /**
     *     验证码图片的长和宽
     */
    private int weight = 100;
    private int height = 40;
    private String codes;
    private static Random random = new Random();

    private ImageCode() {
    }

    /**
     * 获得一个随机的字符串 并把字符串返回
     * @param length
     * @return
     */
    private static String randomCode(int length){
        char[] chars = "abdefghijkmnpqrstuvwxyABCDEFGHIJKLMNPQRSTUVWXY0123456789".toCharArray();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<length;i++){
            sb.append(chars[random.nextInt(chars.length)]);
        }
        return sb.toString();
    }

    /**
     * 得到一个随机的颜色
     * @return
     */
    private Color randomColor() {
        int r = random.nextInt(225);
        int g = random.nextInt(225);
        int b = random.nextInt(225);
        return new Color(r, g, b);
    }

    /**
     * 获得一个随机的字体
     * @return
     */
    private Font randomFont() {
        String[] fontNames = {"宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312"};
        int index = random.nextInt(fontNames.length);
        String fontName = fontNames[index];
        //获得字体样式
        int style = random.nextInt(4);
        //获得字体的大小
        int size = random.nextInt(10) + 24;
        return new Font(fontName, style, size);
    }


    /**
     * 获取随机字符
     *
     * @return
     */
    private char randomChar() {
        char[] chars = "abdefghijkmnpqrstuvwxyABCDEFGHIJKLMNPQRSTUVWXY0123456789".toCharArray();
        int index = random.nextInt(chars.length);
        return chars[index];
    }

    private void drawLine(BufferedImage image) {
        //定义干扰线的数量
        int num = 0;
        do {
            num = random.nextInt(10);
        }while (num > 4);

        Graphics2D g = (Graphics2D) image.getGraphics();
        for (int i = 0; i < num; i++) {
            int x1 = random.nextInt(weight);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(weight);
            int y2 = random.nextInt(height);
            g.setColor(randomColor());
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 获取验证码图片
     * @return
     */
    public BufferedImage getImage() {
        //创建图片
        BufferedImage image = new BufferedImage(weight, height, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics2D g = (Graphics2D) image.getGraphics();
        //设置背景色随机
        g.setColor(new Color(255, 255, random.nextInt(245) + 10));
        g.fillRect(0, 0, weight, height);

        StringBuilder sb = new StringBuilder();
        //开始画
        for (int i = 0; i < 4; i++)
        {
            String s = randomChar() + "";
            sb.append(s);
            float x = i * 1.0F * weight / 4;
            //设置随机的字体和颜色
            g.setFont(randomFont());
            g.setColor(randomColor());
            g.drawString(s, x, height - 5);
        }

        this.codes = sb.toString();
        drawLine(image);
        g.dispose();
        return image;
    }


    public String getCodes() {
        return codes;
    }

    public static String outPut(OutputStream out) throws IOException {
        com.dzt.util.ImageCode image = new com.dzt.util.ImageCode();
        ImageIO.write(image.getImage(), "JPEG", out);
        return image.getCodes();
    }

}
