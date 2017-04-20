/*
package com.jeefw.model.sys;

        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;

        import android.app.Activity;
        import android.graphics.Bitmap;
        import android.graphics.Bitmap.Config;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Matrix;
        import android.graphics.Paint;
        import android.graphics.PorterDuff.Mode;
        import android.graphics.PorterDuffXfermode;
        import android.graphics.Rect;
        import android.graphics.RectF;
        import android.graphics.drawable.BitmapDrawable;
        import android.graphics.drawable.Drawable;
        import android.media.ThumbnailUtils;
        import android.os.Bundle;
        import android.os.Environment;
        import android.view.View;
        import android.widget.ImageView;

        import com.example.administrator.mychatdemo.R;

//方法:
//1 生成圆角Bitmap图片
//2 生成Bitmap缩量图
//3 压缩图片场长宽以及kB
//注意:
//以上代码,测试其中一个方法时最好注释掉其余的代码
public class MainActivity extends Activity {
    private ImageView imageView;
    private Bitmap copyRawBitmap1;
    private Bitmap copyRawBitmap2;
    private Bitmap copyRawBitmap3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        imageView = (ImageView) findViewById(R.id.imageView);
//第一种方式:从资源文件中得到图片
        Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.haha);
        copyRawBitmap1=rawBitmap;
        copyRawBitmap2=rawBitmap;
        copyRawBitmap3=rawBitmap;
//第二种方式:从SD卡中得到图片(方法1)
        String SDCarePath=Environment.getExternalStorageDirectory().toString();
        String filePath=SDCarePath+"/"+"haha.jpg";
        Bitmap rawBitmap1 = BitmapFactory.decodeFile(filePath, null);
//第二种方式:从SD卡中得到图片(方法2)
        InputStream inputStream=getBitmapInputStreamFromSDCard("haha.jpg");
        Bitmap rawBitmap2 = BitmapFactory.decodeStream(inputStream);

//————>以下为将设置图片的圆角
        Bitmap roundCornerBitmap=this.toRoundCorner(rawBitmap, 40);
        imageView.setImageBitmap(roundCornerBitmap);
//————>以上为将设置图片的圆角

//————>以下为将图片高宽和的大小kB压缩
// 得到图片原始的高宽
        int rawHeight = rawBitmap.getHeight();
        int rawWidth = rawBitmap.getWidth();
// 设定图片新的高宽
        int newHeight = 500;
        int newWidth = 500;
// 计算缩放因子
        float heightScale = ((float) newHeight) / rawHeight;
        float widthScale = ((float) newWidth) / rawWidth;
// 新建立矩阵
        Matrix matrix = new Matrix();
        matrix.postScale(heightScale, widthScale);
// 设置图片的旋转角度
//matrix.postRotate(-30);
// 设置图片的倾斜
//matrix.postSkew(0.1f, 0.1f);
//将图片大小压缩
//压缩后图片的宽和高以及kB大小均会变化
        Bitmap newBitmap = Bitmap.createBitmap(rawBitmap, 0, 0, rawWidth,rawWidth, matrix, true);
// 将Bitmap转换为Drawable
        Drawable newBitmapDrawable = new BitmapDrawable(newBitmap);
        imageView.setImageDrawable(newBitmapDrawable);
//然后将Bitmap保存到SDCard中,方便于原图片的比较
        this.compressAndSaveBitmapToSDCard(newBitmap, "xx100.jpg", 80);
//问题:
//原图大小为625x690 90.2kB
//如果设置图片500x500 压缩后大小为171kB.即压缩后kB反而变大了.
//原因是将:compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
//第二个参数quality设置得有些大了(比如100).
//常用的是80,刚设100太大了造成的.
//————>以上为将图片高宽和的大小kB压缩


//————>以下为将图片的kB压缩,宽高不变
        this.compressAndSaveBitmapToSDCard(copyRawBitmap1,"0011fa.jpg",80);
//————>以上为将图片的kB压缩,宽高不变

//————>以下为获取SD卡图片的缩略图方法1
        String SDCarePath1=Environment.getExternalStorageDirectory().toString();
        String filePath1=SDCarePath1+"/"+"haha.jpg";
        Bitmap bitmapThumbnail1=this.getBitmapThumbnail(filePath1);
        imageView.setImageBitmap(bitmapThumbnail1);
//————>以上为获取SD卡图片的缩略图方法1

//————>以下为获取SD卡图片的缩略图方法2
        String SDCarePath2=Environment.getExternalStorageDirectory().toString();
        String filePath2=SDCarePath2+"/"+"haha.jpg";
        Bitmap tempBitmap=BitmapFactory.decodeFile(filePath2);
        Bitmap bitmapThumbnail2=ThumbnailUtils.extractThumbnail(tempBitmap, 100, 100);
        imageView.setImageBitmap(bitmapThumbnail2);
//————>以上为获取SD卡图片的缩略图方法2

    }
    //读取SD卡下的图片
    private InputStream getBitmapInputStreamFromSDCard(String fileName){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String SDCarePath=Environment.getExternalStorageDirectory().toString();
            String filePath=SDCarePath+File.separator+fileName;
            File file=new File(filePath);
            try {
                FileInputStream fileInputStream=new FileInputStream(file);
                return fileInputStream;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    //获取SDCard的目录路径功能
    private String getSDCardPath() {
        String SDCardPath = null;
// 判断SDCard是否存在
        boolean IsSDcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (IsSDcardExist) {
            SDCardPath = Environment.getExternalStorageDirectory().toString();
        }
        return SDCardPath;
    }
    //压缩且保存图片到SDCard
    private void compressAndSaveBitmapToSDCard(Bitmap rawBitmap,String fileName,int quality){
        String saveFilePaht=this.getSDCardPath()+File.separator+fileName;
        File saveFile=new File(saveFilePaht);
        if (!saveFile.exists()) {
            try {
                saveFile.createNewFile();
                FileOutputStream fileOutputStream=new FileOutputStream(saveFile);
                if (fileOutputStream!=null) {
//imageBitmap.compress(format, quality, stream);
//把位图的压缩信息写入到一个指定的输出流中
//第一个参数format为压缩的格式
//第二个参数quality为图像压缩比的值,0-100.0 意味着小尺寸压缩,100意味着高质量压缩
//第三个参数stream为输出流
                    rawBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    //获取图片的缩略图
    private Bitmap getBitmapThumbnail(String filePath){
        BitmapFactory.Options options=new BitmapFactory.Options();
//true那么将不返回实际的bitmap对象,不给其分配内存空间但是可以得到一些解码边界信息即图片大小等信息
        options.inJustDecodeBounds=true;
//此时rawBitmap为null
        Bitmap rawBitmap = BitmapFactory.decodeFile(filePath, options);
        if (rawBitmap==null) {
            System.out.println("此时rawBitmap为null");
        }
//inSampleSize表示缩略图大小为原始图片大小的几分之一,若该值为3
//则取出的缩略图的宽和高都是原始图片的1/3,图片大小就为原始大小的1/9
//计算sampleSize
        int sampleSize=computeSampleSize(options, 150, 200*200);
//为了读到图片,必须把options.inJustDecodeBounds设回false
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
//原图大小为625x690 90.2kB
//测试调用computeSampleSize(options, 100, 200*100);
//得到sampleSize=8
//得到宽和高位79和87
//79*8=632 87*8=696
        Bitmap thumbnailBitmap=BitmapFactory.decodeFile(filePath, options);
//保存到SD卡方便比较
        this.compressAndSaveBitmapToSDCard(thumbnailBitmap, "15.jpg", 80);
        return thumbnailBitmap;
    }

    //参考资料：
//http://my.csdn.net/zljk000/code/detail/18212
//第一个参数:原本Bitmap的options
//第二个参数:希望生成的缩略图的宽高中的较小的值
//第三个参数:希望生成的缩量图的总像素
    public static int computeSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
//原始图片的宽
        double w = options.outWidth;
//原始图片的高
        double h = options.outHeight;
        System.out.println("========== w="+w+",h="+h);
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
// return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    */
/**
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     *//*

//参考资料:
//http://blog.csdn.net/c8822882/article/details/6906768
    public Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap roundCornerBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(roundCornerBitmap);
        int color = 0xff424242;//int color = 0xff424242;
        Paint paint = new Paint();
        paint.setColor(color);
//防止锯齿
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = pixels;
//相当于清屏
        canvas.drawARGB(0, 0, 0, 0);
//先画了一个带圆角的矩形
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//再把原来的bitmap画到现在的bitmap！！！注意这个理解
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return roundCornerBitmap;
    }
 */
/*   *Bitmap放大的方法
    private static Bitmap big(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(1.5f,1.5f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }
    *Bitmap缩小的方法
    private static Bitmap small(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.8f,0.8f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }*//*

}*/
