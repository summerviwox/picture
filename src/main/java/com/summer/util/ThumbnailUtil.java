package com.summer.util;

import com.summer.base.OnFinishI;
import com.summer.mybatis.entity.Record;
import com.summer.util.gif.GifDecoder;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ThumbnailUtil {

    static final String start = "E:\\record";

    static final String start2 = "E:\\records";


    public static void zoomImagesScale(ArrayList<Record> records){
        for(int i=0;i<records.size();i++){
            if(i>10861){
                int x = 0;
            }
            simglezoomImageScale(records.get(i));
            System.out.println(i+"--"+records.get(i).getNetpath());
        }
    }

    public static void simglezoomImageScale(Record record){
        if(record==null||record.getNetpath()==null||"".equals(record.getNetpath())){
            return;
        }
        if(!(record.getAtype().equals("image")||record.getAtype().equals("1"))){
            return;
        }
        String path = "";
        if(record.getNetpath().startsWith(start2)){
            path= record.getNetpath().substring(start2.length());
        }else if(record.getNetpath().startsWith(start)){
            path= record.getNetpath().substring(start.length());
        }
        File file = new File("E:\\records"+path);
        if(!file.exists()){
            return;
        }
        File folder = new File("E:\\thumbnail");
        if(!folder.exists()){
            folder.mkdirs();
        }
        File newfile = new File("E:\\thumbnail"+path);
        if(!newfile.getParentFile().exists()){
            newfile.getParentFile().mkdirs();
        }
        if(newfile.exists()){
            return;
        }
        try {
            zoomImageScale(file,newfile.getPath(),200);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        }
    }

    /**
     * 按指定高度 等比例缩放图片
     *
     * @param imageFile
     * @param newPath
     * @param newWidth 新图的宽度
     * @throws IOException
     */
    private static void zoomImageScale(File imageFile, String newPath, int newWidth) throws IOException {
        if(!imageFile.canRead())
            return;
        BufferedImage bufferedImage = null;
        // 处理gif
        if(imageFile.getName().toLowerCase().endsWith("gif")){
            bufferedImage = GifDecoder.read(new FileInputStream(imageFile)).getFrame(0);
        }else{
            bufferedImage = ImageIO.read(imageFile);
        }
        if (null == bufferedImage)
            return;

        int originalWidth = bufferedImage.getWidth();
        int originalHeight = bufferedImage.getHeight();
        double scale = (double)originalWidth / (double)newWidth;    // 缩放的比例

        int newHeight =  (int)(originalHeight / scale);

        zoomImageUtils(imageFile, newPath, bufferedImage, newWidth, newHeight);
    }


    private static void zoomImageUtils(File imageFile, String newPath, BufferedImage bufferedImage, int width, int height)
            throws IOException{

        String suffix = imageFile.getName().substring(imageFile.getName().lastIndexOf(".")+1);

        // 处理 png 背景变黑的问题
        if(suffix != null && (suffix.trim().toLowerCase().endsWith("png") || suffix.trim().toLowerCase().endsWith("gif"))){
            BufferedImage to= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = to.createGraphics();
            to = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g2d.dispose();

            g2d = to.createGraphics();
            Image from = bufferedImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();

            ImageIO.write(to, suffix, new File(newPath));
        }else{
            BufferedImage newImage = null;
            try {
                newImage = new BufferedImage(width, height, bufferedImage.getType());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
            if(newImage==null){
                System.out.println("error--"+imageFile);
                return;
            }
            Graphics g = newImage.getGraphics();
            g.drawImage(bufferedImage, 0, 0, width, height, null);
            g.dispose();
            ImageIO.write(newImage, suffix, new File(newPath));
        }
    }

}
