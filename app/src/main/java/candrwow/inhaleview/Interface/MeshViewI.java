package candrwow.inhaleview.Interface;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import candrwow.inhaleview.Listener.CoverListener;

/**
 * Created by Candrwow on 2017/2/27.
 * MeshView与OutterMeshView的逻辑方法
 * TODO 根据attrs获得的参数定位relativeLayout的位置和大小，如果是MeshView设定为MATCH_PARENT,OutterMeshView则根据attr设置
 * MeshView采用xml的配置构造；OutterView采用代码配置
 */

public interface MeshViewI {
    /**
     * 根据MeshView或OutterMeshView设置RelativeLayout的初始参数，包含大小，圆角，位置等，位置可以根据父View和标志位计算具体坐标
     *
     * @param attr  构造函数的attr
     * @param ResId style文件ID
     */
    void initViewParams(Context context, AttributeSet attr, int[] ResId);

    /**
     * 对Canvas内ChildView截图并隐藏
     * TODO 考虑在这一过程结束后增加缓存，比如ChildView为RecyclerView,再次点击打开后记录原来的RecyclerView位置。
     */
    void ShotAndHideChildView();

    /**
     * 外部调用此方法开始动画
     *
     * @param animDurationTime 动画播放总时长
     * @param x                锚点的x坐标
     * @param y                锚点的y坐标
     */
    void startAnim(long animDurationTime, final float x, final float y);

    /**
     * @param animDurationTime
     * @param anchorPoint
     */
    void startAnim(long animDurationTime, final PointF anchorPoint);

    /**
     * @param animDurationTime
     * @param ResId            锚点所在的View ID，只能取view的中心点为锚点，锚点位于中心点以外的无法采用此方法需要指定xy
     */
    void startAnim(long animDurationTime, final int ResId);

    /**
     * 以anchorView中心点为锚点开始动画
     *
     * @param animDurationTime
     * @param anchorView
     */
    void startAnim(long animDurationTime, final View anchorView);

    void pauseAnim();

    void resumeAnim();

    /**
     * 针对动画当前的进度计算Mesh
     *
     * @param value 动画当前运行的进度，限定动画进度是0f->1f!
     */
    void CalcuMesh(float value);

    /**
     * 初始化动画执行需要的数值，必须在ValueAnimator.start之前阻塞执行，否则掉帧明显
     * 需要计算出初始的Vertices,复制一份于orignalVertices,用于之后进行位移进度,还需要计算出每个Vertices动画执行的总时长
     *
     * @param x 锚点的x坐标
     * @param y 锚点的y坐标
     */
    void initAnim(float x, float y);

    //TODO 准备提供给父View进行遮罩层绘制的进度信息
    void setCoverListener(CoverListener listener);


}
