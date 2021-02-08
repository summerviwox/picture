package com.summer.util;

import com.summer.base.OnFinishI;
import com.summer.global.Value;
import com.summer.mybatis.entity.Record;
import com.summer.util.gif.GifDecoder;
import net.coobird.thumbnailator.Thumbnails;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ThumbnailUtil {

    public static final String start = "E:\\record";

    public static final String start2 = "E:\\records";


//    static {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  //加载动态链接库 opencv
//    }


    public static void zoomImagesScale(ArrayList<Record> records) throws IOException {
        for (int i = 0; i < records.size(); i++) {
            System.out.println(i + "before--" + records.get(i).getNetpath());
            simglezoomImageScale(records.get(i));
            System.out.println(i + "after--" + records.get(i).getNetpath());
        }
    }

    public static void simglezoomImageScale(Record record) throws IOException {
        if (record == null || record.getNetpath() == null || "".equals(record.getNetpath())) {
            return;
        }
//        if ((!(record.getAtype().equals("image") || record.getAtype().equals("1")))) {
        if(record.getAtype()==null||record.getAtype().equals("")){
            return;
        }
        File windowsFile = Value.toWinddowsFile(record.getNetpath());
        String type = "image";
        if(record.getAtype().equals("image")||record.getAtype().equals("1")){
            type = "image";
        }else{
            type = "video";
        }
        File thumnailFile = Value.toThumbnailPath(type,record.getNetpath());
        if (!windowsFile.exists()||thumnailFile.exists()) {
            return;
        }
        switch (record.getAtype()){//com.luciad.imageio.webp.WebPReadParam
            case "1":
            case   "image":
                try {
                    Thumbnails.of(windowsFile)
                            .size(200,200)
                            .toFile(Value.toThumbnailPathCreateParent(type,record.getNetpath()));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }

                //zoomImageScale(windowsFile, 200);
                break;
            case "3":
            case "video":
                zoomVideoScale(windowsFile, 200);
                break;
        }
    }

    public static void zoomVideoScale(File videoFile,int newWidth) throws IOException {
        Frame frame = null;
        File windowsFile = Value.toWinddowsFile(videoFile.getPath());
        if(!windowsFile.exists()||windowsFile.length()>1024*1024*200){
            return;
        }
        FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(windowsFile);
        String[] str = windowsFile.getPath().split("\\.");
        fFmpegFrameGrabber.setFormat(str[str.length-1].toLowerCase());
        fFmpegFrameGrabber.start();
        int ftp = fFmpegFrameGrabber.getLengthInFrames();
        fFmpegFrameGrabber.setFrameNumber(ftp/2);
        frame = fFmpegFrameGrabber.grabImage();
        if(frame==null){
            fFmpegFrameGrabber.release();
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        if (bufferedImage == null) {
            fFmpegFrameGrabber.release();
            return;
        }
        fFmpegFrameGrabber.release();
        int originalWidth = bufferedImage.getWidth();
        int originalHeight = bufferedImage.getHeight();
        int newHeight =(newWidth*originalHeight)/originalWidth;
        zoomImageUtils("video",videoFile, bufferedImage, newWidth, newHeight);
    }


    /**
     * 按指定高度 等比例缩放图片
     *
     * @param imageFile
     * @param newWidth  新图的宽度
     * @throws IOException
     */
    public static void zoomImageScale(File imageFile, int newWidth) throws IOException {
        if (!imageFile.canRead())
            return;
        BufferedImage bufferedImage = null;
        // 处理gif
        if (imageFile.getName().toLowerCase().endsWith("gif")) {
            bufferedImage = GifDecoder.read(new FileInputStream(imageFile)).getFrame(0);
        } else {
            // 处理普通图片
            try {
                bufferedImage = ImageIO.read(new FileInputStream(imageFile));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        if (bufferedImage == null) {
            //处理webp
//            ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();
//            WebPReadParam readParam = new WebPReadParam();
//            readParam.setBypassFiltering(true);
//            reader.setInput(new FileImageInputStream(imageFile));
//            bufferedImage = reader.read(0, readParam);
            return;
        }
        if(bufferedImage==null){
            return;
        }
        int originalWidth = bufferedImage.getWidth();
        int originalHeight = bufferedImage.getHeight();
        int newHeight =(newWidth*originalHeight)/originalWidth;
        zoomImageUtils("image",imageFile,bufferedImage, newWidth, newHeight);
    }


    public static void zoomImageUtils(String type,File imageFile,BufferedImage bufferedImage, int width, int height)
            throws IOException {
        String[] ss = imageFile.getName().split("\\.");
        String suffix =ss[ss.length-1];
       if(!type.equals("image")){
               suffix = "jpg";
       }else{
           if(!suffix.toLowerCase().equals("png")){
               suffix = "jpg";
           }
       }
        File file = Value.toThumbnailPathCreateParent(type,imageFile.getPath());
        // 处理 png 背景变黑的问题
        if (suffix != null && (suffix.trim().toLowerCase().endsWith("png") || suffix.trim().toLowerCase().endsWith("gif"))) {
            BufferedImage to = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = to.createGraphics();
            to = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g2d.dispose();

            g2d = to.createGraphics();
            Image from = bufferedImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();
            ImageIO.write(to, suffix, file);
            System.out.println(file.getPath());
        } else {
            BufferedImage newImage = null;
            if(bufferedImage.getType()==BufferedImage.TYPE_CUSTOM){
                System.out.println(imageFile.getPath()+": java.lang.IllegalArgumentException: Unknown image type 0");
                return;
            }
            newImage = new BufferedImage(width, height, bufferedImage.getType());
            Graphics g = newImage.getGraphics();
            g.drawImage(bufferedImage, 0, 0, width, height, null);
            g.dispose();
            ImageIO.write(newImage, suffix, file);
            System.out.println(file.getPath());
        }
    }

}
