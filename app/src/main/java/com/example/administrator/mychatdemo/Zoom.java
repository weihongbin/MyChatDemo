package com.example.administrator.mychatdemo;

/**
 * Created by Administrator on 2016/9/26.
 */

        import android.view.View;
        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.drawable.Drawable;
        import android.view.KeyEvent;
public class Zoom extends View {
    private Drawable image;
    private int zoomControler=20;

    public Zoom(Context context){
        super(context);
        image=context.getResources().getDrawable(R.drawable.common_ic_googleplayservices);
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //控制图像的宽度和高度
        image.setBounds((getWidth()/2)-zoomControler, (getHeight()/2)-zoomControler, (getWidth()/2)+zoomControler, (getHeight()/2)+zoomControler);
        image.draw(canvas);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_DPAD_UP)//放大
            zoomControler+=10;

        if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN) //缩小
            zoomControler-=10;

        if(zoomControler<10)
            zoomControler=10;

        invalidate();
        return true;
    }
}