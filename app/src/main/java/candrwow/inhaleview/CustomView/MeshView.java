package candrwow.inhaleview.CustomView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import org.xml.sax.Attributes;

import candrwow.inhaleview.Interface.MeshViewI;
import candrwow.inhaleview.Listener.CoverListener;
import candrwow.inhaleview.R;

/**
 * Created by Candrwow on 2017/2/26.
 * 锚点位于canvas内部的情况
 */

public class MeshView extends RelativeLayout implements MeshViewI {
    //记录动画的总时长
    long animDurationTime = 0;
    //记录动画当前运行的时间
    long animRunTime = 0;
    PointF anchorPoint;
    ValueAnimator valueAnimator;
    //0是stop,1是run,2是pause
    public int animStatus = 0;
    int meshWidth = 40;
    int meshHeight = 40;
    //标记动画是否开始，在dispatchDraw中使用
    boolean isStartAnim = false;
    //view的大小与位置信息
    int meshViewWidth, meshViewHeight, meshViewLeft, meshViewTop, meshViewRight, meshViewBottom;

    public MeshView(Context context) {
        super(context);
//        this.setLayoutParams();
    }

    /**
     * 构造并配置大小位置参数
     *
     * @param context
     * @param meshViewWidth
     * @param meshViewHeight
     * @param meshViewLeft
     * @param meshViewTop
     * @param meshViewRight
     * @param meshViewBottom
     */
    public MeshView(Context context, int meshViewWidth, int meshViewHeight, int meshViewLeft, int meshViewTop, int meshViewRight, int meshViewBottom) {
        super(context);
        this.meshViewWidth = meshViewWidth;
        this.meshViewHeight = meshViewHeight;
        this.meshViewLeft = meshViewLeft;
        this.meshViewTop = meshViewTop;
        this.meshViewRight = meshViewRight;
        this.meshViewBottom = meshViewBottom;

    }

    public MeshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewParams(context, attrs, R.styleable.MeshView);
    }

    public MeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewParams(context, attrs, R.styleable.MeshView);
    }

    /**
     * @param context
     * @param attr    构造函数的attr
     * @param ResId   style文件ID
     */
    @Override
    public void initViewParams(Context context, AttributeSet attr, int[] ResId) {
        TypedArray typedArray = context.obtainStyledAttributes(attr, ResId);
        //如果是默认值-5，在后续绘图时丢弃这个参数(-5是为了不和WRAP_CONTENT或MATCHPARENT冲突)
        meshViewWidth = typedArray.getInteger(R.styleable.MeshView_meshViewWidth, -5);
        meshViewHeight = typedArray.getInteger(R.styleable.MeshView_meshViewHeight, -5);
        meshViewLeft = typedArray.getInteger(R.styleable.MeshView_meshViewLeft, -5);
        meshViewTop = typedArray.getInteger(R.styleable.MeshView_meshViewTop, -5);
        meshViewRight = typedArray.getInteger(R.styleable.MeshView_meshViewRight, -5);
        meshViewBottom = typedArray.getInteger(R.styleable.MeshView_meshViewBottom, -5);
        typedArray.recycle();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (isStartAnim && screenShot != null) {
            long startTime = System.nanoTime();
            int save = canvas.save();
            canvas.drawBitmapMesh(screenShot, meshWidth, meshHeight, vertices, 0, null, 0, null);
            super.dispatchDraw(canvas);
            canvas.restoreToCount(save);
            Log.d("dispatchDrawTime", "dispatchDrawTime:" + (System.nanoTime() - startTime));
        } else {
            super.dispatchDraw(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //下列代码应当在OutterMeshView中处理
//        int widthMeasureSpec1 = 0;
//        int heightMeasureSpec1 = 0;
//        if (meshViewWidth != -5 && meshViewWidth > 0) {
//            widthMeasureSpec1 = MeasureSpec.makeMeasureSpec(meshViewWidth, MeasureSpec.EXACTLY);
//        }
//        if (meshViewHeight != -5 && meshViewHeight > 0) {
//            heightMeasureSpec1 = MeasureSpec.makeMeasureSpec(meshViewHeight, MeasureSpec.EXACTLY);
//        }
//        setMeasuredDimension(widthMeasureSpec1 == 0 ? widthMeasureSpec : widthMeasureSpec1, heightMeasureSpec1 == 0 ? heightMeasureSpec : heightMeasureSpec1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, meshViewLeft == -5 ? l : meshViewLeft, meshViewTop == -5 ? t : meshViewTop, meshViewRight == -5 ? r : meshViewRight, meshViewBottom == -5 ? b : meshViewBottom);
//        super.onLayout(changed, l, t, r, b);
    }

    public void pauseAnim() {
        valueAnimator.pause();
        animStatus = 2;
    }

    public void resumeAnim() {
        valueAnimator.resume();
        animStatus = 1;
    }

    @Override
    public void startAnim(long animDurationTime, PointF anchorPoint) {

    }

    @Override
    public void startAnim(long animDurationTime, int ResId) {

    }

    /**
     * 外部调用此方法开始动画
     *
     * @param animDurationTime 动画播放总时长
     * @param x                锚点的x坐标
     * @param y                锚点的y坐标
     */
    public void startAnim(long animDurationTime, final float x, final float y) {

        animStatus = 1;
        checkAnchorPoint(x, y);
        anchorPoint = new PointF(x, y);
        //TODO 动画开始时调用ScreenShot，销毁ViewGroup的ChildView,使用AnimStart监听会导致异步耗时，前几帧出现问题,此方法需要在调用动画前开始，耗时0.13秒左右，动画开始后调用会掉帧
        this.animDurationTime = animDurationTime * 1000;
        ShotAndHideChildView();
        initAnim(x, y);
        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d("animation.value", "animationValue:" + animation.getAnimatedValue());
                //TODO 根据animation修改MeshView的animRunTime，再重绘主界面
                CalcuMesh((float) animation.getAnimatedValue());
                invalidate();
            }
        });
        valueAnimator.setDuration(this.animDurationTime);
        valueAnimator.start();
        isStartAnim = true;
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isStartAnim = false;
            }
        });
    }

    @Override
    public void startAnim(long animDurationTime, View anchorView) {

    }

    public void CalcuMesh(float value) {
        for (int i = 0; i < runTimeOfVertices.length; i++) {
            //当前进度
            double progress = value * animDurationTime / runTimeOfVertices[i];
            if (progress >= 1) {
                vertices[2 * i] = anchorPoint.x;
                vertices[2 * i + 1] = anchorPoint.y;
            } else if (progress <= 0) {
                vertices[2 * i] = orignVertices[2 * i];
                vertices[2 * i + 1] = orignVertices[2 * i + 1];
            } else {
                vertices[2 * i] = (float) (orignVertices[2 * i] + (anchorPoint.x - orignVertices[2 * i]) * progress);
                vertices[2 * i + 1] = (float) (orignVertices[2 * i + 1] + (anchorPoint.y - orignVertices[2 * i + 1]) * progress);
            }
        }

    }

    //8个特殊点，顺时针排序.
    PointF[] pointFs;
    float[] vertices;
    float[] orignVertices;
    //mesh中每个顶点的运行时长
    double[] runTimeOfVertices;
    double maxDisplacement;

    /**
     * 初始化特殊点和各个点的运行时长，距离锚点越远运行时间越长，越近的点越早被吸入到锚点，吸入速度都一样，如果要造成黑洞效果，需要将各点的速度根据位移重新计算
     * 当MeshView时锚点位于内部，所以不需要对canvas作点的映射，OutterMeshView需要映射
     */
    public void initAnim(float x, float y) {
        long startTime = System.nanoTime();
        pointFs = new PointF[]{
                new PointF(0, 0),
                new PointF(x, 0),
                new PointF(this.getWidth(), 0),
                new PointF(this.getWidth(), y),
                new PointF(this.getWidth(), this.getHeight()),
                new PointF(x, this.getHeight()),
                new PointF(0, this.getHeight()),
                new PointF(0, y),
        };
        maxDisplacement = 0;
        for (int i = 0; i < pointFs.length; i++) {
            double displacement = Math.sqrt(Math.pow((pointFs[i].x - x), 2) + Math.pow((pointFs[i].y - y), 2));
            if (displacement >= maxDisplacement)
                maxDisplacement = displacement;
        }
        //这里可能有this.getWidth不是整数的问题
        vertices = new float[(meshWidth + 1) * (meshHeight + 1) * 2];
        for (int i = 0; i <= meshHeight; i++) {
            for (int j = 0; j <= meshWidth; j++) {
                vertices[2 * (i * (meshWidth + 1) + j)] = this.getMeasuredWidth() * j / meshWidth;
                vertices[2 * (i * (meshWidth + 1) + j) + 1] = this.getMeasuredHeight() * i / meshHeight;
            }
        }
        orignVertices = vertices;
        runTimeOfVertices = new double[(meshWidth + 1) * (meshHeight + 1)];
        //计算每个点的运行时长
        Log.d("0227", "maxDisplacement:" + maxDisplacement);
        for (int i = 0; i < runTimeOfVertices.length; i++) {
            runTimeOfVertices[i] = animDurationTime * Math.sqrt(Math.pow((orignVertices[i * 2] - x), 2) + Math.pow((orignVertices[i * 2 + 1] - y), 2)) / maxDisplacement;
            Log.d("0227", "runTimeOfVertices:" + runTimeOfVertices[i] + "       pointF.x:" + orignVertices[i * 2] + "  y:" + orignVertices[i * 2 + 1]);
        }
        long endTime = System.nanoTime();
        Log.d("dTime", endTime - startTime + " 纳秒");
    }

    Bitmap screenShot;

    public void ShotAndHideChildView() {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        screenShot = Bitmap.createBitmap(this.getDrawingCache(), 0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
        this.setDrawingCacheEnabled(false);
        this.destroyDrawingCache();
        //将所有的子view隐藏，在动画结束后销毁view。在这里销毁会导致WRAP_CONTENT布局大小发生改变!
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (this.getChildAt(i).getVisibility() == VISIBLE)
                this.getChildAt(i).setVisibility(INVISIBLE);
        }
    }

    /**
     * 校验锚点是否在canvas内
     *
     * @param x
     * @param y
     * @return
     */
    public boolean checkAnchorPoint(float x, float y) {
        return true;
    }

    /**
     * 取4个顶点和法线的四个点，作8条贝塞尔曲线裁剪出区域，当锚点位于canvas内部时，canvas不需要进行缩放，但是需要设置canvas背景透明
     * issue:8条Bezier考虑用4条4阶Bezier替代
     */
    public void clipCanvas() {
        CalcuPoint();
        GeneratePath();
    }

    /**
     * 根据动画运行进度和初始8个特殊点与锚点的距离计算8个特殊点当前进度下的位置
     */
    public void CalcuPoint() {

    }

    /**
     * 根据当前的8个特殊点绘制8条Bezier曲线
     */
    public void GeneratePath() {

    }

    @Override
    public void setCoverListener(CoverListener listener) {
    }
}
