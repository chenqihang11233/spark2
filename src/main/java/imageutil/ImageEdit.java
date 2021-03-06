package imageutil;

/**
 * @author ：tyy
 * @date ：Created in 2020/5/15 20:20
 * @description：
 * @modified By：
 * @version: $
 */

import detection.Box;
import lombok.extern.log4j.Log4j;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import pr.PlateInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.opencv.core.CvType.CV_8UC3;
@Log4j(topic =  "app2.ImageEdit")
public class ImageEdit {
    public static BufferedImage createImageFromBytes(final byte[] imageData) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException ex) {
            throw ex;
        }
    }

    /***
     *
     * @param image
     * @param plates
     * @return 返回的是像素数组
     * @throws IOException
     */
    public static byte[] editPlateInfos(byte[] image, List<PlateInfo> plates) throws IOException {

        BufferedImage bimage = createImageFromBytes(image);
        Graphics2D g=bimage.createGraphics();
        Color mycolor = Color.RED;

        g.setFont(new Font("宋体",Font.BOLD,15)); //字体、字型、字号

        for (int i = 0; i < plates.size(); i++) {

            g.setColor(mycolor);
            Box region = plates.get(i).getBox();
            log.info("annoted Image region" + region);
           // g.drawRect( (int)(region.x +15), (int)(region.y +15),  (int)(region.w - 20) ,(int)(region.h -20));
            g.fillRect((int)(region.x ) ,(int)(region.y - 15),15*7,15);
            g.setColor(Color.WHITE);
            g.drawString(plates.get(i).getName(),region.x ,region.y ); //画文字
        }
        g.dispose();
        byte[] data = ((DataBufferByte) bimage.getData().getDataBuffer()).getData();
        return data;

    }
    public static byte[] editChinese(byte[] image, String markContent,Point pos) throws IOException {

        BufferedImage bimage = createImageFromBytes(image);
        Graphics2D g=bimage.createGraphics();
        Color mycolor = Color.GREEN;
        g.setColor(mycolor);

        g.setFont(new Font("宋体",Font.PLAIN,15)); //字体、字型、字号
        g.drawString(markContent,pos.x,pos.y); //画文字
        g.setColor(Color.blue );
        //g.fillRect(pos.x ,pos.y - 15,15*4,15);
        g.dispose();
        byte[] data = ((DataBufferByte) bimage.getData().getDataBuffer()).getData();
        return data;

    }
    public static void main(String[] a) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat image = Imgcodecs.imread("F:/LPR/data/7.jpg");
        MatOfByte bytemat = new MatOfByte();
        //该函数对图像进行压缩，并将其存储在调整大小以适应结果的内存缓冲区中。
        Imgcodecs.imencode(".jpg", image, bytemat);
        byte[] bytes = bytemat.toArray();
        System.out.println("byte length :"+ bytes.length);
        byte[] data = ImageEdit.editChinese(bytes, "测试文字",new Point(0,100));
        Mat res = new Mat(image.height(),image.width(),CV_8UC3); // make a copy
        res.put(0,0,data);
        Imgcodecs.imwrite("result11.jpg",res);
    }


}
